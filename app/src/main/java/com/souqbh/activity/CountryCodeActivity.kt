package com.souqbh.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils.isEmpty
import android.text.TextWatcher
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.souqbh.R
import com.souqbh.adapter.CountryCodeAdapter
import com.souqbh.base.BaseActivity
import com.souqbh.data.db.entity.CountryCode
import com.souqbh.databinding.ActivityCountryCodeBinding
import com.souqbh.di.component.DaggerCountryCodeComponent
import com.souqbh.di.module.CountryCodeModule
import com.souqbh.viewmodel.CountryCodeViewModel
import javax.inject.Inject
import androidx.recyclerview.widget.DividerItemDecoration
import com.souqbh.base.BaseBindingAdapter
import com.souqbh.data.db.AppDatabase
import com.souqbh.utils.AppUtils
import com.souqbh.utils.constants.AppConstants


class CountryCodeActivity : BaseActivity<CountryCodeViewModel>(), BaseBindingAdapter.ItemClickListener<CountryCode>,
    BaseActivity.ToolbarLeftImageClickListener {

    @Inject
    lateinit var countryCodeViewModel: CountryCodeViewModel

    @Inject
    lateinit var appDatabase: AppDatabase

    private lateinit var binding: ActivityCountryCodeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val countryCodeComponent = DaggerCountryCodeComponent.builder()
            .appComponent(getAppComponent())
            .localDataComponent(getLocalDataComponent())
            .countryCodeModule(CountryCodeModule(this))
            .build()
        countryCodeComponent.injectCountryCodeActivity(this)

        binding = DataBindingUtil.setContentView(this, com.souqbh.R.layout.activity_country_code)
        super.onCreate(savedInstanceState)

        initToolbar()
        init()
    }

    private fun initToolbar() {
        setToolbarLeftIcon(com.souqbh.R.drawable.ic_back_black, this)
        setToolbarTitleIcon(com.souqbh.R.drawable.home_logo)
    }

    private fun init() {
        setSearchFilterFeature()
        binding.rvCountryCodeList.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
        val dividerItemDecoration = DividerItemDecoration(
            this,
            RecyclerView.VERTICAL
        )
        binding.rvCountryCodeList.addItemDecoration(dividerItemDecoration)

        countryCodeViewModel.getCountryCodeList("").observe({ this.lifecycle }, { countryCodeList ->
            countryCodeList.let {
                val categoriesAdapter = CountryCodeAdapter()
                categoriesAdapter.setItem(it as ArrayList<CountryCode>)
                binding.rvCountryCodeList.adapter = categoriesAdapter
                categoriesAdapter.itemClickListener = this
            }
        })

    }

    private fun setSearchFilterFeature() {

        binding.etSearchCountryCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(editable: Editable?) {
                if (!isEmpty(editable.toString().trim())) {
                    countryCodeViewModel.getCountryCodeList(editable.toString())
                }
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
    }

    override fun onItemClick(view: View, data: CountryCode, position: Int) {
        val intent = Intent()
        intent.putExtra(AppConstants.COUNTRY_CODE, data)
        setResult(Activity.RESULT_OK, intent)
        finish()
        AppUtils.finishFromLeftToRight(this)
    }

    override fun onBackPressed() {
        onLeftImageClicked()
    }

    override fun onLeftImageClicked() {
        finish()
        AppUtils.finishFromLeftToRight(this)
    }


    override fun getViewModel(): CountryCodeViewModel {
        return countryCodeViewModel
    }
}