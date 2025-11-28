package com.example.nanithappybirthday.model

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeout
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

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

    suspend fun makeWebSocketRequest(): Result<BirthdayData> = withContext(Dispatchers.IO) {
        try {
            val ip = getIpAddress()
            if (ip.isEmpty()) {
                return@withContext Result.failure(Exception("IP address not set"))
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build()

            val request = Request.Builder()
                .url("ws://$ip:8080/nanit")
                .build()

            val deferred = CompletableDeferred<Result<BirthdayData>>()

            client.newWebSocket(request, object : okhttp3.WebSocketListener() {
                override fun onOpen(webSocket: okhttp3.WebSocket, response: okhttp3.Response) {
                    Log.d("NanitRepo", "WebSocket connected")
                    webSocket.send("HappyBirthday")
                }

                override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
                    Log.d("NanitRepo", "Received: $text")
                    try {
                        val birthdayData = gson.fromJson(text, BirthdayData::class.java)
                        deferred.complete(Result.success(birthdayData))
                    } catch (e: Exception) {
                        deferred.complete(Result.failure(e))
                    }
                    webSocket.close(1000, "Done")
                }

                override fun onFailure(webSocket: okhttp3.WebSocket, t: Throwable, response: okhttp3.Response?) {
                    Log.d("NanitRepo", "WebSocket failed: ${t.message}")
                    deferred.complete(Result.failure(t))
                }

                override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
                    Log.d("NanitRepo", "WebSocket closed: $code - $reason")
                }
            })

            withTimeout(10.seconds.inWholeMilliseconds) {
                deferred.await()
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

}