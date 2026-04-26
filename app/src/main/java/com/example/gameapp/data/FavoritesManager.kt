package com.example.gameapp.data

import android.content.Context
import android.content.SharedPreferences
import com.example.gameapp.data.model.GameDeal
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * FavoritesManager - Handles persistence of favorite games
 * Uses SharedPreferences to store favorite games as JSON
 * 
 * Provides methods to:
 * - Save a game to favorites
 * - Remove a game from favorites
 * - Check if a game is favorited
 * - Get all favorite games
 * - Clear all favorites
 */
class FavoritesManager(context: Context) {
    
    private val sharedPreferences: SharedPreferences = 
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    
    private val gson = Gson()
    
    companion object {
        private const val PREFS_NAME = "game_deals_favorites"
        private const val FAVORITES_KEY = "favorites"
    }

    /**
     * Add a game to favorites
     * Serializes the GameDeal object to JSON and stores it
     * 
     * @param game The GameDeal to add to favorites
     */
    fun addToFavorites(game: GameDeal) {
        val favorites = getAllFavorites().toMutableList()
        
        // Check if already in favorites (by dealId)
        if (!favorites.any { it.dealId == game.dealId }) {
            favorites.add(game)
            saveFavorites(favorites)
        }
    }

    /**
     * Remove a game from favorites
     * 
     * @param dealId The dealId of the game to remove
     */
    fun removeFromFavorites(dealId: String) {
        val favorites = getAllFavorites().toMutableList()
        favorites.removeAll { it.dealId == dealId }
        saveFavorites(favorites)
    }

    /**
     * Check if a game is in favorites
     * 
     * @param dealId The dealId to check
     * @return true if game is in favorites, false otherwise
     */
    fun isFavorite(dealId: String): Boolean {
        return getAllFavorites().any { it.dealId == dealId }
    }

    /**
     * Get all favorite games
     * Deserializes JSON from SharedPreferences to GameDeal objects
     * 
     * @return List of favorite GameDeal objects
     */
    fun getAllFavorites(): List<GameDeal> {
        val json = sharedPreferences.getString(FAVORITES_KEY, "[]") ?: "[]"
        
        return try {
            val type = object : TypeToken<List<GameDeal>>() {}.type
            gson.fromJson(json, type) ?: emptyList()
        } catch (e: Exception) {
            // Handle parsing error
            emptyList()
        }
    }

    /**
     * Get count of favorite games
     * 
     * @return Number of games in favorites
     */
    fun getFavoritesCount(): Int {
        return getAllFavorites().size
    }

    /**
     * Clear all favorites
     */
    fun clearAllFavorites() {
        saveFavorites(emptyList())
    }

    /**
     * Private helper method to save favorites to SharedPreferences
     * 
     * @param favorites List of GameDeal objects to save
     */
    private fun saveFavorites(favorites: List<GameDeal>) {
        val json = gson.toJson(favorites)
        sharedPreferences.edit().apply {
            putString(FAVORITES_KEY, json)
            apply()
        }
    }
}
