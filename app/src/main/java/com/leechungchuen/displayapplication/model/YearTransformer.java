package com.leechungchuen.displayapplication.model;

import java.util.ArrayList;
import java.util.List;

public class YearTransformer {

    public static List<Integer> transformYear(int hatch_year){
        /*
        int num;

        for (int i = 0;  i < 4; i++){
            int divider = 1;
            num = hatch_year/(1000/divider);
            hatch_year = hatch_year - num*(1000/divider);
            digitList.add(num);
            divider = divider * 10;
        }
        */
        List<Integer> digitList = new ArrayList<>();
        int thousand = hatch_year/1000;
        digitList.add(thousand);
        int hundred = hatch_year/100%10;
        digitList.add(hundred);
        int ten = hatch_year/10%10;
        digitList.add(ten);
        int one= hatch_year%10;
        digitList.add(one);

        return digitList;
    }
}
