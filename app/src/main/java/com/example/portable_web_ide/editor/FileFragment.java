package com.example.portable_web_ide.editor;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.portable_web_ide.MyApp;
import com.example.portable_web_ide.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * A placeholder fragment containing a simple view.
 */
public class FileFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_FILEPATH = "filename";
    private static final String APP_TAG = "Portable_web_ide";


    ViewPager2 viewPager;
    FilesPagerAdapter pagerAdapter;


    public static FileFragment newInstance(int index, String filename) {

        Log.i(APP_TAG, "Instance ");
        FileFragment fragment = new FileFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putString(ARG_FILEPATH,filename);

        fragment.setArguments(bundle);
        Log.i(APP_TAG,"Фрагмент" + String.valueOf(index));
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        /*pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);*/

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        /*root = inflater.inflate(R.layout.fragment_main, container, false);

        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //saveFile(fileName);
        return root;*/

        View root = inflater.inflate(R.layout.fragment_edit_files, container, false);


        Log.i(APP_TAG, "OnCreateView" + this.getArguments().getString(ARG_FILEPATH));
        EditText editText  = root.findViewById(R.id.textArea);
        //Log.i(APP_TAG,"OnCreateView " + this.getArguments().getString(ARG_FILENAME));
        editText.setText(this.getArguments().getString(ARG_FILEPATH));


        readFile(this.getArguments().getString(ARG_FILEPATH),root);

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       // Log.i(APP_TAG, "OnViewCreated");

    }

    public void readFile(String filePath,View root) {
        EditText editText = root.findViewById(R.id.textArea);

        //Log.i(APP_TAG, String.valueOf(R.id.edit_text));
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(filePath));
            //InputStream inputStream = getContext().openFileInput(filename);

            if (fileInputStream != null) {
                InputStreamReader isr = new InputStreamReader(fileInputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                fileInputStream.close();
                editText.setText(builder.toString());
            }
        } catch (Throwable t) {
            Toast.makeText(getContext(),
                    "Ошибка: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    public void saveFile() {

        Log.i(APP_TAG, "Значение в  EditText: " + R.id.textArea);
        String filePath = this.getArguments().getString(ARG_FILEPATH);
        File file = new File(filePath);

        Log.i(APP_TAG, "Сохранение файла " + filePath);

        Context context = MyApp.get();

        Log.i(APP_TAG,context.getPackageName());

        EditText editText = getView().findViewById(R.id.textArea);

        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(new FileOutputStream(file));
            outputStreamWriter.write(editText.getText().toString());
            outputStreamWriter.close();

        } catch (IOException e) {

        }


    }


}