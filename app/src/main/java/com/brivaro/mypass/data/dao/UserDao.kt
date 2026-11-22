package com.brivaro.mypass.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.brivaro.mypass.data.model.User

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: User)

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): User?
} 