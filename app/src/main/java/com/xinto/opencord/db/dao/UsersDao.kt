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
    suspend fun insertUsers(users: List<EntityUser>)

    @Insert(
        onConflict = OnConflictStrategy.REPLACE,
    )
    suspend fun insertUser(user: EntityUser)

    // --------------- Deletes ---------------
    @Query("DELETE FROM USERS WHERE id NOT IN(SELECT author_id FROM MESSAGES)")
    suspend fun deleteUnusedUsers()

    // --------------- Queries ---------------
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    suspend fun getUser(userId: Long): EntityUser?
}
