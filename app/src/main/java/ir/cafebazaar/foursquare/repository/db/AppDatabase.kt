package ir.cafebazaar.foursquare.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ir.cafebazaar.foursquare.repository.db.dao.VenueDao
import ir.cafebazaar.foursquare.repository.model.CategoryConverter
import ir.cafebazaar.foursquare.repository.model.Venue

@Database(entities = arrayOf(Venue::class), version = 1)
@TypeConverters(CategoryConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao
}

