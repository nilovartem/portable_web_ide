package com.example.portable_web_ide.main;

import android.util.Log;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FtpManager {

    private static final String MODULE_TAG = "FtpManager";

    public FTPClient FTPClient = null;

    public boolean ftpConnect(String host, String username, String password,int port) {
        try {
            FTPClient = new FTPClient();
            // connecting to the host
            FTPClient.connect(host, port);

            // now check the reply code, if positive mean connection success
            if (FTPReply.isPositiveCompletion(FTPClient.getReplyCode())) {
                // login using username & password
                boolean status = FTPClient.login(username, password);

                /*
                 * Set File Transfer Mode
                 *
                 * To avoid corruption issue you must specified a correct
                 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
                 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
                 * transferring text, image, and compressed files.
                 */
                FTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                FTPClient.enterLocalPassiveMode();

                return status;
            }
        } catch (Exception e) {
            Log.d(MODULE_TAG, "Error: could not connect to host " + host);
        }

        return false;
    }
}
