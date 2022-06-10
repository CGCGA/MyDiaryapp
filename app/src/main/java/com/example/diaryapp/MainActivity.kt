package com.example.diaryapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Vibrator
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.contentValuesOf
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.example.diaryapp.logic.model.*
import com.example.diaryapp.ui.Initialize
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_main.*
import java.io.ByteArrayOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.thread


@SuppressLint("Range")
class MainActivity : AppCompatActivity() {

    val adapter = DiaryAdapter(this, Initialize.diarylist)
    val dbHelper = MyDiarydatabaseHelper(DiaryApplication.context, "Diary.db", 2)
    lateinit var navView : View
    var vibrate = false  //震动参数

    init {
        Initialize.searchDiary()



    }

    @SuppressLint("WrongThread")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent("com.example.diaryapp.logic.model.MY_BROADCAST_RERIVER")
        intent.setPackage(packageName)
        sendBroadcast(intent)

        val intentFileter = IntentFilter()
        intentFileter.addAction("")


        //获取上次操作的震动开关
        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        vibrate = prefs.getBoolean("vibrate", false)

        navView = nav.inflateHeaderView(R.layout.nav_header)
        initchangeUser()

        setSupportActionBar(toolbar)
        //若ActionBar不为空则加入右侧菜单键
        supportActionBar?.let {
            //Home控件默认在左边
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.menu)
        }

        //判断震动开关
        if (vibrate) {
            nav.setCheckedItem(R.id.openVibrate)
        } else {
            nav.setCheckedItem(R.id.closeVibrate)
        }

        nav.setNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.openVibrate -> {
                    vibrate = true
                    val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
                    editor.putBoolean("vibrate", vibrate)
                    editor.apply()
                }
                R.id.closeVibrate -> {
                    vibrate = false
                    val editor = getSharedPreferences("data", Context.MODE_PRIVATE).edit()
                    editor.putBoolean("vibrate", vibrate)
                    editor.apply()
                }
                R.id.feedback -> Toast.makeText(this, "反馈邮箱：1075396344@qq.com", Toast.LENGTH_SHORT).show()
                R.id.about -> Toast.makeText(this, "感谢使用", Toast.LENGTH_SHORT).show()
            }
            true
        }







        val layoutManager = GridLayoutManager(this, 2)
        diaryrecy.layoutManager = layoutManager

        diaryrecy.adapter = adapter


        //悬浮按钮，预做点击增加新日记
        fab.setOnClickListener {


            //获取今日日期
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val today = current.format(formatter)


            if (Initialize.judgeToday(today)) {
                Toast.makeText(this, "今日日记已存在", Toast.LENGTH_SHORT).show()
            } else {

                if (vibrate) {
                    val vibrator = this.getSystemService(VIBRATOR_SERVICE) as Vibrator
                    vibrator.vibrate(200)
                }




                    //图片的储存
                    val os = ByteArrayOutputStream()
                    val bitmap = BitmapFactory.decodeResource(resources, R.drawable.bg_test)
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
                    //将初始化数据存入数据库

                    Databasejudge.insertDatabase() {
                        val values = contentValuesOf(
                            "title" to "今日日记", "time" to today, "content" to "今日。。。",
                            "picture" to os.toByteArray(), "emotion" to 1
                        )
                        return@insertDatabase values
                    }


                    //Mytest.diarylist.add(0, Diarydata("今日日记", today.toString(), "今日...", R.drawable.bg_test))

                    Initialize.diarylist.add(
                        0,
                        Diarydata("今日日记", today, "今日...", os.toByteArray(), 1)
                    )
                    adapter.notifyItemInserted(0)
                }

                Toast.makeText(this, "成功创建新日记", Toast.LENGTH_SHORT).show()




        }
    }

    //重新进入页面后
    override fun onRestart() {
        super.onRestart()
        //Log.d("cgtest", "触发，返回")
        Initialize.searchDiary()
        initchangeUser()
        diaryrecy.adapter = adapter
        //Log.d("cgtest", "赢")
    }

    override fun onDestroy() {
        super.onDestroy()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> diaryLayout.openDrawer(GravityCompat.START)
            //R.id.backup -> Toast.makeText(this, "Backup", Toast.LENGTH_SHORT).show()
            R.id.the1 -> {
                intent = Intent(this, ChangeActivity::class.java)
                startActivity(intent)
            }
            R.id.the2 -> Toast.makeText(this, "test", Toast.LENGTH_SHORT).show()
        }
        return true
    }


    @SuppressLint("Range")
    private fun initchangeUser(){
        val db = dbHelper.writableDatabase
        val cursor = db.query("user", null, null, null, null, null, null)
        //val iview : NavigationView = findViewById(R.id.nav)
//
        val ihead = navView.findViewById<CircleImageView>(R.id._headicon)
        val iname = navView.findViewById<TextView>(R.id.name)
        val imotto = navView.findViewById<TextView>(R.id.motto)

        if (cursor.moveToFirst()) {
            val thead = cursor.getBlob(cursor.getColumnIndex("head"))
            val tname = cursor.getString(cursor.getColumnIndex("name"))
            val tsign = cursor.getString(cursor.getColumnIndex("sign"))
            val headimg = thead?.let { BitmapFactory.decodeByteArray(thead, 0, it.size) }

            ihead.setImageBitmap(headimg)
            //Log.d("cgtest", tname)
            iname.text = tname
            imotto.text = tsign
        } else {

            val os = ByteArrayOutputStream()
            val bitmap = BitmapFactory.decodeResource(DiaryApplication.context.resources, R.drawable.head)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os)
            Databasejudge.insertUser {
                val values = contentValuesOf("head" to os.toByteArray(), "name" to "用户cg",
                    "sign" to "好好生活")
                return@insertUser values
            }
        }

    }

}