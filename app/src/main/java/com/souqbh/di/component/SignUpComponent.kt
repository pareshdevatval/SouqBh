package com.souqbh.di.component

import com.souqbh.activity.SignUpActivity
import com.souqbh.di.module.SignUpModule
import com.souqbh.viewmodel.SignUpViewModel
import dagger.Component

@Component(modules = [SignUpModule::class], dependencies = [NetworkComponent::class, LocalDataComponent::class])
interface SignUpComponent {

    fun getSignUpViewModel(): SignUpViewModel

    fun injectSignUpActivity(signUpActivity: SignUpActivity)
}