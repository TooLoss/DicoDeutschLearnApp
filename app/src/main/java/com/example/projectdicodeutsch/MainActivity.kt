package com.example.projectdicodeutsch

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import org.w3c.dom.Text
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection


class MainActivity : AppCompatActivity() {

    private val client = OkHttpClient()
    private val apiResponse = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //APICallVersion().start()

        val versionText = findViewById<TextView>(R.id.versionText)
        versionText.text = "Version de l'application : " + BuildConfig.VERSION_NAME
    }

    val pageList = listOf(translate_search::class.java, Exercice_frenchtodeutsch::class.java, wordlist::class.java)

    /*

    private fun APICallVersion(): Thread
    {
        return Thread() {
            val url = URL("https://api.github.com/repos/TooLoss/DicoDeutschLearnApp/releases")
            val connection = url.openConnection() as HttpsURLConnection
            if(connection.responseCode == 200) {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val gsonBuilder = GsonBuilder()
                val gson = gsonBuilder.create()

                val request: Array<TestCase> = gson.fromJson(inputStreamReader, Array<TestCase>::class.java)
                //val request = Gson().fromJson(inputStreamReader, Request::class.java)
                hasNewUpdate()
                inputStreamReader.close()
                inputSystem.close()
            } else {
                println("TEST")
            }
        }
    }

    */

    private class TestCase {
        @SerializedName("test")
        private val field: String? = null
    }

    /*
    private fun hasNewUpdate() {
        runOnUiThread {
            kotlin.run {
            }
            println(field)
        }
    }

     */
    fun goToPage(v: View) {
        val tagString = v.tag.toString()
        val pageNumber = Integer.valueOf(tagString)
        val intent = Intent(this, pageList[pageNumber])
        startActivity(intent)
    }
}
