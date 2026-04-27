package com.example.gameapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.example.gameapp.data.model.GameDeal
import com.example.gameapp.data.model.StoreNames
import com.example.gameapp.viewmodel.GameDetailViewModel
import com.google.android.material.button.MaterialButton
import kotlin.math.round

/**
 * GameDetailActivity - Displays detailed information about a single game deal
 * 
 * Shows:
 * - Game title and thumbnail
 * - Store name
 * - Original and sale prices
 * - Discount percentage
 * - Deal rating
 * - Save to favorites option
 * - Link to view on store
 */
class GameDetailActivity : AppCompatActivity() {

    private lateinit var gameDetailThumbnail: ImageView
    private lateinit var gameDetailTitle: TextView
    private lateinit var gameDetailStore: TextView
    private lateinit var gameDetailNormalPrice: TextView
    private lateinit var gameDetailSalePrice: TextView
    private lateinit var gameDetailDiscount: TextView
    private lateinit var gameDetailRating: TextView
    private lateinit var gameDetailSavings: TextView
    private lateinit var addToFavoritesBtn: MaterialButton
    private lateinit var viewStoreBtn: MaterialButton

    // ViewModel
    private val viewModel: GameDetailViewModel by viewModels()

    companion object {
        const val EXTRA_GAME_DEAL = "game_deal"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_game_detail)

        // Setup edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize views
        initializeViews()

        // Setup toolbar back button
        setupToolbar()

        // Setup ViewModel observers
        setupViewModelObservers()

        // Get game data from intent
        val game = intent.getSerializableExtra(EXTRA_GAME_DEAL) as? GameDeal
        if (game != null) {
            viewModel.setCurrentGame(game)
        } else {
            Toast.makeText(this, "Error loading game details", Toast.LENGTH_SHORT).show()
            finish()
        }

        // Setup button listeners
        setupButtonListeners()
    }

    /**
     * Initialize all view references from the layout
     */
    private fun initializeViews() {
        gameDetailThumbnail = findViewById(R.id.gameDetailThumbnail)
        gameDetailTitle = findViewById(R.id.gameDetailTitle)
        gameDetailStore = findViewById(R.id.gameDetailStore)
        gameDetailNormalPrice = findViewById(R.id.gameDetailNormalPrice)
        gameDetailSalePrice = findViewById(R.id.gameDetailSalePrice)
        gameDetailDiscount = findViewById(R.id.gameDetailDiscount)
        gameDetailRating = findViewById(R.id.gameDetailRating)
        gameDetailSavings = findViewById(R.id.gameDetailSavings)
        addToFavoritesBtn = findViewById(R.id.addToFavoritesBtn)
        viewStoreBtn = findViewById(R.id.viewStoreBtn)
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
     * Display game details in the UI
     * 
     * @param game The GameDeal to display
     */
    private fun displayGameDetails(game: GameDeal) {
        // Set title
        gameDetailTitle.text = game.title

        // Set store name
        gameDetailStore.text = StoreNames.getStoreName(game.storeId)

        // Load image with Glide
        if (game.thumb.isNotEmpty()) {
            Glide.with(this)
                .load(game.thumb)
                .centerCrop()
                .into(gameDetailThumbnail)
        }

        // Set prices
        gameDetailNormalPrice.text = formatPrice(game.normalPrice)
        gameDetailSalePrice.text = formatPrice(game.salePrice)

        // Calculate and display discount
        val discountPercent = calculateDiscountPercent(game.normalPrice, game.salePrice)
        gameDetailDiscount.text = "-${discountPercent}%"

        // Set deal rating
        gameDetailRating.text = "%.1f/10".format(game.dealRating)

        // Calculate and display savings
        val savings = game.normalPrice - game.salePrice
        gameDetailSavings.text = formatPrice(savings)
    }

    /**
     * Setup ViewModel observers
     */
    private fun setupViewModelObservers() {
        // Observe current game changes
        viewModel.currentGame.observe(this, Observer { game ->
            displayGameDetails(game)
        })
        
        // Observe favorite status changes
        viewModel.isFavorited.observe(this, Observer { isFavorited ->
            updateFavoritesButton(isFavorited)
        })
        
        // Observe action messages
        viewModel.actionMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        })
    }

    /**
     * Setup click listeners for buttons
     */
    private fun setupButtonListeners() {
        addToFavoritesBtn.setOnClickListener {
            viewModel.toggleFavorite()
        }

        viewStoreBtn.setOnClickListener {
            // Get current game and open store page
            val currentGame = viewModel.currentGame.value
            if (currentGame != null && currentGame.dealId.isNotEmpty()) {
                openStoreLink(currentGame.dealId)
            } else {
                Toast.makeText(this, "Deal information not available", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Open the store link in a browser using CheapShark search
     * Opens the game page on CheapShark which shows all store options
     * 
     * @param dealId The unique deal ID from CheapShark
     */
    private fun openStoreLink(dealId: String) {
        try {
            val currentGame = viewModel.currentGame.value
            if (currentGame != null) {
                // Use CheapShark game search page as fallback
                // This shows the game with all available stores
                val searchUrl = "https://www.cheapshark.com/search?q=${currentGame.title.replace(" ", "+")}"
                
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(searchUrl)
                startActivity(intent)
                Toast.makeText(this, "Opening store page...", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Game information not available", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Could not open store page: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Check if the current game is in favorites and update button accordingly
     */
    private fun checkIfFavorited() {
        // This method is kept for reference but is now handled by ViewModel
        // The viewModel.setCurrentGame() call handles this
    }

    /**
     * Update the favorites button appearance based on favorited state
     */
    private fun updateFavoritesButton(isFavorited: Boolean) {
        if (isFavorited) {
            addToFavoritesBtn.text = "Remove from Favorites"
            addToFavoritesBtn.setStrokeColorResource(android.R.color.holo_red_light)
        } else {
            addToFavoritesBtn.text = "Add to Favorites"
            addToFavoritesBtn.setStrokeColorResource(R.color.material_dynamic_primary)
        }
    }

    /**
     * Format price as currency string
     */
    private fun formatPrice(price: Double): String {
        return if (price == 0.0) "Free" else "$%.2f".format(price)
    }

    /**
     * Calculate discount percentage
     */
    private fun calculateDiscountPercent(normalPrice: Double, salePrice: Double): Int {
        if (normalPrice <= 0) return 0
        val discountPercent = ((normalPrice - salePrice) / normalPrice) * 100
        return round(discountPercent).toInt()
    }
}
