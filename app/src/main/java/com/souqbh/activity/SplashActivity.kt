package com.souqbh.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.souqbh.R
import com.souqbh.databinding.ActivitySplashBinding
import com.souqbh.utils.AppUtils
import com.souqbh.utils.Prefs
import com.souqbh.utils.constants.PrefsConstants

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME: Long = 3000

    private val mRunnable: Runnable = Runnable {
        if (!isFinishing) {
            val prefs: Prefs = Prefs.getInstance(this)!!
            val intent: Intent
            if (prefs.getBoolean(PrefsConstants.IS_USER_LOGGED_IN, false))
                intent = Intent(applicationContext, DashboardActivity::class.java)
            else
                intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
            AppUtils.startFromRightToLeft(this)
        }
    }
    private var handler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySplashBinding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        Glide.with(this).load(R.drawable.splash).into(binding.ivSplashLogo)

        handler = Handler()
        handler!!.postDelayed(mRunnable, SPLASH_TIME)
    }

    override fun onResume() {
        super.onResume()
        handler!!.postDelayed(mRunnable, SPLASH_TIME)
    }

    override fun onPause() {
        super.onPause()
        handler!!.removeCallbacks(mRunnable)
    }
}