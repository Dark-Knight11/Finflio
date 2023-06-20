package com.finflio.core.data.repository

import android.util.Log
import com.finflio.core.data.network.resource.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

open class BaseRepo {

    protected suspend fun <T> makeRequest(
        networkCall: suspend () -> Resource<T>
    ) = flow<Resource<T>> {
        emit(Resource.loading())
        val response = networkCall.invoke()
        when (response.status) {
            Resource.Status.SUCCESS -> {
                if (response.data != null) {
                    emit(Resource.success(response.data))
                } else {
                    emit(Resource.error(response.message.toString()))
                }
            }

            Resource.Status.ERROR -> {
                emit(Resource.error(response.message.toString()))
            }

            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    protected fun <A> makeRequestAndSave(
        databaseQuery: suspend () -> Unit,
        networkCall: suspend () -> Resource<A>,
        saveCallResult: suspend (A) -> Unit
    ) = flow<Resource<A>> {
        emit(Resource.loading())
        val response = networkCall.invoke()
        when (response.status) {
            Resource.Status.SUCCESS -> {
                databaseQuery.invoke()
                Log.d("BaseRepo", response.data.toString())
                response.data?.let {
                    Log.d("BaseRepo", "makeRequestAndSave: $it")
                    saveCallResult.invoke(it)
                }
                if (response.data != null) {
                    emit(Resource.success(response.data))
                } else {
                    emit(Resource.error(response.message.toString()))
                }
            }

            Resource.Status.ERROR -> {
                emit(Resource.error(response.message ?: "Something went wrong"))
            }

            else -> {}
        }
    }.flowOn(Dispatchers.IO)

    protected suspend fun <T> requestAndSave(
        networkCall: suspend () -> Resource<T>,
        saveCallResult: suspend (T) -> Unit
    ) = flow<Resource<T>> {
        emit(Resource.loading())

        val response = networkCall.invoke()
        when (response.status) {
            Resource.Status.SUCCESS -> {
                response.data?.let { saveCallResult.invoke(it) }
                if (response.data != null) {
                    emit(Resource.success(response.data))
                } else {
                    emit(Resource.error(response.message.toString()))
                }
            }

            Resource.Status.ERROR -> {
                emit(Resource.error(response.message.toString()))
            }

            else -> {}
        }
    }.flowOn(Dispatchers.IO)
}