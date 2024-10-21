package ua.rikutou.studio.data.local

import kotlinx.coroutines.flow.Flow

interface DbDataSource {
    val dbFlow: Flow<AppDb>
    val db: AppDb
}