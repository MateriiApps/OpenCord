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

    // --------------- Updates ---------------
    @Query("UPDATE accounts SET cookies = :cookies WHERE token = :token")
    fun setCookies(token: String, cookies: String)

    // --------------- Queries ---------------
    @Query("SELECT * FROM accounts WHERE token = :token LIMIT 1")
    fun getAccount(token: String): EntityAccount?

    @Query("SELECT cookies FROM accounts WHERE token = :token LIMIT 1")
    fun getCookies(token: String): String?
}
