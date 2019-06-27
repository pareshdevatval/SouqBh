package com.souqbh.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.souqbh.R
import com.souqbh.databinding.ActivityLanguageSelectionBinding
import com.souqbh.utils.AppUtils
import com.souqbh.utils.constants.AppConstants

class LanguageSelectionActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLanguageSelectionBinding
    private var languageType: String = AppConstants.LANG_ENGLISH
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_language_selection)

        init()
    }

    private fun init() {
        binding.rlEngLanguage.setOnClickListener(this)
        binding.rlArabicLanguage.setOnClickListener(this)
        binding.btnContinue.setOnClickListener(this)
    }

    override fun onClick(view: View?) {

        when (view!!.id) {
            R.id.rlEngLanguage -> {
                languageType = AppConstants.LANG_ENGLISH
                binding.ivLanArabicSelection.visibility = View.GONE
                binding.ivLanEngSelection.visibility = View.VISIBLE
            }
            R.id.rlArabicLanguage -> {
                languageType = AppConstants.LANG_ARABIC
                binding.ivLanArabicSelection.visibility = View.VISIBLE
                binding.ivLanEngSelection.visibility = View.GONE
            }
            R.id.btnContinue -> {

                val intent = Intent(this, IntroductionActivity::class.java)
                intent.putExtra(AppConstants.SELECTED_LANG, languageType)
                startActivity(intent)
                finish()
                AppUtils.startFromRightToLeft(this)
            }
        }
    }
}