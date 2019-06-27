package com.souqbh.di.module

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.souqbh.activity.LoginActivity
import com.souqbh.activity.OtpVerificationActivity
import com.souqbh.data.db.AppDatabase
import com.souqbh.data.remote.ApiService
import com.souqbh.utils.Prefs
import com.souqbh.viewmodel.LoginViewModel
import com.souqbh.viewmodel.OtpVerificationViewModel
import dagger.Module
import dagger.Provides

@Module
class OtpVerificationModule constructor(private val otpVerificationActivity: OtpVerificationActivity) {

    @Provides
    fun providesOtpVerificationViewModel(
        apiService: ApiService,
        prefs: Prefs,
        application: Application
    ): OtpVerificationViewModel {
        return ViewModelProviders.of(otpVerificationActivity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return OtpVerificationViewModel(apiService, prefs, application) as T
            }
        }).get(OtpVerificationViewModel::class.java)
    }

}