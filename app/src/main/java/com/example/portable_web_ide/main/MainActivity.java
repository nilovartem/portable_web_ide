package com.example.portable_web_ide.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.example.portable_web_ide.MyApp;
import com.example.portable_web_ide.R;
import com.example.portable_web_ide.Section;
import com.example.portable_web_ide.SettingsActivity;
import com.example.portable_web_ide.editor.ActiveFiles;
import com.example.portable_web_ide.editor.EditFilesActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String MODULE_TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initToolbar();

        MyApp singletonExample = MyApp.getInstance();
        if (MyApp.get() == null) {
            singletonExample.initContext(getApplicationContext());
        }
        setInitialDirectories();

        ActiveFiles activeFiles = ActiveFiles.getInstance();
        activeFiles.filesUri = new ArrayList<Uri>();

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this);
        /*
        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

       /*
        Button moveButton = findViewById(R.id.move_button);
        moveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(MODULE_TAG,"Нажата кнопка Переместить");

               //Section fragment = sectionsPagerAdapter.getCurrentFragment();

               int fragment = sectionsPagerAdapter.viewPager.getCurrentItem();
               LocalFragment localFragment = (LocalFragment) getSupportFragmentManager().findFragmentById(0);
               Log.i(MODULE_TAG,String.valueOf(localFragment.multipleSelectedFiles.size()));




            }
        });
        */


    }

    protected void setInitialDirectories() {
        MyApp singletonExample = MyApp.getInstance();
        //проверка на существование трех стандартных папок
        ArrayList<File> initialDirectories = new ArrayList<>();

        File rootDirectory = MyApp.get().getFilesDir();
        File initialDirectory;

        initialDirectory = new File(rootDirectory.getPath() + "/local");
        initialDirectories.add(initialDirectory);
        singletonExample.initLocalDirectory(initialDirectory);


        initialDirectory = new File(rootDirectory.getPath() + "/ftp");
        initialDirectories.add(initialDirectory);
        singletonExample.initFtpDirectory(initialDirectory);

        initialDirectory = new File(rootDirectory.getPath() + "/git");
        initialDirectories.add(initialDirectory);
        singletonExample.initGitDirectory(initialDirectory);

        for (File file : initialDirectories
        ) {
            if (!file.exists()) {
                file.mkdir();
            }
        }

    }

    public void initToolbar() {
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
            Log.i(MODULE_TAG, "Переход на активити с настройками");
            Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);

    }


}