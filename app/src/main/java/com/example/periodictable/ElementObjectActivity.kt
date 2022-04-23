package com.example.periodictable

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import com.example.periodictable.databinding.ActivityElementObjectBinding
import kotlinx.coroutines.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class ElementObjectActivity : AppCompatActivity() {
    private lateinit var binding: ActivityElementObjectBinding
    private var imageJob: Job? = null
    private lateinit var elementInfo: Element
    private var imageCheck = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityElementObjectBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = getIntent()
        val data: Element = intent.getSerializableExtra(
            getString(R.string.intent_data_key)
        ) as Element

        elementInfo = data

        val imageUrl = "https://images-of-elements.com/${data.name.lowercase()}.jpg"
        if (isNetworkAvailable()) {
            if (imageJob?.isActive != true) {
                getImage(imageUrl)
            } else {
                Toast.makeText(
                    applicationContext,
                    "No connection to network", Toast.LENGTH_LONG
                ).show()
            }
        }
        getImage(imageUrl)

        val widgets = listOf<TextView>(
            binding.atomicNumberTextView, binding.symbolTextView, binding.nameTextView,
            binding.atomicMassTextView, binding.electronicConfigTextView,
            binding.atomicRadiusTextView, binding.standardStateTextView,
            binding.bondingTypeTextView, binding.meltingPointTextView, binding.boilingPointTextView,
            binding.densityTextView, binding.groupBlockTextView, binding.yearDiscoveredTextView,
            binding.blockTextView, binding.periodTextView, binding.groupTextView
        )

        val stringIds = listOf<Int>(
            R.string.atomicNum, R.string.symbol, R.string.element, R.string.atomicMass,
            R.string.electronicConfig, R.string.atomicRadius, R.string.standardState,
            R.string.bondingType, R.string.meltingPoint, R.string.boilingPoint, R.string.density,
            R.string.groupBlock, R.string.yearDiscovered, R.string.block, R.string.period,
            R.string.group
        )

        val elementData = listOf<String>(
            data.atomicNumber, data.symbol, data.name, data.atomicMass,
            data.electronicConfiguration, data.atomicRadius, data.standardState, data.bondingType,
            data.meltingPoint, data.boilingPoint, data.density, data.groupBlock,
            data.yearDiscovered, data.block, data.period, data.group
        )

        for (index in widgets.indices) {
            if (elementData[index].contains("unknown")) {
                widgets[index].setText(getResources().getString(stringIds[index], "Unknown"))
            } else {
                widgets[index].setText(
                    getResources().getString(
                        stringIds[index],
                        elementData[index].replaceFirstChar { letter ->
                            if (letter.isLowerCase()) letter.titlecase(Locale.getDefault())
                            else letter.toString()
                        })
                )
            }
        }
        val wikiUrl = "https://en.wikipedia.org/wiki/${data.name}"
        binding.wikiButton.setOnClickListener {
            val openURL = Intent(Intent.ACTION_VIEW)
            openURL.data = Uri.parse(wikiUrl)
            startActivity(openURL)
        }
    }

    private fun getImage(link: String) {
        imageJob = CoroutineScope(Dispatchers.IO).launch {

            val url = URL(link)
            val connection: HttpURLConnection = url.openConnection() as HttpURLConnection

            var bitmap: Bitmap? = null
            try {
                connection.getInputStream().use { stream ->
                    bitmap = BitmapFactory.decodeStream(stream)
                }
            } catch (e: java.io.FileNotFoundException) {
            } finally {
                connection.disconnect()
            }

            withContext(Dispatchers.Main) {
                if (bitmap != null) {
                    binding.elementImageView.setImageBitmap(bitmap)
                } else {
                    binding.elementImageView.setImageResource(R.drawable.ic_broken_image)
                    imageCheck = false
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.element_object_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        if (!imageCheck) {
            menu?.findItem(R.id.menu_acknknowledgment)?.setEnabled(false)
        }
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_acknknowledgment) {
            val intent = Intent(this, AcknowledgmentActivity::class.java)
            intent.putExtra(getString(R.string.intent_data_key), elementInfo)
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
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
}