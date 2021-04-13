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
            val item = Gson().fromJson(it, DoPayment::class.java)

            Log.d("NM", "2) Recibo ${item}")


            val data = PaymentResault(
                    transactionResault = "APPROVED",
                    errorCode = "00",
                    issuer = "VISA DEBITO",
                    installments = 1,
                    approvedCode = "123456",
                    rrn = "123456123456",
                    maskedCardNo = "4567 89XX XXXX 1234"
            )
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

data class DoPayment(
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