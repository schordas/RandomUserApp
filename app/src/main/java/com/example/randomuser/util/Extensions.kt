package com.example.randomuser.util

import android.content.Context
import android.content.Intent

/**
 * Extension to check that there is an app to handle the built intent
 */
fun Intent.appAvailable(context: Context?): Boolean {
    return resolveActivity(context?.packageManager!!) != null
}
