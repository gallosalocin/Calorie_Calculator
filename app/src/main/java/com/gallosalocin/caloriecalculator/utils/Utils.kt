package com.gallosalocin.caloriecalculator.utils

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.widget.TextView
import androidx.appcompat.widget.AppCompatSpinner
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Utils {

    companion object {

        /** Check if the phone is connected to internet */
        fun hasInternetConnection(application: Application): Boolean {
            val connectivityManager = application.getSystemService(
                Context.CONNECTIVITY_SERVICE
            ) as ConnectivityManager
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }

        /** Check if edit text input is null or empty */
        fun TextInputEditText.validateInputEditText(errorMessage: String): Boolean {
            return if (this.text?.isEmpty() == true) {
                this.error = errorMessage
                false
            } else {
                this.error = null
                true
            }
        }

        /** Check if edit text input with layout is null or empty */
        fun TextInputEditText.validateInputEditTextWithLayout(layout: TextInputLayout, errorMessage: String): Boolean {
            return if (this.text?.isEmpty() == true) {
                layout.error = errorMessage
                false
            } else {
                layout.error = null
                true
            }
        }

        /** Check if spinner is null or empty */
        fun AppCompatSpinner.validateSpinnerCategory(errorMessage: String): Boolean {
            val category = this.selectedItem.toString().trim()
            val errorText: TextView = this.selectedView as TextView

            return if (category == "") {
                errorText.error = errorMessage
                false
            } else {
                errorText.error = null
                true
            }
        }


    }
}