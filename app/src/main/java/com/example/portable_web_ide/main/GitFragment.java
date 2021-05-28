package com.example.portable_web_ide.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.portable_web_ide.R;
import com.example.portable_web_ide.Section;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GitFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GitFragment extends Section {


    public GitFragment() {
        // Required empty public constructor
    }
    /*
    public static GitFragment newInstance(String param1, String param2) {
        GitFragment fragment = new GitFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_git, container, false);
    }
}