package ir.cafebazaar.foursquare.repository

import androidx.lifecycle.MutableLiveData
import ir.cafebazaar.foursquare.repository.api.RetrofitBuilder
import ir.cafebazaar.foursquare.repository.api.respons.BaseResponse
import ir.cafebazaar.foursquare.repository.model.Base
import ir.cafebazaar.foursquare.utils.Constant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VenueRepository() {

    companion object

    val loadFromCash: Boolean = false
    var venueList: MutableLiveData<BaseResponse<Base>> = MutableLiveData()

    suspend fun readVenueList(
        lientId: String,
        clientSecret: String,
        v: String,
        ll: String,
        query: String,
        limit: Int,
        offset: Int
    ) {

        if (loadFromCash)
            callApi(lientId, clientSecret, v, ll, query, limit, offset)
        else
            readFromDb()
    }

    private suspend fun readFromDb() = withContext(Dispatchers.IO) {


    }

    private suspend fun callApi(
        clientId: String,
        clientSecret: String,
        v: String,
        ll: String,
        query: String,
        limit: Int,
        offset: Int
    ) {
        RetrofitBuilder.apiVenueService.getVenueExplore(
            clientId,
            clientSecret,
            v,
            ll,
            query,
            limit,
            offset
        ).enqueue(object : Callback<Base> {
            override fun onFailure(call: Call<Base>, t: Throwable) {
                venueList.value = ir.cafebazaar.foursquare.repository.api.respons.BaseResponse(
                    Constant.Status.ERROR,
                    null,
                    t.message
                )
            }

            override fun onResponse(call: Call<Base>, response: Response<Base>) {
                venueList.value = ir.cafebazaar.foursquare.repository.api.respons.BaseResponse(
                    Constant.Status.ERROR,
                    response.body(),
                    null
                )
            }
        })
    }
}