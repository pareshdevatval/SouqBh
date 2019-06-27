package com.souqbh.di.component

import com.souqbh.data.db.AppDatabase
import com.souqbh.di.module.LocalDataModule
import com.souqbh.utils.Prefs
import dagger.Component

@Component(modules = [LocalDataModule::class], dependencies = [AppComponent::class])
interface LocalDataComponent {

    fun getPref(): Prefs

    fun getAppDatabase(): AppDatabase
}