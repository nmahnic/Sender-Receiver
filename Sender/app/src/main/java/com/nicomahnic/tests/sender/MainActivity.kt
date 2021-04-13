package com.nicomahnic.tests.sender

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
//    adb shell am start -n com.nicomahnic.tests.sender/com.nicomahnic.tests.sender.MainActivity
    private lateinit var texto : TextView
    val REQUEST_CODE = 255

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("NM", "1) onCreate")
        texto = findViewById<TextView>(R.id.tvResult)

        val boton = findViewById<Button>(R.id.btnEnter)
        boton.setOnClickListener {
            launchIngpPinpad(Student("Nicolas", "Mahnic"))
        }
    }


    private fun launchIngpPinpad(data: Student) {
        Log.d("NM","1) Envio ${data.name} ${data.lastName}")
        val sendData = Gson().toJson(data)

        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, sendData)
        sendIntent.type = "text/plain"
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivityForResult(shareIntent, REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            val resultFromActivity2 = data!!.getStringExtra("result")
            resultFromActivity2?.let{
                val item = Gson().fromJson(it,Engineer::class.java)
                Log.d("NM","1) Respuesta ${item.name} ${item.lastName} ${item.job}")
            }
        }
//        Log.d("NM","1) FIN requestCode:${requestCode} resultCode:${resultCode}")
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