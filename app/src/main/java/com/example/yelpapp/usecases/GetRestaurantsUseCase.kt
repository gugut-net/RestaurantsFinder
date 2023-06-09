package com.example.yelpapp.usecases

import android.location.Location
import com.example.yelpapp.domain.Restaurant
import com.example.yelpapp.domain.mapToRestaurant
import com.example.yelpapp.service.NetworkRepository
import com.example.yelpapp.utils.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

import javax.inject.Inject

class GetRestaurantsUseCase @Inject constructor(
    private val networkRepository: NetworkRepository,
    private val networkState: NetworkState,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    operator fun invoke(
        location: Location,
        attributes: String,
        sortedBy: ApiSortedBy
    ): Flow<UIState<List<Restaurant>>> = flow {
        emit(UIState.LOADING)
        try {
            if (networkState.isInternetEnabled()) {
                val response = networkRepository.getRestaurants(
                    location.latitude,
                    location.longitude,
                    attributes,
                    sortedBy,

                )
                if (response.isSuccessful) {
                    response.body()?.let { result ->
                        val restaurants = result.businesses.mapToRestaurant()
                        emit(UIState.SUCCESS(restaurants))
                    } ?: throw NoResponseException()
                } else throw NetworkCallFailureException(response.errorBody().toString())
            } else throw InternetConnectionRequiredException()
        } catch (e: Exception) {
            emit(UIState.ERROR(e))
        }
    }.flowOn(ioDispatcher)

}