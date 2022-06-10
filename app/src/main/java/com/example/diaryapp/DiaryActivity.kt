package com.example.diaryapp

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.diaryapp.logic.model.Databasejudge
import com.example.diaryapp.logic.model.MyService
import com.example.diaryapp.ui.Initialize
import kotlinx.android.synthetic.main.activity_diary.*
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread

class DiaryActivity : AppCompatActivity() {

    companion object {
        const val DIARY_POSITION = "diary_position"
    }

    lateinit var diaryData: String
    val os = ByteArrayOutputStream()
    var temnum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val diarypositon = intent.getIntExtra(DIARY_POSITION, 0)
        val diary = Initialize.diarylist[diarypositon]
        //测试暂用,点击进入的内容
        var diaryName = diary.title


        //字节组照片转化
        val diaryImagebyt = diary.picture
        val diaryImage =
            diaryImagebyt?.let { BitmapFactory.decodeByteArray(diaryImagebyt, 0, it.size) }


        diaryData = diary.date ?: "2022-0-0"
        val diaryContent = diary.content ?: "。。。"

        setSupportActionBar(diarytoolbar)
        //设置toolbar上按钮
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        collasingToolbar.title = diaryData //设置时间日期标题
        collasingToolbar.setExpandedTitleColor(getResources().getColor(R.color.white))
        diarytitle.setText(diaryName)

        //Glide赋图片上去
        //Glide.with(this).load(diaryImageID).into(diaryimg)
        diaryimg.setImageBitmap(diaryImage)//设置照片

        //设置日记内容
        writediary.setText(diaryContent)

        Initialize.emoid = diary.emotion
        emoji.setImageResource(Initialize.choseImg(Initialize.emoid))


        val intent = Intent(this, MyService::class.java)
        startService(intent)

        val eintent = Intent(this, EmotionActivity::class.java)
        emoji.setOnClickListener {
            startActivity(eintent)
        }

    }

    override fun onPause() {
        super.onPause()
        val intent = Intent(this, MyService::class.java)
        stopService(intent)
        getNowData(diaryData)
    }

    override fun onResume() {
        super.onResume()
        emoji.setImageResource(Initialize.choseImg(Initialize.emoid))
    }

    //定义状态栏上菜单键功能
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {

                //保存数据
                getNowData(diaryData)
                finish()
                return true
            }

            R.id.deleteDiary -> {

                //预计再做一个确认按键

                val deldiary = AlertDialog.Builder(this)
                deldiary.setIcon(R.drawable.test2)
                    .setTitle("是否确认删除今日日记？")
                    .setMessage("删除后将无法复原该日记")
                    .setPositiveButton("确认",
                        DialogInterface.OnClickListener { dialogInterface, id ->
                            Databasejudge.deleteDatabase(diaryData)
                            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    )
                    .setNegativeButton("取消",
                        DialogInterface.OnClickListener { dialogInterface, id ->

                        }
                    )
                deldiary.show()


                return true
            }

            R.id.changeImg -> {

                //更换图片
                //打开文件选择器
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                //限制只找图片
                intent.type = "image/*"

                startActivityForResult(intent, 0)

            }


        }
        return super.onOptionsItemSelected(item)
    }

    private fun getNowData(date: String) {

        val title = diarytitle.text
        val content = writediary.text
        val emotion = Initialize.emoid
        Databasejudge.updateDatabase(date) {
            val values = ContentValues()

            values.put("title", title.toString())
            values.put("content", content.toString())
            values.put("emotion", emotion)
            if (temnum == 1) {
                values.put("picture", os.toByteArray())
            }


            return@updateDatabase values
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.diary_menu, menu)
        return true
    }


    //获取并储存图片
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            //从uri中获取图片
            data.data?.let { uri ->
                val bitmap = getBitmapFromUri(uri)
                if (bitmap != null) {
                    if (bitmap.allocationByteCount <= 64218112) {//若图片大于5m无法选择
                        temnum = 1
                        diaryimg.setImageBitmap(bitmap)
                        thread {//压缩画质
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, os)
                        }
                    } else {
                        Log.d("cgtest", bitmap.allocationByteCount.toString())
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


}

