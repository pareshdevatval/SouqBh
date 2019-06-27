package com.souqbh.di.component

import android.app.Application
import android.content.Context
import com.souqbh.di.module.AppModule
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {

    fun getApplication(): Application

    fun getContext(): Context

}