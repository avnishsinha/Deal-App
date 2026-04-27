package com.example.gameapp.data

import android.content.Context
import com.example.gameapp.data.api.RetrofitInstance
import com.example.gameapp.data.model.GameDeal

/**
 * Repository for Game Deals data
 * Centralizes data access from both API and local storage
 * Single source of truth for all data operations
 */
class GameDealsRepository(private val context: Context) {
    
    private val favoritesManager = FavoritesManager(context)
    private val apiService = RetrofitInstance.apiService

    /**
     * Fetch deals from API
     */
    suspend fun getDeals(
        pageNumber: Int = 0,
        pageSize: Int = 20,
        sortBy: String = "Deal Rating",
        desc: Int = 1
    ): List<GameDeal> = try {
        apiService.getDeals(
            pageNumber = pageNumber,
            pageSize = pageSize,
            sortBy = sortBy,
            desc = desc
        )
    } catch (e: Exception) {
        throw Exception("Failed to load deals: ${e.message}")
    }

    /**
     * Search for games by title
     */
    suspend fun searchDeals(
        title: String,
        limit: Int = 60,
        sortBy: String = "Deal Rating",
        desc: Int = 1
    ): List<GameDeal> = try {
        apiService.searchDeals(
            title = title,
            limit = limit,
            sortBy = sortBy,
            desc = desc
        )
    } catch (e: Exception) {
        throw Exception("Failed to search games: ${e.message}")
    }

    /**
     * Add game to favorites
     */
    fun addToFavorites(game: GameDeal) {
        favoritesManager.addToFavorites(game)
    }

    /**
     * Remove game from favorites
     */
    fun removeFromFavorites(dealId: String) {
        favoritesManager.removeFromFavorites(dealId)
    }

    /**
     * Get all favorite games
     */
    fun getAllFavorites(): List<GameDeal> {
        return favoritesManager.getAllFavorites()
    }

    /**
     * Check if game is favorited
     */
    fun isFavorite(dealId: String): Boolean {
        return favoritesManager.isFavorite(dealId)
    }

    /**
     * Get count of favorite games
     */
    fun getFavoritesCount(): Int {
        return favoritesManager.getFavoritesCount()
    }
}
