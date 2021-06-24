package com.example.portable_web_ide.main.ftp;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.portable_web_ide.MyApp;
import com.example.portable_web_ide.R;
import com.example.portable_web_ide.Section;
import com.example.portable_web_ide.main.local.LocalAddDialogFragment;
import com.example.portable_web_ide.main.local.LocalFileListAdapter;


import org.apache.commons.net.ftp.FTPFile;
import org.kohsuke.github.GitHub;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;


public class FtpFragment extends Fragment implements DialogInterface.OnDismissListener {

    private static final String MODULE_TAG = "FtpFragment";
    FtpManager ftpManager;
    ListView listView;
    FtpAddDialogFragment addServerfragmentDialog;
    FTPAddFileDialogFragment addFilefragmentDialog;
    ArrayList<Server> servers;
    List<FTPFile> files;
    private static final int SERVERS_ADAPTER = 0;
    private static final int FILES_ADAPTER = 1;
    int currentAdapter;
    FTPFile currentFile;
    Server currentServer;
    int listViewMode;
    ArrayList<FTPFile> multipleSelectedFiles;
    ArrayList<Server> multipleSelectedServers;
    Stack<FTPFile> navigationStack;

    private static final int LV_DEFAULT_MODE = 0;
    private static final int LV_MULTIPLE_SELECT_MODE = 1;
    private static final int LV_MOVE_MODE = 2;

    public FtpFragment() {
        // Required empty public constructor
        multipleSelectedFiles = new ArrayList<>();
        multipleSelectedServers = new ArrayList<>();
        navigationStack = new Stack<>();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void DownloadFile(FTPFile file,String previousDirectory) throws IOException {
        if(file.isFile())
        {
            String path = null;
            if(previousDirectory!=null)
            {
                path = previousDirectory+"/"+file.getName();
            }
            else
            {
                path = MyApp.getLocalDirectory().getPath()+"/"+file.getName();

            }
            ftpManager.ftpDownload(file.getName(),path);
        }
        if(file.isDirectory())
        {
            File directory;
            if(previousDirectory == null)
            {
                directory = new File(MyApp.getLocalDirectory().getPath()+"/"+file.getName());
            }
            else
            {
                directory = new File(previousDirectory + "/" + file.getName());
            }
            directory.mkdir();
            List<FTPFile> directoryFiles = ftpManager.getFileList(file.getName());
            for (FTPFile directoryFile:directoryFiles
            ) {
            DownloadFile(directoryFile,directory.getPath());
        }

        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        servers = new ArrayList<>();
        files = new ArrayList<>();
        currentAdapter = SERVERS_ADAPTER;
        View root = inflater.inflate(R.layout.fragment_ftp, container, false);

        Button downloadButton = getActivity().findViewById(R.id.download);
        downloadButton.setEnabled(false);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentAdapter)
                {
                    case FILES_ADAPTER:{
                        for (FTPFile file:multipleSelectedFiles
                        ) {
                        try {
                            DownloadFile(file,null);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                        break;
                    }
                }
            }
        });
        ImageButton buttonAdd = root.findViewById(R.id.add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentAdapter){
                    case SERVERS_ADAPTER:{
                        FragmentManager fragmentManager = getChildFragmentManager();
                        addServerfragmentDialog = FtpAddDialogFragment.newInstance();
                        addServerfragmentDialog.show(fragmentManager,"ftp_dialog_fragment");
                        break;
                    }
                    case FILES_ADAPTER:{
                        FragmentManager fragmentManager = getChildFragmentManager();
                        addFilefragmentDialog = FTPAddFileDialogFragment.newInstance(currentFile==null?null:currentFile.getName());
                        addFilefragmentDialog.show(fragmentManager,"local_add_dialog_fragment");
                        break;
                    }
                }

            }
        });
        Button selectButton = root.findViewById(R.id.select);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (currentAdapter)
                {
                    case SERVERS_ADAPTER:{

                        Log.i(MODULE_TAG, "Выбрать");
                        View view = getActivity().findViewById(R.id.ftp_select_panel);
                        view.setVisibility(view.isShown() ? View.GONE : View.VISIBLE);
                        selectButton.setText(view.isShown() ? "Отмена" : "Выбрать");

                        SetServerAdapter(currentServer, listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE ? LV_MULTIPLE_SELECT_MODE : LV_DEFAULT_MODE);

                        break;
                    }
                    case FILES_ADAPTER:{
                        Log.i(MODULE_TAG, "Выбрать");
                        View view = getActivity().findViewById(R.id.ftp_select_panel);
                        view.setVisibility(view.isShown() ? View.GONE : View.VISIBLE);
                        selectButton.setText(view.isShown() ? "Отмена" : "Выбрать");
                        try {
                            SetFileAdapter(currentFile, listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE ? LV_MULTIPLE_SELECT_MODE : LV_DEFAULT_MODE);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                        break;
                    }
                }
            }
        });
        ImageButton backButton = root.findViewById(R.id.back);
        backButton.setEnabled(false);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!navigationStack.isEmpty()) {
                    navigationStack.pop();
                    if (!navigationStack.isEmpty()) {
                        FTPFile file = navigationStack.pop();
                        try {
                            SetFileAdapter(file,LV_DEFAULT_MODE);
                        } catch (IOException exception) {
                            exception.printStackTrace();
                        }
                    }
                    else
                    {
                        ftpManager.ftpDisconnect();
                        SetServerAdapter(null,LV_DEFAULT_MODE);
                        //поменять картинку кнопки на серый
                        backButton.setEnabled(false);
                    }
                }
                else{
                    ftpManager.ftpDisconnect();
                    SetServerAdapter(null,LV_DEFAULT_MODE);
                    //поменять картинку кнопки на серый
                    backButton.setEnabled(false);
                }

            }
        });
        listView = root.findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(servers.size()!=0)
                {
                    switch (currentAdapter) {
                        case SERVERS_ADAPTER: {
                            Server selectedServer = servers.get(position);
                            ftpManager.ftpConnect(selectedServer);
                            switch (listViewMode) {
                                case LV_DEFAULT_MODE: {
                                    Log.i(MODULE_TAG,"Дефолтный сервак");
                                    try {
                                        SetFileAdapter(null,LV_DEFAULT_MODE);
                                        backButton.setEnabled(true);
                                    } catch (IOException exception) {
                                        exception.printStackTrace();
                                    }
                                    break;
                                }

                                case LV_MULTIPLE_SELECT_MODE: {

                                    Log.i(MODULE_TAG,"MULTIPLE MODE");
                                    CheckBox checkBox = view.findViewById(R.id.checkbox);
                                    if (!checkBox.isChecked()) {
                                        checkBox.setChecked(true);
                                        Log.i(MODULE_TAG, "Выбран файл " + selectedServer.getHostName());
                                        multipleSelectedServers.add(selectedServer);

                                    } else {
                                        Log.i(MODULE_TAG, "Значение выбора снято с файла " + selectedServer.getHostName());
                                        multipleSelectedServers.remove(selectedServer);
                                        checkBox.setChecked(false);
                                    }
                                    //Button moveButton = getActivity().findViewById(R.id.move);
                                    // moveButton.setEnabled(multipleSelectedServers.size()!=0 ? true : false);
                                    break;

                                }
                            }

                            break;
                        }
                        case FILES_ADAPTER: {
                            FTPFile selectedFile = files.get(position);
                            switch (listViewMode) {
                                case LV_DEFAULT_MODE: {

                                    if (selectedFile.isDirectory()) {
                                        try {
                                            SetFileAdapter(selectedFile, LV_DEFAULT_MODE);

                                            backButton.setEnabled(true);
                                        } catch (IOException exception) {
                                            exception.printStackTrace();
                                        }
                                    }
                                    break;
                                }
                                case LV_MULTIPLE_SELECT_MODE: {
                                    Log.i(MODULE_TAG,"MULTIPLE MODE");
                                    CheckBox checkBox = view.findViewById(R.id.checkbox);

                                    if (!checkBox.isChecked()) {
                                        checkBox.setChecked(true);
                                        Log.i(MODULE_TAG, "Выбран файл " + selectedFile.getName());
                                        multipleSelectedFiles.add(selectedFile);

                                    } else {
                                        Log.i(MODULE_TAG, "Значение выбора снято с файла " + selectedFile.getName());
                                        multipleSelectedFiles.remove(selectedFile);
                                        checkBox.setChecked(false);
                                    }
                                    downloadButton.setEnabled(multipleSelectedFiles.size()!=0 ? true : false);
                                    break;

                                }
                            }
                            break;

                        }
                    }
                }

            }
        });

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        switch (currentAdapter) {
            case SERVERS_ADAPTER: {
                SetServerAdapter(currentServer, listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE ? LV_DEFAULT_MODE : LV_MULTIPLE_SELECT_MODE);
                break;
            }
            case FILES_ADAPTER: {

                try {
                    SetFileAdapter(currentFile, listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE ? LV_DEFAULT_MODE : LV_MULTIPLE_SELECT_MODE);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {

        switch (currentAdapter) {
            case SERVERS_ADAPTER: {
                if(servers.size()>0)
                {
                    ftpManager.ftpDisconnect();
                }
                Log.i(MODULE_TAG,"Диалог добавления сервера");
                Log.i(MODULE_TAG, addServerfragmentDialog.hostName);
                Server server = new Server(addServerfragmentDialog.serverName, addServerfragmentDialog.hostName, addServerfragmentDialog.userName, addServerfragmentDialog.password, addServerfragmentDialog.port);
                ftpManager = ftpManager.getInstance();
                boolean status = ftpManager.ftpConnect(server);
                if (status) {
                    Log.i(MODULE_TAG, "Ура");
                    Log.i(MODULE_TAG, "Дефолтный путь " + null);
                    if(servers.contains(server))
                    {
                        Log.i(MODULE_TAG,"SERVER_CONTAINS");
                        //Написать, что сервер уже существует
                        CharSequence text = "Hello toast!";
                        int duration = Toast.LENGTH_SHORT;
                        Toast toast = Toast.makeText(MyApp.get(), text, duration);
                        toast.show();
                    }
                    else
                    {
                        Log.i(MODULE_TAG,"!SERVER_CONTAINS");
                        servers.add(server);
                        SetServerAdapter(server, LV_DEFAULT_MODE);
                    }

                } else {
                    Log.i(MODULE_TAG, "Подключение не удалось(");
                }
                break;
            }
            case FILES_ADAPTER:{
                Log.i(MODULE_TAG,"Диалог добавления файла");
                if(currentFile==null)
                {
                    SetServerAdapter(currentServer,LV_DEFAULT_MODE);
                }
                else
                {
                    try {
                        SetFileAdapter(currentFile,LV_DEFAULT_MODE);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
                break;
            }
        }





        //SetFileAdapter(addFilefragmentDialog.newFile.getParentFile(),LV_DEFAULT_MODE);

    }
    public void SetServerAdapter(Server selectedServer,int mode){
        currentAdapter = SERVERS_ADAPTER;
        currentServer = selectedServer;
        FTPServerListAdapter serverListAdapter = null;
        switch (mode)
        {

            case LV_DEFAULT_MODE:{
                Log.i(MODULE_TAG, "Одиночный режим выбора");
                //registerForContextMenu(listView);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                serverListAdapter = new FTPServerListAdapter(MyApp.get(),R.layout.text_row_item,servers);
                break;
            }
            case LV_MULTIPLE_SELECT_MODE:{
                //unregisterForContextMenu(listView);
                Log.i(MODULE_TAG, "Множественный режим выбора");
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                serverListAdapter = new FTPServerListAdapter(MyApp.get(),R.layout.checkable_text_row_item,servers);
                break;
            }
            case LV_MOVE_MODE:{
                Log.i(MODULE_TAG, "Режим перемещения");
                //unregisterForContextMenu(listView);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                serverListAdapter = new FTPServerListAdapter(MyApp.get(),R.layout.checkable_text_row_item,servers);
                break;
            }
        }
        listViewMode = mode;
        listView.setAdapter(serverListAdapter);
    }
    public void SetFileAdapter(FTPFile selectedFile,int mode) throws IOException {

        currentFile = selectedFile;
        navigationStack.push(currentFile);
        currentAdapter = FILES_ADAPTER;
        FTPFileListAdapter fileListAdapter = null;
        if(currentFile==null)//Сервееер зовееет)
        {
            files = ftpManager.getFileList(null);
        }
        else
        {
            files = ftpManager.getFileList(currentFile.getName());
        }
        switch (mode)
        {
            case LV_DEFAULT_MODE:{
                //registerForContextMenu(listView);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                fileListAdapter = new FTPFileListAdapter(MyApp.get(), R.layout.text_row_item,files);
                break;
            }
            case LV_MULTIPLE_SELECT_MODE:{
                //unregisterForContextMenu(listView);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                fileListAdapter = new FTPFileListAdapter(MyApp.get(), R.layout.checkable_text_row_item,files);
                break;
            }
            case LV_MOVE_MODE:{
                //unregisterForContextMenu(listView);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                fileListAdapter = new FTPFileListAdapter(MyApp.get(), R.layout.text_row_item,files);
                break;
            }
        }
        listViewMode = mode;
        listView.setAdapter(fileListAdapter);

    }

}