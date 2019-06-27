package com.souqbh.activity

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import com.souqbh.R
import com.souqbh.base.BaseActivity
import com.souqbh.databinding.ActivityDashboardBinding
import com.souqbh.di.component.DaggerDashboardComponent
import com.souqbh.di.component.DashboardComponent
import com.souqbh.di.module.DashboardModule
import com.souqbh.viewmodel.DashboardViewModel
import javax.inject.Inject
import androidx.navigation.ui.NavigationUI
import androidx.navigation.fragment.NavHostFragment
import com.luseen.spacenavigation.SpaceItem


class DashboardActivity : BaseActivity<DashboardViewModel>(), BaseActivity.ToolbarRightImageClickListener,
    BaseActivity.ToolbarLeftImageClickListener {

    @Inject
    lateinit var dashboardViewModel: DashboardViewModel

    private lateinit var binding: ActivityDashboardBinding

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            com.souqbh.R.id.navigation_home -> {
                binding.message.setText(com.souqbh.R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            com.souqbh.R.id.navigation_myads -> {
                binding.message.setText(com.souqbh.R.string.title_my_ads)
                return@OnNavigationItemSelectedListener true
            }
            com.souqbh.R.id.navigation_camera -> {
                binding.message.setText(com.souqbh.R.string.title_watchlist)
                return@OnNavigationItemSelectedListener true
            }
            com.souqbh.R.id.navigation_messages -> {
                binding.message.setText(com.souqbh.R.string.title_messages)
                return@OnNavigationItemSelectedListener true
            }
            com.souqbh.R.id.navigation_account -> {
                binding.message.setText(com.souqbh.R.string.title_account)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        val dashboardComponent: DashboardComponent =
            DaggerDashboardComponent.builder().appComponent(getAppComponent()).dashboardModule(
                DashboardModule(this)
            ).build()
        dashboardComponent.injectHomeActivity(this)

        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_dashboard)

        initToolbar()
        init()
    }

    private fun initToolbar() {
        setToolbarTitleIcon(com.souqbh.R.drawable.home_logo)
        setToolbarLeftIcon(com.souqbh.R.drawable.home_menu, this)
        setToolbarRightIcon(com.souqbh.R.drawable.home_search, this)
    }

    private fun init() {
        /* val navController = Navigation.findNavController(this, R.id.navigation_host_fragment)
         NavigationUI.setupWithNavController(binding.navView, navController)*/


        /*binding.navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        binding.navView.enableAnimation(false)
        binding.navView.enableShiftingMode(0, false)
        binding.navView.enableShiftingMode(1, false)
        binding.navView.enableShiftingMode(2, false)
        binding.navView.enableShiftingMode(3, false)
        binding.navView.enableShiftingMode(4, false)*/

        binding.bottomNavigation.addSpaceItem(
            SpaceItem(
                R.id.navigation_home,
                getString(R.string.title_home),
                R.drawable.home_sel
            )
        )
        binding.bottomNavigation.addSpaceItem(
            SpaceItem(
                R.id.navigation_myads,
                getString(R.string.title_my_ads),
                R.drawable.my_ad_sel
            )
        )
        binding.bottomNavigation.addSpaceItem(
            SpaceItem(
                R.id.navigation_messages,
                getString(R.string.title_messages),
                R.drawable.messages_sel
            )
        )
        binding.bottomNavigation.addSpaceItem(
            SpaceItem(
                R.id.navigation_account,
                getString(R.string.title_account),
                R.drawable.account_sel
            )
        )
        binding.bottomNavigation.shouldShowFullBadgeText(false)

        binding.bottomNavigation.setCentreButtonId(R.id.navigation_camera)
        binding.bottomNavigation.setCentreButtonIconColorFilterEnabled(false)
        binding.bottomNavigation.showIconOnly()

    }

    override fun onSupportNavigateUp() =
        Navigation.findNavController(this, R.id.navigation_host_fragment).navigateUp()

    override fun onRightImageClicked() {

    }

    override fun onLeftImageClicked() {

    }

    override fun getViewModel(): DashboardViewModel {
        return dashboardViewModel
    }

}
