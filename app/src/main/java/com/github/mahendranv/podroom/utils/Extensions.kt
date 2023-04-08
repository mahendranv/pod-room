package com.github.mahendranv.podroom.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

inline fun ViewModel.ioOperation(crossinline block: suspend () -> Unit) =
    viewModelScope.launch(Dispatchers.IO) {
        block()
    }