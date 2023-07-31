package com.rogers.api.utils;


import lombok.extern.slf4j.Slf4j;
import net.schmizz.sshj.SSHClient;
import net.schmizz.sshj.sftp.SFTPClient;
import net.schmizz.sshj.transport.verification.PromiscuousVerifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SSHDFileTransferServiceImpl implements FileTransferService {

    FileTransferConfigurationLoad fileTransferConfigurationLoad;


    @Override
    public boolean uploadFile(String localFilePath, String remoteFilePath) throws NullPointerException {
        try (SSHClient client = new SSHClient()) {
            client.addHostKeyVerifier(new PromiscuousVerifier());
            client.connect(fileTransferConfigurationLoad.getSftpHost(), fileTransferConfigurationLoad.getSftpPort());
            client.authPassword(fileTransferConfigurationLoad.getSftpUser(), fileTransferConfigurationLoad.getSftpPasword());
            client.startSession();
            if (client.isConnected()) {
                try (SFTPClient sftpclient = client.newStatefulSFTPClient()) {
                    sftpclient.put(localFilePath, remoteFilePath);
                }
                return true;
            }
        } catch (Exception e) {
            log.error("Error upload file", e);
        }
        return false;
    }

    public SSHDFileTransferServiceImpl(FileTransferConfigurationLoad fileTransferConfigurationLoad) {
        this.fileTransferConfigurationLoad = fileTransferConfigurationLoad;
    }


    @Override
    public boolean downloadFile(String localFilePath, String remoteFilePath) {
        try (SSHClient client = new SSHClient()) {
            client.addHostKeyVerifier(new PromiscuousVerifier());
            client.connect(fileTransferConfigurationLoad.getSftpHost(), fileTransferConfigurationLoad.getSftpPort());
            client.authPassword(fileTransferConfigurationLoad.getSftpUser(), fileTransferConfigurationLoad.getSftpPasword());
            client.startSession();
            if (client.isConnected()) {
                try (SFTPClient sftpclient = client.newStatefulSFTPClient()) {
                    sftpclient.get(localFilePath, remoteFilePath);
                }
                return true;
            }
        } catch (Exception e) {
            log.error("Error upload file", e);
        }
        return false;

    }

}