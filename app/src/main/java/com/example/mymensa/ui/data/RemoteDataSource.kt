package com.example.mymensa.ui.data

import android.icu.text.SimpleDateFormat
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.mymensa.ui.home.FoodItem
import com.example.mymensa.ui.home.Prices
import org.koin.core.component.KoinComponent
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
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
    var mensaIDList = getMensaIDList()

    private val api = retrofit.create(JsonPlaceholderApi::class.java)

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getTodayFoodItem(mensaID:Int): List<FoodItem> {
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
        for (mensaIDs in mensaIDList){
            allTodayFoodItems.add(getTodayFoodItem(mensaIDs))
        }
        return allTodayFoodItems[0]
    }
    override suspend fun getAllTomorrowFoodItems(): List<FoodItem> {
        var allTomorrowFoodItems: MutableList<List<FoodItem>> = mutableListOf<List<FoodItem>>()
        for (mensaIDs in mensaIDList){
            allTomorrowFoodItems.add(getTomorrowFoodItem(mensaIDs))
        }
        return allTomorrowFoodItems[0]
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override suspend fun getTomorrowFoodItem(mensaID: Int): List<FoodItem> {

        try {
            return foodItemfilter(api.getTomorrowFoodItem(tomorrowDate, mensaID))
        } catch (e: Exception) {

            val dummy1 = FoodItem(
                id = 0,
                category = "Hauptkomponente",
                name = "Sloppy Joe Burger",
                prices = Prices(2.9),
                notes = mutableListOf("vegan", "alkoholfrei")
            )
            val dummy2 = FoodItem(
                id = 1,
                category = "Hauptkomponente",
                name = "Gemüsesuppe",
                prices = Prices(2.0),
                notes = mutableListOf("vegan", "alkoholfrei")
            )
            val dummy3 = FoodItem(
                id = 2,
                category = "Hauptkomponente",
                name = "Spaghetti Bolognese",
                prices = Prices(2.2),
                notes = mutableListOf("Rind", "alkoholfrei")
            )
            val dummy4 = FoodItem(
                id = 3,
                category = "Nebenkomponente",
                name = "Ofenkartoffeln",
                prices = Prices(1.2),
                notes = mutableListOf("schwein", "alkoholfrei")
            )
            val dummy5 = FoodItem(
                id = 4,
                category = "Nebenkomponente",
                name = "Belegtes Baguette",
                prices = Prices(1.0),
                notes = mutableListOf("geflügel", "alkoholfrei")
            )
            val dummy6 = FoodItem(
                id = 5,
                category = "Nebenkomponente",
                name = "Pfannkuchen",
                prices = Prices(1.3),
                notes = mutableListOf("glutenfrei", "vegan", "alkoholfrei")
            )
            val dummy7 = FoodItem(
                id = 6,
                category = "Sättigungsbeilage",
                name = "Gemüseeis",
                prices = Prices(1.7),
                notes = mutableListOf("vegan", "alkoholfrei")
            )

            var dummyList = listOf(
                dummy1,
                dummy2,
                dummy3,
                dummy4,
                dummy5,
                dummy6,
                dummy7,
            )
            return foodItemfilter(dummyList)
        }


    }


}

interface JsonPlaceholderApi {
    @RequiresApi(Build.VERSION_CODES.N)
    @GET("canteens/{mensaID}/days/{date}/meals")
    suspend fun getTodayFoodItem
        (@Path("date") date: String, @Path("mensaID") mensaID: Int): List<FoodItem>

    @GET("canteens/{mensaID}/days/{date}/meals")
    suspend fun getTomorrowFoodItem
        (@Path("date") date: String, @Path("mensaID") mensaID: Int): List<FoodItem>
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

private fun foodItemfilter(toFilterList: List<FoodItem>): List<FoodItem> {
    var filterCritList = getFilterCrit()
    var translatedCritList = translateCrit(filterCritList)
    var mutableFoodList: MutableList<FoodItem> = mutableListOf()
    mutableFoodList.addAll(toFilterList)
    if (translatedCritList.contains("vegan")) {
       mutableFoodList.removeAll {
                    it.notes.filter {  note ->
                        note.lowercase().contains("vegan")
                    }.isEmpty()
                }
    }
    if (translatedCritList.contains("vegetarisch")) {
        mutableFoodList.removeAll {
            it.notes.filter {  note ->
                note.lowercase().contains("vegetarisch")
            }.isEmpty()
        }
    }
    if (translatedCritList.contains("gluten")) {
        mutableFoodList.removeAll {
            it.notes.filter {  note ->
                note.lowercase().contains("gluten")
            }.isNotEmpty()
        }
    }
    if (translatedCritList.contains("alkohol")) {
        mutableFoodList.removeAll {
            it.notes.filter {  note ->
                note.lowercase().contains("alkohol")
            }.isNotEmpty()
        }
    }
    if (translatedCritList.contains("laktose", )) {
        mutableFoodList.removeAll {
            it.notes.filter {  note ->
                note.lowercase().contains("laktose")
            }.isNotEmpty()
        }
    }
    if (translatedCritList.contains("milch")) {
        mutableFoodList.removeAll {
            it.notes.filter {  note ->
                note.lowercase().contains("milch")
            }.isNotEmpty()
        }
    }
    if (translatedCritList.contains("schwein")) {
        mutableFoodList.removeAll {
            it.notes.filter {  note ->
                note.lowercase().contains("schwein")
            }.isNotEmpty()
        }
    }
    if (translatedCritList.contains("rind")) {
        mutableFoodList.removeAll {
            it.notes.filter {  note ->
                note.lowercase().contains("rind")
            }.isNotEmpty()
        }
    }
    if (translatedCritList.contains("geflügel")) {
        mutableFoodList.removeAll {
            it.notes.filter {  note ->
                note.lowercase().contains("geflügel")
            }.isNotEmpty()
        }
    }

    mutableFoodList.distinct()
    return mutableFoodList
}


private fun getFilterCrit(): List<String> {
    //TODO(getter Filterkriterien)

    return listOf( "porkFree", "poultryFree")
}

private fun getMensaIDList(): List<Int> {
    //TODO(getter MensaIDs)

    return listOf(63, 64, 65)
}

private fun translateCrit(critList: List<String>): List<String> {
    var translatedList: MutableList<String> = mutableListOf()
    for (crits in critList) {
        when (crits) {
            "vegan" -> translatedList.add("vegan")
            "vegetarien" -> translatedList.add("vegetarsich")
            "glutenFree" -> translatedList.add("gluten")
            "lactoseFree" -> translatedList.addAll(listOf("laktose", "milch"))
            "porkFree" -> translatedList.add("schwein")
            "beefFree" -> translatedList.add("rind")
            "poultryFree" -> translatedList.add("geflügel")
            "alcoholFree" -> translatedList.add("alkohol")

        }
    }
    return translatedList
}
/*
@RequiresApi(Build.VERSION_CODES.O)
fun getWeekDayName(s: String?): String? {
    val dtfInput: DateTimeFormatter = DateTimeFormatter.ofPattern("u-M-d", Locale.ENGLISH)
    return LocalDate.parse(s, dtfInput).getDayOfWeek()
        .getDisplayName(TextStyle.FULL, Locale.ENGLISH)
}*/
