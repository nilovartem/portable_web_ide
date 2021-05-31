package com.example.portable_web_ide.main.local;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.portable_web_ide.MyApp;
import com.example.portable_web_ide.R;
import com.example.portable_web_ide.Section;
import com.example.portable_web_ide.editor.ActiveFiles;
import com.example.portable_web_ide.editor.EditFilesActivity;
import com.example.portable_web_ide.main.local.LocalAddDialogFragment;
import com.example.portable_web_ide.main.local.LocalFileListAdapter;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class LocalFragment extends Fragment implements DialogInterface.OnDismissListener{

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String MODULE_TAG = "LocalFragment";
    private static final int LV_DEFAULT_MODE = 0;
    private static final int LV_MULTIPLE_SELECT_MODE = 1;
    private static final int LV_MOVE_MODE = 2;
    LocalAddDialogFragment fragmentDialog;
    ListView listView;
    public int listViewMode;
    File currentFile;
    ArrayList<File> files;
    ArrayList<File> multipleSelectedFiles;

    public LocalFragment(){
        multipleSelectedFiles = new ArrayList<>();
        listViewMode = LV_DEFAULT_MODE;
    }
/*
    public static LocalFragment newInstance(int index) {
        LocalFragment fragment = new LocalFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }
*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_local, container, false);
        View include = root.findViewById(R.id.include);
        Toolbar toolbar = include.findViewById(R.id.toolbar);
        ImageButton backButton = root.findViewById(R.id.back);
        backButton.setEnabled(false);
        ImageButton homeButton = root.findViewById(R.id.home);
        homeButton.setEnabled(false);
        ImageButton addButton = root.findViewById(R.id.add);


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentDialog = LocalAddDialogFragment.newInstance(currentFile.getPath());
                fragmentDialog.show(fragmentManager,"local_add_dialog_fragment");
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(MODULE_TAG,"Текущий путь " + currentFile.getPath());
                Log.i(MODULE_TAG,"Стандартный путь " + MyApp.getLocalDirectory().getPath());

                if(!currentFile.equals(MyApp.getLocalDirectory())){
                    Log.i(MODULE_TAG,"Директории разные!");
                    File parentFile = currentFile.getParentFile();
                    SetListViewAdapter(parentFile,LV_DEFAULT_MODE);
                    if(parentFile.equals(MyApp.getLocalDirectory()))
                    {
                        backButton.setEnabled(false);
                        backButton.setImageResource(R.drawable.ic_back_inactive_24);
                        homeButton.setEnabled(false);
                        homeButton.setImageResource(R.drawable.ic_home_inactive_24);

                    }
                }


            }
        });
        Button selectButton = root.findViewById(R.id.select);
        selectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i(MODULE_TAG,"Выбрать");
                multipleSelectedFiles.clear();
                View view = getActivity().findViewById(R.id.local_select_panel);
                view.setVisibility(view.isShown() ? View.GONE : View.VISIBLE);
                selectButton.setText(view.isShown() ? "Отмена" : "Выбрать");
                SetListViewAdapter(currentFile, listView.getChoiceMode() == ListView.CHOICE_MODE_SINGLE ? LV_MULTIPLE_SELECT_MODE : LV_DEFAULT_MODE);
                Button moveButton = getActivity().findViewById(R.id.move_button);
                moveButton.setEnabled(false);

            }
        });
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!currentFile.equals(MyApp.getLocalDirectory())){

                        SetListViewAdapter(MyApp.getLocalDirectory(),LV_DEFAULT_MODE);
                        homeButton.setEnabled(false);
                        homeButton.setImageResource(R.drawable.ic_home_inactive_24);
                        backButton.setEnabled(false);
                        backButton.setImageResource(R.drawable.ic_back_inactive_24);
                }

            }
        });
        //Выводим файлы в стандартное представление списка
        listView = root.findViewById(R.id.listView);
        File initialDirectory = new File(MyApp.getLocalDirectory().getPath());
        Log.i(MODULE_TAG,"Начальная директория " + initialDirectory.getPath());
        SetListViewAdapter(initialDirectory,LV_DEFAULT_MODE);
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(MODULE_TAG, "Клик");
                File selectedFile = files.get(position);

                switch (listViewMode) {
                    case LV_DEFAULT_MODE: {
                        Log.i(MODULE_TAG,"DEFAULT MODE");

                        if (selectedFile.isDirectory()) {

                            backButton.setEnabled(true);
                            backButton.setImageResource(R.drawable.ic_back_24);


                            homeButton.setEnabled(true);
                            homeButton.setImageResource(R.drawable.ic_home_24);

                            SetListViewAdapter(selectedFile, LV_DEFAULT_MODE);

                        }
                        if (selectedFile.isFile()) {

                            //Открыть редактор файлов
                            Intent intent = new Intent(getActivity(), EditFilesActivity.class);
                            Log.i(MODULE_TAG, "Открываем файл" + selectedFile.getPath());
                            intent.putExtra("filePath", selectedFile.getPath());
                            if (!ActiveFiles.getInstance().filesUri.contains(Uri.fromFile(selectedFile))) {
                                ActiveFiles.getInstance().filesUri.add(Uri.fromFile(selectedFile));
                                startActivity(intent);
                            } else {
                                Log.i(MODULE_TAG, "Внимание!!!Такой файл уже открыт!");
                                startActivity(intent);
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
                        Button moveButton = getActivity().findViewById(R.id.move_button);
                        moveButton.setEnabled(multipleSelectedFiles.size()!=0 ? true : false);
                        break;

                    }
                    case LV_MOVE_MODE: {
                        //Выбор места для переноса файла
                        Log.i(MODULE_TAG,"MOVE MODE");
                        if (selectedFile.isDirectory()) {
                            SetListViewAdapter(selectedFile, LV_MOVE_MODE);

                            backButton.setEnabled(true);
                            backButton.setImageResource(R.drawable.ic_back_24);

                            ImageButton homeButton = getView().findViewById(R.id.home);
                            homeButton.setEnabled(true);
                            homeButton.setImageResource(R.drawable.ic_home_24);

                        }

                        Log.i(MODULE_TAG,"Выбран" + selectedFile.getName());
                        break;
                    }
                }
            }
        });
        //отлов кнопок нижней панели
        Button moveButton = getActivity().findViewById(R.id.move_button);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MODULE_TAG,"Нажата кнопка Переместить");
                SetListViewAdapter(currentFile, LV_MOVE_MODE);
                selectButton.setEnabled(false);
                selectButton.setText("Выбрать");
                View firstPanel = getActivity().findViewById(R.id.local_select_panel);
                firstPanel.setVisibility(View.GONE);
                View secondPanel = getActivity().findViewById(R.id.local_move_panel);
                secondPanel.setVisibility(View.VISIBLE);

                //поменять layout

            }
        });
        Button transferButton = getActivity().findViewById(R.id.transfer_ftp);
        transferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO:вызвать метод вставки в ftp_fragment
            }
        });
        Button moveHere = getActivity().findViewById(R.id.move_here);
        moveHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MODULE_TAG,"Нажата кнопка переместить сюда");

                for (File selectedFile:multipleSelectedFiles
                     ) {
                    try {
                        moveFile(selectedFile,currentFile);
                    } catch (IOException exception) {
                        exception.printStackTrace();
                    }
                }
                SetListViewAdapter(currentFile,LV_DEFAULT_MODE);
                View secondPanel = getActivity().findViewById(R.id.local_move_panel);
                secondPanel.setVisibility(View.GONE);
                selectButton.setEnabled(true);
                multipleSelectedFiles.clear();
            }
        });
        Button cancelMove = getActivity().findViewById(R.id.cancel_move);
        cancelMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MODULE_TAG,"Нажата кнопка Отмена");
                multipleSelectedFiles.clear();
                SetListViewAdapter(currentFile,LV_DEFAULT_MODE);
                View secondPanel = getActivity().findViewById(R.id.local_move_panel);
                secondPanel.setVisibility(View.GONE);
                selectButton.setEnabled(true);
            }
        });

        return root;
    }
    public void moveFile(File file,File previousDirectory) throws IOException {
        if(file.isFile())
        {
            File destFile = new File(previousDirectory.getPath()+"/"+file.getName());
            file.renameTo(destFile);
        }
        if(file.isDirectory())
        {
            File destFile = new File(previousDirectory.getPath()+"/"+file.getName());
            file.renameTo(destFile);
            List<File> directoryFiles = Arrays.asList(destFile.listFiles());
            for (File directoryFile:directoryFiles
            ) {
                moveFile(directoryFile,destFile);
            }


        }
    }
    void createFileDialog(String type){

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

        alert.setTitle("Новый объект");

        final EditText editText = new EditText(getContext());

        alert.setView(editText);
        alert.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String fileName = editText.getText().toString();
                String path = currentFile.getPath();
                //удалить всякие спецсимволы, типа точек, слешей и тд.
                File file = new File(path + "/" + fileName);

                if(type == "folder")
                {
                    Log.i(MODULE_TAG,"Диалог - новая папка " + file.getPath());
                    file.mkdir();
                }
                if(type == "file")
                {
                    try {
                        Log.i(MODULE_TAG,"Диалог - новый файл " + file.getPath());
                        file.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                //обновляем коллекцию
                SetListViewAdapter(file.getParentFile(),LV_DEFAULT_MODE);

            }
        });

        alert.setNegativeButton("Отмена", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert.show();
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.menu_context_file,menu);
    }
    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();

        switch (item.getItemId())
        {
            case R.id.action_rename_file:{
                Log.i(MODULE_TAG,"Переименовать файл " + String.valueOf(info.position));

                AlertDialog.Builder alert = new AlertDialog.Builder(getContext());

                alert.setTitle("Переименование");
                //alert.setMessage("Message");
                // Set an EditText view to get user input
                final EditText editText = new EditText(getContext());
                editText.setText(files.get(info.position).getName());
                alert.setView(editText);
                alert.setPositiveButton("Ок", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String value = editText.getText().toString();

                        String path = files.get(info.position).getPath();

                        ArrayList<String> arrayList = new ArrayList<>(Arrays.asList(path.split("/")));
                        arrayList.remove(0);
                        arrayList.set(arrayList.size()-1,value);
                        String newPath = new String();
                        for (String s:arrayList
                        ) {
                            Log.i(MODULE_TAG, "Символ" + s);
                            newPath += "/" + s;
                        }
                        Log.i(MODULE_TAG,"Новая строка " + newPath);
                        //Убираем пробелы(на всякий случай)
                        newPath.trim();
                        File renamedFile = new File(newPath);
                        files.get(info.position).renameTo(renamedFile);
                        //обновляем коллекцию
                        SetListViewAdapter(files.get(info.position).getParentFile(),LV_DEFAULT_MODE);

                    }
                });

                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });
                alert.show();
                return true;
            }
            case R.id.action_delete_file:{

                /*Старый метод
                Log.i(MODULE_TAG,"Удалить файл" + String.valueOf(info.position));
                File parentFile = files.get(info.position).getParentFile();
                files.get(info.position).delete();
                SetListViewAdapter(parentFile,LV_DEFAULT_MODE);
                return true;*/
                File parentFile = files.get(info.position).getParentFile();
                ActiveFiles activeFiles = ActiveFiles.getInstance();
                try {
                    DeleteFile(files.get(info.position),activeFiles);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                SetListViewAdapter(parentFile,LV_DEFAULT_MODE);
            }
            default:{
                return super.onContextItemSelected(item);
            }
        }
    }
    public void DeleteFile(File file,ActiveFiles activeFiles) throws IOException {
        if(file.isFile())
        {
            Uri fileUri = Uri.fromFile(file);
            if(activeFiles.filesUri.contains(fileUri))
            {
                activeFiles.filesUri.remove(fileUri);
            }
            file.delete();
        }
        if(file.isDirectory())
        {
            List<File> directoryFiles = Arrays.asList(file.listFiles());
            for (File directoryFile:directoryFiles
            ) {

                DeleteFile(directoryFile,activeFiles);
            }

            file.delete();

        }
    }


    public void SetListViewAdapter(File fileDirectory, int mode){

        currentFile = fileDirectory;

        files = new ArrayList<>(Arrays.asList(fileDirectory.listFiles()));
        LocalFileListAdapter fileListAdapter = null;
        switch (mode)
        {
            case LV_DEFAULT_MODE:{

                registerForContextMenu(listView);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                fileListAdapter = new LocalFileListAdapter(MyApp.get(),R.layout.text_row_item,files);
                break;

            }
            case LV_MULTIPLE_SELECT_MODE:{
                unregisterForContextMenu(listView);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
                fileListAdapter = new LocalFileListAdapter(MyApp.get(), R.layout.checkable_text_row_item,files);
                break;

            }
            case LV_MOVE_MODE:{
                unregisterForContextMenu(listView);
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
                fileListAdapter = new LocalFileListAdapter(MyApp.get(),R.layout.text_row_item,files);
                break;

            }
        }
        listViewMode = mode;
        Log.i(MODULE_TAG,"Режим"+ String.valueOf(mode));


        //old

        listView.setAdapter(fileListAdapter);

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
       SetListViewAdapter(fragmentDialog.newFile.getParentFile(),LV_DEFAULT_MODE);
    }
}