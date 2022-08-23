package com.example.projectdicodeutsch

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Déclarer les variables
        var SwitchTranslateBtn = findViewById<Button>(R.id.SwitchTranslate) as Button
        var SwitchExercicesBtn = findViewById<Button>(R.id.SwitchExercices) as Button
        // QUAND TRANSLATE BUTTON EST CLIQUE
        SwitchTranslateBtn.setOnClickListener {
            val intent = Intent(this, translate_search::class.java)
            startActivity(intent)
        }
        SwitchExercicesBtn.setOnClickListener {
            val intent = Intent(this, Exercice_frenchtodeutsch::class.java)
            startActivity(intent)
        }
    }
}