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
            val transaction = Payment(
                    currency = "UYU",
                    currencyCode = 858,
                    transactionType = TransactionType.SALE,
                    amount = 12.50,

            )
            launchIngpPinpad(transaction)
        }
    }


    private fun launchIngpPinpad(data: Payment) {
        Log.d("NM", "1) Envio ${data}")
        val sendData = Gson().toJson(data)

        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, sendData)
        sendIntent.type = "text/plain"

        val pm = getPackageManager()
        val sharedIntent = CustomSenderIntent.create(pm,sendIntent,"com.nicomahnic.tests.receiver")

        startActivityForResult(sharedIntent, REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {

            val resultFromActivity2 = data!!.getStringExtra("result")
            resultFromActivity2?.let{
                val item = Gson().fromJson(it, PaymentResault::class.java)
                Log.d("NM", "1) Respuesta ${item}")
            }
        }
    }
}

data class Payment(
        val currency: String,
        val currencyCode: Int,
        val transactionType: TransactionType,
        val amount: Double
)

data class PaymentResault(
        val transactionResault: String,
        val errorCode: String,
        val issuer: String,
        val installments: Int,
        val approvedCode: String,
        val rrn: String,
        val maskedCardNo: String
)

enum class TransactionType {
    SALE,
    OFFLINE_SALE,
    VOID,
    REFUND,
}