package com.example.androiddata.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androiddata.R
import com.example.androiddata.data.Monster
import com.example.androiddata.databinding.MonsterGridItemBinding
import com.example.androiddata.utilities.PrefsHelper

class MainRecyclerAdapter(
    private val context: Context,
    private val monsters: List<Monster>,
    private val itemListener: MonsterItemListener
) : RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = MonsterGridItemBinding.bind(itemView)

        val nameText = binding.nameText
        val monsterImage = binding.monsterImage
        val ratingBar = binding.ratingBar
    }

    override fun getItemCount(): Int = monsters.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val layoutStyle = PrefsHelper.getItemType(parent.context)
        val layoutId =
            if (layoutStyle == "grid") R.layout.monster_grid_item else R.layout.monster_list_item

        val view = inflater.inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val monster = monsters[position]

        with(holder) {
            nameText.let {
                it.text = monster.monsterName
                it.contentDescription = monster.monsterName
            }

            ratingBar.rating = monster.scariness.toFloat()

            Glide.with(context)
                .load(monster.imageUrl)
                .fitCenter()
                .into(monsterImage)

            holder.itemView.setOnClickListener {
                itemListener.onMonsterItemClick(monster)
            }
        }
    }

    interface MonsterItemListener {
        fun onMonsterItemClick(monster: Monster)
    }
}
