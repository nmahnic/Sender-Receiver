package com.nicomahnic.tests.sender

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {

    private lateinit var texto : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        texto = findViewById<TextView>(R.id.tvResult)

        val boton = findViewById<Button>(R.id.btnEnter)
        boton.setOnClickListener {
            launchIngpPinpad(Item("Nicolas","Mahnic"))
        }
    }


    private fun launchIngpPinpad(data: Item) {
        val sendData = Gson().toJson(data)
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, sendData)
        sendIntent.type = "text/plain"
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}

data class Item(
    val name: String,
    val lastName: String
)