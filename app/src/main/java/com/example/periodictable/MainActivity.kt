package com.example.periodictable

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.periodictable.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.BufferedReader
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var searchJob: Job? = null
    private val button = ArrayList<Button>()

    private fun loopThrough(parent: ViewGroup) {
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            if (child is Button) button.add(child)
            else if (child is ViewGroup) loopThrough(child)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loopThrough(findViewById(R.id.root))

        for (index in button.indices) {
            button[index].setOnClickListener {
                val elementSymbol = button[index].getText().toString()
                if (isNetworkAvailable()) {
                    if (searchJob?.isActive != true) {
                        findElement(elementSymbol)
                    } else {
                        Toast.makeText(
                            applicationContext,
                            "No connection to network", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun isNetworkAvailable(): Boolean {
        var available = false
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        cm?.run {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cm.getNetworkCapabilities(cm.activeNetwork)?.run {
                    if (hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                        || hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
                        || hasTransport(NetworkCapabilities.TRANSPORT_VPN)
                    ) {
                        available = true
                    }
                }

            } else {
                cm.getActiveNetworkInfo()?.run {
                    if (type == ConnectivityManager.TYPE_MOBILE
                        || type == ConnectivityManager.TYPE_WIFI
                        || type == ConnectivityManager.TYPE_VPN
                    ) {
                        available = true
                    }
                }
            }
        }
        return available
    }

    private fun findElement(element: String) {

        searchJob = CoroutineScope(Dispatchers.IO).launch {

            val builder = Uri.Builder()
                .scheme("https")
                .authority("periodic-table-elements-info.herokuapp.com")
                .path("/elements")

            val link = builder.build().toString()
            val url = URL(link)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

            val jsonStr: String

            try {
                jsonStr = connection.getInputStream().bufferedReader()
                    .use(BufferedReader::readText)
            } finally {
                connection.disconnect()
            }

            val json = JSONArray(jsonStr)

            for (elementIndex in 0 until json.length()) {
                val elementObject = json.getJSONObject(elementIndex)
                val symbol = elementObject.getString("symbol").toString()

            }
        }
    }
}