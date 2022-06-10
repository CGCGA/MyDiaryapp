package com.example.diaryapp.logic.model

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.diaryapp.DiaryActivity
import com.example.diaryapp.R

class MyService : Service() {

    override fun onCreate() {
        super.onCreate()

        val manger = getSystemService(Context.NOTIFICATION_SERVICE) as
                NotificationManager
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("my_service", "前台service通知",
            NotificationManager.IMPORTANCE_DEFAULT)
            manger.createNotificationChannel(channel)
        }
        val intent = Intent(this, DiaryActivity::class.java)
        val pi = PendingIntent.getActivity(this, 0, intent, 0)
        val notification = NotificationCompat.Builder(this, "my_service")
            .setContentTitle("生活简记")
            .setContentText("正在记录日记")
            .setSmallIcon(R.drawable.add)
            .setContentIntent(pi)
            .build()
        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }
}
