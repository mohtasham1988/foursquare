package ir.cafebazaar.foursquare.repository.model

import androidx.room.Ignore

data class Location(
    val address: String?,
    val cc: String?,
    val city: String?,
    val country: String?,
    val crossStreet: String?,
    val distance: Int?,
    @Ignore val formattedAddress: List<String>?,
    @Ignore  val labeledLatLngs: List<LabeledLatLng>?,
    val lat: Double?,
    val lng: Double?,
    val neighborhood: String?,
    val postalCode: String?
)
