package com.example.portable_web_ide.ui.main;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.portable_web_ide.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentStateAdapter {

    private static final String APP_TAG = "Portable_web_ide";
    TabLayout tabLayout;
    ArrayList<PlaceholderFragment> pages = new ArrayList<>();
    ArrayList<String> pageTitles = new ArrayList<>();
    ArrayList<Long> pagesId = new ArrayList<>();
    ViewPager2 viewPager;
    SectionsPagerAdapter pagerAdapter;

    public SectionsPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);

        pagerAdapter = this;

        viewPager = fragmentActivity.findViewById(R.id.view_pager);
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = fragmentActivity.findViewById(R.id.tab_layout);
        new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(pageTitles.get(position));
            }
        }).attach();

    }
    /*
    public SectionsPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager,lifecycle);


    }*/

    public void addPage(String filename)
    {

        PlaceholderFragment fragment = createFragment(pages.size());
        fragment.filename = filename;
        pages.add(fragment);
        pageTitles.add(filename);
        pagesId.add((long) (fragment.hashCode()));
        Log.i(APP_TAG,"FOREACH");
        for (String a:pageTitles
             ) {
            Log.i(APP_TAG,a);

        }
       // Log.i(APP_TAG,String.valueOf(pages.size()));
    }
    public void closePage()
    {
        int position = viewPager.getCurrentItem();
        Log.i(APP_TAG,"position " + position);

        if(pageTitles.size() != 0 && pageTitles.get(position) != null) {
            pageTitles.remove(position);
            pagesId.remove(position);
            pages.remove(position);
            this.notifyDataSetChanged();
        }
        Log.i(APP_TAG,"CLOSE PAGE");
    }
    @NonNull
    @Override
    public PlaceholderFragment createFragment(int position) {

        Log.i(APP_TAG,"CreateFragment");
        //небольшой обман
        return PlaceholderFragment.newInstance(position);
    }
    @Override
    public long getItemId(int position)
    {
        return ((long) pages.get(position).getId());
    }
    @Override
    public boolean containsItem(long itemId)
    {
       return pagesId.contains(itemId);
    }
    @Override
    public int getItemCount() {
        return pages.size();
    }
}
