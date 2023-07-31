package com.rogers.api.utils;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileTransferConfigurationLoad {

    @Value("${application.sftp.host}")
    private String sftpHost;

    @Value("${application.sftp.port}")
    private int sftpPort;

    @Value("${application.sftp.username}")
    private String sftpUser;

    @Value("${application.sftp.privateKey:#{null}}")
    private Resource sftpPrivateKey;

    @Value("${application.sftp.privateKeyPassphrase:#{null}")
    private String sftpPrivateKeyPassphrase;

    @Value("${application.sftp.password:#{null}}")
    private String sftpPasword;

    @Value("${application.sftp.filepath:/}")
    private String sftpFilePath;

    @Value("${application.sftp.outFilePath:/}")
    private String sftpOutFilePath;

    @Value("${application.sftp.sasOutFilePath:/}")
    private String sftpSasOutFilePath;

    @Value("${application.sftp.filePattern:/}")
    private String sftpFilePattern;

    @Value("${application.sftp.sessionTimeout}")
    private Integer sessionTimeout;

    @Value("${application.sftp.channelTimeout}")
    private Integer channelTimeout;

    public String getSftpHost() {
        return sftpHost;
    }

    public int getSftpPort() {
        return sftpPort;
    }

    public String getSftpUser() {
        return sftpUser;
    }

    public Resource getSftpPrivateKey() {
        return sftpPrivateKey;
    }

    public String getSftpPrivateKeyPassphrase() {
        return sftpPrivateKeyPassphrase;
    }

    public String getSftpPasword() {
        return sftpPasword;
    }

    public String getSftpOutFilePath() {
        return sftpOutFilePath;
    }

    public String getSftpSasOutFilePath() {
        return sftpSasOutFilePath;
    }

    public Integer getSessionTimeout() {
        return sessionTimeout;
    }

    public Integer getChannelTimeout() {
        return channelTimeout;
    }

    public void setSftpHost(String sftpHost) {
        this.sftpHost = sftpHost;
    }

    public void setSftpPort(int sftpPort) {
        this.sftpPort = sftpPort;
    }

    public void setSftpUser(String sftpUser) {
        this.sftpUser = sftpUser;
    }

    public void setSftpPrivateKey(Resource sftpPrivateKey) {
        this.sftpPrivateKey = sftpPrivateKey;
    }

    public void setSftpPrivateKeyPassphrase(String sftpPrivateKeyPassphrase) {
        this.sftpPrivateKeyPassphrase = sftpPrivateKeyPassphrase;
    }

    public void setSftpPasword(String sftpPasword) {
        this.sftpPasword = sftpPasword;
    }

    public void setSftpOutFilePath(String sftpOutFilePath) {
        this.sftpOutFilePath = sftpOutFilePath;
    }

    public void setSessionTimeout(Integer sessionTimeout) {
        this.sessionTimeout = sessionTimeout;
    }

    public void setChannelTimeout(Integer channelTimeout) {
        this.channelTimeout = channelTimeout;
    }

    public String getSftpFilePath() {
        return sftpFilePath;
    }

    public void setSftpFilePath(String sftpFilePath) {
        this.sftpFilePath = sftpFilePath;
    }

    public String getSftpFilePattern() {
        return sftpFilePattern;
    }

    public void setSftpFilePattern(String sftpFilePattern) {
        this.sftpFilePattern = sftpFilePattern;
    }
}
