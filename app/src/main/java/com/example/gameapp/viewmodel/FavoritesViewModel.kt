package com.example.gameapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.gameapp.data.GameDealsRepository
import com.example.gameapp.data.model.GameDeal

/**
 * ViewModel for FavoritesActivity
 * Handles displaying and managing favorite games
 */
class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = GameDealsRepository(application)
    
    // LiveData for favorites list
    private val _favorites = MutableLiveData<List<GameDeal>>()
    val favorites: LiveData<List<GameDeal>> = _favorites
    
    // LiveData for empty state
    private val _isEmpty = MutableLiveData<Boolean>(true)
    val isEmpty: LiveData<Boolean> = _isEmpty
    
    // LiveData for action messages
    private val _actionMessage = MutableLiveData<String>()
    val actionMessage: LiveData<String> = _actionMessage

    /**
     * Load all favorite games
     */
    fun loadFavorites() {
        val favorites = repository.getAllFavorites()
        _favorites.value = favorites
        _isEmpty.value = favorites.isEmpty()
    }

    /**
     * Remove a game from favorites
     */
    fun removeFavorite(game: GameDeal) {
        repository.removeFromFavorites(game.dealId)
        _actionMessage.value = "Removed from favorites"
        
        // Reload favorites list
        loadFavorites()
    }

    /**
     * Get count of favorite games
     */
    fun getFavoritesCount(): Int {
        return repository.getFavoritesCount()
    }
}
