package com.example.portable_web_ide.main.local;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.portable_web_ide.R;

import java.io.File;
import java.util.List;

public class CheckableFileListAdapter extends ArrayAdapter<File> {

    private LayoutInflater inflater;
    private int layout;
    private List<File> files;

    public CheckableFileListAdapter(Context context, int resource, List<File> files) {
        super(context, resource, files);
        this.files = files;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        //ImageView flagView = (ImageView) view.findViewById(R.id.flag);
        //TextView nameView = (TextView) view.findViewById(R.id.name);
        TextView textView = (TextView) view.findViewById(R.id.textView);

        //State state = states.get(position);

        //flagView.setImageResource(state.getFlagResource());
        //nameView.setText(state.getName());
        textView.setText(files.get(position).getName());

        return view;
    }
}
