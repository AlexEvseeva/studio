package ua.rikutou.studio.data.local

import android.content.Context
import android.util.Log
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import ua.rikutou.studio.data.datasource.profile.ProfileDataSource

class DbDataSourceImpl(
    private val context: Context,
    private val userRepository: ProfileDataSource
) : DbDataSource {
    val TAG by lazy { DbDataSourceImpl::class.simpleName }
    override val db: AppDb
        get() = checkNotNull(_db) {"Db must be initializes"}
    override val dbFlow: Flow<AppDb>
        get() = _dbFlow.asStateFlow().filterNotNull()

    init {
        CoroutineScope(Dispatchers.IO).launch {
            userRepository.userFlow.distinctUntilChanged { old, new ->  old?.userId == new?.userId}.collect { user ->
                if(user == null) {
                    synchronized(this) {
                        _db = null
                    }
                    _dbFlow.emit(null)
                } else {
                    synchronized(this) {
                        _db = createDb(user.userId.toString())
                    }
                    _dbFlow.value = _db
                }
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
            .also {
                Log.d(TAG, "createDb: ${dbName}")
            }

    companion object {
        private val _dbFlow: MutableStateFlow<AppDb?> = MutableStateFlow(null)
        private var _db: AppDb? = null
    }
}