package com.example;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Command {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("nameBut")
    @Expose
    private String nameBut;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("imageName")
    @Expose
    private String imageName;
    @SerializedName("able")
    @Expose
    private String able;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameBut() {
        return nameBut;
    }

    public void setNameBut(String nameBut) {
        this.nameBut = nameBut;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getAble() {
        return able;
    }

    public void setAble(String able) {
        this.able = able;
    }

}