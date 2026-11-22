package com.brivaro.mypass.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.brivaro.mypass.data.model.PasswordEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface PasswordDao {
    @Insert
    suspend fun insert(passwordEntry: PasswordEntry)

    @Update
    suspend fun update(passwordEntry: PasswordEntry)

    @Delete
    suspend fun delete(passwordEntry: PasswordEntry)

    @Query("SELECT * FROM password_entry ORDER BY createdAt DESC")
    fun getAllPasswords(): Flow<List<PasswordEntry>>
} 