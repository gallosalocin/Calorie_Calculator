package com.gallosalocin.caloriecalculator.data.repositories

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.createDataStore
import com.gallosalocin.caloriecalculator.utils.Constants.DATA_STORE_NAME
import com.gallosalocin.caloriecalculator.utils.Constants.PREFERENCES_BACK_ONLINE
import com.gallosalocin.caloriecalculator.utils.Constants.PREFERENCES_FIRST_TIME_TOGGLE
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject

@ActivityRetainedScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val isFirstAppOpen = booleanPreferencesKey(PREFERENCES_FIRST_TIME_TOGGLE)
        val backOnline = booleanPreferencesKey(PREFERENCES_BACK_ONLINE)
    }

    private val dataStore: DataStore<Preferences> = context.createDataStore(name = DATA_STORE_NAME)

    suspend fun saveFirstTimeToggle(firstTimeToggle: Boolean) {
        dataStore.edit {
            it[PreferenceKeys.isFirstAppOpen] = firstTimeToggle
        }
    }

    suspend fun saveBackOnline(backOnline: Boolean) {
        dataStore.edit {
            it[PreferenceKeys.backOnline] = backOnline
        }
    }

    val readFirstTimeToggle: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
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

    val readBackOnline: Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.d("DataStore : ${exception.message}")
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preference ->
            val isBackOnline = preference[PreferenceKeys.backOnline] ?: false
            isBackOnline
        }

}