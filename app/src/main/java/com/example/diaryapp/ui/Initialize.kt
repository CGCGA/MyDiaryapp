package com.example.diaryapp.ui

import android.annotation.SuppressLint
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.example.diaryapp.DiaryApplication
import com.example.diaryapp.R
import com.example.diaryapp.logic.model.DiaryAdapter
import com.example.diaryapp.logic.model.Diarydata
import com.example.diaryapp.logic.model.MyDiarydatabaseHelper
import de.hdodenhof.circleimageview.CircleImageView

object Initialize {

    val dbHelper = MyDiarydatabaseHelper(DiaryApplication.context, "Diary.db", 2)
    val diarylist = ArrayList<Diarydata>()
    var emoid = 1

    //查询数据库
    @SuppressLint("Range")
    fun searchDiary() {
        val db = dbHelper.writableDatabase
        val cursor = db.query("Diary", null, null, null, null, null, null)
        diarylist.clear()
        if (cursor.moveToLast()) {
            do {

                val title = cursor.getString(cursor.getColumnIndex("title"))
                val date = cursor.getString(cursor.getColumnIndex("time"))
                val content = cursor.getString(cursor.getColumnIndex("content"))
                val picture = cursor.getBlob(cursor.getColumnIndex("picture"))
                val emotion = cursor.getInt(cursor.getColumnIndex("emotion"))

                diarylist.add(Diarydata(title, date, content, picture, emotion))
            }while (cursor.moveToPrevious())
        }
        cursor.close()
    }

    fun judgeToday(date: String) : Boolean {
        val db = dbHelper.writableDatabase
        val cursor = db.query("Diary", arrayOf("time"), "time=?", arrayOf(date), null, null, null)
        if(cursor.moveToFirst()) {
            return true
        }
        return false
    }

    fun choseImg(id : Int) : Int{

        when(id) {
            1 -> return R.drawable.emotion_1
            2 -> return R.drawable.emotion_2
            3 -> return R.drawable.emotion_3
            4 -> return R.drawable.emotion_4
            5 -> return R.drawable.emotion_5
            6 -> return R.drawable.emotion_6
            7 -> return R.drawable.emotion_7
            8 -> return R.drawable.emotion_8

        }
        return R.drawable.emotion_1
    }



}