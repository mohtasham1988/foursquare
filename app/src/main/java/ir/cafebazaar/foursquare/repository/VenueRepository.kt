package ir.cafebazaar.foursquare.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ir.cafebazaar.foursquare.FoursquareApp
import ir.cafebazaar.foursquare.fragment.model.MainFragmentModel
import ir.cafebazaar.foursquare.repository.api.RetrofitBuilder
import ir.cafebazaar.foursquare.repository.api.respons.BaseResponse
import ir.cafebazaar.foursquare.repository.db.DatabaseBuilder
import ir.cafebazaar.foursquare.repository.model.Base
import ir.cafebazaar.foursquare.repository.model.Venue
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VenueRepository(private val mainFragmentModel: MainFragmentModel) {

    fun fetchVenueList(
        clientId: String,
        clientSecret: String,
        v: String,
        ll: String,
        query: String,
        limit: Int,
        offset: Int
    ): LiveData<BaseResponse<List<Venue>>> {
        return object : NetworkBoundResource<List<Venue>, List<Venue>>() {
            override suspend fun saveCallResult(item: List<Venue>) {
                getVenueDao().insertAll(item)
            }

            override fun shouldFetch(data: List<Venue>?): Boolean {
                return data == null || data.isEmpty() || offset >= mainFragmentModel.getLastOffset()
            }

            override fun loadFromDb(): LiveData<List<Venue>> {
                return getVenueDao().getAll(limit, offset)
            }

            override fun createCall(): LiveData<BaseResponse<List<Venue>>> {
                return callApi(clientId, clientSecret, v, ll, query, limit, offset)
            }

            override fun setLastOffset() {
                mainFragmentModel.setLastOffset(offset)
            }

            override fun processResponse(response: BaseResponse<List<Venue>>): List<Venue>? {
                return response.data
            }
        }.asLiveData()


    }


    private fun callApi(
        clientId: String,
        clientSecret: String,
        v: String,
        ll: String,
        query: String,
        limit: Int,
        offset: Int
    ): LiveData<BaseResponse<List<Venue>>> {
        val liveData = MutableLiveData<BaseResponse<List<Venue>>>()

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
                liveData.value = BaseResponse.error(null, t.message)
            }

            override fun onResponse(call: Call<Base>, response: Response<Base>) {
                liveData.value = BaseResponse.success(parsResponse(response))
            }
        })
        return liveData
    }

    fun parsResponse(response: Response<Base>): ArrayList<Venue> {
        val venueList = ArrayList<Venue>()
        if (response.isSuccessful)
            for (item in response.body()?.response!!.groups[0].items) {
                venueList.add(item.venue)
            }
        return venueList
    }

    private fun getVenueDao() = DatabaseBuilder.getInstance(FoursquareApp.mInstance).venueDao()
}