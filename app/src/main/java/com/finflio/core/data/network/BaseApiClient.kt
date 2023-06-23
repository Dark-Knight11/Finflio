package com.finflio.core.data.network

import android.util.Log
import com.finflio.BuildConfig
import com.finflio.core.data.model.CommonErrorResponse
import com.finflio.core.data.network.resource.Resource
import com.finflio.core.data.util.UtilityMethods
import kotlinx.serialization.json.Json
import org.json.JSONObject
import retrofit2.Response

open class BaseApiClient constructor(
    private val utilityMethods: UtilityMethods
) {
    protected suspend fun <T> getResult(request: suspend () -> Response<T>): Resource<T> {
        try {
            if (!utilityMethods.checkNet()) {
                return Resource.error("Check your internet connection!")
            }
            val response = request()
            val body = response.body()
            return if (response.isSuccessful) {
                if (body != null) {
                    Resource.success(body)
                } else {
                    Resource.error("Server response error")
                }
            } else {
                val jObjError: String = JSONObject(response.errorBody()?.string() ?: "").toString()
                val errorString = Json.decodeFromString(
                    CommonErrorResponse.serializer(),
                    jObjError
                )
                Log.d("BaseApiClient", errorString.toString())
                Resource.error(errorString.message)
            }
        } catch (e: Exception) {
            val errorMessage = e.message ?: e.toString()

            return if (BuildConfig.DEBUG) {
                Resource.error("Network call failed with message $errorMessage")
            } else {
                Resource.error("Check your internet connection!")
            }
        }
    }

    protected suspend fun <T> makePostRequest(request: suspend () -> Response<T>): Resource<T> {
        try {
            val response = request()
            val body = response.body()
            return if (response.isSuccessful) {
                if (body != null) {
                    Resource.success(body)
                } else {
                    Resource.error("Server response error")
                }
            } else {
                val jObjError: String = JSONObject(response.errorBody()?.string() ?: "").toString()
                val errorString = Json.decodeFromString(
                    CommonErrorResponse.serializer(),
                    jObjError
                )
                Log.d("BaseApiClient", errorString.toString())
                Resource.error(errorString.message)
            }
        } catch (e: Exception) {
            val errorMessage = e.toString()
            return if (BuildConfig.DEBUG) {
                Resource.error("Network call failed with message $errorMessage")
            } else {
                Resource.error("Check your internet connection!")
            }
        }
    }
}