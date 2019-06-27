package com.souqbh.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.souqbh.base.BaseViewModel
import com.souqbh.data.api.CategoryResponse

class HomeViewModel(application: Application) : BaseViewModel(application) {

    private val categoryResponse: MutableLiveData<CategoryResponse> by lazy {
        MutableLiveData<CategoryResponse>()
    }

    fun getCategoryData(): LiveData<CategoryResponse> {
        return categoryResponse
    }

}