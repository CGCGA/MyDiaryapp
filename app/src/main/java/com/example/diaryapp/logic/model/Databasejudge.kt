package com.example.diaryapp.logic.model

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.diaryapp.DiaryApplication

object Databasejudge {

    //val dbHelper = MyDiarydatabaseHelper(DiaryApplication.context, "Diary.db", 1)
    val dbHelper = MyDiarydatabaseHelper(DiaryApplication.context, "Diary.db", 2)

    //新建日记
    fun insertDatabase(block: ()-> ContentValues) {
        val db = dbHelper.writableDatabase
        val values = block()
        db.insert("Diary", null, values)

    }

    //更新日记表数据
    fun updateDatabase(theDate: String, block: () -> ContentValues) {
        val db = dbHelper.writableDatabase
        val values = block()
        db.update("Diary", values, "time = ?", arrayOf(theDate))
    }

    //删除日记
    fun deleteDatabase(theDate: String) {
        val db = dbHelper.writableDatabase
        db.delete("Diary", "time = ?", arrayOf(theDate))
    }

    //初始化用户
    fun insertUser(block: ()-> ContentValues) {
        val db = dbHelper.writableDatabase
        val values = block()
        db.insert("User", null, values)

    }

    //更新用户信息
    fun updateUser(block: () -> ContentValues) {
        val db = dbHelper.writableDatabase
        val values = block()
        db.update("User", values, "id = ?", arrayOf("1"))
    }

}