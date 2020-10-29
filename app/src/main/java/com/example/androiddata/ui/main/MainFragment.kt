package com.example.androiddata.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androiddata.R
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.monsterData.observe(viewLifecycleOwner, {
            val monsterNames = StringBuilder()
            for (monster in it) {
                monsterNames
                    .append(monster.name)
                    .append("\n")
            }
            message.text = monsterNames
        })
        return inflater.inflate(R.layout.main_fragment, container, false)
    }
}
