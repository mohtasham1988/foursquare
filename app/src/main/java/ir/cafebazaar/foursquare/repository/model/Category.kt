package ir.cafebazaar.foursquare.repository.model

import androidx.room.Entity

@Entity
data class Category(

    val id: String?,
    val name: String?,
    val pluralName: String?,
    val primary: Boolean?,
    val shortName: String?
)
