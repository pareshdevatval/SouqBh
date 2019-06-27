package com.souqbh.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.gson.Gson
import com.souqbh.base.BaseApplication
import com.souqbh.base.BaseViewModel
import com.souqbh.data.api.UserDataResponse
import com.souqbh.data.db.AppDatabase
import com.souqbh.data.db.CountryCodeResponse
import com.souqbh.data.remote.ApiService
import com.souqbh.utils.AppUtils
import com.souqbh.utils.Prefs
import com.souqbh.utils.constants.ApiParams
import com.souqbh.utils.constants.AppConstants
import com.souqbh.utils.constants.PrefsConstants
import com.souqbh.utils.validator.ValidationErrorModel
import com.souqbh.utils.validator.Validator
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.InputStream
import javax.inject.Inject

class LoginViewModel @Inject constructor(
    application: Application,
    private val apiService: ApiService,
    private val prefs: Prefs,
    private val appDatabase: AppDatabase
) : BaseViewModel(application) {

    private var subscription: Disposable? = null
    val context = getApplication<BaseApplication>().applicationContext!!

    private val validationError: MutableLiveData<ValidationErrorModel>by lazy {
        MutableLiveData<ValidationErrorModel>()
    }

    private val userDataResponse: MutableLiveData<UserDataResponse> by lazy {
        MutableLiveData<UserDataResponse>()
    }

    fun getValidationError(): LiveData<ValidationErrorModel> {
        return validationError
    }

    fun getUserDataResponse(): LiveData<UserDataResponse> {
        return userDataResponse
    }

    fun addCountryCodeToDatabase() {

        Observable.just(insertCountryCodeToDb()).observeOn(AndroidSchedulers.mainThread()).observeOn(Schedulers.io())
            .subscribe()
    }

    private fun insertCountryCodeToDb() {
        var json: String? = null
        try {

            val inputStream: InputStream = context.assets.open("CountryCodes.json")
            json = inputStream.bufferedReader().use { it.readText() }
            val countryCodeResponse = Gson().fromJson(json, CountryCodeResponse::class.java)

            countryCodeResponse.data.forEach {
                appDatabase.appDao().insertCountryCode(it)
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun getFCMDeviceToken() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("FCM token", "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token: String = task.result?.token!!
                prefs.save(PrefsConstants.DEVICE_TOKEN, token)
            })
    }

    fun checkEmailValidation(email: String) {
        Validator.validateEmail(email)?.let {
            validationError.value = it
            return
        }

        callApiForLogin(email, AppConstants.LOGIN_EMAIL)
    }

    fun checkPhoneNumberValidation(phoneNumber: String) {
        Validator.validateTelephone(phoneNumber)?.let {
            validationError.value = it
            return
        }

        callApiForLogin(phoneNumber, AppConstants.LOGIN_MOBILE)
    }

    private fun callApiForLogin(value: String, loginType: String) {

        if (AppUtils.hasInternet(getApplication())) {
            val params: HashMap<String, String?> = HashMap()
            params[ApiParams.DEVICE_TOKEN] = prefs.getString(PrefsConstants.DEVICE_TOKEN, "")
            params[ApiParams.DEVICE_TYPE] = AppConstants.ANDROID_DEVICE_TYPE

            if (loginType == AppConstants.LOGIN_EMAIL) {
                params[ApiParams.USER_TYPE] = AppConstants.LOGIN_EMAIL
                params[ApiParams.EMAIL] = value
            } else {
                params[ApiParams.USER_TYPE] = AppConstants.LOGIN_MOBILE
                params[ApiParams.MOBILE] = value
            }

            subscription = apiService
                .apiLogin(params)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe { onApiStart() }
                .doOnTerminate { onApiFinish() }
                .subscribe(this::handleLoginResponse, this::handleError)

        } else {
            onInternetError()
        }
    }

    private fun handleLoginResponse(response: UserDataResponse) {

        if (response.status) {
            userDataResponse.value = response
        } else {
            apiErrorMessage.value = response.message
        }
    }

    private fun handleError(error: Throwable) {
        apiErrorMessage.value = error.message
    }

    override fun onCleared() {
        super.onCleared()
        subscription?.dispose()
    }

}