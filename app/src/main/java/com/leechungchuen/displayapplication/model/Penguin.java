package com.leechungchuen.displayapplication.model;

import com.google.gson.annotations.SerializedName;

public class Penguin {
    @SerializedName("name")
    private String name;
    @SerializedName("band_color1")
    private String bandColor1;
    @SerializedName("band_color2")
    private String bandColor2;
    @SerializedName("id")
    private String id;
    @SerializedName("gender")
    private String gender;
    @SerializedName("hatch_year")
    private Integer hatchYear;
    @SerializedName("fun_fact")
    private String funFact;
    @SerializedName("personality")
    private String personality;
    @SerializedName("species")
    private String species;
    @SerializedName("imageurl")
    private String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBandcolor1() {
        return bandColor1;
    }

    public void setBandcolor1(String bandColor1) {
        this.bandColor1 = bandColor1;
    }

    public String getBandcolor2() {
        return bandColor2;
    }

    public void setBandcolor2(String bandColor2) {
        this.bandColor2 = bandColor2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getHatchyear() {
        return hatchYear;
    }

    public void setHatchyear(Integer hatchYear) {
        this.hatchYear = hatchYear;
    }

    public String getFunfact() {
        return funFact;
    }

    public void setFunfact(String funFact) {
        this.funFact = funFact;
    }

    public String getPersonality() {
        return personality;
    }

    public void setPersonality(String personality) {
        this.personality = personality;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }








}


