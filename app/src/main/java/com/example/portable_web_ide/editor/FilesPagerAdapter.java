package com.example.portable_web_ide.editor;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.portable_web_ide.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class FilesPagerAdapter extends FragmentStateAdapter {

    private static final String APP_TAG = "Portable_web_ide";
    TabLayout tabLayout;
    ArrayList<FileFragment> pages = new ArrayList<>();
    ArrayList<String> pageTitles = new ArrayList<>();
    ArrayList<Long> pagesId = new ArrayList<>();
    ArrayList<Uri> pagesUri = new ArrayList<>();
    ViewPager2 viewPager;
    FilesPagerAdapter pagerAdapter;

    public FilesPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

        pagerAdapter = this;

        viewPager = fragmentActivity.findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = fragmentActivity.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(pageTitles.get(position));
            }
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

                try {
                    Log.i(APP_TAG, "Выбрана страница " + pageTitles.get(position));
                    super.onPageSelected(position);
                } catch (Throwable e) {

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });
        Log.i(APP_TAG, "Конструктор");
    }
    /*
    public SectionsPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager,lifecycle);


    }*/
/*
    public void addPage(Uri uri){

        String filename = new File(uri.getPath()).getName();
        Log.i(APP_TAG,"Путь");
        Log.i(APP_TAG,"Путь END");
        String realPath = uri.getPath();
        Log.i(APP_TAG,"Реальный путь " + realPath);
        File file = new File(uri.getPath());
        String fullname = file.getName();
        fullname = fullname.replace(":","/");

       // Log.i(APP_TAG,filename);
       // Log.i(APP_TAG,fullname);


        FileFragment fragment = FileFragment.newInstance(pages.size(),realPath);
        //fragment.filename = filename;

        pages.add(fragment);

        Log.i(APP_TAG,"Фрагменты");
        for (FileFragment f:pages
        ) {
            Log.i(APP_TAG,f.getArguments().getString("filename"));

        }
        Log.i(APP_TAG,"Конец фрагменты");

        pageTitles.add(filename);
        pagesId.add((long) (fragment.hashCode()));
        pagerAdapter.notifyDataSetChanged();
        Log.i(APP_TAG,"Хэш " + String.valueOf((long) (fragment.hashCode())));
        /*
        Log.i(APP_TAG,"FOREACH");
        for (String a:pageTitles
             ) {
            Log.i(APP_TAG,a);

        }*/
    // Log.i(APP_TAG,String.valueOf(pages.size()))};

    public void addPages() {


        ArrayList<Uri> filesUri = ActiveFiles.getInstance().filesUri;

        for (Uri uri : filesUri
        ) {
            String filename = new File(uri.getPath()).getName();
            String realPath = uri.getPath();
            FileFragment fragment = FileFragment.newInstance(pages.size(), realPath);
            pages.add(fragment);
            pageTitles.add(filename);
            pagesId.add((long) (fragment.hashCode()));
            pagesUri.add(uri);
            pagerAdapter.notifyDataSetChanged();
            viewPager.setCurrentItem(pages.size() - 1);
        }
        //viewPager.setCurrentItem(0);
        Log.i(APP_TAG, "Установить страницу");


    }

    public void saveFiles() {
        for (FileFragment fileFragment : pages
        ) {

            fileFragment.saveFile();


        }

    }


    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(APP_TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public void closePage() {
        int position = viewPager.getCurrentItem();
        Log.i(APP_TAG, "Позиция:" + position);

        if (pageTitles.size() != 0 && pageTitles.get(position) != null) {

            FileFragment fragment = pages.get(position);
            Uri currentUri = pagesUri.get(position);
            ActiveFiles.getInstance().filesUri.remove(currentUri);
            fragment.saveFile();

            pageTitles.remove(position);
            pagesId.remove(position);
            pages.remove(position);

            this.notifyDataSetChanged();
        }
        Log.i(APP_TAG, "CLOSE PAGE");
    }

    public String getCurrentPagePath() {

        int position = viewPager.getCurrentItem();
        FileFragment fragment = pages.get(position);
        return fragment.getFilePath();
    }

    @NonNull
    @Override
    public FileFragment createFragment(int position) {

        Log.i(APP_TAG, "CreateFragment");

        Log.i(APP_TAG, "Position " + String.valueOf(position));

        return pages.get(position);
        // return PlaceholderFragment.newInstance(position);

    }

    /*
    @Override
    public long getItemId(int position)
    {
        return ((long) pages.get(position).getId());
    }
    @Override
    public Fragment getItem(int position)
    {

    }
    @Override
    public boolean containsItem(long itemId)
    {
       return pagesId.contains(itemId);
    }*/
    @Override
    public int getItemCount() {
        return pages.size();
    }
}
