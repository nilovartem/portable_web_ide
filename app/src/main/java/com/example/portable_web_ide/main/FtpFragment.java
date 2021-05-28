package com.example.portable_web_ide.main;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.portable_web_ide.R;
import com.example.portable_web_ide.Section;

import org.apache.commons.net.ftp.FTPClient;


public class FtpFragment extends Section implements DialogInterface.OnDismissListener {

    private static final String MODULE_TAG = "FtpFragment";
    FtpDialogFragment fragmentDialog;
    public String serverName;
    public String hostName;
    public String userName;
    public String password;
    public int port;
    public FtpFragment() {
        // Required empty public constructor


    }


    /*
    public static FtpFragment newInstance(String param1, String param2) {
        FtpFragment fragment = new FtpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);




    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_ftp, container, false);
        View include = root.findViewById(R.id.include);
        Toolbar toolbar = include.findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.menu_ftp);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_add:{
                        //показать модальное окно
                        FragmentManager fragmentManager = getChildFragmentManager();
                        fragmentDialog = FtpDialogFragment.newInstance();

                        fragmentDialog.show(fragmentManager,"ftp_dialog_fragment");

                        return true;
                    }
                    default:{

                        return false;
                    }
                }
            }
        });
        return root;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        Log.i(MODULE_TAG,fragmentDialog.hostName);
        FtpManager ftpManager = new FtpManager();
        boolean status = ftpManager.ftpConnect(hostName,userName,password,port);
        if(status){
            Log.i(MODULE_TAG,"Ура");
        }
        else
        {
            Log.i(MODULE_TAG,":(((((");
        }


    }
}