package com.bonioctavianus.android.instafake.ui.navigation

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.bonioctavianus.android.instafake.HomeActivity
import com.bonioctavianus.android.instafake.SplashActivity
import com.bonioctavianus.android.instafake.ui.auth.AuthActivity
import com.bonioctavianus.android.instafake.ui.home.HomeFragmentDirections
import javax.inject.Inject

class Navigator @Inject constructor() {

    fun navigateToSplashActivity(activity: FragmentActivity) {
        activity.startActivity(
            Intent(activity, SplashActivity::class.java)
        )
        activity.finish()
    }

    fun navigateToAuthActivity(activity: FragmentActivity) {
        activity.startActivity(
            Intent(activity, AuthActivity::class.java)
        )
        activity.finish()
    }

    fun navigateToMainActivity(activity: FragmentActivity) {
        activity.startActivity(
            Intent(activity, HomeActivity::class.java)
        )
        activity.finish()
    }

    fun navigateToCreatePostFragment(fragment: Fragment) {
        val direction = HomeFragmentDirections.actionHomeFragmentToCreatePostFragment()
        fragment.findNavController().navigate(direction)
    }
}