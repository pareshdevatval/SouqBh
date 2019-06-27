package com.souqbh.di.component

import com.souqbh.activity.CountryCodeActivity
import com.souqbh.di.module.CountryCodeModule
import dagger.Component

@Component(modules = [CountryCodeModule::class], dependencies = [AppComponent::class, LocalDataComponent::class])
interface CountryCodeComponent {

    fun getCountryCodeViewModel(): CountryCodeComponent

    fun injectCountryCodeActivity(countryCodeActivity: CountryCodeActivity)

}