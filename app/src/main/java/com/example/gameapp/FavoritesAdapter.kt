package com.example.gameapp

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gameapp.data.model.GameDeal
import com.example.gameapp.data.model.StoreNames
import com.google.android.material.card.MaterialCardView
import kotlin.math.round

/**
 * FavoritesAdapter - RecyclerView adapter for displaying favorite games
 * Binds GameDeal data to the item_favorite_game layout
 * Provides options to remove favorites
 */
class FavoritesAdapter(
    private val favorites: MutableList<GameDeal> = mutableListOf(),
    private val onRemoveClick: (GameDeal) -> Unit = {},
    private val onItemClick: (GameDeal) -> Unit = {}
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteGameViewHolder>() {

    /**
     * ViewHolder for individual favorite game items
     */
    inner class FavoriteGameViewHolder(private val cardView: MaterialCardView) :
        RecyclerView.ViewHolder(cardView) {

        private val favGameThumbnail: ImageView = cardView.findViewById(R.id.favGameThumbnail)
        private val favGameTitle: TextView = cardView.findViewById(R.id.favGameTitle)
        private val favStoreName: TextView = cardView.findViewById(R.id.favStoreName)
        private val favSalePrice: TextView = cardView.findViewById(R.id.favSalePrice)
        private val favDiscountBadge: LinearLayout = cardView.findViewById(R.id.favDiscountBadge)
        private val favDiscountPercentage: TextView = cardView.findViewById(R.id.favDiscountPercentage)
        private val removeFavBtn: ImageButton = cardView.findViewById(R.id.removeFavBtn)

        /**
         * Bind favorite game data to the views
         */
        fun bind(game: GameDeal) {
            // Set game title
            favGameTitle.text = game.title

            // Set store name
            favStoreName.text = StoreNames.getStoreName(game.storeId)

            // Load game thumbnail with Glide
            if (game.thumb.isNotEmpty()) {
                Glide.with(favGameThumbnail.context)
                    .load(game.thumb)
                    .centerCrop()
                    .into(favGameThumbnail)
            }

            // Set sale price
            favSalePrice.text = formatPrice(game.salePrice)

            // Calculate and display discount percentage
            val discountPercent = calculateDiscountPercent(game.normalPrice, game.salePrice)
            if (discountPercent > 0) {
                favDiscountPercentage.text = "-${discountPercent}%"
                favDiscountBadge.visibility = android.view.View.VISIBLE
            } else {
                favDiscountBadge.visibility = android.view.View.GONE
            }

            // Set up remove button
            removeFavBtn.setOnClickListener {
                onRemoveClick(game)
            }

            // Set up card click listener
            cardView.setOnClickListener {
                onItemClick(game)
            }
        }

        /**
         * Format a price value as a currency string
         */
        private fun formatPrice(price: Double): String {
            return if (price == 0.0) {
                "Free"
            } else {
                "$%.2f".format(price)
            }
        }

        /**
         * Calculate discount percentage between normal and sale price
         */
        private fun calculateDiscountPercent(normalPrice: Double, salePrice: Double): Int {
            if (normalPrice <= 0) return 0
            val discountPercent = ((normalPrice - salePrice) / normalPrice) * 100
            return round(discountPercent).toInt()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteGameViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_favorite_game, parent, false) as MaterialCardView
        return FavoriteGameViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: FavoriteGameViewHolder, position: Int) {
        holder.bind(favorites[position])
    }

    override fun getItemCount(): Int = favorites.size

    /**
     * Update the adapter with new favorite data
     */
    fun updateFavorites(newFavorites: List<GameDeal>) {
        favorites.clear()
        favorites.addAll(newFavorites)
        notifyDataSetChanged()
    }

    /**
     * Remove a favorite from the list
     */
    fun removeFavorite(game: GameDeal) {
        val index = favorites.indexOfFirst { it.dealId == game.dealId }
        if (index >= 0) {
            favorites.removeAt(index)
            notifyItemRemoved(index)
        }
    }

    /**
     * Clear all favorites
     */
    fun clearAll() {
        favorites.clear()
        notifyDataSetChanged()
    }
}
