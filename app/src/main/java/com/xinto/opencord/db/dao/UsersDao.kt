package com.xinto.opencord.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.xinto.opencord.db.entity.user.EntityUser

@Dao
interface UsersDao {
    // --------------- Inserts ---------------
    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
        entity = EntityUser::class,
    )
    fun insertUsers(users: List<EntityUser>)

    // --------------- Deletes ---------------

    // --------------- Queries ---------------
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUser(userId: Long): EntityUser?
}
