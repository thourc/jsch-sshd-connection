package com.rogers.api.utils;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.bcpg.CompressionAlgorithmTags;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.operator.jcajce.JcaKeyFingerprintCalculator;
import org.bouncycastle.openpgp.operator.jcajce.JcePGPDataEncryptorBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyDataDecryptorFactoryBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePublicKeyKeyEncryptionMethodGenerator;
import org.bouncycastle.util.io.Streams;

import java.io.*;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.Iterator;

/**
 * A simple utility class that encrypts/decrypts public key based
 * encryption files.
 * <p>
 * To encrypt a file: KeyBasedFileProcessor -e [-a|-ai] fileName publicKeyFile.<br>
 * If -a is specified the output file will be "ascii-armored".
 * If -i is specified the output file will be have integrity checking added.
 * <p>
 * To decrypt: KeyBasedFileProcessor -d fileName secretKeyFile passPhrase.
 * <p>
 * Note 1: this example will silently overwrite files, nor does it pay any attention to
 * the specification of "_CONSOLE" in the filename. It also expects that a single pass phrase
 * will have been used.
 * <p>
 * Note 2: if an empty file name has been specified in the literal data object contained in the
 * encrypted packet a file with the name filename.out will be generated in the current working directory.
 */
@Slf4j
public class KeyBasedFileProcessor {

    public static final int CAST5 = 3;       // CAST5 (128 bit key, as per RFC 2144)

    private KeyBasedFileProcessor() {
    }

    public static void decryptFile(
            String inputFileName,
            String keyFileName,
            char[] passwd,
            String defaultFileName)
            throws IOException, NoSuchProviderException {
        try (InputStream in = new BufferedInputStream(new FileInputStream(inputFileName))) {
            InputStream keyIn = new BufferedInputStream(new FileInputStream(keyFileName));
            decryptFile(in, keyIn, passwd, defaultFileName);
            keyIn.close();
        }
    }

    /**
     * decrypt the passed in message stream
     */
    private static void decryptFile(
            InputStream in,
            InputStream keyIn,
            char[] passwd,
            String defaultFileName)
            throws IOException, NoSuchProviderException {
        in = org.bouncycastle.openpgp.PGPUtil.getDecoderStream(in);

        try {
            JcaPGPObjectFactory pgpF = new JcaPGPObjectFactory(in);
            Object o = pgpF.nextObject();

            //
            // the first object might be a PGP marker packet.
            //
            PGPEncryptedDataList enc = getEncryptedDataList(pgpF, o);


            //
            // find the secret key
            //
            Iterator<PGPEncryptedData> it = enc.getEncryptedDataObjects();
            PGPPrivateKey sKey = null;
            PGPPublicKeyEncryptedData pbe = null;
            PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
                    org.bouncycastle.openpgp.PGPUtil.getDecoderStream(keyIn), new JcaKeyFingerprintCalculator());

            while (sKey == null && it.hasNext()) {
                pbe = (PGPPublicKeyEncryptedData) it.next();

                sKey = PGPUtils.findSecretKey(pgpSec, pbe.getKeyID(), passwd);
            }

            if (sKey == null) {
                throw new IllegalArgumentException("secret key for message not found.");
            }

            InputStream clear = pbe.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().setProvider("BC").build(sKey));

            JcaPGPObjectFactory plainFact = new JcaPGPObjectFactory(clear);

            Object message = plainFact.nextObject();

            if (message instanceof PGPCompressedData) {
                PGPCompressedData cData = (PGPCompressedData) message;
                JcaPGPObjectFactory pgpFact = new JcaPGPObjectFactory(cData.getDataStream());

                message = pgpFact.nextObject();
            }

            if (message instanceof PGPLiteralData) {
                PGPLiteralData ld = (PGPLiteralData) message;

                InputStream unc = ld.getInputStream();
                OutputStream fOut = new BufferedOutputStream(new FileOutputStream(defaultFileName));

                Streams.pipeAll(unc, fOut);

                fOut.close();
            } else if (message instanceof PGPOnePassSignatureList) {
                throw new PGPException("encrypted message contains a signed message - not literal data.");
            } else {
                throw new PGPException("message is not a simple encrypted file - type unknown.");
            }

            if (pbe.isIntegrityProtected()) {
                if (!pbe.verify()) {
                    log.error("message failed integrity check");
                } else {
                    log.error("message integrity check passed");
                }
            } else {
                log.warn("no message integrity check");
            }
        } catch (PGPException e) {
            log.error(e.getLocalizedMessage());
            if (e.getUnderlyingException() != null) {
                log.warn(e.getUnderlyingException().getLocalizedMessage());
            }
        }
    }

    private static PGPEncryptedDataList getEncryptedDataList(JcaPGPObjectFactory pgpF, Object o) throws IOException {
        PGPEncryptedDataList enc;
        if (o instanceof PGPEncryptedDataList) {
            enc = (PGPEncryptedDataList) o;
        } else {
            enc = (PGPEncryptedDataList) pgpF.nextObject();
        }
        return enc;
    }

    public static void encryptFile(
            String inputFileName,
            String outputFileName,
            String encKeyFileName)
            throws IOException, PGPException {
        try (OutputStream out = new BufferedOutputStream(new FileOutputStream(outputFileName))) {
            PGPPublicKey encKey = PGPUtils.readPublicKey(encKeyFileName);
            encryptFile(out, inputFileName, encKey);
        }
    }

    private static void encryptFile(
            OutputStream out,
            String fileName,
            PGPPublicKey encKey)
            throws IOException {

        try {
            byte[] bytes = PGPUtils.compressFile(fileName, CompressionAlgorithmTags.ZIP);

            PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
                    new JcePGPDataEncryptorBuilder(CAST5).setWithIntegrityPacket(false).setSecureRandom(new SecureRandom()).setProvider("BC"));

            encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(encKey).setProvider("BC"));

            try (OutputStream cOut = encGen.open(out, bytes.length)) {
                cOut.write(bytes);
            }

        } catch (PGPException e) {
            log.error(e.getLocalizedMessage());
            if (e.getUnderlyingException() != null) {
                log.warn(e.getUnderlyingException().getLocalizedMessage());
            }
        }
    }
}
