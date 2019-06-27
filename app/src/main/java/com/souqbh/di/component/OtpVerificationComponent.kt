package com.souqbh.di.component

import com.souqbh.activity.LoginActivity
import com.souqbh.activity.OtpVerificationActivity
import com.souqbh.di.module.OtpVerificationModule
import com.souqbh.viewmodel.LoginViewModel
import com.souqbh.viewmodel.OtpVerificationViewModel
import dagger.Component

@Component(
    modules = [OtpVerificationModule::class],
    dependencies = [NetworkComponent::class, LocalDataComponent::class]
)
interface OtpVerificationComponent {

    fun getOtpVerificationViewModel(): OtpVerificationViewModel

    fun injectOtpVerificationActivity(otpVerificationActivity: OtpVerificationActivity)

}