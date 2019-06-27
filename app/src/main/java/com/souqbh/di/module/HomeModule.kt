package com.souqbh.di.module

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.souqbh.fragment.HomeFragment
import com.souqbh.viewmodel.HomeViewModel
import com.souqbh.viewmodel.LoginViewModel
import dagger.Module
import dagger.Provides

@Module
class HomeModule constructor(private val homeFragment: HomeFragment) {


    @Provides
    fun providesHomeViewModel(application: Application): HomeViewModel {
        return ViewModelProviders.of(homeFragment, object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeViewModel(application) as T
            }
        }).get(HomeViewModel::class.java)
    }

}