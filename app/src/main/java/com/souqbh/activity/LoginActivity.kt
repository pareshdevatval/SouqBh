package com.souqbh.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.TextUtils.isEmpty
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.souqbh.R
import com.souqbh.base.BaseActivity
import com.souqbh.data.db.AppDatabase
import com.souqbh.data.db.entity.CountryCode
import com.souqbh.databinding.ActivityLoginBinding
import com.souqbh.di.component.DaggerLoginComponent
import com.souqbh.di.component.LoginComponent
import com.souqbh.di.module.LoginModule
import com.souqbh.utils.AppUtils
import com.souqbh.utils.Prefs
import com.souqbh.utils.constants.AppConstants
import com.souqbh.utils.constants.PrefsConstants
import com.souqbh.viewmodel.LoginViewModel
import javax.inject.Inject
import android.text.InputFilter
import android.view.MotionEvent
import androidx.core.view.MotionEventCompat
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions


class LoginActivity : BaseActivity<LoginViewModel>(), View.OnClickListener {

    private var binding: ActivityLoginBinding? = null

    @Inject
    lateinit var prefs: Prefs

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var appDatabase: AppDatabase

    lateinit var countryCode: CountryCode

    private var emailAddress: String = ""
    private var phoneNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        val loginComponent: LoginComponent = DaggerLoginComponent.builder()
            .networkComponent(getNetworkComponent())
            .localDataComponent(getLocalDataComponent())
            .loginModule(
                LoginModule(this)
            ).build()
        loginComponent.injectLoginActivity(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        super.onCreate(savedInstanceState)
        transparentStatusBar(transparent = false)
        init()
    }

    private fun init() {
        binding!!.etPhoneEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

        Glide.with(this).load(R.drawable.login_bg)
            .apply(
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
            )
            .placeholder(R.drawable.login_bg)
            .into(binding!!.ivLoginBg)
        setupLayoutTabs()
        setSignUpClickable()
        setObservables()

        if (isEmpty(prefs.getString(PrefsConstants.DEVICE_TOKEN, ""))) {
            loginViewModel.getFCMDeviceToken()
        }

        if (appDatabase.appDao().getAllCountryCode().isEmpty()) {
            loginViewModel.addCountryCodeToDatabase();
        }


        binding!!.btnGetOtp.setOnClickListener(this)
        binding!!.tvCountryCode.setOnClickListener(this)
    }

    private fun setObservables() {
        loginViewModel.getValidationError().observe({ this.lifecycle }, { validationError ->
            validationError?.let {
                AppUtils.showSnackBar(binding!!.root, getString(validationError.msg))
            }
        })

        loginViewModel.getUserDataResponse().observe({ this.lifecycle }, { userDataModel ->
            userDataModel?.let {
                val intent = Intent(this, OtpVerificationActivity::class.java)
                intent.putExtra(AppConstants.USER_DATA_RESPONSE, userDataModel)
                intent.putExtra(
                    AppConstants.LOGIN_TYPE,
                    if (binding!!.tabs.selectedTabPosition == 0) AppConstants.LOGIN_EMAIL else AppConstants.LOGIN_MOBILE
                )
                startActivity(intent)
                AppUtils.startFromRightToLeft(this)
            }
        }
        )
    }

    private fun setupLayoutTabs() {
        binding!!.tabs.addTab(binding!!.tabs.newTab().setText(R.string.lbl_email))
        binding!!.tabs.addTab(binding!!.tabs.newTab().setText(R.string.lbl_phone))

        binding!!.tabs.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val FilterArray = arrayOfNulls<InputFilter>(1)

                if (tab!!.position == 0) {
                    binding!!.tvCountryCode.visibility = View.GONE
                    binding!!.etPhoneEmail.hint = getString(R.string.hint_email_address)
                    binding!!.etPhoneEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    FilterArray[0] = InputFilter.LengthFilter(100)
                    binding!!.etPhoneEmail.filters = FilterArray
                    phoneNumber = binding!!.etPhoneEmail.text.toString()
                    binding!!.etPhoneEmail.setText(emailAddress)
                } else {
                    binding!!.tvCountryCode.visibility = View.VISIBLE
                    binding!!.etPhoneEmail.hint = getString(R.string.hint_phone_number)
                    binding!!.etPhoneEmail.inputType = InputType.TYPE_CLASS_PHONE
                    FilterArray[0] = InputFilter.LengthFilter(10)
                    binding!!.etPhoneEmail.filters = FilterArray
                    emailAddress = binding!!.etPhoneEmail.text.toString()
                    binding!!.etPhoneEmail.setText(phoneNumber)
                }
            }
        })
    }

    override fun onClick(view: View?) {
        AppUtils.hideKeyboard(this)
        when (view!!.id) {
            R.id.tvGuestButton -> {
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                AppUtils.startFromRightToLeft(this)
                finish()

            }
            R.id.btnGetOtp -> {
                if (binding!!.tabs.selectedTabPosition == 0) {
                    loginViewModel.checkEmailValidation(binding!!.etPhoneEmail.text.toString())
                } else {
                    loginViewModel.checkPhoneNumberValidation(binding!!.etPhoneEmail.text.toString())
                }
            }
            R.id.tvCountryCode -> {
                val intent = Intent(this, CountryCodeActivity::class.java)
                startActivityForResult(intent, AppConstants.INTENT_COUNTRY_CODE)
                AppUtils.startFromRightToLeft(this)
            }
        }
    }

    private fun setSignUpClickable() {
        val fullText = getString(R.string.lbl_sign_up_message)
        val clickableText = getString(R.string.lbl_sign_up)

        binding!!.tvSignUp.text = fullText
        binding!!.tvSignUp.movementMethod = LinkMovementMethod.getInstance()
        binding!!.tvSignUp.setText(addClickablePart(fullText, clickableText), TextView.BufferType.SPANNABLE)
        val spannable = SpannableString(binding!!.tvSignUp.text)
        /*To change the specific font style to bold*/
        spannable.setSpan(
            StyleSpan(Typeface.NORMAL), fullText.indexOf(clickableText),
            fullText.indexOf(clickableText) + clickableText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, R.color.black_1)),
            fullText.indexOf(clickableText),
            fullText.indexOf(clickableText) + clickableText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding!!.tvSignUp.text = spannable
    }

    private fun addClickablePart(str: String, clickableText: String): Spannable {
        val ssb = SpannableStringBuilder(str)
        ssb.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val intent = Intent(this@LoginActivity, SignUpActivity::class.java)
                    startActivityForResult(intent, AppConstants.INTENT_COUNTRY_CODE)
                    AppUtils.startFromRightToLeft(this@LoginActivity)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.isUnderlineText = false
                }
            },
            str.indexOf(clickableText),
            str.indexOf(clickableText) + clickableText.length,
            Spanned.SPAN_EXCLUSIVE_INCLUSIVE
        )

        return ssb
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.INTENT_COUNTRY_CODE) {
                countryCode = intent!!.getParcelableExtra(AppConstants.COUNTRY_CODE)
                binding!!.tvCountryCode.text = countryCode.dial_code
            }
        }
    }

    override fun getViewModel(): LoginViewModel {
        return loginViewModel
    }
}