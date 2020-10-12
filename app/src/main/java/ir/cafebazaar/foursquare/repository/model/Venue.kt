package ir.cafebazaar.foursquare.repository.model

import androidx.room.*

@Entity
data class Venue(
    @PrimaryKey val id: String,
    @Embedded val location: Location?,
    val name: String?,
    @TypeConverters(CategoryConverter::class) val categories: List<Category>,

 )