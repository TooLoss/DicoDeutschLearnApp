package com.example.projectdicodeutsch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    val pageList = listOf(translate_search::class.java, Exercice_frenchtodeutsch::class.java, wordlist::class.java)

    fun goToPage(v: View) {
        val tagString = v.tag.toString()
        val pageNumber = Integer.valueOf(tagString)
        val intent = Intent(this, pageList[pageNumber])
        startActivity(intent)
    }
}