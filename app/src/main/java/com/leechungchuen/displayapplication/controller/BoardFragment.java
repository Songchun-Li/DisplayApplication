package com.leechungchuen.displayapplication.controller;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.leechungchuen.displayapplication.R;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class BoardFragment extends Fragment {

    private TextView nameView;
    private  String penguinName;

    public static BoardFragment newInstance(String name) {
        BoardFragment boardFragment = new BoardFragment();
        Bundle bundle = new Bundle();
        bundle.putString("name", name);
        boardFragment.setArguments(bundle);
        return boardFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_board, container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameView = (TextView) view.findViewById(R.id.board_slogan_name);
        if (getArguments() != null){
            nameView.setText(getArguments().getString("name"));
        }
        AssetManager mgr = getActivity().getAssets();
        Typeface boldItalic = Typeface.createFromAsset(mgr, "font/tisasans_bi.otf");
        nameView.setTypeface(boldItalic);
        TextView sen1 = (TextView) view.findViewById(R.id.board_slogan1);
        TextView sen2 = (TextView) view.findViewById(R.id.board_slogan2);
        Typeface italic = Typeface.createFromAsset(mgr, "font/tisasans_i.otf");
        sen1.setTypeface(italic);
        sen2.setTypeface(italic);
        //penguin animate
        GifImageView penguinGif = (GifImageView) view.findViewById(R.id.board_penguin_gif);
        GifDrawable gifDrawable = (GifDrawable) penguinGif.getDrawable();
        gifDrawable.setLoopCount(10);
    }
}
