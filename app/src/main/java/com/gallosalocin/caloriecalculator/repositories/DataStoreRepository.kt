package com.gallosalocin.caloriecalculator.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.createDataStore
import com.gallosalocin.caloriecalculator.utils.Constants.DATA_STORE_NAME
import com.gallosalocin.caloriecalculator.utils.Constants.KEY_FIRST_TIME_TOGGLE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException

class DataStoreRepository(context: Context) {

    private object PreferenceKeys {
        val isFirstAppOpen = booleanPreferencesKey(KEY_FIRST_TIME_TOGGLE)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(name = DATA_STORE_NAME)

    suspend fun saveToDataStore(value: Boolean) {
        dataStore.edit {
            it[PreferenceKeys.isFirstAppOpen] = value
        }
    }

    val readFromDataStore: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException){
                Timber.d("DataStore : ${exception.message}")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val isFirstTimeOpen = preference[PreferenceKeys.isFirstAppOpen] ?: true
            isFirstTimeOpen
        }

}