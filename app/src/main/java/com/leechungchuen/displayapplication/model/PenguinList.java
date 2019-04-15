package com.leechungchuen.displayapplication.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PenguinList {
    @SerializedName("penguinlist")
    private List<Penguin> penguinlist;
    public List<Penguin> getPenguinList() {
        return penguinlist;
    }

    public void setPenguinList(List<Penguin> penguinList) {
        this.penguinlist = penguinList;
    }


}
