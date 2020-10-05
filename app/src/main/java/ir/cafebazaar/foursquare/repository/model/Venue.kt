package ir.cafebazaar.foursquare.repository.model

data class Venue(
    val categories: List<Category>,
    val id: String,
    val location: Location,
    val name: String,
    val photos: Photos,
    val venuePage: VenuePage
)