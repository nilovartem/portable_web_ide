package com.example.portable_web_ide.main;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.portable_web_ide.R;

public class FtpDialogFragment extends DialogFragment {
    private static final String MODULE_TAG = "FtpDialogFragment";
    public String serverName;
    public String hostName;
    public String userName;
    public String password;
    public int port;

    public FtpDialogFragment()
    {

    }
    public static FtpDialogFragment newInstance() {
        FtpDialogFragment fragment = new FtpDialogFragment();

       return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.ThemeOverlay_Material_ActionBar);
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_ftp_dialog,container,false);
        Toolbar toolbar = root.findViewById(R.id.toolbar);

        Button buttonDone = root.findViewById(R.id.done);
        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MODULE_TAG,"Кнопка Done");
                try {
                    //сохраняем данные
                    serverName = ((EditText) root.findViewById(R.id.server_name)).getText().toString();
                    hostName = ((EditText) root.findViewById(R.id.host_name)).getText().toString();
                    userName = ((EditText) root.findViewById(R.id.user_name)).getText().toString();
                    password = ((EditText) root.findViewById(R.id.password)).getText().toString();
                    port = Integer.getInteger(((EditText) root.findViewById(R.id.password)).getText().toString());
                }
                catch (Exception exception){

                    Log.i(MODULE_TAG,"Не введены все данные");
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Вы ввели не все данные!!!!");
                    builder.setCancelable(true);

                    builder.setPositiveButton(
                            "Ок",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();
                    //return;
                }
                getDialog().dismiss();
                Fragment parentFragment = getParentFragment();
                if (parentFragment instanceof DialogInterface.OnDismissListener) {
                    ((DialogInterface.OnDismissListener) parentFragment).onDismiss(getDialog());
                }


            }
        });
        //toolbar.inflateMenu(R.menu.menu_ftp_add_dialog);

        return root;

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
       // dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        return dialog;
    }



}
