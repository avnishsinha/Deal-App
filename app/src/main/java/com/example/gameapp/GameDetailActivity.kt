package com.example.gameapp

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.gameapp.data.FavoritesManager
import com.example.gameapp.data.model.GameDeal
import com.example.gameapp.data.model.StoreNames
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

    private lateinit var favoritesManager: FavoritesManager
    private var currentGame: GameDeal? = null
    private var isFavorited = false

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

        // Initialize FavoritesManager
        favoritesManager = FavoritesManager(this)

        // Initialize views
        initializeViews()

        // Setup toolbar back button
        setupToolbar()

        // Get game data from intent
        val game = intent.getSerializableExtra(EXTRA_GAME_DEAL) as? GameDeal
        if (game != null) {
            currentGame = game
            displayGameDetails(game)
            checkIfFavorited()
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
     * Check if the current game is in favorites and update button accordingly
     */
    private fun checkIfFavorited() {
        currentGame?.let { game ->
            isFavorited = favoritesManager.isFavorite(game.dealId)
            updateFavoritesButton()
        }
    }

    /**
     * Update the favorites button appearance based on favorited state
     */
    private fun updateFavoritesButton() {
        if (isFavorited) {
            addToFavoritesBtn.text = "Remove from Favorites"
            addToFavoritesBtn.setStrokeColorResource(android.R.color.holo_red_light)
        } else {
            addToFavoritesBtn.text = "Add to Favorites"
            addToFavoritesBtn.setStrokeColorResource(R.color.material_dynamic_primary)
        }
    }

    /**
     * Setup click listeners for buttons
     */
    private fun setupButtonListeners() {
        addToFavoritesBtn.setOnClickListener {
            currentGame?.let { game ->
                if (isFavorited) {
                    favoritesManager.removeFromFavorites(game.dealId)
                    isFavorited = false
                    Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                } else {
                    favoritesManager.addToFavorites(game)
                    isFavorited = true
                    Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                }
                updateFavoritesButton()
            }
        }

        viewStoreBtn.setOnClickListener {
            Toast.makeText(this, "Redirecting to store...", Toast.LENGTH_SHORT).show()
            // TODO: Open browser or deep link to store
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
