package com.example.portable_web_ide.editor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.portable_web_ide.MyApp;
import com.example.portable_web_ide.R;
import com.example.portable_web_ide.main.MainActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class PromptArrayAdapter extends ArrayAdapter<String> {
    private List<String> prompts;
    private static final String MODULE_TAG = "PromptArrayAdapter";
    //int layoutResourceId;
    LayoutInflater layoutInflater;

    List<String> staticPrompts = Arrays.asList(MyApp.get().getResources().getStringArray(R.array.prompts));

    public PromptArrayAdapter(@NonNull Context context, int resource) {
        super(context, resource);
       //layoutResourceId = resource;
        layoutInflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        //this.prompts = objects;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       if(convertView == null){

           convertView = layoutInflater.inflate(R.layout.autocomplete_prompt,parent,false);
           TextView textView = convertView.findViewById(R.id.textView);
           textView.setText(prompts.get(position));
       }
       return convertView;

    }

    @Override
    public int getCount() {
        return prompts.size();
    }

    @Nullable
    @Override
    public String getItem(int position) {
        return prompts.get(position);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                prompts = new ArrayList<String>();
                FilterResults filterResults = new FilterResults();
                String input = constraint.toString();
                prompts.clear(); // очищаем старый список

                for (String prompt:staticPrompts
                     ) {
                    if(prompt.startsWith(input) /*&& !prompt.equals(input)*/){

                        prompts.add(prompt);
                        Log.i(MODULE_TAG,prompt + " - Подсказка");
                    }

                }

                filterResults.values = prompts;
                filterResults.count = prompts.size();
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                //clear();
                addAll(prompts);
                for (String prompt:prompts
                     ) {
                    Log.i(MODULE_TAG,prompt);
                }
                notifyDataSetInvalidated();
                notifyDataSetChanged();
            }
        };
    }

}
