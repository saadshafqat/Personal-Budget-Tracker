package com.saarimproj.myapplication.Models;

public class IncomeModel {
    String UID;
    String transID;
    String date;
    String month;
    String year;
    String transammount;
    String title;
    String description;
    String category;
    String type;

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getTransID() {
        return transID;
    }

    public void setTransID(String transID) {
        this.transID = transID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getTransammount() {
        return transammount;
    }

    public void setTransammount(String transammount) {
        this.transammount = transammount;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public IncomeModel(String UID, String transID, String date, String month, String year, String transammount, String title, String description, String category, String type) {
        this.UID = UID;
        this.transID = transID;
        this.date = date;
        this.month = month;
        this.year = year;
        this.transammount = transammount;
        this.title = title;
        this.description = description;
        this.category = category;
        this.type = type;
    }
}
