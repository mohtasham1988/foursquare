package ir.cafebazaar.foursquare.repository.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*


class CategoryConverter {

    var gson = Gson()

    @TypeConverter
    fun stringToCategoryList(data: String?): List<Category?>? {
        if (data == null) {
            return Collections.emptyList()
        }
        val listType = object : TypeToken<List<Category?>?>() {}.type
        return gson.fromJson<List<Category?>>(data, listType)
    }

    @TypeConverter
    fun listToString(list: List<Category?>?): String? {
        return gson.toJson(list)
    }
}