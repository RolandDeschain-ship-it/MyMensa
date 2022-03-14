package com.example.mymensa

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mymensa.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.food_item.*

class MainActivity : AppCompatActivity() {

    private lateinit var foodItemAdapter: FoodItemAdapter
    private lateinit var binding: ActivityMainBinding
    private lateinit var testList: MutableList<FoodItem>
    private lateinit var testFoodItem: FoodItem

    fun sendMessage(view: View) {
        // Do something in response to button
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        //testFoodItem = FoodItem("Hallo", "Hallo2", false)
        //testList = mutableListOf( testFoodItem)

        super.onCreate(savedInstanceState)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        foodItemAdapter = FoodItemAdapter(mutableListOf())
        rvFoodItems.adapter = foodItemAdapter
        rvFoodItems.layoutManager = LinearLayoutManager(this)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}