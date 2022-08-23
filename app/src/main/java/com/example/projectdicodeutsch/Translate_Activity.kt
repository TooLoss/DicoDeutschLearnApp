package com.example.projectdicodeutsch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView

class Translate_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_translate)

        var BackBtn = findViewById<ImageView>(R.id.TranslateGotoMain)
        BackBtn.setOnClickListener(){
            setContentView(R.layout.activity_main)
            val intent = Intent(this, Translate_Activity::class.java)
            startActivity(intent)
        }

        var SearchBtn = findViewById<Button>(R.id.WordSearchBtn)
        SearchBtn.setOnClickListener() {

        }
    }
}