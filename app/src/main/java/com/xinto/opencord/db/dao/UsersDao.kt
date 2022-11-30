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
    @Query("DELETE FROM USERS WHERE id NOT IN(SELECT author_id FROM MESSAGES)")
    fun deleteUnusedUsers()

    // --------------- Queries ---------------
    @Query("SELECT * FROM users WHERE id = :userId LIMIT 1")
    fun getUser(userId: Long): EntityUser?
}
