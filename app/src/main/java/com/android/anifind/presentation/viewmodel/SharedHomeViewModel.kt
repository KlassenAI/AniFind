package com.android.anifind.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.anifind.data.repository.Repository
import com.android.anifind.domain.model.Anime
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SharedHomeViewModel @Inject constructor(repository: Repository) : SharedViewModel(repository) {

    private val _anime = MutableLiveData<Anime>()
    val anime: LiveData<Anime> get() = _anime
}