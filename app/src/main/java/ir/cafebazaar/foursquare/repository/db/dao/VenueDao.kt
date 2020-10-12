package ir.cafebazaar.foursquare.repository.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ir.cafebazaar.foursquare.repository.model.Venue

@Dao
interface VenueDao {
    @Query("SELECT * FROM Venue LIMIT :limit OFFSET :offset ")
    fun getAll(limit: Int, offset: Int): LiveData<List<Venue>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(users: List<Venue>)
}