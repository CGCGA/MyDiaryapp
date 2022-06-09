package com.example.diaryapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.diaryapp.ui.Initialize
import kotlinx.android.synthetic.main.emotion_item.*

class EmotionActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.emotion_item)

        emoitem1.setOnClickListener {
            Initialize.emoid = 1
            finish()
        }
        emoitem2.setOnClickListener {
            Initialize.emoid = 2
            finish()
        }
        emoitem3.setOnClickListener {
            Initialize.emoid = 3
            finish()
        }
        emoitem4.setOnClickListener {
            Initialize.emoid = 4
            finish()
        }
        emoitem5.setOnClickListener {
            Initialize.emoid = 5
            finish()
        }
        emoitem6.setOnClickListener {
            Initialize.emoid = 6
            finish()
        }
        emoitem7.setOnClickListener {
            Initialize.emoid = 7
            finish()
        }
        emoitem8.setOnClickListener {
            Initialize.emoid = 8
            finish()
        }
    }
}