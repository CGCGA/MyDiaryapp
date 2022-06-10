package com.example.diaryapp.logic.dao

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.net.Uri
import com.example.diaryapp.logic.model.MyDiarydatabaseHelper

class DatabaseProvider : ContentProvider() {
    //开放用户信息
    private val userDir = 0
    private val userItem = 1
    private val authority = "com.example.Diary.provider"
    private var dbHelper: MyDiarydatabaseHelper? = null

    private val uriMatcher by lazy {
        val matcher = UriMatcher(UriMatcher.NO_MATCH)
        matcher.addURI(authority, "User", userDir)
        matcher.addURI(authority, "User/#", userItem)
        matcher
    }

    override fun onCreate() = context?.let {
        dbHelper = MyDiarydatabaseHelper(it, "Diary.db", 2)
        true
    } ?: false

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ) = dbHelper?.let {
        // 查询数据
        val db = it.readableDatabase
        val cursor = when (uriMatcher.match(uri)) {
            userDir -> db.query("User", projection, selection, selectionArgs, null, null, sortOrder)
            userItem -> {
                val userId = uri.pathSegments[1]
                db.query("User", projection, "id = ?", arrayOf(userId), null, null, sortOrder)
            }
            else -> null
        }
        cursor
    }

    override fun insert(uri: Uri, values: ContentValues?) = dbHelper?.let {
        // 添加数据
        val db = it.writableDatabase
        val uriReturn = when (uriMatcher.match(uri)) {
            userDir, userItem -> {
                val newUserId = db.insert("User", null, values)
                Uri.parse("content://$authority/user/$newUserId")
            }
            else -> null
        }
        uriReturn
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?) =
        dbHelper?.let {
            // 更新数据
            val db = it.writableDatabase
            val updatedRows = when (uriMatcher.match(uri)) {
                userDir -> db.update("User", values, selection, selectionArgs)
                userItem -> {
                    val userId = uri.pathSegments[1]
                    db.update("User", values, "id = ?", arrayOf(userId))
                }
                else -> 0
            }
            updatedRows
        } ?: 0

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?) = dbHelper?.let {
        // 删除数据
        val db = it.writableDatabase
        val deletedRows = when (uriMatcher.match(uri)) {
            userDir -> db.delete("Book", selection, selectionArgs)
            userItem -> {
                val bookId = uri.pathSegments[1]
                db.delete("Book", "id = ?", arrayOf(bookId))
            }
            else -> 0
        }
        deletedRows
    } ?: 0

    override fun getType(uri: Uri) = when (uriMatcher.match(uri)) {
        userDir -> "vnd.android.cursor.dir/vnd.com.example.Diary.provider.book"
        userItem -> "vnd.android.cursor.item/vnd.com.example.Diary.provider.book"
        else -> null
    }

}