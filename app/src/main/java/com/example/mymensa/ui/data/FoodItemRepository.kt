package com.example.mymensa.ui.data

import com.example.mymensa.ui.home.FoodItem

interface FoodItemRepository {
    suspend fun getFoodItem(): List<FoodItem>
}

class FoodItemRepositoryImpl(
    private val dataSource: RemoteDataSource
): FoodItemRepository {

    override suspend fun getFoodItem(): List<FoodItem> {
        return dataSource.getFoodItem()
    }

}