package com.example.diaryapp.ui

import com.example.diaryapp.R
import com.example.diaryapp.logic.model.Diarydata

object Mytest {

    val diarylist = ArrayList<Diarydata>()

    fun initdiarytest() {
         diarylist.clear()
        repeat(5) {
            //diarylist.add(Diarydata("你热爱的就是你的生活", "2022-6-1", "看看你的", R.drawable.bg_test))
        }
    }

}