package com.apostleFox.demoFoxBot.model;

public class Menu {
    private String category;

    private String type;

    private String proportion;

    private Integer number;

    private String activeMenuType = "category";

    public String getActiveMenuType() {
        return activeMenuType;
    }

    public String itemMenuResult(){
        return category + " " + type + " " + proportion;
    }

    public String buildMenuResult(){
        return category + " " + type + " " + proportion + " " + number;

    }

    public void clearMenu(){
        category = null;
        type = null;
        proportion = null;
        number = null;
        activeMenuType = "category";
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        activeMenuType = "category";
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        activeMenuType = "type";
    }

    public String getProportion() {
        return proportion;
    }

    public void setProportion(String proportion) {
        this.proportion = proportion;
        activeMenuType = "proportion";
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
