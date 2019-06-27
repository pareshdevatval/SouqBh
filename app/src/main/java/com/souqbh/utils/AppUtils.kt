package com.souqbh.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.text.Selection
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.core.content.res.ResourcesCompat
import com.google.android.material.snackbar.Snackbar
import com.souqbh.BuildConfig
import com.souqbh.R
import com.souqbh.utils.filePick.FileUri
import java.io.File
import java.io.IOException


object AppUtils {
    /**
     * A method which returns the state of internet connectivity of user's phone.
     */
    fun hasInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    fun startFromRightToLeft(context: Context) {
        (context as Activity).overridePendingTransition(com.souqbh.R.anim.trans_left_in, R.anim.trans_left_out)
    }

    fun finishFromLeftToRight(context: Context) {
        (context as Activity).overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
    }

    fun startFromBottomToUp(activity: Activity) {
        activity.overridePendingTransition(R.anim.trans_bottom_up, R.anim.no_animation)
    }

    fun finishFromUpToBottom(activity: Activity) {
        activity.overridePendingTransition(R.anim.no_animation, R.anim.trans_up_bottom)
    }

    fun finishFromRightToLeft(activity: Activity) {
        activity.overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out)
    }

    fun getScreenWidth(activity: Activity?): Int {
        if (activity != null) {
            val display = activity.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.x
        }
        return 0
    }

    fun getScreenHeight(activity: Activity?): Int {
        if (activity != null) {
            val display = activity.windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            return size.y
        }
        return 0
    }

    fun getText(textView: TextView): String {
        return textView.text.toString().trim()
    }

    /**
     * A common method used in whole application to show a snack bar
     */
    fun showSnackBar(v: View, msg: String) {
        val mSnackBar = Snackbar.make(v, msg, Snackbar.LENGTH_LONG)
        val view = mSnackBar.view
        val typeface = ResourcesCompat.getFont(v.context, R.font.montserrat_regular)
        val params = view.layoutParams as FrameLayout.LayoutParams
        //  params.gravity = Gravity.TOP
        view.layoutParams = params
        view.setBackgroundColor(ContextCompat.getColor(v.context, R.color.red_1))
        val mainTextView = view.findViewById<TextView>(R.id.snackbar_text)
        // val mainTextView: TextView = view.findViewById(android.support.design.R.id.snackbar_text)
        mainTextView.setTextColor(ContextCompat.getColor(v.context, R.color.white_1))
        mainTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, v.context.resources.getDimension(R.dimen.regular))
        mainTextView.textAlignment = View.TEXT_ALIGNMENT_CENTER
        mainTextView.maxLines = 2
        mainTextView.typeface = typeface
        mainTextView.gravity = Gravity.CENTER_HORIZONTAL
        mSnackBar.show()
    }

    /**
     * A method to show device keyboard for user input
     */
    fun showKeyboard(activity: Activity?, view: EditText) {
        try {
            val inputManager = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
        } catch (e: Exception) {
            Log.e("Exception showKeyboard", e.toString())
        }
    }

    /**
     * A method to hide the device's keyboard
     */
    fun hideKeyboard(activity: Activity) {
        if (activity.currentFocus != null) {
            val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(activity.currentFocus!!.windowToken, 0)
        }
    }

    /**
     * A method to get the external directory of phone, to store the image file
     */
    fun getWorkingDirectory(): File {
        val directory = File(Environment.getExternalStorageDirectory(), BuildConfig.APPLICATION_ID)
        if (!directory.exists()) {
            directory.mkdir()
        }
        return directory
    }

    /**
     * This method is for making new jpg file.
     * @param activity Instance of activity
     * @param prefix file name prefix
     */
    fun createImageFile(activity: Activity, prefix: String): FileUri? {
        val fileUri = FileUri()

        var image: File? = null
        try {
            image = File.createTempFile(prefix + System.currentTimeMillis().toString(), ".jpg", getWorkingDirectory())
        } catch (e: IOException) {
            e.printStackTrace()
        }

        if (image != null) {
            fileUri.file = image
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fileUri.imageUrl =
                    (FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", image))
            } else {
                fileUri.imageUrl = (Uri.parse("file:" + image.absolutePath))
            }
        }
        return fileUri
    }
}