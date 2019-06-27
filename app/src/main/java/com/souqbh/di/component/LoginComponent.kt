package com.souqbh.di.component

import com.souqbh.activity.LoginActivity
import com.souqbh.di.module.LoginModule
import com.souqbh.viewmodel.LoginViewModel
import dagger.Component

@Component(
    modules = [LoginModule::class],
    dependencies = [LocalDataComponent::class, NetworkComponent::class]
)
interface LoginComponent {

    fun getLoginViewModel(): LoginViewModel

    fun injectLoginActivity(loginActivity: LoginActivity)
}