package com.example.gameapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.gameapp.data.GameDealsRepository
import com.example.gameapp.data.model.GameDeal
import kotlinx.coroutines.launch

/**
 * ViewModel for MainActivity
 * Handles fetching and managing game deals data
 * Provides LiveData for UI observation
 */
class GameDealsViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository = GameDealsRepository(application)
    
    // LiveData for game deals list
    private val _deals = MutableLiveData<List<GameDeal>>()
    val deals: LiveData<List<GameDeal>> = _deals
    
    // LiveData for loading state
    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading
    
    // LiveData for error messages
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage
    
    // LiveData for empty state
    private val _isEmpty = MutableLiveData<Boolean>(false)
    val isEmpty: LiveData<Boolean> = _isEmpty

    private var selectedMinDiscount = 0

    /**
     * Load initial deals on app start
     */
    fun loadInitialDeals(minDiscount: Int = selectedMinDiscount) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = ""
                selectedMinDiscount = minDiscount.coerceIn(0, 100)
                
                val deals = repository.getDeals(
                    pageNumber = 0,
                    pageSize = 20,
                    sortBy = "Deal Rating",
                    desc = 1
                )
                
                val filteredDeals = filterDealsByDiscount(deals, selectedMinDiscount)
                _deals.value = filteredDeals
                _isEmpty.value = filteredDeals.isEmpty()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Unknown error occurred"
                _isEmpty.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Search for games by title
     */
    fun searchDeals(query: String, minDiscount: Int = selectedMinDiscount) {
        if (query.trim().isEmpty()) {
            _errorMessage.value = "Please enter a game title"
            return
        }
        
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = ""
                selectedMinDiscount = minDiscount.coerceIn(0, 100)
                
                val deals = repository.searchDeals(
                    title = query,
                    limit = 60,
                    sortBy = "Deal Rating",
                    desc = 1
                )
                
                val filteredDeals = filterDealsByDiscount(deals, selectedMinDiscount)
                _deals.value = filteredDeals
                _isEmpty.value = filteredDeals.isEmpty()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Search failed"
                _isEmpty.value = true
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun filterDealsByDiscount(deals: List<GameDeal>, minDiscount: Int): List<GameDeal> {
        if (minDiscount <= 0) return deals
        return deals.filter { it.savings >= minDiscount }
    }
}
