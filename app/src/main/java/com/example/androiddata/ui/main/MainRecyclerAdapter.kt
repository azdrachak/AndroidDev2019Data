package com.example.androiddata.ui.main

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.androiddata.R
import com.example.androiddata.data.Monster
import com.example.androiddata.databinding.MonsterGridItemBinding
import kotlinx.android.synthetic.main.monster_grid_item.view.*

class MainRecyclerAdapter(val context: Context, val monsters: List<Monster>) :
    RecyclerView.Adapter<MainRecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding = MonsterGridItemBinding.bind(itemView)

        val nameText = binding.nameText
        val monsterImage = binding.monsterImage
        val ratingBar = binding.ratingBar
//        val nameText = itemView.findViewById<TextView>(R.id.nameText)
//        val monsterImage = itemView.findViewById<ImageView>(R.id.monsterImage)
//        val ratingBar = itemView.findViewById<RatingBar>(R.id.ratingBar)
    }

    override fun getItemCount(): Int = monsters.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.monster_grid_item, parent, false)
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
        }
    }
}
