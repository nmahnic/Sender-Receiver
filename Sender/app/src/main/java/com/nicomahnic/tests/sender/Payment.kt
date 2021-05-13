package com.nicomahnic.tests.sender

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class Payment private constructor(val context: Context){

    companion object{
        @Volatile
        private var INSTANCE: Payment? = null
        const val REQUEST_CODE = 255


        fun getInstance(context: Context): Payment {
            val tempInstance = INSTANCE

            tempInstance?.let { return tempInstance }

            synchronized(this) {
                val instance = Payment(context)
                INSTANCE = instance
                return instance
            }
        }
    }

    fun launchIngpPinpad(data: DoPayment, pm: PackageManager) :
            Pair<Intent, Int>{
        Log.d("NM", "1) Envio ${data}")
        val sendData = Gson().toJson(data)

        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, sendData)
        sendIntent.type = "text/plain"


        val sharedIntent = CustomSenderIntent.create(pm,sendIntent,"com.nicomahnic.tests.receiver")
        return Pair(sharedIntent, REQUEST_CODE)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {
            val resultFromActivity2 = data!!.getStringExtra("result")
            resultFromActivity2?.let{
                val item = Gson().fromJson(it, PaymentResault::class.java)
                Log.d("NM", "1) Respuesta ${item}")
            }
        }
    }
}