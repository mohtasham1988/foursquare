package ir.cafebazaar.foursquare.repository.model

data class Item(
    val reasons: Reasons,
    val referralId: String,
    val venue: Venue
)
