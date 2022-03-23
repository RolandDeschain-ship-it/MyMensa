package com.example.mymensa.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.mymensa.R
import com.example.mymensa.databinding.FoodItemBinding
import kotlinx.android.synthetic.main.food_item.view.*


class FoodItemAdapter : ListAdapter<FoodItem, FoodItemAdapter.FoodItemViewHolder>(Diff) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FoodItemViewHolder(FoodItemBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            fooditemHeadline.text = item.name.toString()
            fooditemSubheading.text = item.category
            priceView.text = item.prices.students.toString()
        }


    }


    class FoodItemViewHolder(
        val binding: FoodItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    object Diff : DiffUtil.ItemCallback<FoodItem>() {
        override fun areItemsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: FoodItem, newItem: FoodItem): Boolean {
            return oldItem == newItem
        }
    }
}