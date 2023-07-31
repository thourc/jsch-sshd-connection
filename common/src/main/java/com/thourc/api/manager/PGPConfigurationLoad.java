package com.rogers.api.manager;

import com.rogers.springboot.YamlPropertySourceFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
@PropertySource(value = "classpath:application-common.yml", factory = YamlPropertySourceFactory.class)
public class PGPConfigurationLoad {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    @Value("${application.pgp.pgpFilePath}")
    private String pgpFilePath;

    @Value("${application.pgp.key.publicKeyFilePath}")
    private String publicKeyFilePath;

    @Value("${application.pgp.key.sasPublicKeyFilePath}")
    private String sasPublicKeyFilePath;

    @Value("${application.pgp.key.secretKeyFilePath}")
    private String secretKeyFilePath;

    @Value("${application.pgp.key.passphrase}")
    private String passphrase;

    @Value("${application.pgp.file.errorSourceFile}")
    private String errorSourceFile;

    @Value("${application.pgp.file.snapshotSourceFile}")
    private String snapshotSourceFile;

    @Value("${application.pgp.file.sasSnapshotFile}")
    private String sasSnapshotSourceFile;

    @Value("${application.backupRetention}")
    private Integer backupRetention;

    public Integer getBackupRetention() {
        return backupRetention;
    }

    public void setBackupRetention(Integer backupRetention) {
         this.backupRetention = backupRetention;
    }
    public String getPublicKeyFilePath() {
        return publicKeyFilePath;
    }

    public String getSasPublicKeyFilePath() {
        return sasPublicKeyFilePath;
    }

    public void setPublicKeyFilePath(String publicKeyFilePath) {
        this.publicKeyFilePath = publicKeyFilePath;
    }

    public void setSasPublicKeyFilePath(String sasPublicKeyFilePath) {
        this.sasPublicKeyFilePath = sasPublicKeyFilePath;
    }

    public String getSecretKeyFilePath() {
        return secretKeyFilePath;
    }

    public void setSecretKeyFilePath(String secretKeyFilePath) {
        this.secretKeyFilePath = secretKeyFilePath;
    }

    public String getPassphrase() {
        return passphrase;
    }

    public void setPassphrase(String passphrase) {
        this.passphrase = passphrase;
    }

    public String getErrorSourceFile() {

        LocalDate localDate = LocalDate.now();
        return errorSourceFile + formatter.format(localDate) + ".csv";
    }

    public String getSnapshotSourceFile() {

        LocalDate localDate = LocalDate.now();
        return snapshotSourceFile + formatter.format(localDate) + ".csv";
    }

    public String getEncryptErrorFile() {

        LocalDate localDate = LocalDate.now();
        return errorSourceFile + formatter.format(localDate) + ".pgp";
    }

    public String getEncryptSnapshotFile() {

        LocalDate localDate = LocalDate.now();
        return snapshotSourceFile + formatter.format(localDate) + ".pgp";
    }

    public String getEncryptSasSnapshotFile() {

        LocalDate localDate = LocalDate.now();
        return sasSnapshotSourceFile + formatter.format(localDate) + ".pgp";
    }

    public void setErrorSourceFile(String errorSourceFile) {
        this.errorSourceFile = errorSourceFile;
    }

    public void setSnapshotSourceFile(String snapshotSourceFile) {
        this.snapshotSourceFile = snapshotSourceFile;
    }

    public void setSasSnapshotSourceFile(String sasSnapshotSourceFile) {
        this.sasSnapshotSourceFile = sasSnapshotSourceFile;
    }

    public String getPgpFilePath() {
        return pgpFilePath;
    }

    public void setPgpFilePath(String pgpFilePath) {
        this.pgpFilePath = pgpFilePath;
    }
}
