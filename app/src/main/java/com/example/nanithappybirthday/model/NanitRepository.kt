package com.example.nanithappybirthday.model

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class NanitRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val dataStore: DataStore<Preferences> = context.dataStore
    private val gson = Gson()

    private val ipKey = stringPreferencesKey("ip_address")
    private val lastBirthdayData = stringPreferencesKey("birthday_data")

    suspend fun saveIpAddress(ip: String) {
        dataStore.edit { preferences ->
            preferences[ipKey] = ip
        }
    }

    suspend fun getIpAddress(): String {
        return dataStore.data.map { preferences ->
            preferences[ipKey] ?: ""
        }.first()
    }

    suspend fun saveLastBirthdayData(result: BirthdayData) {
        dataStore.edit { preferences ->
            preferences[lastBirthdayData] = gson.toJson(result)
        }
    }

    suspend fun getLastBirthdayData(): BirthdayData? {
        val lastBirthdayDataJson = dataStore.data.map { preferences ->
            preferences[lastBirthdayData]
        }.first()

        return try {
            gson.fromJson(lastBirthdayDataJson, BirthdayData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun makeHttpRequest(): Result<BirthdayData> = withContext(Dispatchers.IO) {
        try {
            val ip = getIpAddress()
            if (ip.isEmpty()) {
                return@withContext Result.failure(Exception("IP address not set"))
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()

            val requestBody = "HappyBirthday".toRequestBody("text/plain".toMediaType())

            val request = Request.Builder()
                .url("http://$ip:8080/nanit")
                .post(requestBody)
                .build()

            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string()
                    val birthdayData = gson.fromJson(body, BirthdayData::class.java)
                    Result.success(birthdayData)
                } else {
                    Result.failure(Exception("HTTP Error: ${response.code} - ${response.body?.string()}"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }


}