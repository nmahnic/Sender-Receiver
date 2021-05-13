package com.nicomahnic.tests.sender

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.nicomahnic.tests.sender.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
//    adb shell am start -n com.nicomahnic.tests.sender/com.nicomahnic.tests.sender.MainActivity
    private lateinit var texto : TextView
    private val REQUEST_CODE = 255
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d("NM", "1) onCreate")
        texto = findViewById<TextView>(R.id.tvResult)


        binding.btnVoid.setOnClickListener {
            launchIngpPinpad(transactionVoid, getPackageManager())
        }

        binding.btnSale.setOnClickListener {
            launchIngpPinpad(transactionSale, getPackageManager())
        }

        binding.btnRefund.setOnClickListener {
            launchIngpPinpad(transactionRefund, getPackageManager())

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
                    errorCode = data.getStringExtra("errorCode"),
                    issuer = data.getStringExtra("issuer"),
                    cardholder = data.getStringExtra("cardholder"),
                    transactionType = data.getStringExtra("transactionType")!!,
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

    val transactionVoid = Payment(
            currency = "UYU",
            currencyCode = 858,
            transactionType = TransactionType.VOID.name
    )
    val transactionSale = Payment(
            currency = "UYU",
            currencyCode = 858,
            transactionType = TransactionType.SALE.name,
            amount = 101.50,
    )
    val transactionRefund = Payment(
            currency = "UYU",
            currencyCode = 858,
            transactionType = TransactionType.REFUND.name,
            amount = 90.00,
    )
}

data class Payment(
        val currency: String,
        val currencyCode: Int,
        val transactionType: String,
        val amount: Double? = null
)

data class PaymentResault(
        val transactionResault: String,
        val errorCode: String?,
        val issuer: String?,
        val cardholder: String?,
        val transactionType: String,
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