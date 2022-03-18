package com.example.mymensa.ui.data

import com.example.mymensa.ui.home.FoodItem
import org.koin.core.component.KoinComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET



interface RemoteDataSource {
    suspend fun getFoodItem(): List<FoodItem>
}

class RetrofitDataSource: RemoteDataSource, KoinComponent {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://openmensa.org/api/v2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()

    private val api = retrofit.create(JsonPlaceholderApi::class.java)



    override suspend fun getFoodItem(): List<FoodItem> {
        return api.getFoodItem()
    }
}
interface JsonPlaceholderApi {
    @GET("canteens/64/days/2022-03-18/meals")
    suspend fun getFoodItem(): List<FoodItem>
}