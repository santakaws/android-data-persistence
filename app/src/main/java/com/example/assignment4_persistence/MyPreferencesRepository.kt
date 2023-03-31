// Name: Brennon Hahs
// CWID: 887487148
// Email: brennonhahs@csu.fullerton.edu

package com.example.assignment4_persistence

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.Flow


class MyPreferencesRepository private constructor(private val dataStore: DataStore<Preferences>){
    val redVal: Flow<String> = this.dataStore.data.map { prefs ->
        prefs[RVAL_KEY] ?: "1.0"
    }.distinctUntilChanged()
    val greenVal: Flow<String> = this.dataStore.data.map { prefs ->
        prefs[GVAL_KEY] ?: "1.0"
    }.distinctUntilChanged()
    val blueVal: Flow<String> = this.dataStore.data.map { prefs ->
        prefs[BVAL_KEY] ?: "1.0"
    }.distinctUntilChanged()
    val redBut: Flow<Boolean> = this.dataStore.data.map { prefs ->
        prefs[RBUT_KEY] ?: true
    }.distinctUntilChanged()
    val greenBut: Flow<Boolean> = this.dataStore.data.map { prefs ->
        prefs[GBUT_KEY] ?: true
    }.distinctUntilChanged()
    val blueBut: Flow<Boolean> = this.dataStore.data.map { prefs ->
        prefs[BBUT_KEY] ?: true
    }.distinctUntilChanged()

    private suspend fun  saveStringValue(key: Preferences.Key<String>, value: String) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    private suspend fun  saveBooleanValue(key: Preferences.Key<Boolean>, value: Boolean) {
        this.dataStore.edit { prefs ->
            prefs[key] = value
        }
    }

    suspend fun saveInput(value: String, index: Int) {
        val key: Preferences.Key<String> = when (index) {
            1 -> RVAL_KEY
            2 -> GVAL_KEY
            3 -> BVAL_KEY
            else -> {
                throw NoSuchFieldException("Invalid input index: $index")
            }
        }
        this.saveStringValue(key, value)
    }

    suspend fun saveBooleanInput(value: Boolean, index: Int) {
        val key: Preferences.Key<Boolean> = when (index) {
            4 -> RBUT_KEY
            5 -> GBUT_KEY
            6 -> BBUT_KEY
            else -> {
                throw NoSuchFieldException("Invalid input index: $index")
            }
        }
        this.saveBooleanValue(key, value)
    }

    companion object {
        private const val PREFERENCES_DATA_FILE_NAME = "settings"
        private val RVAL_KEY = stringPreferencesKey("redVal")
        private val GVAL_KEY = stringPreferencesKey("greenVal")
        private val BVAL_KEY = stringPreferencesKey("blueVal")
        private val RBUT_KEY = booleanPreferencesKey("redBut")
        private val GBUT_KEY = booleanPreferencesKey("greenBut")
        private val BBUT_KEY = booleanPreferencesKey("blueBut")

        private var INSTANCE: MyPreferencesRepository? = null

        fun initialize(context: Context) {
            if(INSTANCE == null) {
                val dataSore = PreferenceDataStoreFactory.create {
                    context.preferencesDataStoreFile(PREFERENCES_DATA_FILE_NAME)
                }
                INSTANCE = MyPreferencesRepository(dataSore)
            }
        }
        fun get(): MyPreferencesRepository {
            return INSTANCE ?: throw IllegalStateException("MyPreferencesRepository has not been initialized yet")
        }
    }
}