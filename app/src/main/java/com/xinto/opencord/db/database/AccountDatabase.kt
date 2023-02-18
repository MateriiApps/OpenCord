package com.xinto.opencord.db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.xinto.opencord.db.dao.AccountsDao
import com.xinto.opencord.db.entity.EntityAccount

@Database(
    version = 1,
    entities = [
        EntityAccount::class,
    ],
    exportSchema = false,
)
abstract class AccountDatabase : RoomDatabase() {
    abstract fun accounts(): AccountsDao
}
