package com.example.androiddata.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.androiddata.R
import com.example.androiddata.databinding.FragmentDetailBinding
import com.example.androiddata.ui.shared.SharedViewModel


class DetailFragment : Fragment() {

    private lateinit var viewModel: SharedViewModel
    private val navController by lazy {
        Navigation.findNavController(requireActivity(), R.id.navHost)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val binding = FragmentDetailBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return binding.root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navController.navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }
}