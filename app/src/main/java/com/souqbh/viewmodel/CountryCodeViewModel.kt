package com.souqbh.viewmodel

import android.app.Application
import android.text.TextUtils.isEmpty
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.souqbh.base.BaseViewModel
import com.souqbh.data.db.AppDatabase
import com.souqbh.data.db.entity.CountryCode
import javax.inject.Inject

class CountryCodeViewModel @Inject constructor(application: Application, private val appDatabase: AppDatabase) :
    BaseViewModel(application) {

    private val countryCodeList: MutableLiveData<List<CountryCode>> by lazy {
        MutableLiveData<List<CountryCode>>()
    }

    fun getCountryCodeList(strSearch: String): LiveData<List<CountryCode>> {
        if (isEmpty(strSearch))
            countryCodeList.postValue(appDatabase.appDao().getAllCountryCode())
        else
            countryCodeList.postValue(appDatabase.appDao().getFilteredCountryCode(strSearch))
        return countryCodeList
    }

}