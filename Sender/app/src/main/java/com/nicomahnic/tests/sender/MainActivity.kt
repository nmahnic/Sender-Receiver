package com.nicomahnic.tests.sender

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson

class MainActivity : AppCompatActivity() {
//    adb shell am start -n com.nicomahnic.tests.sender/com.nicomahnic.tests.sender.MainActivity
    private lateinit var texto : TextView
    private val REQUEST_CODE = 255

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
                    transactionType = TransactionType.SALE.name,
                    amount = 130.50,
            )
            launchIngpPinpad(transaction, getPackageManager())
        }
    }

    private fun launchIngpPinpad(data: Payment, pm: PackageManager){
        Log.d("NM", "1) Envio ${data}")
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra("currency", data.currency)
        sendIntent.putExtra("currencyCode", data.currencyCode)
        sendIntent.putExtra("transactionType", data.transactionType)
        sendIntent.putExtra("amount", data.amount)
        sendIntent.type = "text/plain"


        val sharedIntent = CustomSenderIntent.create(pm,sendIntent,"com.ingenico.ingp.standalone")
//        val sharedIntent = CustomSenderIntent.create(pm,sendIntent,"com.nicomahnic.tests.receiver")
        startActivityForResult(sharedIntent, REQUEST_CODE)
//        startActivity(sharedIntent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE && resultCode == AppCompatActivity.RESULT_OK) {

            val resultFromActivity2 = PaymentResault(
                    transactionResault = data!!.getStringExtra("transactionResault")!!,
                    errorCode = data.getStringExtra("errorCode")!!,
                    issuer = data.getStringExtra("issuer"),
                    cardholder = data.getStringExtra("cardholder"),
                    installments = data.getIntExtra("installments", 0),
                    approvedCode = data.getStringExtra("approvedCode"),
                    rrn = data.getStringExtra("rrn"),
                    maskedCardNo = data.getStringExtra("maskedCardNo")
            )

            resultFromActivity2?.let{
                Log.d("NM", "1) Respuesta ${it}")
            }
        }
    }
}

data class Payment(
        val currency: String,
        val currencyCode: Int,
        val transactionType: String,
        val amount: Double
)

data class PaymentResault(
        val transactionResault: String,
        val errorCode: String,
        val issuer: String?,
        val cardholder: String?,
        val installments: Int?,
        val approvedCode: String?,
        val rrn: String?,
        val maskedCardNo: String?
)

enum class TransactionType {
    SALE,
    VOID,
    REFUND,
}