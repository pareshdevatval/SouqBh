package com.souqbh.di.component

import android.app.Application
import android.content.Context
import com.souqbh.data.remote.ApiService
import com.souqbh.di.module.NetworkModule
import dagger.Component

@Component(modules = [NetworkModule::class], dependencies = [AppComponent::class])
interface NetworkComponent {

    fun getApiClient(): ApiService

    fun getApplication(): Application

    fun getContext(): Context
}