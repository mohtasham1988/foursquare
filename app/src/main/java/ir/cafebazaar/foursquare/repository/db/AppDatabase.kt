package ir.cafebazaar.foursquare.repository.db

import androidx.room.Database
import androidx.room.RoomDatabase
import ir.cafebazaar.foursquare.repository.db.dao.VenueDao
import ir.cafebazaar.foursquare.repository.model.Venue

@Database(entities = arrayOf(Venue::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun venueDao(): VenueDao
}

