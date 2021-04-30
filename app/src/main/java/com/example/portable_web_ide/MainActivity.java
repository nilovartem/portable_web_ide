package com.example.portable_web_ide;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.VoiceInteractor;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.activity.*;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.example.portable_web_ide.ui.main.*;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CHOOSE_FILE = 1;
    private static final String APP_TAG = "Portable_web_ide";
    private SectionsPagerAdapter sectionsPagerAdapter;
    private ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        //tabs.setVisibility(View.GONE);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Log.i(APP_TAG, "Переход на активити с настройками");
            Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_open_file){
            //browseFiles();
            mGetContent.launch("text/*");

            Log.i(APP_TAG, "Нажата кнопка Открыть файл");
          //  sectionsPagerAdapter.newTab();
          //  viewPager.setAdapter(sectionsPagerAdapter);
        }
        if(id == R.id.action_save_file){
            //browseFiles();
            FragmentManager fragmentManager = getSupportFragmentManager();


            Log.i(APP_TAG, "Нажата кнопка Сохранить файл");
            int fragmentPosition = viewPager.getCurrentItem();

            //fragmentManager.findFragmentById(fragmentPosition);

            sectionsPagerAdapter.closeTab(fragmentPosition);
            Log.i(APP_TAG,"Номер фрагмента" + String.valueOf(fragmentPosition));

            //  sectionsPagerAdapter.newTab();
            //  viewPager.setAdapter(sectionsPagerAdapter);
        }


        return super.onOptionsItemSelected(item);
    }


    private void deprecated_browseFiles()
    {
        Intent chooseFile;
        Intent intent;
        chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("file/*");
        intent = Intent.createChooser(chooseFile, "Choose a file");
        startActivityForResult(intent, REQUEST_CHOOSE_FILE);
        //openFile("NewTextFile.txt");
    }
    private void browseFiles()
    {
        mGetContent.launch("text/*");
    }
    //Нужно будет переделать, так как можно несколько раз открыть один и тот же файл,
    //также нужно будет изменить название файла(чтобы можно было различать файлы с одинаковым названием,
    //но с разным местоположением
    private String parseUri(Uri uri)
    {
        String fileName = new File(uri.getPath()).getName();
        Log.i(APP_TAG,fileName);
        return fileName;
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    // Handle the returned Uri
                    Log.i(APP_TAG,String.valueOf(uri));
                    sectionsPagerAdapter.newTab(parseUri(uri));
                    viewPager.setAdapter(sectionsPagerAdapter);
                }
            });

}
