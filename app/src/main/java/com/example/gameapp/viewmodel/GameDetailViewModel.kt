package com.example.gameapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gameapp.data.GameDealsRepository
import com.example.gameapp.data.model.GameDeal

/**
 * ViewModel for GameDetailActivity
 * Handles game details and favorites management
 */
class GameDetailViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = GameDealsRepository(application)
    
    // LiveData for current game
    private val _currentGame = MutableLiveData<GameDeal>()
    val currentGame: LiveData<GameDeal> = _currentGame
    
    // LiveData for favorite status
    private val _isFavorited = MutableLiveData<Boolean>(false)
    val isFavorited: LiveData<Boolean> = _isFavorited
    
    // LiveData for action messages
    private val _actionMessage = MutableLiveData<String>()
    val actionMessage: LiveData<String> = _actionMessage

    /**
     * Set the current game and check if it's favorited
     */
    fun setCurrentGame(game: GameDeal) {
        _currentGame.value = game
        checkIfFavorited(game.dealId)
    }

    /**
     * Check if game is favorited
     */
    private fun checkIfFavorited(dealId: String) {
        _isFavorited.value = repository.isFavorite(dealId)
    }

    /**
     * Toggle favorite status of current game
     */
    fun toggleFavorite() {
        val game = _currentGame.value ?: return
        
        if (_isFavorited.value == true) {
            repository.removeFromFavorites(game.dealId)
            _isFavorited.value = false
            _actionMessage.value = "Removed from favorites"
        } else {
            repository.addToFavorites(game)
            _isFavorited.value = true
            _actionMessage.value = "Added to favorites"
        }
    }

    /**
     * Add game to favorites
     */
    fun addToFavorites(game: GameDeal) {
        repository.addToFavorites(game)
        _isFavorited.value = true
        _actionMessage.value = "Added to favorites"
    }

    /**
     * Remove game from favorites
     */
    fun removeFromFavorites(dealId: String) {
        repository.removeFromFavorites(dealId)
        _isFavorited.value = false
        _actionMessage.value = "Removed from favorites"
    }
}
