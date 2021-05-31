package com.example.portable_web_ide.main;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.portable_web_ide.R;
import com.example.portable_web_ide.Section;
import com.example.portable_web_ide.main.ftp.FtpFragment;
import com.example.portable_web_ide.main.local.LocalFragment;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {

    private static final String MODULE_TAG = "SectionPagerAdapter";
    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,R.string.tab_text_3};

    private ArrayList<Fragment> fragments = new ArrayList<>();

    ViewPager2 viewPager;
    SectionsPagerAdapter pagerAdapter;

    public SectionsPagerAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);


        pagerAdapter = this;

        viewPager = fragmentActivity.findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);
        if(viewPager != null)
        {
            Log.i(MODULE_TAG,"NOT NULL");
        }

        TabLayout tabLayout = fragmentActivity.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(TAB_TITLES[position]);
            }
        }).attach();

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {

                try{
                    Log.i(MODULE_TAG,"Выбрана страница " + TAB_TITLES[position]);
                    super.onPageSelected(position);
                }
                catch (Throwable e){

                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

    @Override
    public Fragment createFragment(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position)
        {
            case 0:
            {
                LocalFragment localFragment = new LocalFragment();
                fragments.add(localFragment);
                return localFragment;
            }
            case 1:
            {
                FtpFragment ftpFragment = new FtpFragment();
                fragments.add(ftpFragment);
                return ftpFragment;
            }
            case 2:
            {
                GitFragment gitFragment = new GitFragment();
                fragments.add(gitFragment);
                return gitFragment;
            }
            default:{
               return null;
            }
        }

    }

    public Fragment getCurrentFragment()
    {
       return fragments.get(viewPager.getCurrentItem());
    }
    /*
    public ArrayList<File> getSelectedFiles()
    {
        Section fragment = fragments.get(viewPager.getCurrentItem());
        fragment.multipleSelectedFiles.get
    }*/

    @Override
    public int getItemCount() {
        return TAB_TITLES.length;
    }
}