package com.example.gameapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameapp.data.FavoritesManager
import com.example.gameapp.data.model.GameDeal

/**
 * FavoritesActivity - Displays list of all favorite games
 * 
 * Features:
 * - View all saved favorite games
 * - Remove games from favorites
 * - Click on game to view details
 * - Empty state when no favorites
 * 
 * Users can manage their collection of favorite deals from here
 */
class FavoritesActivity : AppCompatActivity() {

    private lateinit var favoriteGamesRecyclerView: RecyclerView
    private lateinit var emptyFavoritesLayout: LinearLayout
    private lateinit var favoritesAdapter: FavoritesAdapter
    private lateinit var favoritesManager: FavoritesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_favorites)

        // Setup edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize FavoritesManager
        favoritesManager = FavoritesManager(this)

        // Initialize views
        initializeViews()

        // Setup toolbar
        setupToolbar()

        // Setup RecyclerView
        setupRecyclerView()

        // Load and display favorites
        loadFavorites()
    }

    override fun onResume() {
        super.onResume()
        // Refresh favorites list when activity resumes
        loadFavorites()
    }

    /**
     * Initialize all view references from the layout
     */
    private fun initializeViews() {
        favoriteGamesRecyclerView = findViewById(R.id.favoriteGamesRecyclerView)
        emptyFavoritesLayout = findViewById(R.id.emptyFavoritesLayout)
    }

    /**
     * Setup toolbar with back navigation
     */
    private fun setupToolbar() {
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    /**
     * Setup RecyclerView with adapter for favorites
     */
    private fun setupRecyclerView() {
        favoritesAdapter = FavoritesAdapter(
            onRemoveClick = { game ->
                removeFavoriteConfirm(game)
            },
            onItemClick = { game ->
                openGameDetail(game)
            }
        )

        favoriteGamesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@FavoritesActivity)
            adapter = favoritesAdapter
            setHasFixedSize(true)
        }
    }

    /**
     * Load all favorites from storage and display them
     */
    private fun loadFavorites() {
        val favorites = favoritesManager.getAllFavorites()

        if (favorites.isEmpty()) {
            showEmptyState(true)
        } else {
            showEmptyState(false)
            favoritesAdapter.updateFavorites(favorites)
        }

        // Update toolbar subtitle with count
        supportActionBar?.subtitle = "${favorites.size} games saved"
    }

    /**
     * Show confirmation and remove favorite
     */
    private fun removeFavoriteConfirm(game: GameDeal) {
        favoritesManager.removeFromFavorites(game.dealId)
        favoritesAdapter.removeFavorite(game)
        
        val remaining = favoritesManager.getAllFavorites()
        if (remaining.isEmpty()) {
            showEmptyState(true)
        }
        
        supportActionBar?.subtitle = "${remaining.size} games saved"
        Toast.makeText(this, "${game.title} removed from favorites", Toast.LENGTH_SHORT).show()
    }

    /**
     * Open game detail activity for a selected favorite
     */
    private fun openGameDetail(game: GameDeal) {
        val intent = Intent(this, GameDetailActivity::class.java).apply {
            putExtra(GameDetailActivity.EXTRA_GAME_DEAL, game)
        }
        startActivity(intent)
    }

    /**
     * Show or hide empty state based on whether favorites exist
     */
    private fun showEmptyState(show: Boolean) {
        emptyFavoritesLayout.visibility = if (show) View.VISIBLE else View.GONE
        favoriteGamesRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }
}
