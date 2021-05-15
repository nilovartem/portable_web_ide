package com.example.portable_web_ide.editor;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.example.portable_web_ide.MyApp;
import com.example.portable_web_ide.R;
import com.example.portable_web_ide.SettingsActivity;
import com.example.portable_web_ide.main.LocalFragment;
import com.example.portable_web_ide.main.MainActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.*;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Environment;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;

import java.io.File;

public class EditFilesActivity extends AppCompatActivity {

    private static final int REQUEST_CHOOSE_FILE = 1;
    private static final String APP_TAG = "Portable_web_ide";
    private FilesPagerAdapter pagerAdapter;
    private ViewPager2 viewPager;
    Uri filePathUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(APP_TAG,"EditFiles OnCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_files);

        String filePath = getIntent().getStringExtra("filePath");
        initToolbar();
        pagerAdapter = new FilesPagerAdapter(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        //File file = new File(filePath);
        //filePathUri = Uri.fromFile(file);
        //pagerAdapter.addPage(filePathUri);
        pagerAdapter.addPages();
        //pagerAdapter
    }

    public void initToolbar()
    {
        EditFilesActivity currentActivity  = this;
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(APP_TAG,"Нажата кнопка Back");

                pagerAdapter.saveFiles();
                currentActivity.onBackPressed();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_files, menu);
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
            Intent intent = new Intent(EditFilesActivity.this, SettingsActivity.class);
            startActivity(intent);
        }
        if(id == R.id.action_open_file){
            //Нужно будет переделать, так как можно несколько раз открыть один и тот же файл,
            //нужно сохранить все открытые фрагменты
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

        }
        if(id == R.id.action_close_file){
            Log.i(APP_TAG, "Нажата кнопка Закрыть");
            pagerAdapter.closePage();
            if(pagerAdapter.pages.size() == 0)
            {
                this.onBackPressed();
            }

        }
        return super.onOptionsItemSelected(item);


    }


    private String parseUri(Uri uri)
    {
        String fileName = new File(uri.getPath()).getName();
        Log.i(APP_TAG,fileName);
        return fileName;
    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {

        }
    });


}
