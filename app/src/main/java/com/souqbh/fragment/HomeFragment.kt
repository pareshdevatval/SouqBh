package com.souqbh.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.souqbh.R
import com.souqbh.adapter.TestAdapter
import com.souqbh.base.BaseFragment
import com.souqbh.data.api.Category
import com.souqbh.data.api.CategoryResponse
import com.souqbh.databinding.FragmentHomeBinding
import com.souqbh.di.component.DaggerHomeComponent
import com.souqbh.di.component.HomeComponent
import com.souqbh.di.module.HomeModule
import com.souqbh.viewmodel.HomeViewModel
import java.io.InputStream
import javax.inject.Inject


class HomeFragment : BaseFragment<HomeViewModel>() {

    @Inject
    lateinit var homeViewModel: HomeViewModel

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvCategoryList.layoutManager = LinearLayoutManager(context)
        callApiForGettingCategory()
    }

    private fun callApiForGettingCategory() {
        var json: String? = null
        try {
            val inputStream: InputStream = context!!.assets.open("categories.json")
            json = inputStream.bufferedReader().use { it.readText() }

            val categoryResponse = Gson().fromJson(json, CategoryResponse::class.java)

            val testAdapter = TestAdapter(context!!)
            testAdapter.setItem(categoryResponse.result as ArrayList<Category>)
            binding.rvCategoryList.adapter = testAdapter

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun getViewModel(): HomeViewModel {
        return homeViewModel
    }

    override fun onAttach(context: Context?) {
        val homeComponent: HomeComponent = DaggerHomeComponent
            .builder()
            .appComponent(getAppComponent())
            .homeModule(HomeModule(this))
            .build()
        homeComponent.injectHomeFragment(this)
        super.onAttach(context)
    }

}