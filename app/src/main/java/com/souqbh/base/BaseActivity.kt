package com.souqbh.base

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.souqbh.R
import com.souqbh.databinding.AppToolbarBinding
import com.souqbh.utils.AppUtils


@SuppressLint("Registered")


/**
 * Created by Paresh Devatval
 */
abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity() {

    private lateinit var mViewModel: T
    private var progressDialog: Dialog? = null
    private val toolbarBinding: AppToolbarBinding by lazy {
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(com.souqbh.R.id.toolbarLayout)
        DataBindingUtil.getBinding<AppToolbarBinding>(toolbar) as AppToolbarBinding
    }
    private var errorSnackbar: Snackbar? = null
    private val errorClickListener = View.OnClickListener {
        //internetErrorRetryClicked()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mViewModel = getViewModel()

        mViewModel.errorMessage.observe(this, Observer { errorMessage ->
            if (errorMessage != null) showError(errorMessage) else hideError()
        })

        mViewModel.loadingVisibility.observe(this, Observer { loadingVisibility ->
            loadingVisibility?.let { if (loadingVisibility) showProgress() else hideProgress() }
        })

        mViewModel.apiErrorMessage.observe(this, Observer { apiError ->
            apiError?.let {
                AppUtils.showSnackBar(getRootView(), it)
            }
        })

        //initToolbar()
    }

    fun transparentStatusBar(transparent: Boolean) {

        val window = window
        if (transparent) {
            window.statusBarColor = ContextCompat.getColor(this, android.R.color.transparent)
            window.decorView.systemUiVisibility =
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = ContextCompat.getColor(this, R.color.black_1)
        }

    }

    private fun getRootView(): View {
        val contentViewGroup = findViewById<View>(android.R.id.content) as ViewGroup
        var rootView = contentViewGroup.getChildAt(0)
        if (rootView == null) rootView = window.decorView.rootView
        return rootView!!
    }

    // initializing application toolbar
    /*fun initToolbar() {
        // getting toolbar binding
        toolbarBinding =
    }*/

    private fun showError(@StringRes errorMessage: Int) {
        errorSnackbar = Snackbar.make(getRootView(), errorMessage, Snackbar.LENGTH_LONG)
        errorSnackbar?.setAction(com.souqbh.R.string.action_retry, errorClickListener)
        errorSnackbar?.show()
    }

    private fun hideError() {
        errorSnackbar?.dismiss()
    }

    /**
     * Override for set view model
     *
     * @return view model instance
     */
    abstract fun getViewModel(): T

    /*  abstract fun internetErrorRetryClicked()*/

    fun hideToolbar() {
        toolbarBinding.toolbar.visibility = View.GONE
    }

    // toolabr title
    fun setToolbarTitle(title: Int) {
        toolbarBinding.tvToolbarTitle.visibility = View.VISIBLE
        toolbarBinding.tvToolbarTitle.text = resources.getString(title)
    }

    // set icon of toolbar left icon
    fun setToolbarTitleIcon(resourceId: Int) {
        toolbarBinding.ivToolbarTitle.setImageResource(resourceId)
        toolbarBinding.ivToolbarTitle.visibility = View.VISIBLE
    }

    // set icon of toolbar left icon
    fun setToolbarLeftIcon(resourceId: Int, toolbarLeftImageClickListener: ToolbarLeftImageClickListener) {
        toolbarBinding.ivToolbarLeft.setImageResource(resourceId)
        toolbarBinding.ivToolbarLeft.visibility = View.VISIBLE

        toolbarBinding.ivToolbarLeft.setOnClickListener { toolbarLeftImageClickListener.onLeftImageClicked() }
    }

    // set toolbar right icon and implement its click
    fun setToolbarRightIcon(resourceId: Int, toolbarRightClickListener: ToolbarRightImageClickListener) {
        toolbarBinding.ivToolbarRight.setImageResource(resourceId)
        toolbarBinding.ivToolbarRight.visibility = View.VISIBLE

        toolbarBinding.ivToolbarRight.setOnClickListener { toolbarRightClickListener.onRightImageClicked() }
    }

    // hide toolbar right icon when not needed
    fun hideToolbarRightIcon() {
        toolbarBinding.ivToolbarRight.visibility = View.GONE
    }

    // hide toolbar left icon when not needed
    fun hideToolbarLeftIcon() {
        toolbarBinding.ivToolbarLeft.visibility = View.GONE
    }

    @SuppressLint("InflateParams")
    fun showProgress() {
        if (progressDialog == null) {
            progressDialog = Dialog(this)
            progressDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        }
        // inflating and setting view of custom dialog
        val view = LayoutInflater.from(this).inflate(com.souqbh.R.layout.app_loading_dialog, null, false)
        val imageView = view.findViewById<ImageView>(com.souqbh.R.id.imageView2)
        // applying rotate animation
        //imageView.pivotX = 0.5f
        ///imageView.pivotY = 0.5f
        ObjectAnimator.ofFloat(imageView, View.ROTATION, 360f, 0f).apply {
            repeatCount = ObjectAnimator.INFINITE
            duration = 1500
            interpolator = LinearInterpolator()
            start()
        }
        progressDialog!!.setContentView(view)

        // setting background of dialog as transparent
        val window = progressDialog!!.window
        window?.setBackgroundDrawable(ContextCompat.getDrawable(this, android.R.color.transparent))
        // preventing outside touch and setting cancelable false
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        progressDialog!!.show()
    }

    private fun hideProgress() {
        progressDialog?.run {
            if (isShowing) {
                dismiss()
            }
        }
    }

    /* [START] Check if an active internet connection is present or not*/
    /* return boolen value true or false */
    fun isInternetAvailable(): Boolean {
        // getting Connectivity service as ConnectivtyManager
        return AppUtils.hasInternet(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        progressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
            progressDialog = null
        }
    }

    // interface class for toolbar right icon click
    interface ToolbarRightImageClickListener {
        fun onRightImageClicked()
    }

    interface ToolbarLeftImageClickListener {
        fun onLeftImageClicked()
    }

    private fun getApp() = (application as BaseApplication)

    fun getAppComponent() = getApp().getAppComponent()

    fun getLocalDataComponent() = getApp().getLocalDataComponent()

    fun getNetworkComponent() = getApp().getNetworkComponent()
}