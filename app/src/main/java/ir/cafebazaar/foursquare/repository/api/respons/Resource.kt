package ir.cafebazaar.foursquare.repository.api.respons

import ir.cafebazaar.foursquare.utils.Constant

data class Resource<out T>(val status: Constant.Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(status = Constant.Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String): Resource<T> =
            Resource(status = Constant.Status.ERROR, data = data, message = message)
    }
}