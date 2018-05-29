package com.example.hole1.recipebook;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        Log.d("g54mdp", "DBHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("g54mdp", "onCreate DB");

        db.execSQL("CREATE TABLE recipeTable("+
                "_id INTEGER PRIMARY KEY AUTOINCREMENT ,"+
                "title VARCHAR(128) NOT NULL ,"+
                "instruction VARCHAR(128) NOT NULL ,"+
                "ingredients VARCHAR(128) NOT NULL ,"+
                "category VARCHAR(128) NOT NULL"+
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS recipe");
        onCreate(db);
    }
}