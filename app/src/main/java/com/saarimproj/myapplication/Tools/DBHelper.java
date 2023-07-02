package com.saarimproj.myapplication.Tools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(Context context) {
        super(context, "daytodayexpenses.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table Accounts(UID TEXT, currency TEXT , profilename TEXT, profilepurpose TEXT, additionalinfo TEXT, profilepic TEXT, accountidentity TEXT, bankdetails TEXT, openingbalance TEXT, currentbalance TEXT)");
        db.execSQL("create Table Notes( note_ID TEXT,  user_ID TEXT , note_date TEXT,  note_Description TEXT, note_DateMonthYear TEXT,  note_WeekDay TEXT)");
        db.execSQL("create Table Transactions(UID TEXT , transID TEXT, date TEXT,month TEXT,year TEXT,transammount TEXT,title TEXT,description TEXT,category TEXT,type TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop Table if exists Accounts");
        db.execSQL("drop Table if exists Transactions");
        db.execSQL("drop Table if exists Notes");

    }
    public Boolean insertNote(String noteID, String userID ,String date , String notedescription ,String MonthYear , String weekday ) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("note_ID", noteID);
        contentValues.put("user_ID", userID);
        contentValues.put("note_date", date);
        contentValues.put("note_Description", notedescription);
        contentValues.put("note_DateMonthYear", MonthYear);
        contentValues.put("note_WeekDay", weekday);

        long result = DB.insert("Notes", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }
        public Cursor getAllNotes() {
            String query = "SELECT* FROM Notes";
            SQLiteDatabase db = this.getWritableDatabase();
            Cursor cursor = null;
            if (db != null) {
                cursor = db.rawQuery(query, null);
            }
            return cursor;

        }
    public Cursor getNotesfrom(String monthYear) {
        String query = "SELECT* FROM Notes WHERE note_DateMonthYear"+"='" + monthYear + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }
    public Boolean insertTransaction(String UID  ,String transID ,String date ,String month ,String year ,String transammount ,String title,String description,String category,String type ) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("UID", UID);
        contentValues.put("transID", transID);
        contentValues.put("date", date);
        contentValues.put("month", month);
        contentValues.put("year", year);
        contentValues.put("transammount", transammount);
        contentValues.put("description", description);
        contentValues.put("category", category);
        contentValues.put("title", title);
        contentValues.put("type", type);

        long result = DB.insert("Transactions", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean insertAccount(String UID, String currency  , String profilename , String profilepurpose , String additionalinfo , String profilepic , String accountidentity ,String bankdetails ,String  openingbalance ,String currentbalance ) {
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("user_id", UID);
        contentValues.put("currency", currency);
        contentValues.put("profile_name", profilename);
        contentValues.put("profile_purpose", profilepurpose);
        contentValues.put("additional_info", additionalinfo);
        contentValues.put("profile_pic", profilepic);
        contentValues.put("account_identity", accountidentity);
        contentValues.put("bank_details", bankdetails);
        contentValues.put("opening_balance", openingbalance);
        contentValues.put("current_balance", currentbalance);

        long result = DB.insert("Accounts", null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getAccounts() {
        String query = "SELECT* FROM Accounts";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }
    public Cursor getAllTransactions() {
        String query = "SELECT* FROM Transactions";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }
    public Cursor countTransactions(String month,String year) {
        String query = "SELECT DISTINCT date FROM Transactions WHERE month"+"='" + month+ "'"+ " AND  year"+"='" + year + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }
    public Cursor countTransactions(String year) {
        String query = "SELECT DISTINCT month FROM Transactions WHERE year"+"='" + year + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }

    public Cursor getIncomeTransaction(String date,String type) {
        String query = "SELECT* FROM Transactions WHERE date"+"='" + date + "'" + " AND  type"+"='" + type + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }
    public Cursor getInTransaction(String type) {
        String query = "SELECT* FROM Transactions WHERE type"+"='" + type + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }
    public Cursor getTransaction(String month,String type) {
        String query = "SELECT* FROM Transactions WHERE month"+"='" + month + "'" + " AND  type"+"='" + type + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }
    public Cursor getIncomeMonthly(String numberMonth,String numberYear,String type) {
        String query = "SELECT* FROM Transactions WHERE month"+"='" + numberMonth + "'" + " AND  type"+"='" + type + "'"+ " AND  year"+"='" + numberYear + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);
        }
        return cursor;

    }
    public void deletefromNotes(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + "Notes" + " WHERE " + "note_ID" + "='" + value + "'");
        db.close();
    }

    public void deleteFromAccount(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + "Accounts" + " WHERE " + "user_id" + "='" + value + "'");
        db.close();
    }

    public void deletefromTransaction(String ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + "Transactions" + " WHERE " + "transID" + "='" + ID + "'");
        db.close();
    }

    public boolean checkfromAccounts(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + "Accounts" + " where " + "user_id" + "='" + value + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        return true;


    }
    public void clearAllTransactions(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM Transactions"); //delete all rows in a table
        db.close();
    }

    public boolean checkfromTransaction(String value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Query = "Select * from " + "Transactions" + " where " + "trans_ID" + "='" + value + "'";
        Cursor cursor = db.rawQuery(Query, null);
        if (cursor.getCount() <= 0) {
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;

    }

//    public String returnDownloadingID(String value) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String s1 = null;
//        String Query = "Select * from " + "Transactions" + " where " + "video_name" + "='" + value + "'";
//        Cursor cursor = db.rawQuery(Query, null);
//        if (cursor.moveToNext()) {
//            s1 = cursor.getString(cursor.getColumnIndex("IDKEY"));
//
//        }
//        db.close();
//        return s1;
// This Function is used to return something from table on a condition
//    }
    public void editTransaction(String UID  ,String transID ,String date ,String month ,String year ,String transammount ,String title,String description,String category,String type) {
        SQLiteDatabase db = this.getWritableDatabase();
        String s1 = null;

        String Query = "UPDATE Transactions SET "+ "date" + "='" + date + "',month"+"='"+month  + "',year"+"='"+year  + "',transammount"+"='"+transammount+"" + "',description"+"='"+description + "',category"+"='"+category + "',title"+"='"+title + "',type"+"='"+type+"'" + " WHERE " + "transID" +"='" + transID + "'" ;
        db.execSQL(Query);

    }
    public void editNote(String noteID ,String date , String notedescription ,String MonthYear , String weekday) {
        SQLiteDatabase db = this.getWritableDatabase();
        String s1 = null;

        String Query = "UPDATE Notes SET "+ "note_date" + "='" + date + "',note_Description"+"='"+notedescription  + "',note_DateMonthYear"+"='"+MonthYear  + "',note_WeekDay"+"='"+weekday+"'" + " WHERE " + "note_ID" +"='" + noteID + "'" ;
        db.execSQL(Query);

    }
}