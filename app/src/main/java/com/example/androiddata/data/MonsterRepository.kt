package com.example.androiddata.data

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import com.example.androiddata.WEB_SERVICE_URL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MonsterRepository(val app: Application) {

    val monsterData = MutableLiveData<List<Monster>>()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            callWebService()
        }
    }

    @WorkerThread
    suspend fun callWebService() {
        if (networkAvailable()) {
            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

            val retrofitBuilder = Retrofit.Builder()
            retrofitBuilder.baseUrl("https://774906.youcanlearnit.net")
            retrofitBuilder.addConverterFactory(MoshiConverterFactory.create(moshi))
            val retrofit = retrofitBuilder.build()

            val service = retrofit.create(MonsterService::class.java)
            val serviceDate = service.getMonsterData().body() ?: emptyList()
            monsterData.postValue(serviceDate)
        }
    }

    @Suppress("DEPRECATION")
    private fun networkAvailable(): Boolean {
        val connectivityManager =
            app.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo

        return networkInfo?.isConnectedOrConnecting ?: false
    }
}