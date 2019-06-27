package com.souqbh.data.remote

import com.souqbh.data.api.BaseResponse
import com.souqbh.data.api.UserDataResponse
import io.reactivex.Observable
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("user/login")
    fun apiLogin(@FieldMap params: HashMap<String, String?>): Observable<UserDataResponse>

    @Multipart
    @POST("user/register")
    fun apiRegister(@PartMap params: HashMap<String, RequestBody>): Observable<UserDataResponse>

    @FormUrlEncoded
    @POST("user/verify_otp")
    fun apiVerifyOTP(@FieldMap params: HashMap<String, String?>): Observable<UserDataResponse>

    @FormUrlEncoded
    @POST("resendotp")
    fun apiResendOTP(@FieldMap params: HashMap<String, String?>): Observable<BaseResponse>
}