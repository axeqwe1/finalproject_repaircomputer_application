import android.app.Application
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.AndroidViewModel
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class SessionManager(application: Application):AndroidViewModel(application) {
    private val dataStore = application.dataStore

    companion object {
        val SESSION_KEY = stringPreferencesKey("session_key")
    }

    suspend fun saveSessionToken(sessionToken: String) {
        dataStore.edit { settings ->
            settings[SESSION_KEY] = sessionToken
        }
    }

    val sessionKey: Flow<String?> = dataStore.data
        .map { preferences ->
            preferences[SESSION_KEY]
        }

    suspend fun clearSession() {
        dataStore.edit { settings ->
            settings.remove(SESSION_KEY)
        }
    }
}