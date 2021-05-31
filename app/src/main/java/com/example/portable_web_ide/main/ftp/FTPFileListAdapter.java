package com.example.portable_web_ide.main.ftp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.portable_web_ide.R;

import org.apache.commons.net.ftp.FTPFile;

import java.io.File;
import java.util.List;

public class FTPFileListAdapter extends ArrayAdapter<FTPFile> {

    private LayoutInflater inflater;
    private int layout;
    private List<FTPFile> files;


    public FTPFileListAdapter (Context context, int resource, List<FTPFile> files) {
        super(context, resource, files);
        this.files = files;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);

    }
    public View getView(int position, View convertView, ViewGroup parent) {


        View view=inflater.inflate(this.layout, parent, false);
        if(view !=null) {
            //ImageView flagView = (ImageView) view.findViewById(R.id.flag);
            //TextView nameView = (TextView) view.findViewById(R.id.name);
            //  if (mode == ListView.CHOICE_MODE_SINGLE) {
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(files.get(position).getName());
            // }
            // if (mode == ListView.CHOICE_MODE_MULTIPLE) {

            //CheckedTextView checkedTextView = (CheckedTextView) view.findViewById(R.id.checked_text_view);
            // checkedTextView.setText(files.get(position).getName());
            //  }
            ImageView imageView = (ImageView) view.findViewById(R.id.imageView);
            if (files.get(position).isDirectory()) {
                imageView.setImageResource(R.drawable.ic_folder_24);
            } else {
                imageView.setImageResource(R.drawable.ic_file_24);
            }


            //State state = states.get(position);

            //flagView.setImageResource(state.getFlagResource());
            //nameView.setText(state.getName());
        }

        return view;
    }
}
