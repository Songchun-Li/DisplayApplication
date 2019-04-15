package com.leechungchuen.displayapplication.controller;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.leechungchuen.displayapplication.GlideApp;
import com.leechungchuen.displayapplication.R;


public class PicFragment extends Fragment {

    public static PicFragment newInstance(String url){
        PicFragment picFragment = new PicFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        picFragment.setArguments(bundle);
        return picFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pic, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView picView = (ImageView) view.findViewById(R.id.info_pic);
        String url = getArguments().getString("url");
        Log.i("PicFragment", url);
        GlideApp.with(this)
                .load(url).placeholder(R.mipmap.container)
                .into(picView);


    }
}
