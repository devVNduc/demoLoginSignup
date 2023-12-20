package com.example.demologinsignup;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String databaseName = "Login.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "Login.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("create table allusers(email TEXT primary key,password TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int oldVersion, int newVersion) {
        MyDatabase.execSQL("drop table if exists allusers");
    }
    public Boolean insertData(String username, String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("Username",username);
        contentValues.put("Password",password);
        long result = MyDatabase.insert("",null,contentValues);
        if (result == -1) {
            return false;
        }else{
            return true;
        }
    }
    public  Boolean checkUsername(String username){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where username=?",new String[]{username});
        if (cursor.getCount()>0){
            return false;
        }else{
            return true;
        }
    }
    public  Boolean checkUsernamePassword(String username,String password){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where username=? and password=?",new String[]{username,password});
        if (cursor.getCount()>0){
            return false;
        }else{
            return true;
        }
    }

}
