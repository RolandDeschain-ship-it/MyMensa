package com.example.mymensa.ui.di

import com.example.mymensa.ui.data.*
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import com.example.mymensa.ui.home.HomeViewModel



    val appModule = module {

        single<RemoteDataSource> { RetrofitDataSource() }
        single<FoodItemRepository> { FoodItemRepositoryImpl(get()) }

        single {
            DataStorageRepository(get())
        }
        viewModel { HomeViewModel(get()) }
    }