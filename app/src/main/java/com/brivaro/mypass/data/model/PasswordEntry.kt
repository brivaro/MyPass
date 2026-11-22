package com.brivaro.mypass.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "password_entry")
data class PasswordEntry(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val label: String,
    val password: String,
    val createdAt: Long = System.currentTimeMillis()
) 