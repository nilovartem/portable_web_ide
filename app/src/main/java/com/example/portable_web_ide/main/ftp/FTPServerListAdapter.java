package com.example.portable_web_ide.main.ftp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.portable_web_ide.R;

import java.util.ArrayList;
import java.util.List;

public class FTPServerListAdapter extends ArrayAdapter<Server> {

    private LayoutInflater inflater;
    private int layout;
    private List<Server> servers;


    public FTPServerListAdapter (Context context, int resource, List<Server> servers) {
        super(context,resource,servers);
        this.servers = servers;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);

    }
    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);
        if(view !=null) {
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(servers.get(position).getServerName());
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            imageView.setImageResource(R.drawable.ic_server_24);
       }
        return view;
    }

}
