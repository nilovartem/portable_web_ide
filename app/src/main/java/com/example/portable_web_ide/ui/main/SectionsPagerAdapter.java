package com.example.portable_web_ide.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.portable_web_ide.R;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    private static final String APP_TAG = "Portable_web_ide";
    public int count;
    //Возможно, придется переделать tab titles в string массив
    //@StringRes
    //private int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private ArrayList<String> tabTitles = new ArrayList<String>();
    private ArrayList<PlaceholderFragment> fragments = new ArrayList<PlaceholderFragment>();
    private final Context mContext;
    FragmentManager fragmentManager;


    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
        fragmentManager = fm;
    }

    @Override//поменял с fragment
    public PlaceholderFragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        PlaceholderFragment fragment = PlaceholderFragment.newInstance(position + 1);
        int fragmentIndex = fragment.getId();
        fragments.add(fragment);
        return fragment;
    }
    public void newTab(String fileName)
    {
        //int position = tabTitles.indexOf(fileName);
        tabTitles.add(fileName);
        count++;
        PlaceholderFragment fragment = getItem(count);
        fragment.fileName = fileName;

    }
    public void closeTab(int position)
    {


        String fileName = tabTitles.get(position);
        Log.i(APP_TAG, fileName);
        //Fragment fragment = fragmentManager.findFragmentById(fragmentIndexes.get(position));
        PlaceholderFragment fragment = fragments.get(position);
        Log.i(APP_TAG,fragment.fileName);
        fragment.saveFile(fileName);
        //Log.i(APP_TAG,)

        //placeholderFragment.saveFile(fileName);

        //уничтожить фрагмент
        //tabTitles.remove(position);
        //count--;
        //tabTitles.remove(fileName);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles.get(position);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return count;
    }
}