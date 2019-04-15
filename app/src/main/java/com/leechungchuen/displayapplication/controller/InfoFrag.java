package com.leechungchuen.displayapplication.controller;

import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.leechungchuen.displayapplication.R;
import com.leechungchuen.displayapplication.model.ColorTransformer;
import com.leechungchuen.displayapplication.model.YearTransformer;

import java.util.List;

public class InfoFrag extends Fragment {

    public static InfoFrag newInstance(Bundle infoBundle) {
        InfoFrag infoFrag = new InfoFrag();
        infoFrag.setArguments(infoBundle);
        return infoFrag;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialize views
        TextView nameView = (TextView) view.findViewById(R.id.info_name);
        TextView factView = (TextView) view.findViewById(R.id.info_fun_fact_value);
        TextView personalityView = (TextView) view.findViewById(R.id.info_personality_value);
        TextView speciesView = (TextView) view.findViewById(R.id.info_species);
        ImageView genderView = ((ImageView) view.findViewById(R.id.info_gender));
        TextView personalityKey = ((TextView) view.findViewById(R.id.main_personality_key));
        TextView factKey = ((TextView) view.findViewById(R.id.info_fun_fact_key));
        TextView colorKey = (TextView) view.findViewById(R.id.info_band_color_key);
        TextView yearKey = (TextView) view.findViewById(R.id.info_hatch_year_key);
        TextView year_container1 = (TextView) view.findViewById(R.id.info_hatch_year_container_1);
        TextView year_container2 = (TextView) view.findViewById(R.id.info_hatch_year_container_2);
        TextView year_container3 = (TextView) view.findViewById(R.id.info_hatch_year_container_3);
        TextView year_container4 = (TextView) view.findViewById(R.id.info_hatch_year_container_4);
        TextView colorband1 = (TextView) view.findViewById(R.id.info_band_color_value_1);
        TextView colorband2 = (TextView) view.findViewById(R.id.info_band_color_value_2);

        //Setting Fonts
        AssetManager mgr = getActivity().getAssets();
        Typeface boldItalic = Typeface.createFromAsset(mgr, "font/tisasans_bi.otf");
        nameView.setTypeface(boldItalic);

        Typeface italic = Typeface.createFromAsset(mgr, "font/tisasans_i.otf");
        personalityView.setTypeface(italic);
        factView.setTypeface(italic);
        speciesView.setTypeface(italic);
        year_container1.setTypeface(italic);
        year_container2.setTypeface(italic);
        year_container3.setTypeface(italic);
        year_container4.setTypeface(italic);

        Typeface mediumItalic = Typeface.createFromAsset(mgr, "font/tisasans_mi.otf");
        factKey.setTypeface(mediumItalic);
        personalityKey.setTypeface(mediumItalic);
        colorKey.setTypeface(mediumItalic);
        yearKey.setTypeface(mediumItalic);


        nameView.setText(getArguments().getString("name"));

        genderView.setVisibility(View.VISIBLE);
        if (getArguments().getString("gender") == null) {
            genderView.setVisibility(View.INVISIBLE);
        } else if ("female".equalsIgnoreCase(getArguments().getString("gender"))) {
            genderView.setImageResource(R.mipmap.female);
        } else if ("male".equalsIgnoreCase(getArguments().getString("gender"))) {
            genderView.setImageResource(R.mipmap.male);
        }

        if (getArguments().getString("funfact") != null) {
            factView.setVisibility(View.VISIBLE);
            factKey.setVisibility(View.VISIBLE);
            factView.setText(getArguments().getString("funfact"));
        } else {
            // hide the key
            factView.setVisibility(View.INVISIBLE);
            factKey.setVisibility(View.INVISIBLE);
        }

        if (getArguments().getString("personality") != null) {
            personalityView.setVisibility(View.VISIBLE);
            personalityKey.setVisibility(View.VISIBLE);
            personalityView.setText(getArguments().getString("personality"));
        } else {
            //hide the key
            personalityView.setVisibility(View.GONE);
            personalityKey.setVisibility(View.GONE);
        }

        if (getArguments().getString("species") != null) {
            speciesView.setVisibility(View.VISIBLE);
            speciesView.setText(getArguments().getString("species"));
        } else {
            //
            speciesView.setVisibility(View.INVISIBLE);
        }
        int year = getArguments().getInt("hatchyear");
        if (year != 0) {
            yearKey.setVisibility(View.VISIBLE);
            year_container1.setVisibility(View.VISIBLE);
            year_container2.setVisibility(View.VISIBLE);
            year_container3.setVisibility(View.VISIBLE);
            year_container4.setVisibility(View.VISIBLE);
            List<Integer> digitList = YearTransformer.transformYear(year);
            year_container1.setText(String.valueOf(digitList.get(0)));
            year_container2.setText(String.valueOf(digitList.get(1)));
            year_container3.setText(String.valueOf(digitList.get(2)));
            year_container4.setText(String.valueOf(digitList.get(3)));
        } else {
            yearKey.setVisibility(View.GONE);
            year_container1.setVisibility(View.GONE);
            year_container2.setVisibility(View.GONE);
            year_container3.setVisibility(View.GONE);
            year_container4.setVisibility(View.GONE);
        }
        //color
        String bandColor1 = getArguments().getString("bandcolor1");
        String bandColor2 = getArguments().getString("bandcolor2");
        colorband1.setBackgroundColor(Color.parseColor(ColorTransformer.decode(bandColor1)));
        colorband2.setBackgroundColor(Color.parseColor(ColorTransformer.decode(bandColor2)));
    }
}
