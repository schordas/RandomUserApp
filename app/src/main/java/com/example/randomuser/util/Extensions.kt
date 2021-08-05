package com.example.randomuser.util

import android.content.Context
import android.content.Intent

fun Intent.appAvailable(context: Context?): Boolean {
    return resolveActivity(context?.packageManager!!) != null
}
