package com.example.diaryapp.logic.model

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class MyBroadcastRecriver : BroadcastReceiver() {

    override fun onReceive(p0: Context, p1: Intent) {
        Toast.makeText(p0, "今天也要记得记录下自己的生活哦~", Toast.LENGTH_SHORT).show()
    }

}