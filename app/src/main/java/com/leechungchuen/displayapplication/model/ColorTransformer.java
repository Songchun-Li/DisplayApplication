package com.leechungchuen.displayapplication.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorTransformer {

    public static List<String> getColor(String colorstring) {
        List<String> colorList = new ArrayList<>();


        String pattern = "(\\|)";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(colorstring);
        if (m.find()) {
            //two color
            Log.d("Find two",m.group(0));
            String capturePattern = "(\\w+)\\\\|(\\w+)";

            Pattern captureR = Pattern.compile(capturePattern);
            Matcher matcher = captureR.matcher(colorstring);
            if (matcher.find()) {
                Log.d("colorTransformer", "Group count: " + matcher.groupCount());
                String color1 = matcher.group(1);
                String color2 = matcher.group(2);
                String colorCode1 = decode(color1);
                String colorCode2 = decode(color2);
                colorList.add(colorCode1);
                colorList.add(colorCode2);
            } else {
                colorList.add(decode("no"));
                colorList.add(decode("no"));
            }

        } else {
            //only on color
            String colorCode = decode(colorstring);
            colorList.add(colorCode);
            colorList.add(colorCode);
        }


        return colorList;
    }

    public static String decode(String colorName){
        Log.d("decoding", "Color name: " + colorName);
        String colorCode;
        if ("white".equalsIgnoreCase(colorName)){
            return "#FFFFFF";
        } else if ("yellow".equalsIgnoreCase(colorName)) {
            return "#F5E83F";
        } else if ("pink".equalsIgnoreCase(colorName)) {
            return "#FFAFE3";
        } else if ("purple".equalsIgnoreCase(colorName)) {
            return "#A588FF";
        } else if ("blue".equalsIgnoreCase(colorName)) {
            return "#1181EC";
        } else if ("red".equalsIgnoreCase(colorName)) {
            return "#F32929";
        } else if ("green".equalsIgnoreCase(colorName)) {
            return "#7FDB82";
        }  else if ("brown".equalsIgnoreCase(colorName)) {
            return "#9E6600";
        } else if ("orange".equalsIgnoreCase(colorName)) {
            return "#F4AF1B";
        } else if ("black".equalsIgnoreCase(colorName)) {
            return "#080808";
        } else if ("lt. blue".equalsIgnoreCase(colorName)) {
            return "#5DC5F9";
        } else {
            return "#00000000";
        }
    }

}
