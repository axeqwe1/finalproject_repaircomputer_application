package com.example.repaircomputerapplication_finalproject.viewModel

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore by preferencesDataStore(name = "settings")