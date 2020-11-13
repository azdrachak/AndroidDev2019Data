package com.example.androiddata.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.androiddata.LOG_TAG
import com.example.androiddata.R
import com.example.androiddata.data.Monster
import com.example.androiddata.databinding.MainFragmentBinding
import com.example.androiddata.ui.shared.SharedViewModel

class MainFragment : Fragment(), MainRecyclerAdapter.MonsterItemListener {

    private lateinit var viewModel: SharedViewModel

    private val navController by lazy {
        Navigation.findNavController(requireActivity(), R.id.navHost)
    }

    private var _binding: MainFragmentBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = MainFragmentBinding.inflate(inflater, container, false)

        (requireActivity() as AppCompatActivity).run {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        }

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.monsterData.observe(viewLifecycleOwner, {
            val adapter = MainRecyclerAdapter(requireContext(), it, this)
            binding.recyclerView.adapter = adapter
            binding.swipeLayout.isRefreshing = false
        })

        binding.swipeLayout.setOnRefreshListener {
            viewModel.refreshData()
        }

        return binding.root
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    override fun onMonsterItemClick(monster: Monster) {
        Log.i(LOG_TAG, "onMonsterItemClick: ${monster.monsterName}")
        viewModel.selectedMonster.value = monster
        navController.navigate(R.id.action_nav_detail)
    }
}
