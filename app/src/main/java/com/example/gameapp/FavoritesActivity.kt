package com.example.gameapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.gameapp.data.model.GameDeal
import com.example.gameapp.viewmodel.FavoritesViewModel

/**
 * FavoritesActivity - Displays list of all favorite games
 */
class FavoritesActivity : BaseActivity() {

    private lateinit var favoriteGamesRecyclerView: RecyclerView
    private lateinit var emptyFavoritesLayout: LinearLayout
    private lateinit var favoritesAdapter: FavoritesAdapter
    
    // ViewModel
    private val viewModel: FavoritesViewModel by viewModels()

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

        // Initialize views
        initializeViews()

        // Setup toolbar
        setupToolbar()

        // Setup RecyclerView
        setupRecyclerView()

        // Setup ViewModel observers
        setupViewModelObservers()

        // Load and display favorites
        viewModel.loadFavorites()
    }

    override fun onResume() {
        super.onResume()
        // Refresh favorites list when activity resumes
        viewModel.loadFavorites()
    }

    private fun initializeViews() {
        favoriteGamesRecyclerView = findViewById(R.id.favoriteGamesRecyclerView)
        emptyFavoritesLayout = findViewById(R.id.emptyFavoritesLayout)
    }

    private fun setupToolbar() {
        val toolbar = findViewById<com.google.android.material.appbar.MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

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

    private fun setupViewModelObservers() {
        viewModel.favorites.observe(this, Observer { favorites ->
            favoritesAdapter.updateFavorites(favorites)
            supportActionBar?.subtitle = "${favorites.size} games saved"
        })
        
        viewModel.isEmpty.observe(this, Observer { isEmpty ->
            showEmptyState(isEmpty)
        })
        
        viewModel.actionMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }

    private fun removeFavoriteConfirm(game: GameDeal) {
        viewModel.removeFavorite(game)
    }

    private fun openGameDetail(game: GameDeal) {
        val intent = Intent(this, GameDetailActivity::class.java).apply {
            putExtra(GameDetailActivity.EXTRA_GAME_DEAL, game)
        }
        startActivity(intent)
    }

    private fun showEmptyState(show: Boolean) {
        emptyFavoritesLayout.visibility = if (show) View.VISIBLE else View.GONE
        favoriteGamesRecyclerView.visibility = if (show) View.GONE else View.VISIBLE
    }
}
