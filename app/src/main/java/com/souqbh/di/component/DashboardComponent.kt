package com.souqbh.di.component

import com.souqbh.activity.DashboardActivity
import com.souqbh.di.module.DashboardModule
import com.souqbh.viewmodel.DashboardViewModel
import dagger.Component

@Component(modules = [DashboardModule::class], dependencies = [AppComponent::class])
interface DashboardComponent {

    fun getHomeViewModel(): DashboardViewModel

    fun injectHomeActivity(dashboardActivity: DashboardActivity)
}