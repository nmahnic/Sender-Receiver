package com.nicomahnic.tests.receiver

import android.content.Intent
import android.net.MacAddress.fromString
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.google.gson.Gson
import java.nio.file.attribute.PosixFilePermissions.fromString
import java.util.UUID.fromString

class MainActivity : AppCompatActivity() {

    var myName: String = ""
    lateinit var texto: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("NM", "onCreate NOMBRE: ${myName}")

        texto = findViewById<TextView>(R.id.tvReceived)

        val intent = intent
        val action = intent.action
        val type = intent.type

        if (Intent.ACTION_SEND == action && type != null) {
            if ("text/plain" == type) {
                handleSendText(intent); // Handle text being sent
            }
        }


    }

    fun handleSendText(intent: Intent) {
        val sharedText = intent.getStringExtra(Intent.EXTRA_TEXT)
        if (sharedText != null) {
            val item = Gson().fromJson(sharedText,Item::class.java)
            texto.text = "${item.name} ${item.lastName}"
            myName = "${item.name} ${item.lastName}"
        }
    }
}

data class Item(
        val name: String,
        val lastName: String
)
