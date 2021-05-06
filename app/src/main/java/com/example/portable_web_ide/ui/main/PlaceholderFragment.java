package com.example.portable_web_ide.ui.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.portable_web_ide.MainActivity;
import com.example.portable_web_ide.MyApp;
import com.example.portable_web_ide.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {






    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String ARG_FILENAME = "filename";
    private static final String APP_TAG = "Portable_web_ide";
    private PageViewModel pageViewModel;

    ViewPager2 viewPager;
    SectionsPagerAdapter pagerAdapter;


    public static PlaceholderFragment newInstance(int index, String filename) {

        Log.i(APP_TAG, "Instance ");
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        bundle.putString(ARG_FILENAME,filename);

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

        View root = inflater.inflate(R.layout.fragment_main, container, false);


        Log.i(APP_TAG, "OnCreateView");
        EditText editText  = root.findViewById(R.id.edit_text);
        //Log.i(APP_TAG,"OnCreateView " + this.getArguments().getString(ARG_FILENAME));
        editText.setText(this.getArguments().getString(ARG_FILENAME));


        readFile(this.getArguments().getString(ARG_FILENAME),root);

        return root;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

       // Log.i(APP_TAG, "OnViewCreated");

    }

    public void readFile(String filename,View root) {
        EditText editText = root.findViewById(R.id.edit_text);
        Log.i(APP_TAG,getActivity().getFilesDir().getPath() + "/" + filename);
        //Log.i(APP_TAG, String.valueOf(R.id.edit_text));
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(getActivity().getFilesDir().getPath() + "/" + filename));
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

        Log.i(APP_TAG, "Значение в  EditText: " + R.id.edit_text);
        String filename = this.getArguments().getString(ARG_FILENAME);
        Log.i(APP_TAG, "Сохранение файла " + filename);

        Context context = MyApp.get();

        Log.i(APP_TAG,context.getPackageName());

        EditText editText = getView().findViewById(R.id.edit_text);

        try {

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, context.MODE_PRIVATE));
            outputStreamWriter.write(editText.getText().toString());
            outputStreamWriter.close();

        } catch (IOException e) {

        }


    }


}