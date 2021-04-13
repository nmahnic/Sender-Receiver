package com.nicomahnic.tests.receiver

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson


class MainActivity : AppCompatActivity() {

    lateinit var texto: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("NM", "2) onCreate")

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
        sharedText?.let {
            val item = Gson().fromJson(it, Student::class.java)

            texto.text = "${item.name} ${item.lastName}"
            Log.d("NM", "2) Recibo ${item.name} ${item.lastName}")


            val data = Engineer("Nicolas","Mahnic", "Developer")
            val dataReturn = Gson().toJson(data)

            val returnIntent = Intent()
            returnIntent.putExtra("result", dataReturn)
            returnIntent.action = Intent.ACTION_SEND
            returnIntent.type = "text/plain"
            setResult(RESULT_OK, returnIntent)

            finish()
        }
    }
}

data class Student(
        val name: String,
        val lastName: String,
)

data class Engineer(
        val name: String,
        val lastName: String,
        val job: String
)