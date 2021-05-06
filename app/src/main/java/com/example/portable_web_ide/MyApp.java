package com.example.portable_web_ide;

import android.app.Application;
import android.content.Context;

public class MyApp{

    private Context appContext;

    private static final MyApp ourInstance = new MyApp();
    public void init(Context context) {
        if(appContext == null) {
            this.appContext = context;
        }
    }
    private Context getContext() {
        return appContext;
    }
    public static Context get() {
        return getInstance().getContext();
    }
    public static synchronized MyApp getInstance() {
        return ourInstance;
    }
    private MyApp() { }
}
