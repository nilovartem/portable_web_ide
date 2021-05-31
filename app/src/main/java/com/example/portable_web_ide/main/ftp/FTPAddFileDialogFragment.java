package com.example.portable_web_ide.main.ftp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.portable_web_ide.R;

import java.io.File;
import java.io.IOException;

public class FTPAddFileDialogFragment extends DialogFragment {
    private static final String MODULE_TAG = "LocalAddDialogFragment";
    String currentPath;

    Fragment parentFragment;


    public static FTPAddFileDialogFragment newInstance(String currentPath) {
        FTPAddFileDialogFragment fragment = new FTPAddFileDialogFragment();
        fragment.currentPath = currentPath;
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL,android.R.style.ThemeOverlay_Material_ActionBar);
        //newFile = null;
        //setStyle(DialogFragment.STYLE_NORMAL, R.style.MY_DIALOG);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        parentFragment = getParentFragment();
        View root = inflater.inflate(R.layout.ftp_add_file_dialog,container,false);

        Button cancelButton = root.findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(MODULE_TAG,"Отмена");
                getDialog().dismiss();

            }
        });
        //AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.0F);
        Button createFolderButton = root.findViewById(R.id.create_folder);
        createFolderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //v.startAnimation(buttonClick);
                Log.i(MODULE_TAG,"Нажатие на кнопку create Folder");
                createFileDialog();


            }
        });


        return root;

    }
    void createFileDialog(){

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        FtpManager ftpManager = FtpManager.getInstance();

        alert.setTitle("Создать папку");


        final EditText editText = new EditText(getContext());
        alert.setView(editText);
        alert.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String fileName = editText.getText().toString();
                String path = currentPath;
                //удалить всякие спецсимволы, типа точек, слешей и тд.
                ftpManager.ftpMakeDirectory(path + "/" + fileName);
                getDialog().dismiss();
                if (parentFragment instanceof DialogInterface.OnDismissListener) {
                    ((DialogInterface.OnDismissListener) parentFragment).onDismiss(getDialog());
                }
            }
        });
        alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

}
