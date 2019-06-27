package com.souqbh.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.souqbh.R
import com.souqbh.base.BaseApplication
import com.souqbh.base.BaseViewModel
import com.souqbh.data.api.BaseResponse
import com.souqbh.data.api.UserData
import com.souqbh.data.api.UserDataResponse
import com.souqbh.data.remote.ApiService
import com.souqbh.utils.AppUtils
import com.souqbh.utils.Prefs
import com.souqbh.utils.constants.ApiParams
import com.souqbh.utils.constants.AppConstants
import com.souqbh.utils.constants.PrefsConstants
import com.souqbh.utils.validator.ValidationError
import com.souqbh.utils.validator.ValidationErrorModel
import com.souqbh.utils.validator.Validator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class OtpVerificationViewModel @Inject constructor(
    private val apiService: ApiService,
    private val prefs: Prefs, application: Application
) :
    BaseViewModel(application) {

    private var subscription: Disposable? = null
    val context = getApplication<BaseApplication>().applicationContext!!

    private val validationError: MutableLiveData<ValidationErrorModel>by lazy {
        MutableLiveData<ValidationErrorModel>()
    }

    fun getValidationError(): LiveData<ValidationErrorModel> {
        return validationError
    }

    private val userDataResponse: MutableLiveData<UserDataResponse> by lazy {
        MutableLiveData<UserDataResponse>()
    }

    fun getUserDataResponse(): LiveData<UserDataResponse> {
        return userDataResponse
    }

    fun callApiForVerifyOTP(userData: UserData, otp: String, loginType: String) {

        if (Validator.isBlank(otp)) {
            validationError.value = ValidationErrorModel(R.string.blank_otp, ValidationError.DATA)
            return
        }

        if (AppUtils.hasInternet(getApplication())) {
            val params: HashMap<String, String?> = HashMap()
            params[ApiParams.DEVICE_TOKEN] = prefs.getString(PrefsConstants.DEVICE_TOKEN, "")
            params[ApiParams.DEVICE_TYPE] = AppConstants.ANDROID_DEVICE_TYPE

            if (loginType == AppConstants.LOGIN_EMAIL) {
                params[ApiParams.USER_TYPE] = AppConstants.LOGIN_EMAIL
                params[ApiParams.EMAIL] = userData.u_email
            } else {
                params[ApiParams.USER_TYPE] = AppConstants.LOGIN_MOBILE
                params[ApiParams.MOBILE] = userData.u_mobile_number
            }
            params[ApiParams.OTP] = otp

            subscription = apiService
                .apiVerifyOTP(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { onApiStart() }
                .doOnTerminate { onApiFinish() }
                .subscribe(this::handleVerifyOtpResponse, this::handleError)

        } else {
            onInternetError()
        }
    }

    private fun handleVerifyOtpResponse(response: UserDataResponse) {
        if (response.status) {
            userDataResponse.value = response
            prefs.userDataModel = response
        } else {
            apiErrorMessage.value = response.message
        }
    }

    private fun handleError(error: Throwable) {
        apiErrorMessage.value = error.message
    }

    fun callApiForResendOTP(userData: UserData, loginType: String) {

        if (AppUtils.hasInternet(getApplication())) {
            val params: HashMap<String, String?> = HashMap()

            if (loginType == AppConstants.LOGIN_EMAIL) {
                params[ApiParams.USER_TYPE] = AppConstants.LOGIN_EMAIL
                params[ApiParams.EMAIL] = userData.u_email
            } else {
                params[ApiParams.USER_TYPE] = AppConstants.LOGIN_MOBILE
                params[ApiParams.MOBILE] = userData.u_mobile_number
            }

            params[ApiParams.USER_ID] = userData.u_id.toString()

            subscription = apiService
                .apiResendOTP(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { onApiStart() }
                .doOnTerminate { onApiFinish() }
                .subscribe(this::handleResendResponse, this::handleError)

        } else {
            onInternetError()
        }
    }

    private fun handleResendResponse(response: BaseResponse) {
        apiErrorMessage.value = response.message
    }


    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }


}