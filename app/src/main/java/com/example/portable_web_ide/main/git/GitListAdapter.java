package com.example.portable_web_ide.main.git;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.portable_web_ide.R;
import com.example.portable_web_ide.main.ftp.Server;

import org.kohsuke.github.GHRepository;

import java.util.List;

public class GitListAdapter extends ArrayAdapter<GHRepository> {

    private LayoutInflater inflater;
    private int layout;
    private List<GHRepository> repositories;
    private static final String MODULE_TAG = "GitFragment";

    public GitListAdapter (Context context, int resource, List<GHRepository> repositories) {
        super(context,resource,repositories);
        this.repositories = repositories;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

/*
        for (GHRepository repo:repositories
             ) {
           // Log.i(MODULE_TAG,repo.getFullName());
        }*/
        //Log.i(MODULE_TAG,repositories.get(position).getFullName());
        if(view !=null) {
            //Log.i(MODULE_TAG,"ADAPTER");
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(repositories.get(position).getName());
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_folder_24);
        }
        return view;
    }
}
