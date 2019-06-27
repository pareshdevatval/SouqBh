package com.souqbh.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.databinding.ViewDataBinding
import com.souqbh.R
import com.souqbh.base.BaseBindingAdapter
import com.souqbh.base.BaseBindingViewHolder
import com.souqbh.data.db.entity.CountryCode
import com.souqbh.databinding.ItemCountryCodeListBinding

class CountryCodeAdapter : BaseBindingAdapter<CountryCode>() {

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return ItemCountryCodeListBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val code = items[position]
        val binding: ItemCountryCodeListBinding = holder.binding as ItemCountryCodeListBinding
        binding.countryCode = code
    }

}