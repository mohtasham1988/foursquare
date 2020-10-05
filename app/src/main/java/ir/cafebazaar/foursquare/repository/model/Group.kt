package ir.cafebazaar.foursquare.repository.model

data class Group(
    val items: List<Item>,
    val name: String,
    val type: String
)