package com.brivaro.mypass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.brivaro.mypass.data.AppDatabase
import com.brivaro.mypass.data.model.PasswordEntry
import com.brivaro.mypass.data.model.User
import com.brivaro.mypass.security.SecurityUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getDatabase(application)
    private val userDao = database.userDao()
    private val passwordDao = database.passwordDao()

    private val _isAuthenticated = MutableStateFlow(false)
    val isAuthenticated: StateFlow<Boolean> = _isAuthenticated

    private val _isFirstLaunch = MutableStateFlow(true)
    val isFirstLaunch: StateFlow<Boolean> = _isFirstLaunch

    private val _passwords = MutableStateFlow<List<PasswordEntry>>(emptyList())
    val passwords: StateFlow<List<PasswordEntry>> = _passwords

    init {
        viewModelScope.launch {
            try {
                _isFirstLaunch.value = userDao.getUser() == null
                passwordDao.getAllPasswords().collect {
                    _passwords.value = it
                }
            } catch (e: Exception) {
                // Log the error
                e.printStackTrace()
                _isFirstLaunch.value = true
            }
        }
    }

    fun createUser(username: String, password: String) {
        viewModelScope.launch {
            val salt = SecurityUtils.generateSalt()
            val passwordHash = SecurityUtils.hashPassword(password, salt)
            val user = User(username, passwordHash, salt)
            userDao.insert(user)
            _isFirstLaunch.value = false
            _isAuthenticated.value = true
        }
    }

    fun authenticate(username: String, password: String) {
        viewModelScope.launch {
            val user = userDao.getUser()
            if (user != null) {
                val passwordHash = SecurityUtils.hashPassword(password, user.salt)
                _isAuthenticated.value = username == user.username && passwordHash == user.passwordHash
            }
        }
    }

    fun addPassword(label: String, password: String) {
        viewModelScope.launch {
            val encryptedPassword = SecurityUtils.encrypt(password)
            val passwordEntry = PasswordEntry(
                label = label,
                password = encryptedPassword
            )
            passwordDao.insert(passwordEntry)
        }
    }

    fun onAuthenticationSuccess() {
        _isAuthenticated.value = true
    }

    fun updatePassword(id: Long, label: String, password: String) {
        viewModelScope.launch {
            val encryptedPassword = SecurityUtils.encrypt(password)
            val updatedEntry = PasswordEntry(
                id = id,
                label = label,
                password = encryptedPassword
            )
            passwordDao.update(updatedEntry)
        }
    }

    fun deletePassword(passwordEntry: PasswordEntry) {
        viewModelScope.launch {
            passwordDao.delete(passwordEntry)
        }
    }
} 