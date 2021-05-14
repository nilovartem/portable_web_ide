package com.example.portable_web_ide;

import android.app.Application;
import android.content.Context;

import java.io.File;

public class MyApp{

    private Context appContext;
    private File localDirectory;
    private File ftpDirectory;
    private File gitDirectory;

    private static final MyApp ourInstance = new MyApp();


    public void initContext(Context context) {
        if(appContext == null) {
            this.appContext = context;
        }
    }

    public void initLocalDirectory(File file){
        if(localDirectory == null){
            this.localDirectory = file;
        }
    }

    public void initFtpDirectory(File file){
        if(ftpDirectory == null){
            this.ftpDirectory = file;
        }
    }

    public void initGitDirectory(File file){
        if(gitDirectory == null){
            this.gitDirectory = file;
        }
    }


    private Context getContext() {
        return appContext;
    }


    public static Context get() {
        return getInstance().getContext();
    }

    public static File getLocalDirectory() {
        return getInstance().localDirectory;
    }

    public static File getFtpDirectory() {
        return getInstance().ftpDirectory;
    }

    public static File getGitDirectory() {
        return getInstance().gitDirectory;
    }



    public static synchronized MyApp getInstance() {
        return ourInstance;
    }
    private MyApp() { }
}
