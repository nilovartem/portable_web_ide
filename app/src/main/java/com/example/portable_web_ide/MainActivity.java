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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;
import androidx.activity.*;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TabWidget;

import com.example.portable_web_ide.ui.main.*;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CHOOSE_FILE = 1;
    private static final String APP_TAG = "Portable_web_ide";
    private SectionsPagerAdapter pagerAdapter;
    private ViewPager2 viewPager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();
        //pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),this.getLifecycle());
        pagerAdapter = new SectionsPagerAdapter(this);
       
        //viewPager = findViewById(R.id.view_pager);
        //TabLayout tabLayout = findViewById(R.id.tab_layout);
       // viewPager.setAdapter(pagerAdapter);
/*
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText("A")
        ).attach();*/

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void initToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
            //Нужно будет переделать, так как можно несколько раз открыть один и тот же файл,
            //также нужно будет изменить название файла(чтобы можно было различать файлы с одинаковым названием,
            //но с разным местоположением

            mGetContent.launch("text/*");
            Log.i(APP_TAG, "Нажата кнопка Открыть файл");
        }
        if(id == R.id.action_close_file){
            Log.i(APP_TAG, "Нажата кнопка Закрыть");
            pagerAdapter.closePage();

        }
        if(id == R.id.action_save_file){
            Log.i(APP_TAG, "Нажата кнопка Сохранить файл");
            int fragmentPosition = viewPager.getCurrentItem();
            Log.i(APP_TAG,"Номер фрагмента" + String.valueOf(fragmentPosition));
        }
        return super.onOptionsItemSelected(item);
    }


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
                    pagerAdapter.addPage(parseUri(uri));
                    pagerAdapter.notifyDataSetChanged();
                }
            });

}
