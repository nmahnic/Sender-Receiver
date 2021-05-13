package com.nicomahnic.tests.sender

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
//    adb shell am start -n com.nicomahnic.tests.sender/com.nicomahnic.tests.sender.MainActivity
    private lateinit var texto : TextView
    private lateinit var payment : Payment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("NM", "1) onCreate")
        texto = findViewById<TextView>(R.id.tvResult)

        val boton = findViewById<Button>(R.id.btnEnter)
        boton.setOnClickListener {
            val transaction = DoPayment(
                    currency = "UYU",
                    currencyCode = 858,
                    transactionType = TransactionType.SALE,
                    amount = 12.50,
            )
            payment = Payment.getInstance(this)
            val newIntent = payment.launchIngpPinpad(transaction, getPackageManager())

            startActivityForResult(newIntent.first,newIntent.second)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        payment.onActivityResult(requestCode, resultCode, data)
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