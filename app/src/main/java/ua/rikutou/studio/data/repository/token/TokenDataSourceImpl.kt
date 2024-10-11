package ua.rikutou.studio.data.repository.token

import android.content.SharedPreferences
import androidx.core.content.edit

class TokenDataSourceImpl (
    private val sharedPreferences: SharedPreferences
) : TokenDataSource {
    override val token
        get() = _token

    init {
        _token = sharedPreferences.getString(TOKEN_KEY, "").takeIf { it?.isNotBlank() == true }
    }

    override suspend fun setToken(newToken: String) {
        _token = newToken
        sharedPreferences.edit {
            putString(TOKEN_KEY, newToken)
        }
    }

    companion object {
        private const val TOKEN_KEY = "token"
        private var _token: String? = null
    }
}