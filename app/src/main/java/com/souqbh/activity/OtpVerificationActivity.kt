package com.souqbh.activity

import android.graphics.Typeface
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import com.souqbh.R
import com.souqbh.base.BaseActivity
import com.souqbh.customview.CustomPinview
import com.souqbh.data.api.UserDataResponse
import com.souqbh.databinding.ActivityOtpVerificationBinding
import com.souqbh.di.component.DaggerOtpVerificationComponent
import com.souqbh.di.module.OtpVerificationModule
import com.souqbh.utils.constants.AppConstants
import com.souqbh.viewmodel.OtpVerificationViewModel
import javax.inject.Inject
import android.text.SpannableStringBuilder
import com.souqbh.data.api.UserData
import android.text.Spanned
import android.R.attr.font
import android.content.Intent
import com.souqbh.customview.CustomTypefaceSpan
import com.souqbh.utils.AppUtils
import com.souqbh.utils.Prefs
import com.souqbh.utils.constants.PrefsConstants


class OtpVerificationActivity : BaseActivity<OtpVerificationViewModel>(), CustomPinview.PinViewEventListener,
    View.OnClickListener, BaseActivity.ToolbarLeftImageClickListener {

    @Inject
    lateinit var otpVerificationViewModel: OtpVerificationViewModel

    @Inject
    lateinit var prefs: Prefs

    private lateinit var binding: ActivityOtpVerificationBinding

    private var strOtp: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = DataBindingUtil.setContentView(this, com.souqbh.R.layout.activity_otp_verification)

        val otpVerificationComponent = DaggerOtpVerificationComponent.builder()
            .networkComponent(getNetworkComponent())
            .localDataComponent(getLocalDataComponent())
            .otpVerificationModule(OtpVerificationModule(this))
            .build()
        otpVerificationComponent.injectOtpVerificationActivity(this)
        super.onCreate(savedInstanceState)

        initToolbar()
        init()
    }

    private fun initToolbar() {
        setToolbarLeftIcon(com.souqbh.R.drawable.ic_back_black, this)
    }

    private fun init() {

        val userDataResponse: UserDataResponse = intent.getParcelableExtra(AppConstants.USER_DATA_RESPONSE)
        val userModel = userDataResponse.userData
        val loginType = intent.getStringExtra(AppConstants.LOGIN_TYPE)
        binding.userData = userModel
        binding.loginType = loginType

        setSpannableForVerificationMessage(loginType, userModel)
        setSignUpClickable()
        setObservables()

        binding.cpPinView.setPinViewEventListener(this)
        binding.btnVerifyOtp.setOnClickListener(this)

    }

    private fun setObservables() {

        otpVerificationViewModel.getUserDataResponse().observe({ this.lifecycle }, { userDataModel ->
            userDataModel?.let {
                prefs.save(PrefsConstants.IS_USER_LOGGED_IN, true)
                prefs.save(PrefsConstants.API_TOKEN, userDataModel.userData.token!!)
                val intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra(AppConstants.USER_DATA_RESPONSE, userDataModel)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                AppUtils.startFromRightToLeft(this)
                finish()
            }
        }
        )
    }

    private fun setSpannableForVerificationMessage(loginType: String?, userModel: UserData) {
        val fontBold: Typeface = ResourcesCompat.getFont(this, com.souqbh.R.font.montserrat_bold)!!

        val fullText: String
        val spannableString: SpannableStringBuilder
        if (loginType == AppConstants.LOGIN_EMAIL) {
            fullText = getString(com.souqbh.R.string.lbl_check_email).plus(" ").plus(userModel.u_email)
            spannableString = SpannableStringBuilder(fullText)
            spannableString.setSpan(
                CustomTypefaceSpan("", fontBold), fullText.indexOf(userModel.u_email!!, 0, false),
                fullText.lastIndex + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )
        } else {
            fullText = getString(com.souqbh.R.string.lbl_check_sms).plus(" ").plus(userModel.u_mobile_number)
            spannableString = SpannableStringBuilder(fullText)
            spannableString.setSpan(
                CustomTypefaceSpan("", fontBold), fullText.indexOf(userModel.u_mobile_number!!, 0, false),
                fullText.lastIndex + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE
            )
        }

        binding.tvVerificationMsg.text = spannableString
    }

    override fun onDataEntered(pinview: CustomPinview, fromUser: Boolean, isFullDataEntered: Boolean) {
        if (isFullDataEntered) {
            strOtp = pinview.value
        } else {
            strOtp = ""
        }
    }

    override fun onClick(view: View?) {
        when (view!!.id) {
            com.souqbh.R.id.btnVerifyOtp -> {
                AppUtils.hideKeyboard(this)
                val userDataResponse: UserDataResponse = intent.getParcelableExtra(AppConstants.USER_DATA_RESPONSE)
                val userModel = userDataResponse.userData
                val loginType = intent.getStringExtra(AppConstants.LOGIN_TYPE)

                otpVerificationViewModel.callApiForVerifyOTP(userModel, strOtp, loginType)
            }
        }
    }

    override fun onBackPressed() {
        onLeftImageClicked()
    }

    override fun onLeftImageClicked() {
        finish()
        AppUtils.finishFromLeftToRight(this)
    }

    private fun setSignUpClickable() {
        val fullText = getString(com.souqbh.R.string.lbl_receive_otp)
        val clickableText = getString(com.souqbh.R.string.lbl_resend)

        binding.tvResendOtp.text = fullText
        binding.tvResendOtp.movementMethod = LinkMovementMethod.getInstance()
        binding.tvResendOtp.setText(addClickablePart(fullText, clickableText), TextView.BufferType.SPANNABLE)
        val spannable = SpannableString(binding.tvResendOtp.text)
        /*To change the specific font style to bold*/
        spannable.setSpan(
            StyleSpan(Typeface.NORMAL), fullText.indexOf(clickableText),
            fullText.indexOf(clickableText) + clickableText.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(this, com.souqbh.R.color.red_1)),
            fullText.indexOf(clickableText),
            fullText.indexOf(clickableText) + clickableText.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        binding.tvResendOtp.text = spannable
    }

    private fun addClickablePart(str: String, clickableText: String): Spannable {
        val ssb = SpannableStringBuilder(str)
        ssb.setSpan(
            object : ClickableSpan() {
                override fun onClick(widget: View) {
                    AppUtils.hideKeyboard(this@OtpVerificationActivity)
                    val userDataResponse: UserDataResponse = intent.getParcelableExtra(AppConstants.USER_DATA_RESPONSE)
                    val userModel = userDataResponse.userData
                    val loginType = intent.getStringExtra(AppConstants.LOGIN_TYPE)

                    otpVerificationViewModel.callApiForResendOTP(userModel, loginType)
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

    override fun getViewModel(): OtpVerificationViewModel {
        return otpVerificationViewModel
    }
}