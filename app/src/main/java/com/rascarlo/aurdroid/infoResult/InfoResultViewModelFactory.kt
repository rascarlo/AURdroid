package com.rascarlo.aurdroid.infoResult

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class InfoResultViewModelFactory(private val name: String) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        @Suppress("unchecked_cast")
        if (modelClass.isAssignableFrom(InfoResultViewModel::class.java)) {
            return InfoResultViewModel(name = this.name) as T
        }
        throw IllegalArgumentException("Dude, that is NOT the view model class I was expecting")
    }
}