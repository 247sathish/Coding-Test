package com.app.wipro.utils

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class EncryptedSharedPreferences(private val context: Context) {
    private val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    val sharedPreferences = EncryptedSharedPreferences.create(
        "PreferencesFilename",
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    fun writeMessage(key: String, message: String) {
        sharedPreferences.edit()
            .putString(key, message)
            .apply()
    }

    fun readMessage(key: String): String? {
        return sharedPreferences.getString(key, "")
    }

    fun <T> put(`object`: T, key: String) {
        val jsonString = Gson().toJson(`object`)
        sharedPreferences.edit().putString(key, jsonString).apply()
    }

    inline fun <reified T> get(key: String): T {
        return Gson().fromJson<T>(sharedPreferences.getString(key, null), object: TypeToken<T>(){}.type)
    }
}