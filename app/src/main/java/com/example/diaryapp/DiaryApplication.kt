package com.example.diaryapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class DiaryApplication : Application() {

    companion object {
        @SuppressLint("StaticFieldleak")
        lateinit var context: Context
    }

    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }

}