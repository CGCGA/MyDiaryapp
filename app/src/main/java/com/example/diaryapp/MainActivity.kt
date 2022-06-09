package com.example.diaryapp

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import androidx.core.view.GravityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.example.diaryapp.logic.model.Databasejudge
import com.example.diaryapp.logic.model.DiaryAdapter
import com.example.diaryapp.logic.model.Diarydata
import com.example.diaryapp.ui.Initialize
import com.example.diaryapp.ui.Mytest
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.log


class MainActivity : AppCompatActivity() {

    val adapter = DiaryAdapter(this, Initialize.diarylist)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        setSupportActionBar(toolbar)
        //若ActionBar不为空则加入右侧菜单键
        supportActionBar?.let {
            //Home控件默认在左边
            it.setDisplayHomeAsUpEnabled(true)
        }

        //右划栏默认选择new1键，点击任1按键后关闭
        nav.setCheckedItem(R.id.new1)
        nav.setNavigationItemSelectedListener {
            diaryLayout.closeDrawers()
            true
        }


        //初始化测试
        //Mytest.initdiarytest()
        Initialize.searchDiary()
        val layoutManager = GridLayoutManager(this, 2)
        diaryrecy.layoutManager = layoutManager

        diaryrecy.adapter = adapter

        //悬浮按钮，预做点击增加新日记
        addfab.setOnClickListener {


            //获取今日日期
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val today = current.format(formatter)


            if (Initialize.judgeToday(today)) {
                Toast.makeText(this, "今日日记已存在", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "成功创建新日记", Toast.LENGTH_SHORT).show()
                //图片的储存
                val os = ByteArrayOutputStream()
                val bitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_test)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                //将初始化数据存入数据库
                Databasejudge.insertDatabase(){
                    val values = contentValuesOf("title" to "今日日记", "time" to today, "content" to "今日。。。",
                        "picture" to os.toByteArray(), "emotion" to 1)
                    return@insertDatabase values
                }

                //Mytest.diarylist.add(0, Diarydata("今日日记", today.toString(), "今日...", R.drawable.bg_test))

                Initialize.diarylist.add(0, Diarydata("今日日记", today, "今日...", os.toByteArray(), 1))
                adapter.notifyItemInserted(0)
            }



        }
    }

    //重新进入页面后
    override fun onRestart() {
        super.onRestart()
        //Log.d("cgtest", "触发，返回")
        Initialize.searchDiary()
        diaryrecy.adapter = adapter
        //Log.d("cgtest", "赢")
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> diaryLayout.openDrawer(GravityCompat.START)
            //R.id.backup -> Toast.makeText(this, "Backup", Toast.LENGTH_SHORT).show()
            R.id.the1 -> Toast.makeText(this, "cg还真是你爹", Toast.LENGTH_SHORT).show()
            R.id.the2 -> Toast.makeText(this, "贝拉拉是你麻麻", Toast.LENGTH_SHORT).show()
            R.id.deleteDiary -> Toast.makeText(this, "testyx", Toast.LENGTH_SHORT).show()
        }
        return true
    }



}