package com.rose.account.utils

import android.content.Context
import android.widget.Toast
import java.math.RoundingMode
import java.text.DecimalFormat

object HelperFunctions {

    @JvmStatic
    fun toastLong(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
    }

    @JvmStatic
    fun getRoundedValue(value: Double): String {
        val mRound = DecimalFormat("#,##0.##")
        mRound.roundingMode = RoundingMode.HALF_UP
        return mRound.format(value)
    }
}