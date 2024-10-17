package ua.rikutou.studio.data.local

import android.content.Context
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.user.UserDataSource

class DbDataSourceImpl(
    private val context: Context,
    private val userRepository: UserDataSource
) : DbDataSource {
    override val dbFlow: Flow<AppDb>
        get() = _dbFlow.asStateFlow().filterNotNull()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.userFlow.distinctUntilChanged { old, new ->  old?.userId == new?.userId}.collect { user ->
                _dbFlow.emit(
                    if (user == null) {
                        null
                    } else {
                        createDb(user.userId.toString())
                    }
                )
            }
        }
    }

    private fun createDb(dbName: String): AppDb =
        Room.databaseBuilder(
            context,
            AppDb::class.java,
            name = dbName
        )
            .fallbackToDestructiveMigration()
            .build()

    companion object {
        private val _dbFlow: MutableStateFlow<AppDb?> = MutableStateFlow(null)
    }
}