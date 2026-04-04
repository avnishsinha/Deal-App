package com.example.gameapp

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import android.widget.ProgressBar
import android.widget.LinearLayout

/**
 * MainActivity - Main screen for displaying game deals
 * This app integrates with the CheapShark API to fetch and display video game deals
 * Users can search for games and view current pricing from various stores
 */
class MainActivity : AppCompatActivity() {

    // UI Components
    private lateinit var searchEditText: TextInputEditText
    private lateinit var searchButton: MaterialButton
    private lateinit var dealsRecyclerView: RecyclerView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var emptyStateLayout: LinearLayout

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
    }

    /**
     * Setup RecyclerView with LinearLayoutManager
     */
    private fun setupRecyclerView() {
        dealsRecyclerView.layoutManager = LinearLayoutManager(this)
        // Adapter will be set when data is fetched from API
    }

    /**
     * Setup search button click listener
     * Will fetch game deals from CheapShark API
     */
    private fun setupSearchButton() {
        searchButton.setOnClickListener {
            val searchQuery = searchEditText.text.toString().trim()
            
            if (searchQuery.isNotEmpty()) {
                // TODO: Implement API call to CheapShark API
                // https://www.cheapshark.com/api
                // Endpoint: /api/1.0/games?title={searchQuery}
                // Then fetch deals using /api/1.0/deals
                showLoading(true)
            } else {
                // Show message to user to enter search query
                searchEditText.error = "Please enter a game name"
            }
        }
    }

    /**
     * Show or hide loading progress bar
     */
    private fun showLoading(isLoading: Boolean) {
        loadingProgressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    /**
     * Show empty state when no deals are found
     */
    private fun showEmptyState(isEmpty: Boolean) {
        emptyStateLayout.visibility = if (isEmpty) View.VISIBLE else View.GONE
        dealsRecyclerView.visibility = if (isEmpty) View.GONE else View.VISIBLE
    }
}