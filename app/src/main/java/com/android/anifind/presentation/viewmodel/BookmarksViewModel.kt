package com.android.anifind.presentation.viewmodel

import com.android.anifind.data.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BookmarksViewModel @Inject constructor(
    private val repository: Repository
) : BaseViewModel(repository) {

    val favoriteAnimes = repository.getFavoriteAnimes()
    val completedAnimes = repository.getCompletedAnimes()
    val droppedAnimes = repository.getDroppedAnimes()
    val holdAnimes = repository.getHoldAnimes()
    val plannedAnimes = repository.getPlannedAnimes()
    val watchingAnimes = repository.getWatchingAnimes()
}