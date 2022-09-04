package com.example.projectdicodeutsch

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
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

        if(if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                isOnline()
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) {
            APICallVersion().start()
        }

        val versionText = findViewById<TextView>(R.id.versionText)
        versionText.text = "Version de l'application : " + BuildConfig.VERSION_NAME
    }

    val pageList = listOf(translate_search::class.java, Exercice_frenchtodeutsch::class.java, wordlist::class.java)

    private fun APICallVersion(): Thread
    {
        return Thread() {
            val url = URL("https://api.github.com/repos/TooLoss/DicoDeutschLearnApp/releases/latest")
            val connection = url.openConnection() as HttpsURLConnection
            if(connection.responseCode == 200) {
                val inputSystem = connection.inputStream
                val inputStreamReader = InputStreamReader(inputSystem, "UTF-8")
                val gsonBuilder = GsonBuilder()
                val gson = gsonBuilder.create()

                val request = gson.fromJson(inputStreamReader, Request::class.java)
                hasNewUpdate(request)
                inputStreamReader.close()
                inputSystem.close()
            } else {
                println("TEST")
            }
        }
    }

    private class TestCase {
        @SerializedName("test")
        private val field: String? = null
    }

    private fun hasNewUpdate(request: Request) {
        runOnUiThread {
            kotlin.run {
                val name = request.tag_name
                val url = request.html_url

                if(name[1].toInt() > BuildConfig.VERSION_NAME[0].toInt()) {
                    newUpdate(url)
                } else if(name[1].toInt() == BuildConfig.VERSION_NAME[0].toInt() && name[3].toInt() > BuildConfig.VERSION_NAME[2].toInt()) {
                    newUpdate(url)
                } else if(name[1].toInt() == BuildConfig.VERSION_NAME[0].toInt() && name[3].toInt() == BuildConfig.VERSION_NAME[2].toInt() && name[5].toInt() > BuildConfig.VERSION_NAME[4].toInt()) {
                    newUpdate(url)
                } else {
                    Toast.makeText(this, "Aucune mise à jour", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun newUpdate(updateUrl: String) {
        var dialog = NewUpdateDialogFragment()
        dialog.show(supportFragmentManager, "updateDialog")
    }

    fun goToPage(v: View) {
        val tagString = v.tag.toString()
        val pageNumber = Integer.valueOf(tagString)
        val intent = Intent(this, pageList[pageNumber])
        startActivity(intent)
    }

    fun isConnected(): Boolean {
        var cm: ConnectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var networdInfo: NetworkInfo? = cm.activeNetworkInfo

        if(networdInfo!=null && networdInfo.isConnected) {
            return true
        }
        return false
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun isOnline(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    return true
                } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                    return true
                }
            }
        }
        return false
    }
}
