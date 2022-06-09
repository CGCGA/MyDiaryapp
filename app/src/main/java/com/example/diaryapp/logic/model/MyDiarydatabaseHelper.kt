package com.example.diaryapp.logic.model

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDiarydatabaseHelper(val context: Context, name: String, version: Int)
    : SQLiteOpenHelper(context, name, null, version) {


    private val createDiary = "create table Diary (" +
            " id integer primary key autoincrement," +
            "title text," +
            "time text," +
            "content text," +
            "picture BLOB," +
            "emotion int)"

    override fun onCreate(p0: SQLiteDatabase) {
        p0.execSQL(createDiary)
        Toast.makeText(context, "Create succeeded", Toast.LENGTH_SHORT).show()
    }

    override fun onUpgrade(p0: SQLiteDatabase, p1: Int, p2: Int) {

    }

}