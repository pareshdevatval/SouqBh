package com.souqbh.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide

import com.souqbh.base.BaseActivity
import com.souqbh.data.db.entity.CountryCode
import com.souqbh.databinding.ActivitySignupBinding
import com.souqbh.di.component.DaggerSignUpComponent
import com.souqbh.di.module.SignUpModule
import com.souqbh.utils.AppUtils
import com.souqbh.utils.constants.AppConstants
import com.souqbh.utils.filePick.FilePickUtils
import com.souqbh.viewmodel.SignUpViewModel
import javax.inject.Inject
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import com.souqbh.R
import com.souqbh.databinding.DialogImageSelectionBinding
import com.souqbh.utils.constants.AppConstants.PICK_IMAGE_CAMERA_REQUEST_CODE
import com.souqbh.utils.constants.AppConstants.PICK_IMAGE_GALLERY_REQUEST_CODE
import android.widget.TextView
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.graphics.Color
import android.graphics.Typeface
import android.text.*
import android.text.TextPaint
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat


class SignUpActivity : BaseActivity<SignUpViewModel>(), BaseActivity.ToolbarLeftImageClickListener,
    View.OnClickListener {

    private lateinit var binding: ActivitySignupBinding

    @Inject
    lateinit var signUpViewModel: SignUpViewModel

    private var countryCode: CountryCode? = null

    private var filePickUtils: FilePickUtils? = null

    private lateinit var mImageSelectionDialog: BottomSheetDialog

    private var emailAddress: String = ""
    private var phoneNumber: String = ""

    private val mOnFileChoose = object : FilePickUtils.OnFileChoose {
        override fun onFileChoose(fileUri: String, requestCode: Int) {
            Glide.with(this@SignUpActivity).load(fileUri).into(binding.ivProfilePic)
            signUpViewModel.imagePath = fileUri
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val signUpComponent = DaggerSignUpComponent.builder()
            .networkComponent(getNetworkComponent())
            .localDataComponent(getLocalDataComponent())
            .signUpModule(SignUpModule(this))
            .build()
        signUpComponent.injectSignUpActivity(this)

        binding = DataBindingUtil.setContentView(this, com.souqbh.R.layout.activity_signup)
        super.onCreate(savedInstanceState)

        init()
        initToolbar()
    }

    private fun init() {
        binding.btnAgreeAndSignUp.setOnClickListener(this)
        binding.tvCountryCode.setOnClickListener(this)
        binding.ivProfilePic.setOnClickListener(this)

        filePickUtils = FilePickUtils(this, mOnFileChoose)

        setObservables()
        setupLayoutTabs()
        setClickableString(
            getString(com.souqbh.R.string.lbl_terms_condition_msg),
            binding.tvTermsAndCondition,
            arrayOf(
                getString(com.souqbh.R.string.lbl_terms_and_condition),
                getString(com.souqbh.R.string.lbl_privacy_policy)
            ),
            arrayOf(clickableTerms, clickablePrivacy)
        )
    }

    private fun initToolbar() {
        setToolbarLeftIcon(com.souqbh.R.drawable.ic_back_black, this)
    }

    private fun setupLayoutTabs() {
        binding.tabs.addTab(binding.tabs.newTab().setText(com.souqbh.R.string.lbl_email))
        binding.tabs.addTab(binding.tabs.newTab().setText(com.souqbh.R.string.lbl_phone))

        binding.tabs.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val FilterArray = arrayOfNulls<InputFilter>(1)

                if (tab!!.position == 0) {
                    binding.tvCountryCode.visibility = View.GONE
                    binding.etPhoneEmail.hint = getString(com.souqbh.R.string.hint_email_address)
                    binding.etPhoneEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    FilterArray[0] = InputFilter.LengthFilter(100)
                    binding.etPhoneEmail.filters = FilterArray
                    phoneNumber = binding.etPhoneEmail.text.toString()
                    binding.etPhoneEmail.setText(emailAddress)
                    binding.etPhoneEmail.imeOptions = EditorInfo.IME_ACTION_DONE
                } else {
                    binding.tvCountryCode.visibility = View.VISIBLE
                    binding.etPhoneEmail.hint = getString(com.souqbh.R.string.hint_phone_number)
                    binding.etPhoneEmail.inputType = InputType.TYPE_CLASS_PHONE
                    FilterArray[0] = InputFilter.LengthFilter(10)
                    binding.etPhoneEmail.filters = FilterArray
                    emailAddress = binding.etPhoneEmail.text.toString()
                    binding.etPhoneEmail.setText(phoneNumber)
                    binding.etPhoneEmail.imeOptions = EditorInfo.IME_ACTION_DONE
                }
            }
        })
    }

    private fun setObservables() {
        signUpViewModel.getValidationError().observe({ this.lifecycle }, { validationError ->
            validationError?.let {
                AppUtils.showSnackBar(binding.root, getString(validationError.msg))
            }
        })

        signUpViewModel.getUserDataResponse().observe({ this.lifecycle }, { userDataModel ->
            userDataModel?.let {
                val intent = Intent(this, OtpVerificationActivity::class.java)
                intent.putExtra(AppConstants.USER_DATA_RESPONSE, userDataModel)
                intent.putExtra(
                    AppConstants.LOGIN_TYPE, AppConstants.LOGIN_EMAIL
                )
                startActivity(intent)
                AppUtils.startFromRightToLeft(this)
            }
        }
        )
    }


    override fun onClick(view: View?) {
        when (view!!.id) {
            com.souqbh.R.id.btnAgreeAndSignUp -> {
                val strCountryCode = if (countryCode == null) "" else countryCode!!.dial_code
                signUpViewModel.checkValidationAndCallApi(
                    AppUtils.getText(binding.etFirstName),
                    AppUtils.getText(binding.etLastName),
                    AppUtils.getText(binding.etPhoneEmail),
                    strCountryCode,
                    if (binding.tabs.selectedTabPosition == 0) AppConstants.LOGIN_EMAIL else AppConstants.LOGIN_MOBILE
                )
            }
            com.souqbh.R.id.tvCountryCode -> {
                val intent = Intent(this, CountryCodeActivity::class.java)
                startActivityForResult(intent, AppConstants.INTENT_COUNTRY_CODE)
                AppUtils.startFromRightToLeft(this)
            }
            com.souqbh.R.id.ivProfilePic -> {
                showImageSelectionDialog()
            }
            com.souqbh.R.id.tvSelectCamera -> {
                filePickUtils?.requestImageCamera(PICK_IMAGE_CAMERA_REQUEST_CODE, false, false)
                mImageSelectionDialog.cancel()
            }
            com.souqbh.R.id.tvSelectGallery -> {
                filePickUtils?.requestImageGallery(PICK_IMAGE_GALLERY_REQUEST_CODE, false, false)
                mImageSelectionDialog.cancel()
            }
        }
    }

    private fun showImageSelectionDialog() {
        mImageSelectionDialog = BottomSheetDialog(this)
        val dialogImageSelectionBinding = DialogImageSelectionBinding.inflate(layoutInflater)
        mImageSelectionDialog.setContentView(dialogImageSelectionBinding.root)

        dialogImageSelectionBinding.tvSelectCamera.setOnClickListener(this)
        dialogImageSelectionBinding.tvSelectGallery.setOnClickListener(this)
        mImageSelectionDialog.show()
    }

    override fun onBackPressed() {
        onLeftImageClicked()
    }

    override fun onLeftImageClicked() {
        finish()
        AppUtils.finishFromLeftToRight(this)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        filePickUtils?.onActivityResult(requestCode, resultCode, intent)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == AppConstants.INTENT_COUNTRY_CODE) {
                countryCode = intent!!.getParcelableExtra(AppConstants.COUNTRY_CODE)
                binding.tvCountryCode.text = countryCode!!.dial_code
            }
        }
    }

    private fun setClickableString(
        wholeValue: String,
        textView: TextView,
        clickableValue: Array<String>,
        clickableSpans: Array<ClickableSpan>
    ) {
        val spannableString = SpannableString(wholeValue)
        val redColor = ForegroundColorSpan(ContextCompat.getColor(this, R.color.red_1))

        for (i in clickableValue.indices) {
            val clickableSpan = clickableSpans[i]
            val link = clickableValue[i]

            val startIndexOfLink = wholeValue.indexOf(link)

            spannableString.setSpan(
                StyleSpan(Typeface.BOLD), wholeValue.indexOf(link),
                wholeValue.indexOf(link) + link.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            spannableString.setSpan(
                clickableSpan,
                startIndexOfLink,
                startIndexOfLink + link.length,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        textView.movementMethod = LinkMovementMethod.getInstance()
        textView.setText(spannableString, TextView.BufferType.SPANNABLE)


    }

    private val clickableTerms: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            AppUtils.showSnackBar(widget, "Terms")
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
            ds.color = ContextCompat.getColor(this@SignUpActivity, R.color.red_1)
        }
    }

    private val clickablePrivacy: ClickableSpan = object : ClickableSpan() {
        override fun onClick(widget: View) {
            AppUtils.showSnackBar(widget, "Privacy")
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.isUnderlineText = false
            ds.color = ContextCompat.getColor(this@SignUpActivity, R.color.red_1)
        }
    }

    override fun getViewModel(): SignUpViewModel {
        return signUpViewModel
    }
}