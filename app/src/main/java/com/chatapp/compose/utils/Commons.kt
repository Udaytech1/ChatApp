package com.chatapp.compose.utils

import android.Manifest
import android.accounts.AccountManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.TelephonyManager
import android.util.Patterns
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.text.SimpleDateFormat
import java.util.Random
import java.util.regex.Pattern


class Commons {
    companion object {
        private var toast: Toast? = null
        fun showToast(context: Context, msg: String) {
            toast?.cancel()
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
            toast?.show()

        }

        fun isValidEmail(email: String): Boolean {
            val pattern: Pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(email).matches()
        }

        fun shareSinglePhotoToInstagram(context: Context) {
            val url = "http://test.tribyssapps.com/userImages/6437aa0da02810656008001681369613.png"
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.putExtra(Intent.EXTRA_STREAM, url)
            shareIntent.type = "imgae/*"
            shareIntent.setPackage("com.instagram.android")
            context.startActivity(Intent.createChooser(shareIntent, "Share"))
        }

        private const val ALLOWED_CHARACTERS = "0123456789qwertyuiopasdfghjklzxcvbnm"

        fun getRandomString(sizeOfRandomString: Int): String? {
            val random = Random()
            val sb = StringBuilder(sizeOfRandomString)
            for (i in 0 until sizeOfRandomString) sb.append(
                ALLOWED_CHARACTERS[random.nextInt(
                    ALLOWED_CHARACTERS.length
                )]
            )
            return sb.toString()
        }

        fun checkWeatherNumberExistOrNotInThisDevice(number: String, context: Context): String {
            val telephonyManager =
                context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val phoneNumber = if ( ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.READ_PHONE_STATE
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return "false"
            } else {
                telephonyManager.line1Number
            }
            return phoneNumber
        }

        fun getTimeFromMilliSecond(milliSecond: Long): String {
            val simpleDateFormat = SimpleDateFormat("dd/MM/yyyy h :m:s")
            val dateString = simpleDateFormat.format(milliSecond)
            return String.format("Time: %s", dateString)
        }

        fun getMobile(context: Context) {
            println("Accounts : getting")
            val am = AccountManager.get(context)
            val accounts = am.accounts

            for (ac in accounts) {
                val acname = ac.name
                val actype = ac.type
                // Take your time to look at all available accounts
                println("Accounts : $acname, $actype")
                if (actype.equals("com.whatsapp")) {
                    val phoneNumber = ac.name
                }
            }
        }

    }
}