package com.idfc_poc
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @Headers( "Content-Type: application/json" )
    @POST("api/device/upi/list-keys") //endpoint
    suspend fun listKeyAPI(@Body req: ListKeyRequest): Response<ResponseModel?>?

    companion object {
        var apiService: ApiService? = null
        fun getInstance(): ApiService {

            val httpBuilder = OkHttpClient.Builder().build()
            val client = OkHttpClient().newBuilder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
                })
                .build()

            if (apiService == null) {
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://3788fixu2h.execute-api.ap-south-1.amazonaws.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
                apiService = retrofit.create(ApiService::class.java)
            }
            return apiService!!
        }
    }
}