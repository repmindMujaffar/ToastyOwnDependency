package com.reapmind.toasty

import android.content.Context
import android.widget.Toast

object ToastyToast {

    fun showToastyToast(context: Context, toastText: String) =
        Toast.makeText(context, toastText, Toast.LENGTH_SHORT).show()

}