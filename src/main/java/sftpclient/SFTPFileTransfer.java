package sftpclient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import com.jcraft.jsch.*;

public class SFTPFileTransfer {

    private static final String REMOTE_HOST = "test.rebex.net";
    private static final String USERNAME = "demo";
    private static final String PASSWORD = "password";
    private static final int REMOTE_PORT = 22;
    private static final int SESSION_TIMEOUT = 10000;
    private static final int CHANNEL_TIMEOUT = 5000;

    public static void main(String[] args) {

        String localFile = "src/main/resources/fichero.txt";
        String remoteFile = "/readme.txt";

        Session jschSession = null;

        try {

            JSch jsch = new JSch();
            jsch.setKnownHosts("src/main/resources/known_hosts");
            jschSession = jsch.getSession(USERNAME, REMOTE_HOST, REMOTE_PORT);
            
            // configure JSch to not use "StrictHostKeyChecking"
            // java.util.Properties config = new java.util.Properties(); 
            // config.put("StrictHostKeyChecking", "no");
            // jschSession.setConfig(config);

            // authenticate using private key
            // jsch.addIdentity("/home/mkyong/.ssh/id_rsa");

            // authenticate using password
            jschSession.setPassword(PASSWORD);

            // 10 seconds session timeout
            jschSession.connect(SESSION_TIMEOUT);

            Channel sftp = jschSession.openChannel("sftp");

            // 5 seconds timeout
            sftp.connect(CHANNEL_TIMEOUT);

            ChannelSftp channelSftp = (ChannelSftp) sftp;

            // transfer file from local to remote server
            // channelSftp.put(localFile);

            // download file from remote server to local
            String output = new BufferedReader(new InputStreamReader(channelSftp.get(remoteFile))).lines().collect(Collectors.joining("\n"));
            System.out.println(output);
            
            channelSftp.exit();

        } catch (Exception e) {

            e.printStackTrace();

        } finally {
            if (jschSession != null) {
                jschSession.disconnect();
            }
        }

        System.out.println("Done");
    }

}