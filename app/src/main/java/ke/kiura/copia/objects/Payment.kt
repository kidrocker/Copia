package ke.kiura.copia.objects

import android.util.Log

data class Payment(
    val receipt:String,
    val reference:String,
    val amount:Int
)
