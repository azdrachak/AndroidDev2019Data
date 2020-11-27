package com.example.androiddata.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androiddata.LOG_TAG
import com.example.androiddata.R
import com.example.androiddata.data.Monster
import com.example.androiddata.databinding.MainFragmentBinding
import com.example.androiddata.ui.shared.SharedViewModel
import com.example.androiddata.utilities.PrefsHelper
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment(), MainRecyclerAdapter.MonsterItemListener {

    private lateinit var viewModel: SharedViewModel

    private val navController by lazy {
        Navigation.findNavController(requireActivity(), R.id.navHost)
    }

    private lateinit var adapter: MainRecyclerAdapter

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

        setHasOptionsMenu(true)

        val layoutStyle = PrefsHelper.getItemType(requireContext())
        binding.recyclerView.layoutManager = if (layoutStyle == "grid") GridLayoutManager(
            requireContext(),
            2
        ) else LinearLayoutManager(requireContext())

        viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel.monsterData.observe(viewLifecycleOwner, {
            adapter = MainRecyclerAdapter(requireContext(), it, this)
            binding.recyclerView.adapter = adapter
            binding.swipeLayout.isRefreshing = false
        })

        viewModel.activityTitle.observe(
            viewLifecycleOwner, {
                requireActivity().title = it
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_main, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.action_view_grid -> {
                PrefsHelper.setItemType(requireContext(), "grid")
                binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            }
            R.id.action_view_list -> {
                PrefsHelper.setItemType(requireContext(), "list")
                binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
            R.id.action_settings -> {
                navController.navigate(R.id.settingsActivity)
            }
        }
        binding.recyclerView.adapter = adapter
        return true
    }
}
