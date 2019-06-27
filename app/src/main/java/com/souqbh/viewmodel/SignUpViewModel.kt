package com.souqbh.viewmodel

import android.app.Application
import android.text.TextUtils.isEmpty
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.souqbh.R
import com.souqbh.base.BaseApplication
import com.souqbh.base.BaseViewModel
import com.souqbh.data.api.UserDataResponse
import com.souqbh.data.remote.ApiService
import com.souqbh.utils.AppUtils
import com.souqbh.utils.Prefs
import com.souqbh.utils.constants.ApiParams
import com.souqbh.utils.constants.AppConstants
import com.souqbh.utils.validator.ValidationError
import com.souqbh.utils.validator.ValidationErrorModel
import com.souqbh.utils.validator.Validator
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType
import okhttp3.RequestBody
import java.io.File
import javax.inject.Inject

class SignUpViewModel @Inject constructor(
    application: Application,
    private val apiService: ApiService,
    private val prefs: Prefs
) : BaseViewModel(application) {

    private var subscription: Disposable? = null
    var imagePath: String? = ""
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

    fun checkValidationAndCallApi(
        firstName: String,
        lastName: String,
        value: String,
        countryCode: String,
        userType: String
    ) {
        Validator.validateFirstName(firstName)?.let {
            validationError.value = it
            return
        }

        Validator.validateLastName(lastName)?.let {
            validationError.value = it
            return
        }

        if (userType.equals(AppConstants.LOGIN_EMAIL)) {
            Validator.validateEmail(value)?.let {
                validationError.value = it
                return
            }
        } else {
            Validator.validateTelephone(value)?.let {
                validationError.value = it
                return
            }

            if (Validator.isBlank(countryCode)) {
                validationError.value = ValidationErrorModel(R.string.select_country_code, ValidationError.DATA)
                return
            }
        }

        /*Validator.validateData(imagePath!!, R.string.select_profile_image)?.let {
            validationError.value = it
            return
        }*/

        callApiForSignUp(firstName, lastName, value, userType)
    }

    private fun callApiForSignUp(firstName: String, lastName: String, value: String, userType: String) {

        if (AppUtils.hasInternet(getApplication())) {
            val params: HashMap<String, String?> = HashMap()
            params[ApiParams.FIRST_NAME] = firstName
            params[ApiParams.LAST_NAME] = lastName
            if (userType.equals(AppConstants.LOGIN_EMAIL))
                params[ApiParams.EMAIL] = value
            else
                params[ApiParams.MOBILE] = value
            params[ApiParams.USER_TYPE] = AppConstants.LOGIN_EMAIL

            val parameters = getParamsRequestBody(params)

            subscription = apiService
                .apiRegister(parameters)
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

    private fun getParamsRequestBody(params: HashMap<String, String?>): HashMap<String, RequestBody> {
        val resultParams = HashMap<String, RequestBody>()

        for ((key, value) in params) {
            val body = RequestBody.create(MediaType.parse("text/plain"), value)
            resultParams.put(key, body)
        }

        if (!isEmpty(imagePath)) {
            val file = File(imagePath)
            val reqFile = RequestBody.create(MediaType.parse("multipart/form-data"), file)
            val imageParams = ApiParams.USER_IMAGE + "\";filename=\"${file.name}\""
            resultParams[imageParams] = reqFile
        }
        return resultParams
    }

}
