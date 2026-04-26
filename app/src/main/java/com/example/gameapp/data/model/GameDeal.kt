package com.example.gameapp.data.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * GameDeal data class represents a single game deal from the CheapShark API
 * Contains information about the game, store, and pricing details
 * 
 * Implements Serializable to allow passing through Android Intents
 */
data class GameDeal(
    @SerializedName("dealID")
    val dealId: String = "",
    
    @SerializedName("gameID")
    val gameId: String = "",
    
    @SerializedName("title")
    val title: String = "",
    
    @SerializedName("storeID")
    val storeId: String = "",
    
    @SerializedName("normalPrice")
    val normalPrice: Double = 0.0,
    
    @SerializedName("salePrice")
    val salePrice: Double = 0.0,
    
    @SerializedName("savings")
    val savings: Double = 0.0,
    
    @SerializedName("metacriticScore")
    val metacriticScore: String? = null,
    
    @SerializedName("steamRatingPercent")
    val steamRatingPercent: String? = null,
    
    @SerializedName("dealRating")
    val dealRating: Double = 0.0,
    
    @SerializedName("thumb")
    val thumb: String = ""
) : Serializable

/**
 * Represents a store where games are sold
 * Used to get store details from the CheapShark API
 */
data class Store(
    @SerializedName("storeID")
    val storeId: String = "",
    
    @SerializedName("storeName")
    val storeName: String = ""
)

/**
 * Container for store list response from API
 */
data class StoresResponse(
    @SerializedName("response")
    val stores: List<Store> = emptyList()
)
