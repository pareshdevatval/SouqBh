package com.souqbh.di.module

import android.app.Application
import android.content.Context
import com.souqbh.base.BaseApplication
import dagger.Module
import dagger.Provides

@Module
class AppModule constructor(val application: BaseApplication) {


    @Provides
    fun provideApplication(): Application = application

    /**
     * Provides the Application context.
     * @return the Application context
     */
    @Provides
    fun provideContext(): Context = application.applicationContext
}