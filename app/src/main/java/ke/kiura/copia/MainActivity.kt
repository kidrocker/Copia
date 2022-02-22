package ke.kiura.copia

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import ke.kiura.copia.objects.Payment
import ke.kiura.copia.objects.Receipt
import ke.kiura.copia.objects.Transaction

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val receipts =
            mutableListOf(Receipt("R001", 100), Receipt("R002", 400), Receipt("R003", 350))
        val transactions = mutableListOf(
            Transaction("MG001", 100),
            Transaction("MG002", 200),
            Transaction("MG003", 300),
            Transaction("MG004", 250)
        )

        val output = generatePaymentsTable(transactions, receipts)

        paymentListToLog(output)
    }
}

/**
 * Combines payments and the transaction lists
 */
private fun generatePaymentsTable(
    transactions: MutableList<Transaction>,
    receipts: MutableList<Receipt>
): List<Payment> {
    val payments = mutableListOf<Payment>()

    var remainder = 0
    var remainderCode = ""
    transactions.forEach { transaction ->
        val receipt = receipts[0]

        // Handle the transaction remainder on the list
        if (remainder > 0) {
            payments.add(Payment(receipt.code, remainderCode, remainder))
            receipts[0].amount = receipts[0].amount - remainder
            remainder = 0
        }

        when {
            receipt.amount == transaction.amount -> {
                payments.add(Payment(receipt.code, transaction.code, transaction.amount))
                receipts.removeAt(0)
            }
            receipt.amount > transaction.amount -> {
                payments.add(Payment(receipt.code, transaction.code, transaction.amount))
                receipts[0].amount = receipt.amount - transaction.amount

            }
            receipt.amount < transaction.amount -> {
                val difference = transaction.amount - receipt.amount
                payments.add(
                    Payment(
                        receipt.code,
                        transaction.code,
                        receipt.amount
                    )
                )
                remainder = difference
                remainderCode = transaction.code
                receipts.removeAt(0)
            }
        }
    }
    return payments
}

/**
 * Prints the list into the Log.e
 */
private fun paymentListToLog(payments: List<Payment>) {
    payments.forEach {
        Log.e("OUTPUT", "${it.receipt} ${it.reference} ${it.amount}")
    }
}