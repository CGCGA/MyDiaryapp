package com.example.diaryapp.logic.model

import android.graphics.Bitmap
import android.graphics.Picture
import android.provider.ContactsContract
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.sql.Blob
import java.util.*


data class Diarydata(
    val title: String, val date: String,
    val content: String, val picture: ByteArray,
    val emotion: Int
)