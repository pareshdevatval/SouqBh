package com.souqbh.di.module

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.souqbh.activity.DashboardActivity
import com.souqbh.viewmodel.DashboardViewModel
import dagger.Module
import dagger.Provides

@Module
class DashboardModule constructor(private val dashboardActivity: DashboardActivity) {

    @Provides
    fun providesHomeViewModel(application: Application): DashboardViewModel {
        return ViewModelProviders.of(dashboardActivity, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return DashboardViewModel(application) as T
            }
        }).get(DashboardViewModel::class.java)
    }
}