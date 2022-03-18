package com.example.mymensa.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymensa.ui.data.FoodItemRepository
import com.example.mymensa.ui.data.FoodItemRepositoryImpl
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

class HomeViewModel(
    //private val mensaRepository: MensaRepository,
    private val foodItemRepository: FoodItemRepository
) : ViewModel() {

    //val mensaItems = MutableLiveData<List<Mensa>>(emptyList())
    val foodItems = MutableLiveData<List<FoodItem>>(emptyList())

    init {
        viewModelScope.launch {
            //val mensen = mensaRepository.getMensa()
            //mensaItems.value = mensen
            val gerichte = foodItemRepository.getFoodItem()
            foodItems.value = gerichte
        }
    }
}