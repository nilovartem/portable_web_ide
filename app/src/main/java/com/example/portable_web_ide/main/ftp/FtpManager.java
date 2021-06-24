package com.example.portable_web_ide.main.ftp;

import android.os.StrictMode;
import android.util.Log;
import android.util.MonthDisplayHelper;
import android.widget.ListView;

import com.example.portable_web_ide.MyApp;
import com.example.portable_web_ide.R;
import com.example.portable_web_ide.main.local.LocalFileListAdapter;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FtpManager {

    private static FtpManager ftpManager = null;
    FTPClient ftpClient;

    private static final String MODULE_TAG = "FtpManager";

    public FtpManager() {
        ftpClient = null;
    }

    public static FtpManager getInstance() {
        if (ftpManager == null) {
            ftpManager = new FtpManager();
        }
        return ftpManager;
    }

    public boolean ftpConnect(Server server) {

        //StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        try {
            ftpClient = new FTPClient();
            // connecting to the host
            Log.i(MODULE_TAG, server.hostName);
            Log.i(MODULE_TAG, String.valueOf(server.port));
            ftpClient.connect(server.hostName, server.port);


            // now check the reply code, if positive mean connection success
            if (FTPReply.isPositiveCompletion(ftpClient.getReplyCode())) {
                // login using username & password
                boolean status = ftpClient.login(server.userName, server.password);

                /*
                 * Set File Transfer Mode
                 *
                 * To avoid corruption issue you must specified a correct
                 * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
                 * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE for
                 * transferring text, image, and compressed files.
                 */
                ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();


                return status;
            }
        } catch (Exception e) {
            Log.e(MODULE_TAG, e.toString());

        }

        return false;
    }


    public boolean ftpDisconnect() {
        try {
            ftpClient.logout();
            ftpClient.disconnect();
            Log.i(MODULE_TAG, "Отключение от сервера");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean ftpMakeDirectory(String new_dir_path) {
        try {
            boolean status = ftpClient.makeDirectory(new_dir_path);
            return status;

        } catch (Exception e) {
            e.printStackTrace();

        }

        return false;
    }


    public String getCurrentWorkingDirectory() {
        try {

            String workingDir = ftpClient.printWorkingDirectory();

            return workingDir;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean ftpDownload(String srcFilePath, String desFilePath) {
        boolean status = false;
        try {
            FileOutputStream desFileStream = new FileOutputStream(desFilePath);
            status = ftpClient.retrieveFile(srcFilePath, desFileStream);
            desFileStream.close();
            return status;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public List<FTPFile> getFileList(String path) throws IOException {

        return Arrays.asList(ftpClient.listFiles(path));
    }


}
