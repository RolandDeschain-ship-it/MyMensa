package com.example.mymensa.ui.data

import com.example.mymensa.ui.home.FoodItem

interface FoodItemRepository {
    suspend fun getTodayFoodItems(): List<FoodItem>
    suspend fun getTomorrowFoodItems(): List<FoodItem>
}

class FoodItemRepositoryImpl(
    private val dataSource: RemoteDataSource
): FoodItemRepository {

    override suspend fun getTodayFoodItems(): List<FoodItem> {
        return dataSource.getAllTodayFoodItems()
    }
    override suspend fun getTomorrowFoodItems(): List<FoodItem> {
        return dataSource.getAllTomorrowFoodItems()
    }


}