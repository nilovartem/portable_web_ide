package com.example.portable_web_ide.editor;


import android.net.Uri;

import com.example.portable_web_ide.MyApp;

import java.util.ArrayList;

public class ActiveFiles {


    private static final ActiveFiles instance = new ActiveFiles();

    public ArrayList<Uri> filesUri;

    public static synchronized ActiveFiles getInstance() {

        return instance;
    }
    private ActiveFiles() { }

}
