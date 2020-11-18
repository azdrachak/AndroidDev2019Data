package com.example.androiddata.ui.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import com.example.androiddata.R


class SplashFragment : Fragment() {

    private val navController by lazy {
        Navigation.findNavController(requireActivity(), R.id.navHost)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        displayMainFragment()
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    private fun displayMainFragment() {
        navController.navigate(
            R.id.action_nav_main,
            null,
            NavOptions.Builder().setPopUpTo(R.id.splashFragment, true).build()
        )
    }
}