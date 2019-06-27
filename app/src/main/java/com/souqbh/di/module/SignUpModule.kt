package com.souqbh.di.module

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.souqbh.activity.LoginActivity
import com.souqbh.activity.SignUpActivity
import com.souqbh.data.db.AppDatabase
import com.souqbh.data.remote.ApiService
import com.souqbh.utils.Prefs
import com.souqbh.viewmodel.LoginViewModel
import com.souqbh.viewmodel.SignUpViewModel
import dagger.Module
import dagger.Provides

@Module
class SignUpModule constructor(private val signUpActivity: SignUpActivity) {

    @Provides
    fun providesSignUpViewModel(
        application: Application,
        apiService: ApiService,
        prefs: Prefs
    ): SignUpViewModel {
        return ViewModelProviders.of(signUpActivity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return SignUpViewModel(application, apiService, prefs) as T
            }
        }).get(SignUpViewModel::class.java)
    }

}