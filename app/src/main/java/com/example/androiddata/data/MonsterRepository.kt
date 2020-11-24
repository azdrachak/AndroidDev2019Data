package com.example.androiddata.data

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.WorkerThread
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import com.example.androiddata.LOG_TAG
import com.example.androiddata.WEB_SERVICE_URL
import com.example.androiddata.utilities.FileHelper
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class MonsterRepository(val app: Application) {

    val monsterData = MutableLiveData<List<Monster>>()
    val monsterDao = MonsterDatabase.getDatabase(app).monsterDao()

    init {
//        val data = readDataFormCache()
//        if (data.isEmpty()) {
//            refreshDataFromWeb()
//        } else {
//            monsterData.value = data
//            Log.i(LOG_TAG, "Using local data")
//        }
        CoroutineScope(Dispatchers.IO).launch {
            val data = monsterDao.getAll()
            if (data.isEmpty()) {
                callWebService()
            } else {
                monsterData.postValue(data)
                withContext(Dispatchers.Main) {
                    Toast.makeText(app, "Data from DB", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    @WorkerThread
    suspend fun callWebService() {
        if (networkAvailable()) {
            withContext(Dispatchers.Main) {
                Toast.makeText(app, "Remote data", Toast.LENGTH_SHORT).show()
            }
            val retrofit = Retrofit.Builder()
                .baseUrl(WEB_SERVICE_URL)
                .addConverterFactory(MoshiConverterFactory.create())
                .build()
            val service = retrofit.create(MonsterService::class.java)
            val serviceData = service.getMonsterData().body() ?: emptyList()
            monsterData.postValue(serviceData)
            Log.i(LOG_TAG, "Calling web service")
            saveDataToCache(serviceData)

            monsterDao.deleteAll()
            monsterDao.insertMonsters(serviceData)
        }
    }

    @Suppress("DEPRECATION")
    private fun networkAvailable(): Boolean {
        val connectivityManager = app.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnectedOrConnecting ?: false
    }

    fun refreshDataFromWeb() {
        CoroutineScope(Dispatchers.IO).launch {
            callWebService()
        }
    }

    private fun saveDataToCache(monsterData: List<Monster>) {
        if (ContextCompat.checkSelfPermission(
                app,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val adapter: JsonAdapter<List<Monster>> = moshiJsonAdapter()
            val json = adapter.toJson(monsterData)

            FileHelper.saveTextToFile(app, json)
        }
    }

    private fun readDataFormCache(): List<Monster> {
        FileHelper.readTextFile(app).let {
            val adapter = moshiJsonAdapter()
            return if (it == null) emptyList() else adapter.fromJson(it) ?: emptyList()
        }
    }

    private fun moshiJsonAdapter(): JsonAdapter<List<Monster>> {
        val moshi = Moshi.Builder().build()
        val listType = Types.newParameterizedType(List::class.java, Monster::class.java)
        return moshi.adapter(listType)
    }
}