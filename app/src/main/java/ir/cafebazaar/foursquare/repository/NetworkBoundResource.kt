package ir.cafebazaar.foursquare.repository

import androidx.annotation.MainThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import ir.cafebazaar.foursquare.repository.api.respons.BaseResponse
import ir.cafebazaar.foursquare.utils.Constant


abstract class NetworkBoundResource<ResultType, RequestType> {
    private val result = MediatorLiveData<ResultType>()

    init {
        @Suppress("LeakingThis")
        val db = loadFromDb()
        result.addSource(db) {
            result.removeSource(db)
            if (shouldFetch(it))
                fetchData(db)
            else result.addSource(db) { it2 ->
                result.value = it2
            }

        }
    }

    private fun fetchData(db: LiveData<ResultType>) {
        val api = createCall()
        result.addSource(api) {
            result.removeSource(api)
            result.removeSource(db)
            when (it.status) {
                Constant.Status.SUCCESS -> {
                    saveCallResult(it.data!!)
                    result.addSource(loadFromDb()) { new ->
                        setValue(new)
                    }
                }
                Constant.Status.ERROR -> {
                    onFetchFailed()
                    result.addSource(loadFromDb()) { new ->
                        setValue(new)
                    }
                }
            }
        }
    }

    @MainThread
    private fun setValue(newValue: ResultType) {
        if (result.value != newValue) {
            result.value = newValue
        }
    }

    @WorkerThread
    protected abstract fun saveCallResult(item: RequestType)

    @MainThread
    protected abstract fun shouldFetch(data: ResultType?): Boolean

    @MainThread
    protected abstract fun loadFromDb(): LiveData<ResultType>

    @MainThread
    protected abstract fun createCall(): LiveData<BaseResponse<RequestType>>

    protected open fun onFetchFailed() {}


    fun asLiveData(): LiveData<ResultType> = result
}
