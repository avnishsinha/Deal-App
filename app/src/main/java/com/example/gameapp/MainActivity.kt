package com.example.gameapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameapp.data.api.RetrofitInstance
import com.example.gameapp.data.model.GameDeal
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.launch

/**
 * MainActivity - Main screen for displaying game deals
 * This app integrates with the CheapShark API to fetch and display video game deals
 * Users can search for games and view current pricing from various stores
 *
 * Features:
 * - Search for games by title
 * - Display game deals from multiple stores
 * - Show real-time prices, discounts, and savings
 * - Load game thumbnails using Glide
 * - Display loading states and empty states
 * - Error handling with user-friendly messages
 * - Click on deals to view details
 * - Save games to favorites
 */
class MainActivity : AppCompatActivity() {

    // UI Components
    private lateinit var searchEditText: TextInputEditText
    private lateinit var searchButton: MaterialButton
    private lateinit var favoritesButton: MaterialButton
    private lateinit var dealsRecyclerView: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var emptyStateLayout: LinearLayout

    // Adapter for displaying deals
    private lateinit var dealAdapter: GameDealAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        
        // Setup edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize UI components
        initializeViews()
        
        // Setup RecyclerView
        setupRecyclerView()
        
        // Setup search functionality
        setupSearchButton()

        // Load initial deals on app start
        loadInitialDeals()
    }

    /**
     * Initialize all view references from the layout
     */
    private fun initializeViews() {
        searchEditText = findViewById(R.id.searchEditText)
        searchButton = findViewById(R.id.searchButton)
        dealsRecyclerView = findViewById(R.id.dealsRecyclerView)
        loadingProgressBar = findViewById(R.id.loadingProgressBar)
        emptyStateLayout = findViewById(R.id.emptyStateLayout)
        
        // Try to find favorites button (may not exist in old layout)
        try {
            favoritesButton = findViewById(R.id.favoritesButton)
            favoritesButton.setOnClickListener {
                openFavoritesActivity()
            }
        } catch (e: Exception) {
            // Favorites button not in layout, that's okay
        }
    }

    /**
     * Setup RecyclerView with LinearLayoutManager and adapter
     */
    private fun setupRecyclerView() {
        dealAdapter = GameDealAdapter(
            onItemClick = { game ->
                openGameDetail(game)
            }
        )
        dealsRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = dealAdapter
            setHasFixedSize(true)
        }
    }

    /**
     * Setup search button click listener
     * Validates input and fetches game deals from CheapShark API
     */
    private fun setupSearchButton() {
        searchButton.setOnClickListener {
            val searchQuery = searchEditText.text.toString().trim()
            
            if (searchQuery.isEmpty()) {
                searchEditText.error = "Please enter a game title"
                Toast.makeText(this, "Enter a game name to search", Toast.LENGTH_SHORT).show()
            } else {
                fetchGameDeals(searchQuery)
            }
        }
    }

    /**
     * Load initial deals on app start
     * Shows the most popular/rated deals without requiring user input
     */
    private fun loadInitialDeals() {
        lifecycleScope.launch {
            try {
                showLoading(true)
                // Fetch top deals
                val deals = RetrofitInstance.apiService.getDeals(
                    pageNumber = 0,
                    pageSize = 20,
                    sortBy = "Deal Rating",
                    desc = 1
                )
                
                if (deals.isNotEmpty()) {
                    dealAdapter.updateDeals(deals)
                    showEmptyState(false)
                } else {
                    showEmptyState(true)
                }
            } catch (e: Exception) {
                handleError("Failed to load initial deals: ${e.message}")
                showEmptyState(true)
            } finally {
                showLoading(false)
            }
        }
    }

    /**
     * Fetch game deals based on search query from CheapShark API
     * Searches for deals matching the user's search query
     *
     * @param query The search query (game title)
     */
    private fun fetchGameDeals(query: String) {
        lifecycleScope.launch {
            try {
                showLoading(true)
                showEmptyState(false)

                // Call API to search for deals
                val deals = RetrofitInstance.apiService.searchDeals(
                    title = query,
                    limit = 60,
                    sortBy = "Deal Rating",
                    desc = 1
                )

                if (deals.isNotEmpty()) {
                    dealAdapter.updateDeals(deals)
                    Toast.makeText(
                        this@MainActivity,
                        "Found ${deals.size} deals",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    showEmptyState(true)
                    Toast.makeText(
                        this@MainActivity,
                        "No deals found for '$query'",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                handleError("Error searching deals: ${e.message}")
                showEmptyState(true)
            } finally {
                showLoading(false)
            }
        }
    }

    /**
     * Open game detail activity for a selected deal
     * 
     * @param game The GameDeal to view details for
     */
    private fun openGameDetail(game: GameDeal) {
        val intent = Intent(this, GameDetailActivity::class.java).apply {
            putExtra(GameDetailActivity.EXTRA_GAME_DEAL, game)
        }
        startActivity(intent)
    }

    /**
     * Open favorites activity to view saved games
     */
    private fun openFavoritesActivity() {
        startActivity(Intent(this, FavoritesActivity::class.java))
    }

    /**
     * Show or hide loading progress bar
     * Used to provide visual feedback during API calls
     *
     * @param isLoading true to show loading, false to hide
     */
    private fun showLoading(isLoading: Boolean) {
        loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    /**
     * Show or hide empty state message
     * Displayed when no deals are found
     *
     * @param show true to show empty state, false to hide
     */
    private fun showEmptyState(show: Boolean) {
        emptyStateLayout.visibility = if (show) View.VISIBLE else View.GONE
        dealsRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }

    /**
     * Handle and display error messages to the user
     * Shows a toast notification with error details
     *
     * @param message Error message to display
     */
    private fun handleError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}