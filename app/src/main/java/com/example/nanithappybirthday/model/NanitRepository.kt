package com.example.nanithappybirthday.model

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class NanitRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {

    companion object {
        const val TAG = "NanitRepository"

        const val IMAGES_DIR_NAME = "baby_images"
        const val JPG_SUFFIX = ".jpg"

        const val PORT = 8080
        const val REQUEST_PATH = "/nanit"
        const val REQUEST_MESSAGE = "HappyBirthday"
        const val CONNECT_TIME_OUT_S: Long = 10
    }

    private val dataStore: DataStore<Preferences> = context.dataStore
    private val gson = Gson()

    private val ipKey = stringPreferencesKey("ip_address")
    private val lastBirthdayData = stringPreferencesKey("birthday_data")
    private val babyImagePath = stringPreferencesKey("baby_image_path")

    private val babyImageDir = File(context.filesDir, IMAGES_DIR_NAME)

    init {
        babyImageDir.apply { mkdirs() }
    }

    // region DataStore

    val birthdayDataFlow: Flow<BirthdayData?> = dataStore.data
        .map { preferences ->
            preferences[lastBirthdayData]
        }
        .map { json ->
            try {
                gson.fromJson(json, BirthdayData::class.java)
            } catch (e: Exception) {
                null
            }
        }

    val ipAddressFlow: Flow<String> = dataStore.data
        .map { preferences ->
            preferences[ipKey] ?: ""
        }

    val babyImagePathFlow: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[babyImagePath]
        }

    suspend fun saveIpAddress(ip: String) {
        dataStore.edit { preferences ->
            preferences[ipKey] = ip
        }
    }

    suspend fun saveLastBirthdayData(result: BirthdayData) {
        dataStore.edit { preferences ->
            preferences[lastBirthdayData] = gson.toJson(result)
        }
    }

    suspend fun saveBabyImagePath(imagePath: String?) {
        dataStore.edit { preferences ->
            if (imagePath == null) {
                preferences.remove(babyImagePath)
            } else {
                preferences[babyImagePath] = imagePath
            }
        }
    }

    // endregion

    // region Request

    suspend fun makeWebSocketRequest() {
        try {
            val ip = ipAddressFlow.firstOrNull()
            if (ip.isNullOrEmpty()) {
                Log.d(TAG, "IP address not set")
                return
            }

            val client = OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIME_OUT_S, TimeUnit.SECONDS)
                .build()

            val request = Request.Builder()
                .url("ws://$ip:$PORT/$REQUEST_PATH")
                .build()

            client.newWebSocket(request, object : okhttp3.WebSocketListener() {
                override fun onOpen(webSocket: okhttp3.WebSocket, response: okhttp3.Response) {
                    Log.d(TAG, "WebSocket connected")
                    webSocket.send(REQUEST_MESSAGE)
                }

                override fun onMessage(webSocket: okhttp3.WebSocket, text: String) {
                    Log.d(TAG, "Received: $text")
                    try {
                        CoroutineScope(Dispatchers.IO).launch {
                            val birthdayData = gson.fromJson(text, BirthdayData::class.java)

                            if (!birthdayData.equals(birthdayDataFlow.first())) {
                                removeAllBabyImages()
                                saveBabyImagePath(null)

                                saveLastBirthdayData(birthdayData)
                                Log.d(TAG, "Success: $birthdayData")
                            }
                        }

                    } catch (e: Exception) {
                        Log.d(TAG, "Failed: ${e.message}")
                    }
                    webSocket.close(1000, "Done")
                }

                override fun onFailure(
                    webSocket: okhttp3.WebSocket,
                    t: Throwable,
                    response: okhttp3.Response?
                ) {
                    Log.d(TAG, "WebSocket failed: ${t.message}")
                }

                override fun onClosed(webSocket: okhttp3.WebSocket, code: Int, reason: String) {
                    Log.d(TAG, "WebSocket closed: $code - $reason")
                }
            })
        } catch (e: Exception) {
            Log.d(TAG, "Failed: ${e.message}")
        }
    }

    // endregion

    // region Baby Photo

    suspend fun updateBabyImage(babyImageUri: Uri?) {
        if (babyImageUri != null) {
            removeAllBabyImages()
            val newImagePath = saveImageToInternalStorage(
                babyImageUri,
                "${System.currentTimeMillis()}$JPG_SUFFIX"
            )

            saveBabyImagePath(newImagePath)
        }
    }

    private fun removeAllBabyImages() {
        babyImageDir.listFiles()?.forEach { it.delete() }
    }

    private fun saveImageToInternalStorage(uri: Uri, fileName: String): String? {
        var resultPath: String? = null

        try {
            val inputStream = context.contentResolver.openInputStream(uri)

            inputStream?.use { stream ->
                val file = File(babyImageDir, fileName)
                val outputStream = FileOutputStream(file)

                outputStream.use { out ->
                    stream.copyTo(out)
                }

                resultPath = file.absolutePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return resultPath
    }

    // endregion

}