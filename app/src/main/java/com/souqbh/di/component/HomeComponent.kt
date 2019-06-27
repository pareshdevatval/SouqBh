package com.souqbh.di.component

import com.souqbh.di.module.HomeModule
import com.souqbh.fragment.HomeFragment
import com.souqbh.viewmodel.HomeViewModel
import dagger.Component

@Component(modules = [HomeModule::class], dependencies = [AppComponent::class])
interface HomeComponent {

    fun getHomeViewModel(): HomeViewModel

    fun injectHomeFragment(homeFragment: HomeFragment)
}