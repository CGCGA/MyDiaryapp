package com.example.diaryapp

import android.R.id.home
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import com.example.diaryapp.logic.model.Databasejudge
import com.example.diaryapp.logic.model.MyDiarydatabaseHelper
import com.example.diaryapp.ui.Initialize
import kotlinx.android.synthetic.main.activity_change.*
import kotlinx.android.synthetic.main.activity_diary.*
import kotlinx.android.synthetic.main.nav_header.*
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread

@SuppressLint("Range")
class ChangeActivity : AppCompatActivity() {

    val dbHelper = MyDiarydatabaseHelper(DiaryApplication.context, "Diary.db", 2)
    var temnum = 0
    val os = ByteArrayOutputStream()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change)

        setSupportActionBar(changetoolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val db = dbHelper.writableDatabase
        val cursor = db.query("user", null, null, null, null, null, null)

        if(cursor.moveToFirst()) {
            val thead = cursor.getBlob(cursor.getColumnIndex("head"))
            val tname = cursor.getString(cursor.getColumnIndex("name"))
            val tsign = cursor.getString(cursor.getColumnIndex("sign"))

            val headimg = thead?.let { BitmapFactory.decodeByteArray(thead, 0, it.size) }

            _yourChosehead.setImageBitmap(headimg)
            changeName.setText(tname)
            changeSign.setText(tsign)
        }




        _yourChosehead.setOnClickListener {

            //打开文件选择器
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            //限制只找图片
            intent.type = "image/*"

            startActivityForResult(intent, 0)


        }

    }

    override fun onPause() {
        super.onPause()
        getNowData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                getNowData()
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    //获取并储存头像
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            //从uri中获取图片
            data.data?.let { uri ->
                val bitmap = getBitmapFromUri(uri)
                if (bitmap != null) {
                    if (bitmap.allocationByteCount <= 64218112) {//若图片大于5m无法选择
                        temnum = 1
                        _yourChosehead.setImageBitmap(bitmap)
                        thread {//压缩画质
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, os)
                        }
                    } else {
                        //Log.d("cgtest", bitmap.allocationByteCount.toString())
                        Toast.makeText(this, "图片大于5M请重新选择", Toast.LENGTH_SHORT).show()
                    }
                }


            }
        }
    }

    //将uri转化成bitmap
    private fun getBitmapFromUri(uri: Uri) = contentResolver
        .openFileDescriptor(uri, "r")?.use {
            BitmapFactory.decodeFileDescriptor(it.fileDescriptor)
        }

    fun getNowData() {

        val name = changeName.text
        val motto = changeSign.text
        Databasejudge.updateUser {
            val values = ContentValues()

            if (temnum == 1) {
                values.put("head", os.toByteArray())
            }
            values.put("sign", motto.toString())
            values.put("name", name.toString())

            return@updateUser values
        }
    }

}