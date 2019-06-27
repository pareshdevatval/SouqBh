package com.souqbh.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.souqbh.R
import com.souqbh.adapter.IntroductionViewPagerAdapter
import com.souqbh.data.other.Introduction
import com.souqbh.databinding.ActivityIntroductionBinding
import com.souqbh.fragment.IntroductionFragment
import com.souqbh.utils.AppUtils
import com.souqbh.utils.constants.AppConstants

class IntroductionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityIntroductionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_introduction)

        init()
    }

    private fun init() {
        setupViewPagerAdapter()

        binding.tvSkipButton.setOnClickListener(this)
        binding.btnContinue.setOnClickListener(this)
    }

    private fun setupViewPagerAdapter() {
        val introductionViewPagerAdapter = IntroductionViewPagerAdapter(supportFragmentManager)

        val bundle1 = Bundle()
        val introduction1 = Introduction("", "", R.drawable.wlk_1)
        bundle1.putParcelable(AppConstants.INTRODUCTION, introduction1)

        val bundle2 = Bundle()
        val introduction2 = Introduction("", "", R.drawable.wlk_2)
        bundle2.putParcelable(AppConstants.INTRODUCTION, introduction2)

        val bundle3 = Bundle()
        val introduction3 = Introduction("", "", R.drawable.wlk_3)
        bundle3.putParcelable(AppConstants.INTRODUCTION, introduction3)

        val introductionFragment1 = IntroductionFragment.newInstance(bundle1)
        val introductionFragment2 = IntroductionFragment.newInstance(bundle2)
        val introductionFragment3 = IntroductionFragment.newInstance(bundle3)

        introductionViewPagerAdapter.addFragment(introductionFragment1)
        introductionViewPagerAdapter.addFragment(introductionFragment2)
        introductionViewPagerAdapter.addFragment(introductionFragment3)

        binding.vpIntroduction.adapter = introductionViewPagerAdapter
        binding.tabLayout.setupWithViewPager(binding.vpIntroduction, true)
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.tvSkipButton -> {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
                AppUtils.startFromRightToLeft(this)
            }
            R.id.btnContinue -> {
                if (binding.vpIntroduction.currentItem == binding.vpIntroduction.adapter!!.count - 1)
                    binding.tvSkipButton.performClick()
                else
                    binding.vpIntroduction.currentItem = binding.vpIntroduction.currentItem + 1
            }
        }

    }

}