package com.example.gameapp.data.api

import com.example.gameapp.data.model.GameDeal
import com.example.gameapp.data.model.StoresResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * CheapShark API service interface using Retrofit
 * Defines all the endpoints we use to fetch game deals and related data
 *
 * Base URL: https://www.cheapshark.com/api/1.0/
 */
interface CheapSharkApiService {

    /**
     * Get current deals - fetches the latest game deals
     *
     * @param pageNumber Page number for pagination (default 0)
     * @param pageSize Number of results per page (default 60, max 60)
     * @param storeID Filter by specific store (optional)
     * @param sortBy Sort results: "Deal Rating", "Savings", "Price" (default "Deal Rating")
     * @param desc Sort in descending order (1 = yes, 0 = no)
     * @param lowerPrice Filter by lower price bound (optional)
     * @param upperPrice Filter by upper price bound (optional)
     *
     * @return List of GameDeal objects
     */
    @GET("deals")
    suspend fun getDeals(
        @Query("pageNumber") pageNumber: Int = 0,
        @Query("pageSize") pageSize: Int = 60,
        @Query("sortBy") sortBy: String = "Deal Rating",
        @Query("desc") desc: Int = 1,
        @Query("lowerPrice") lowerPrice: Double? = null,
        @Query("upperPrice") upperPrice: Double? = null,
        @Query("storeID") storeID: String? = null
    ): List<GameDeal>

    /**
     * Search for games by title
     *
     * @param title Game title to search for
     * @param limit Number of results to return (default 60, max 60)
     *
     * @return List of GameDeal objects matching the search
     */
    @GET("deals")
    suspend fun searchDeals(
        @Query("title") title: String,
        @Query("pageSize") limit: Int = 60,
        @Query("sortBy") sortBy: String = "Deal Rating",
        @Query("desc") desc: Int = 1
    ): List<GameDeal>

    /**
     * Get list of all stores where games are sold
     *
     * @return Response containing list of stores
     */
    @GET("stores")
    suspend fun getStores(): StoresResponse
}
