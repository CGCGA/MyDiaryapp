package com.example.diaryapp.logic.model

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.diaryapp.DiaryActivity
import com.example.diaryapp.R

class DiaryAdapter(val context: Context, val DiaryList: List<Diarydata>)
    : RecyclerView.Adapter<DiaryAdapter.ViewHolder>() {

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val diaryimg: ImageView = view.findViewById(R.id.diaryheadimg)
            val diaryname: TextView = view.findViewById(R.id.diarytitle)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.diary_item, parent, false)

        val holder = ViewHolder(view)
        holder.itemView.setOnClickListener{
            val position = holder.adapterPosition
            //val diary = DiaryList[position]
            val intent = Intent(context, DiaryActivity::class.java).apply {
                putExtra(DiaryActivity.DIARY_POSITION, position)
            }
            //Log.d("cgtest", diary.date)
            context.startActivity(intent)
        }

        return holder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val diary = DiaryList[position]
        holder.diaryname.text = diary.date+" "+diary.title
        val diaryImage = BitmapFactory.decodeByteArray(diary.picture, 0, diary.picture.size)
        holder.diaryimg.setImageBitmap(diaryImage)
    }

    override fun getItemCount(): Int {
        return DiaryList.size
    }

}
