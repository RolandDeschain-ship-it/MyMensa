package com.example.mymensa

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.food_item.view.*


class FoodItemAdapter (
    private val fooditems: MutableList<FoodItem>
        ) : RecyclerView.Adapter<FoodItemAdapter.FoodItemViewHolder>()
{
    class FoodItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodItemViewHolder {
        return FoodItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.food_item,
                parent,
                false
            )
        )
    }

    private fun onCheckBoxEvent(fooditem_box:androidx.constraintlayout.widget.ConstraintLayout, fooditem_headline: TextView,  isChecked: Boolean) {
        if (isChecked) {
            fooditem_box.minHeight = fooditem_box.minHeight*2
            fooditem_headline.paintFlags = fooditem_headline.paintFlags or STRIKE_THRU_TEXT_FLAG
        }else{
            fooditem_box.minHeight = fooditem_box.minHeight
            fooditem_headline.paintFlags = fooditem_headline.paintFlags or STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    override fun onBindViewHolder(holder: FoodItemViewHolder, position: Int) {
        val curFoodItem = fooditems[position]
        holder.itemView.apply {
            fooditem_headline.text = curFoodItem.heading
            fooditem_subheading.text = curFoodItem.subheading
            checkBox.isChecked = curFoodItem.isChecked
            onCheckBoxEvent(fooditem_box, fooditem_headline, curFoodItem.isChecked )
            checkBox.setOnCheckedChangeListener { _, isChecked ->
                onCheckBoxEvent(fooditem_box, fooditem_headline, isChecked)
                curFoodItem.isChecked = !curFoodItem.isChecked
            }
        }
    }

    override fun getItemCount(): Int {
        return fooditems.size
    }


}