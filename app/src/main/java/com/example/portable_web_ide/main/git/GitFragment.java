package com.example.portable_web_ide.main.git;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.StrictMode;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.portable_web_ide.MyApp;
import com.example.portable_web_ide.R;
import com.example.portable_web_ide.Section;
import com.example.portable_web_ide.main.ftp.FTPFileListAdapter;
import com.example.portable_web_ide.main.ftp.FtpAddDialogFragment;
import com.example.portable_web_ide.main.ftp.Server;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPFile;
import org.kohsuke.github.GHAuthorization;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHMyself;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHUser;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.kohsuke.github.PagedIterable;
import org.kohsuke.github.function.InputStreamFunction;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class GitFragment extends Fragment implements DialogInterface.OnDismissListener {

    private static final String MODULE_TAG = "GitFragment";
    GitAddDialogFragment addGitFragmentDialog;
    List<GHRepository> repositories;
    ArrayList<GHRepository> multipleSelectedRepositories;
    ListView listView;
    int listViewMode;
    private static final int LV_DEFAULT_MODE = 0;
    private static final int LV_MULTIPLE_SELECT_MODE = 1;
    private static final int LV_MOVE_MODE = 2;
    GitHub github;


    public GitFragment() {
        // Required empty public constructor
        multipleSelectedRepositories = new ArrayList<>();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repositories = new ArrayList<>();
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //Log.i(MODULE_TAG,String.valueOf(gitHub.getUser("nilovartem").getName()));
        View root = inflater.inflate(R.layout.fragment_git, container, false);
        //SetRepoAdapter();
        ImageButton buttonAdd = root.findViewById(R.id.add);
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getChildFragmentManager();
                addGitFragmentDialog = GitAddDialogFragment.newInstance();
                addGitFragmentDialog.show(fragmentManager,"git_dialog_fragment");
            }
        });

        Button downloadButton = getActivity().findViewById(R.id.git_download);
        downloadButton.setEnabled(false);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MODULE_TAG,"Кнопка скачать");
                for (GHRepository repository:multipleSelectedRepositories
                ) {
                    try {
                        DownloadRepository(repository);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
            }
        });
        listView = root.findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GHRepository selectedRepository = repositories.get(position);
                switch (listViewMode) {
                    case LV_DEFAULT_MODE: {
                        SetRepoAdapter(LV_DEFAULT_MODE);
                        //backButton.setEnabled(true);
                        break;
                    }

                    case LV_MULTIPLE_SELECT_MODE: {

                        Log.i(MODULE_TAG, "MULTIPLE MODE");
                        CheckBox checkBox = view.findViewById(R.id.checkbox);
                        if (!checkBox.isChecked()) {
                            checkBox.setChecked(true);
                            multipleSelectedRepositories.add(selectedRepository);
                            //Log.i(MODULE_TAG, "Выбран файл " + selectedServer.getHostName());
                            //multipleSelectedServers.add(selectedServer);

                        } else {
                           //Log.i(MODULE_TAG, "Значение выбора снято с файла " + selectedServer.getHostName());
                            //multipleSelectedServers.remove(selectedServer);
                            multipleSelectedRepositories.remove(selectedRepository);
                            checkBox.setChecked(false);
                        }
                        downloadButton.setEnabled(multipleSelectedRepositories.size()!=0 ? true : false);
                    }
                    break;
                }
            }
        });
        Button selectButton = root.findViewById(R.id.select);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MODULE_TAG, "Выбрать");
                multipleSelectedRepositories.clear();
                View view = getActivity().findViewById(R.id.git_select_panel);
                view.setVisibility(view.isShown() ? View.GONE : View.VISIBLE);
                selectButton.setText(view.isShown() ? "Отмена" : "Выбрать");
                SetRepoAdapter(listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE ? LV_MULTIPLE_SELECT_MODE : LV_DEFAULT_MODE);
            }
        });

        return root;

    }
    public void DownloadRepository(GHRepository repository) throws IOException {

        //List<GHRepository> repos = github.getMyself().listRepositories().toList();
        Log.i(MODULE_TAG,"Скачать репозиторий" + repository.getName());
        InputStreamFunction<ZipFile> repositoryInputStreamFunction = new InputStreamFunction<ZipFile>() {
            @Override
            public ZipFile apply(InputStream inputStream) throws IOException {
               //github.checkAuth(addGitFragmentDialog.userName,addGitFragmentDialog.tokenValue);
               //inputStream.close();\
               //reader.toString();
                //inputStream.close();

               //String string = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
               //Log.i(MODULE_TAG,"INPUT STREAM");
               //Log.i(MODULE_TAG,string);

               //byte[] buffer = IOUtils.toByteArray(inputStream);
                byte[] buffer = IOUtils.toByteArray(inputStream);
                if(buffer != null) {


                    Log.i(MODULE_TAG, "Длина буфера " + buffer.length);



                    File file = new File(MyApp.getLocalDirectory() + "/" + repository.getName() + ".zip");
                    file.createNewFile();
                    FileUtils.writeByteArrayToFile(file, buffer);//РАБОТАЕТ, если писать в txt

                    try {
                        net.lingala.zip4j.ZipFile zipFile = new net.lingala.zip4j.ZipFile(file.getPath());

                        zipFile.extractAll(MyApp.getLocalDirectory() + "/" + repository.getName());
                    }
                    catch (Exception exception)
                    {
                        exception.printStackTrace();
                    }

/*
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ZipOutputStream zos = new ZipOutputStream(baos);
                    ZipEntry entry = new ZipEntry(file.getName());
                    entry.setSize(buffer.length);
                    zos.putNextEntry(entry);
                    zos.write(buffer);
                    zos.closeEntry();
                    zos.close();*/
                }
               return null;
            }
        };

        ZipFile zipFile = repository.readZip(repositoryInputStreamFunction,repository.getDefaultBranch());
        //zipFile.getName();

        //Log.i(MODULE_TAG,zipFile.getName());
        //GHContent ghContent = (GHContent) repository.getDirectoryContent(null);
        //Log.i(MODULE_TAG,ghContent.getName());
    }


    @Override
    public void onDismiss(DialogInterface dialog) {

        try {
            //addGitFragmentDialog.tokenValue = "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";
            //addGitFragmentDialog.userName = "nilovartem";
            github = new GitHubBuilder().withOAuthToken(addGitFragmentDialog.tokenValue,addGitFragmentDialog.userName).build();
            Log.i(MODULE_TAG,"Репозитории");
            GHMyself me = github.getMyself();
            GHRepository f = me.listRepositories().toList().get(0);


            repositories = me.listRepositories().toList();

            /*
            for (GHRepository repo:repositories
            ) {
                Log.i(MODULE_TAG,repo.getFullName());
            }*/


        } catch (IOException exception) {
            exception.printStackTrace();
        }
        SetRepoAdapter(LV_DEFAULT_MODE);
    }

    @Override
    public void onResume() {
        super.onResume();
        SetRepoAdapter(LV_DEFAULT_MODE);
    }

    public void SetRepoAdapter(int mode) {

        Log.i(MODULE_TAG, "SetFileAdapter");
        Log.i(MODULE_TAG, "Кол-во репозиториев " + repositories.size());
        GitListAdapter gitListAdapter = null;
        //currentFile = selectedFile;
        if (repositories.size() > 0) {
            switch (mode)
            {
                case LV_DEFAULT_MODE:{
                    //registerForContextMenu(listView);
                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    gitListAdapter = new GitListAdapter(MyApp.get(), R.layout.text_row_item, repositories);
                    break;
                }
                case LV_MULTIPLE_SELECT_MODE:{
                    //unregisterForContextMenu(listView);

                    listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                    gitListAdapter = new GitListAdapter(MyApp.get(), R.layout.checkable_text_row_item, repositories);
                    break;
                }
                case LV_MOVE_MODE:{
                    //unregisterForContextMenu(listView);
                    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                    gitListAdapter = new GitListAdapter(MyApp.get(), R.layout.text_row_item, repositories);
                    break;
                }
            }
        }
        listViewMode = mode;
        listView.setAdapter(gitListAdapter);
    }

}