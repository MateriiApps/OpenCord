package com.xinto.opencord.db.dao

import androidx.room.*
import com.xinto.opencord.db.entity.EntityAccount

@Dao
interface AccountsDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityAccount::class,
    )
    fun insertAccount(account: EntityAccount)

    // --------------- Queries ---------------
    @Query("SELECT * FROM accounts WHERE token = :token LIMIT 1")
    fun getAccount(token: String): EntityAccount?
}
