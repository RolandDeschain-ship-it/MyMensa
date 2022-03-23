package com.example.mymensa.ui.data

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mymensa.ui.home.FoodItem
import com.example.mymensa.ui.home.Prices
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*


interface RemoteDataSource {
    suspend fun getTodayFoodItem(mensaID: Int): List<FoodItem>
    suspend fun getTomorrowFoodItem(mensaID: Int): List<FoodItem>
    suspend fun getAllTodayFoodItems(): List<FoodItem>
    suspend fun getAllTomorrowFoodItems(): List<FoodItem>
}


@RequiresApi(Build.VERSION_CODES.N)


class RetrofitDataSource : RemoteDataSource, KoinComponent {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://openmensa.org/api/v2/")
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
    var curDate = getDate(true)
    var tomorrowDate = getDate(false)

    private val dataStore: DataStorageRepository by inject()

    private val api = retrofit.create(JsonPlaceholderApi::class.java)

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getTodayFoodItem(mensaID: Int): List<FoodItem> {
        try {
            return foodItemfilter(api.getTodayFoodItem(curDate, mensaID))
        } catch (e: Exception) {
            val dummy1 = FoodItem(
                id = 0,
                category = "Ein Fehler ist aufgetreten ",
                name = "",
                prices = Prices(0.0),
                notes = mutableListOf()
            )

            var dummyList = listOf(
                dummy1
            )
            return dummyList
        }

    }

    override suspend fun getAllTodayFoodItems(): List<FoodItem> {
        var allTodayFoodItems: MutableList<List<FoodItem>> = mutableListOf<List<FoodItem>>()
        val mensaIDList = getMensaIDList()
        if(mensaIDList.size > 0) {
            return getTodayFoodItem(mensaIDList[0])
        }else{
            return getTodayFoodItem(64)
        }
    }

    override suspend fun getAllTomorrowFoodItems(): List<FoodItem> {
        var allTomorrowFoodItems: MutableList<List<FoodItem>> = mutableListOf<List<FoodItem>>()
        val mensaIDList = getMensaIDList()
        if(mensaIDList.size > 0) {
        return getTomorrowFoodItem(mensaIDList[0])
        }else{
        return getTomorrowFoodItem(64)
    }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getTomorrowFoodItem(mensaID: Int): List<FoodItem> {

        try {
            return foodItemfilter(api.getTomorrowFoodItem(tomorrowDate, mensaID))
        } catch (e: Exception) {

            val dummy1 = FoodItem(
                id = 0,
                category = "Ein Fehler ist aufgetreten ",
                name = "",
                prices = Prices(0.0),
                notes = mutableListOf()
            )

            var dummyList = listOf(
                dummy1
            )
            return dummyList
        }


    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getDate(isToday: Boolean): String {
        val calendar = Calendar.getInstance()
        var date = ""
        val today = calendar.time
        calendar.add(Calendar.DAY_OF_YEAR, 1)
        val tomorrow = calendar.time

        val sdf = SimpleDateFormat("yyyy-MM-dd")

        if (isToday == true) {
            date = sdf.format(today)
        } else {
            date = sdf.format(tomorrow)
        }
        print(date)
        return date
    }

    private suspend fun foodItemfilter(toFilterList: List<FoodItem>): List<FoodItem> {
        var filterCritList = getFilterCrit()
        var translatedCritList = translateCrit(filterCritList)
        var mutableFoodList: MutableList<FoodItem> = mutableListOf()
        mutableFoodList.addAll(toFilterList)
        if (translatedCritList.contains("vegan")) {
            mutableFoodList.removeAll {
                it.notes.filter { note ->
                    note.lowercase().contains("vegan")
                }.isEmpty()
            }
        }
        if (translatedCritList.contains("vegetarisch")) {
            mutableFoodList.removeAll {
                it.notes.filter { note ->
                    note.lowercase().contains("vegetarisch")
                }.isEmpty()
            }
            mutableFoodList.removeAll {
                it.notes.filter { note ->
                    note.lowercase().contains("fleischlos")
                }.isEmpty()
            }
        }
        if (translatedCritList.contains("gluten")) {
            mutableFoodList.removeAll {
                it.notes.filter { note ->
                    note.lowercase().contains("gluten")
                }.isNotEmpty()
            }
        }
        if (translatedCritList.contains("alkohol")) {
            mutableFoodList.removeAll {
                it.notes.filter { note ->
                    note.lowercase().contains("alkohol")
                }.isNotEmpty()
            }
        }
        if (translatedCritList.contains("laktose")) {
            mutableFoodList.removeAll {
                it.notes.filter { note ->
                    note.lowercase().contains("laktose")
                }.isNotEmpty()
            }
        }
        if (translatedCritList.contains("milch")) {
            mutableFoodList.removeAll {
                it.notes.filter { note ->
                    note.lowercase().contains("milch")
                }.isNotEmpty()
            }
        }
        if (translatedCritList.contains("schwein")) {
            mutableFoodList.removeAll {
                it.notes.filter { note ->
                    note.lowercase().contains("schwein")
                }.isNotEmpty()
            }
        }
        if (translatedCritList.contains("rind")) {
            mutableFoodList.removeAll {
                it.notes.filter { note ->
                    note.lowercase().contains("rind")
                }.isNotEmpty()
            }
        }
        if (translatedCritList.contains("geflügel")) {
            mutableFoodList.removeAll {
                it.notes.filter { note ->
                    note.lowercase().contains("geflügel")
                }.isNotEmpty()
            }
        }

        mutableFoodList.distinct()
        return mutableFoodList
    }


    private suspend fun getFilterCrit(): List<String> {

        var list:List<String> = dataStore.getStringArray("diets")
        println("LOOOOOOOOOOL")
        println(list)
        return list

    }


    private suspend fun getMensaIDList(): MutableList<Int> {
        var mensaIDList = mutableListOf<Int>()

            dataStore.getMensen().forEach {
                mensaIDList.add(it.id.toInt())
            }
        return mensaIDList
    }

    private fun translateCrit(critList: List<String>): List<String> {
        var translatedList: MutableList<String> = mutableListOf()
        for (crits in critList) {
            when (crits) {
                "Vegan" -> translatedList.add("vegan")
                "Vegetarisch" -> translatedList.add("vegetarsich")
                "Vegetarisch" -> translatedList.add("fleischlos")
                "Glutenfrei" -> translatedList.add("gluten")
                "Laktosefrei" -> translatedList.addAll(listOf("laktose", "milch"))
                "Ohne Schweinefleisch" -> translatedList.add("schwein")
                "Ohne Rindfleisch" -> translatedList.add("rind")
                "Ohne Geflügel" -> translatedList.add("geflügel")
                "Alkoholfrei" -> translatedList.add("alkohol")

            }
        }
        return translatedList
    }

}

interface JsonPlaceholderApi {
    @RequiresApi(Build.VERSION_CODES.N)
    @GET("canteens/{mensaID}/days/{date}/meals")
    suspend fun getTodayFoodItem(
        @Path("date") date: String,
        @Path("mensaID") mensaID: Int
    ): List<FoodItem>

    @GET("canteens/{mensaID}/days/{date}/meals")
    suspend fun getTomorrowFoodItem(
        @Path("date") date: String,
        @Path("mensaID") mensaID: Int
    ): List<FoodItem>
}

/*
@RequiresApi(Build.VERSION_CODES.O)
fun getWeekDayName(s: String?): String? {
    val dtfInput: DateTimeFormatter = DateTimeFormatter.ofPattern("u-M-d", Locale.ENGLISH)
    return LocalDate.parse(s, dtfInput).getDayOfWeek()
        .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
}*/
