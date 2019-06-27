package com.souqbh.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.souqbh.base.BaseBindingAdapter
import com.souqbh.base.BaseBindingViewHolder
import com.souqbh.data.api.Category
import com.souqbh.databinding.ItemTestListBinding
import com.souqbh.utils.AppUtils


class TestAdapter constructor(var context: Context) : BaseBindingAdapter<Category>() {

    override fun bind(inflater: LayoutInflater, parent: ViewGroup, viewType: Int): ViewDataBinding {
        return ItemTestListBinding.inflate(inflater, parent, false)
    }

    override fun onBindViewHolder(holder: BaseBindingViewHolder, position: Int) {
        val binding: ItemTestListBinding = holder.binding as ItemTestListBinding
        binding.category = items.get(position)


        val screenHeight = AppUtils.getScreenHeight(context as Activity)
        val screenWidth = AppUtils.getScreenHeight(context as Activity)

        /* val categoryParams = binding.cvCategoryImage.layoutParams as ConstraintLayout.LayoutParams
         categoryParams.height = (AppUtils.getScreenHeight(context as Activity) * 0.37f).toInt()
         categoryParams.width = (AppUtils.getScreenWidth(context as Activity) * 0.48f).toInt()
         categoryParams.marginStart = (AppUtils.getScreenWidth(context as Activity) * 0.05f).toInt()
         categoryParams.marginEnd = (AppUtils.getScreenWidth(context as Activity) * 0.05f).toInt()

         val subCategoryParams1 = binding.cvSubCategoryImage1.layoutParams as ConstraintLayout.LayoutParams
         subCategoryParams1.height = (AppUtils.getScreenHeight(context as Activity) * 0.18f).toInt()
         subCategoryParams1.width = (AppUtils.getScreenWidth(context as Activity) * 0.48f).toInt()
        // subCategoryParams1.marginStart = (AppUtils.getScreenWidth(context as Activity) * 0.05f).toInt()
         //subCategoryParams1.marginEnd = (AppUtils.getScreenWidth(context as Activity) * 0.05f).toInt()

         val subCategoryParams2 = binding.cvSubCategoryImage2.layoutParams as ConstraintLayout.LayoutParams
         subCategoryParams2.height = (AppUtils.getScreenHeight(context as Activity) * 0.18f).toInt()
         subCategoryParams2.width = (AppUtils.getScreenWidth(context as Activity) * 0.48f).toInt()
 //        subCategoryParams2.marginStart = (AppUtils.getScreenWidth(context as Activity) * 0.04f).toInt()
 //        subCategoryParams2.marginEnd = (AppUtils.getScreenWidth(context as Activity) * 0.04f).toInt()

         val subCategoryParams3 = binding.cvSubCategoryImage3.layoutParams as ConstraintLayout.LayoutParams
         subCategoryParams3.height = (AppUtils.getScreenHeight(context as Activity) * 0.18f).toInt()
         subCategoryParams3.width = (AppUtils.getScreenWidth(context as Activity) * 0.48f).toInt()
         //subCategoryParams3.marginStart = (AppUtils.getScreenWidth(context as Activity) * 0.04f).toInt()

         val subCategoryParams4 = binding.cvSubCategoryImage4.layoutParams as ConstraintLayout.LayoutParams
         subCategoryParams4.height = (AppUtils.getScreenHeight(context as Activity) * 0.18f).toInt()
         subCategoryParams4.width = (AppUtils.getScreenWidth(context as Activity) * 0.48f).toInt()
         //subCategoryParams4.marginStart = (AppUtils.getScreenWidth(context as Activity) * 0.04f).toInt()*/
    }
}