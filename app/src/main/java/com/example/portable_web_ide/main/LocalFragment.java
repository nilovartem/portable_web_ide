 package com.example.portable_web_ide.main;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.portable_web_ide.MyApp;
import com.example.portable_web_ide.R;
import com.example.portable_web_ide.editor.ActiveFiles;
import com.example.portable_web_ide.editor.EditFilesActivity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class LocalFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String MODULE_TAG = "LocalFragment";
    ListView listView;
    File currentFile;
    ArrayList<File> files;
    public LocalFragment(){

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
        toolbar.inflateMenu(R.menu.menu_local);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_back:{

                        //Log.i(MODULE_TAG, "Текущий путь" + currentFile.getParent());
                       //File initialFile = MyApp.get().getFilesDir();
                        //Log.i(MODULE_TAG, "Начальный путь" + MyApp.get().getFilesDir().getPath());

                        Log.i(MODULE_TAG,"Текущий путь " + currentFile.getPath());
                        Log.i(MODULE_TAG,"Стандартный путь " + MyApp.getLocalDirectory().getPath());
                        if(currentFile.equals(MyApp.getLocalDirectory()))
                        {
                            Log.i(MODULE_TAG,"Директории равны");
                        }
                        else
                        {
                            Log.i(MODULE_TAG,"Директории отличны");
                        }
                        if(!currentFile.equals(MyApp.getLocalDirectory())){

                            SetListViewAdapter(currentFile.getParentFile());
                            return true;
                        }
                        else
                        {
                            return false;
                        }

                    }
                    case R.id.create_folder:{
                        Log.i(MODULE_TAG,"Создать папку");
                        createFileDialog("folder");
                        return true;
                    }
                    case R.id.create_file:{
                        Log.i(MODULE_TAG,"Создать файл");
                        createFileDialog("file");
                        return true;
                    }
                    default:{

                        return false;
                    }
                }

            }
        });

        listView = root.findViewById(R.id.listView);

        File initialDirectory = new File(MyApp.getLocalDirectory().getPath());
        Log.i(MODULE_TAG,"Начальная директория " + initialDirectory.getPath());
        SetListViewAdapter(initialDirectory);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Получаем директорию, отправляем в GetFiles
                File selectedFile = files.get(position);
                if(selectedFile.isDirectory())
                {
                    SetListViewAdapter(selectedFile);
                }
                if(selectedFile.isFile())
                {
                    //Открыть редактор файлов
                    Intent intent = new Intent(getActivity(), EditFilesActivity.class);
                    Log.i(MODULE_TAG,"Открываем файл" + selectedFile.getPath());
                    intent.putExtra("filePath",selectedFile.getPath());
                    if(!ActiveFiles.getInstance().filesUri.contains(Uri.fromFile(selectedFile)))
                    {
                        ActiveFiles.getInstance().filesUri.add(Uri.fromFile(selectedFile));
                        startActivity(intent);
                    }
                    else
                    {
                        Log.i(MODULE_TAG,"Внимание!!!Такой файл уже открыт!");
                        startActivity(intent);
                    }

                }


            }
        });

        registerForContextMenu(listView);

        return root;
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
                SetListViewAdapter(file.getParentFile());

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
                        SetListViewAdapter(files.get(info.position).getParentFile());

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

                Log.i(MODULE_TAG,"Удалить файл" + String.valueOf(info.position));
                File parentFile = files.get(info.position).getParentFile();
                files.get(info.position).delete();
                SetListViewAdapter(parentFile);
                return true;
            }
            default:{
                return super.onContextItemSelected(item);
            }
        }
    }

    public void initToolbar()
    {

    }
    void SetListViewAdapter(File fileDirectory)
    {
        currentFile = fileDirectory;
        files = new ArrayList<>(Arrays.asList(fileDirectory.listFiles()));
        FileListAdapter fileListAdapter = new FileListAdapter(MyApp.get(),R.layout.text_row_item,files);
        listView.setAdapter(fileListAdapter);
    }

}