package com.souqbh.di.module

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.souqbh.activity.CountryCodeActivity
import com.souqbh.data.db.AppDatabase
import com.souqbh.viewmodel.CountryCodeViewModel
import dagger.Module
import dagger.Provides

@Module
class CountryCodeModule(private val countryCodeActivity: CountryCodeActivity) {

    @Provides
    fun providesLoginViewModel(application: Application, appDatabase: AppDatabase): CountryCodeViewModel {
        return ViewModelProviders.of(countryCodeActivity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return CountryCodeViewModel(application, appDatabase) as T
            }
        }).get(CountryCodeViewModel::class.java)
    }
}