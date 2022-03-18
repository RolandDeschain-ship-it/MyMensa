package com.example.mymensa.ui.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.mymensa.ui.data.FoodItemRepository
import com.example.mymensa.ui.data.FoodItemRepositoryImpl
import com.example.mymensa.ui.data.RemoteDataSource
import com.example.mymensa.ui.data.RetrofitDataSource
import com.example.mymensa.ui.home.HomeViewModel



    val appModule = module {

        single<RemoteDataSource> { RetrofitDataSource() }
        single<FoodItemRepository> { FoodItemRepositoryImpl(get()) }

        viewModel { HomeViewModel(get()) }
    }