package com.example.gameapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gameapp.data.model.GameDeal
import com.example.gameapp.data.model.StoreNames
import com.google.android.material.card.MaterialCardView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import kotlin.math.round

/**
 * RecyclerView adapter for displaying game deals
 * Binds GameDeal data to the item_game_deal layout
 */
class GameDealAdapter(
    private val deals: MutableList<GameDeal> = mutableListOf(),
    private val onItemClick: (GameDeal) -> Unit = {},
    private val onAlarmToggle: (GameDeal) -> Unit = {},
    private val isAlarmEnabledForGame: (GameDeal) -> Boolean = { false }
) :
    RecyclerView.Adapter<GameDealAdapter.GameDealViewHolder>() {

    /**
     * ViewHolder for individual game deal items
     */
    inner class GameDealViewHolder(private val cardView: MaterialCardView) :
        RecyclerView.ViewHolder(cardView) {

        private val gameThumbnail: ImageView = cardView.findViewById(R.id.gameThumbnail)
        private val gameTitle: TextView = cardView.findViewById(R.id.gameTitle)
        private val storeName: TextView = cardView.findViewById(R.id.storeName)
        private val normalPrice: TextView = cardView.findViewById(R.id.normalPrice)
        private val salePrice: TextView = cardView.findViewById(R.id.salePrice)
        private val discountBadge: LinearLayout = cardView.findViewById(R.id.discountBadge)
        private val discountPercentage: TextView = cardView.findViewById(R.id.discountPercentage)
        private val alarmButton: MaterialButton = cardView.findViewById(R.id.alarmButton)

        /**
         * Bind game deal data to the views
         * Handles image loading with Glide, formats prices, and calculates discounts
         */
        fun bind(deal: GameDeal) {
            // Set game title
            gameTitle.text = deal.title

            // Set store name
            storeName.text = StoreNames.getStoreName(deal.storeId)

            // Load game thumbnail with Glide
            if (deal.thumb.isNotEmpty()) {
                Glide.with(gameThumbnail.context)
                    .load(deal.thumb)
                    .centerCrop()
                    .into(gameThumbnail)
            }

            // Format and set prices
            normalPrice.text = formatPrice(deal.normalPrice)
            salePrice.text = formatPrice(deal.salePrice)

            // Calculate and display discount percentage
            val discountPercent = calculateDiscountPercent(deal.normalPrice, deal.salePrice)
            if (discountPercent > 0) {
                discountPercentage.text = "-${discountPercent}%"
                discountBadge.visibility = android.view.View.VISIBLE
            } else {
                discountBadge.visibility = android.view.View.GONE
            }

            val alarmEnabled = isAlarmEnabledForGame(deal)
            alarmButton.text = if (alarmEnabled) "Alarm On" else "Set Alarm"

            alarmButton.setOnClickListener {
                onAlarmToggle(deal)
            }

            // Set up card click listener to open detail page
            cardView.setOnClickListener {
                onItemClick(deal)
            }
        }

        /**
         * Format a price value as a currency string
         * Handles edge cases like $0 prices
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameDealViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game_deal, parent, false) as MaterialCardView
        return GameDealViewHolder(cardView)
    }

    override fun onBindViewHolder(holder: GameDealViewHolder, position: Int) {
        holder.bind(deals[position])
    }

    override fun getItemCount(): Int = deals.size

    /**
     * Update the adapter with new deal data
     * Clears existing data and notifies the adapter of changes
     */
    fun updateDeals(newDeals: List<GameDeal>) {
        deals.clear()
        deals.addAll(newDeals)
        notifyDataSetChanged()
    }

    /**
     * Add more deals to the adapter (for pagination)
     */
    fun addDeals(newDeals: List<GameDeal>) {
        val startPosition = deals.size
        deals.addAll(newDeals)
        notifyItemRangeInserted(startPosition, newDeals.size)
    }

    /**
     * Clear all deals from the adapter
     */
    fun clearDeals() {
        deals.clear()
        notifyDataSetChanged()
    }
}
