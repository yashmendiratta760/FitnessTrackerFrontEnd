package com.yash.fitnesstracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yash.fitnesstracker.service.DataStoreManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(): ViewModel()
{
    private val _uiState = MutableStateFlow(UserDataState())
    val uiState: StateFlow<UserDataState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            DataStoreManager.getUserData().collect {userData->
                _uiState.value = userData
            }
        }
    }
}