package com.rogers.api.manager;

import com.rogers.api.utils.KeyBasedFileProcessor;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.NoSuchProviderException;
import java.security.Security;

@Service
public class PGPManager {


    public void encryptFile(String originalFile, String outputFile, String keyFile) throws IOException, PGPException {
        Security.addProvider(new BouncyCastleProvider());
        KeyBasedFileProcessor.encryptFile(originalFile, outputFile, keyFile);
    }

    public void decryptFile(String encryptedFile, String keyFile, String passPhrase, String outputFile) throws IOException, NoSuchProviderException {
        Security.addProvider(new BouncyCastleProvider());
        KeyBasedFileProcessor.decryptFile(encryptedFile, keyFile, passPhrase.toCharArray(), outputFile);
    }

}
