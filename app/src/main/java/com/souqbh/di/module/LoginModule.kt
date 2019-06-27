package com.souqbh.di.module

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.souqbh.activity.LoginActivity
import com.souqbh.data.db.AppDatabase
import com.souqbh.data.remote.ApiService
import com.souqbh.utils.Prefs
import com.souqbh.viewmodel.LoginViewModel
import dagger.Module
import dagger.Provides

@Module
class LoginModule constructor(private val loinActivity: LoginActivity) {


    @Provides
    fun providesLoginViewModel(
        application: Application,
        apiService: ApiService,
        prefs: Prefs,
        appDatabase: AppDatabase
    ): LoginViewModel {
        return ViewModelProviders.of(loinActivity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return LoginViewModel(application, apiService, prefs, appDatabase) as T
            }
        }).get(LoginViewModel::class.java)
    }


}