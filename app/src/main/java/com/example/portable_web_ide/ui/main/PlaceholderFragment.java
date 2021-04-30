package com.example.portable_web_ide.ui.main;

import android.content.Intent;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.portable_web_ide.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private View root;
    public String fileName;
    private static PlaceholderFragment currentFragment;
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String APP_TAG = "Portable_web_ide";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);

        return fragment;
    }
    //пытаюсь понять, тот ли это фрагмент

    public static PlaceholderFragment getInstance(int index){

        return currentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentFragment = this;
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, container, false);
        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        //EditText editText = root.findViewById(R.id.edit_text);
        //browseFiles();

        //readFile(fileName);
        saveFile(fileName);
        return root;
    }

    public void readFile(String fileName) {
        EditText editText = root.findViewById(R.id.edit_text);
        Log.i(APP_TAG,String.valueOf(R.id.edit_text));
        try {
            InputStream inputStream = getContext().openFileInput(fileName);

            if (inputStream != null) {
                InputStreamReader isr = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(isr);
                String line;
                StringBuilder builder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    builder.append(line + "\n");
                }

                inputStream.close();
                editText.setText(builder.toString());
            }
        } catch (Throwable t) {
            Toast.makeText(getContext(),
                    "Ошибка: " + t.toString(), Toast.LENGTH_LONG).show();
        }
    }
    public void saveFile(String fileName) {

        EditText editText = root.findViewById(R.id.edit_text);
            Log.i(APP_TAG, String.valueOf(R.id.edit_text));
            try {
                OutputStream outputStream = getContext().openFileOutput(fileName, 0);
                OutputStreamWriter osw = new OutputStreamWriter(outputStream);
                osw.write(editText.getText().toString());
                osw.close();
            } catch (Throwable t) {
                Toast.makeText(getContext(),
                        "Ошибка: " + t.toString(), Toast.LENGTH_LONG).show();
            }

    }


}