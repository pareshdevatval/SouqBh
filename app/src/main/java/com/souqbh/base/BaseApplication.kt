package com.souqbh.base

import android.app.Application
import androidx.room.Room
import com.google.firebase.FirebaseApp
import com.google.gson.Gson
import com.souqbh.data.db.AppDatabase
import com.souqbh.data.db.CountryCodeResponse
import com.souqbh.di.component.*
import com.souqbh.di.module.AppModule
import com.souqbh.di.module.LocalDataModule
import com.souqbh.di.module.NetworkModule
import com.souqbh.utils.Prefs
import com.souqbh.utils.constants.AppConstants
import com.souqbh.utils.constants.PrefsConstants
import java.io.InputStream


class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        // Fabric.with(this, Crashlytics())
        FirebaseApp.initializeApp(this)
    }

    fun getAppComponent(): AppComponent {
        return DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    fun getLocalDataComponent(): LocalDataComponent {
        return DaggerLocalDataComponent.builder()
            .appComponent(getAppComponent())
            .localDataModule(LocalDataModule())
            .build()
    }

    fun getNetworkComponent(): NetworkComponent {
        return DaggerNetworkComponent.builder().appComponent(getAppComponent())
            .networkModule(NetworkModule()).build()
    }
}