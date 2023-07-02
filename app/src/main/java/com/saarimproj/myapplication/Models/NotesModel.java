package com.saarimproj.myapplication.Models;

public class NotesModel {
    String note_ID ,  user_ID  , note_date ,  note_Description , note_DateMonthYear ,  note_WeekDay ;

    public NotesModel(String note_ID, String user_ID, String note_date, String note_Description, String note_DateMonthYear, String note_WeekDay) {
        this.note_ID = note_ID;
        this.user_ID = user_ID;
        this.note_date = note_date;
        this.note_Description = note_Description;
        this.note_DateMonthYear = note_DateMonthYear;
        this.note_WeekDay = note_WeekDay;
    }

    public NotesModel() {
    }

    public String getNote_ID() {
        return note_ID;
    }

    public void setNote_ID(String note_ID) {
        this.note_ID = note_ID;
    }

    public String getUser_ID() {
        return user_ID;
    }

    public void setUser_ID(String user_ID) {
        this.user_ID = user_ID;
    }

    public String getNote_date() {
        return note_date;
    }

    public void setNote_date(String note_date) {
        this.note_date = note_date;
    }

    public String getNote_Description() {
        return note_Description;
    }

    public void setNote_Description(String note_Description) {
        this.note_Description = note_Description;
    }

    public String getNote_DateMonthYear() {
        return note_DateMonthYear;
    }

    public void setNote_DateMonthYear(String note_DateMonthYear) {
        this.note_DateMonthYear = note_DateMonthYear;
    }

    public String getNote_WeekDay() {
        return note_WeekDay;
    }

    public void setNote_WeekDay(String note_WeekDay) {
        this.note_WeekDay = note_WeekDay;
    }
}
