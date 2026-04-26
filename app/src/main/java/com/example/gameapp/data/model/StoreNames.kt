package com.example.gameapp.data.model

/**
 * Common store ID to name mappings for popular game stores
 * This helps display readable store names instead of just numeric IDs
 */
object StoreNames {
    private val storeMap = mapOf(
        "1" to "Steam",
        "3" to "GamersGate",
        "7" to "Games Republic",
        "11" to "Newegg",
        "13" to "Humble Store",
        "15" to "Ubisoft Store",
        "18" to "Gog.com",
        "20" to "Playstation Store",
        "23" to "GameFly",
        "25" to "Origin",
        "27" to "Xbox Store",
        "28" to "Desire2play",
        "29" to "Razer Game Store",
        "30" to "Savemi",
        "31" to "Fanatical",
        "32" to "Windows Store",
        "33" to "Gamesplanet",
        "35" to "Epic Games Store",
        "36" to "GOG",
        "37" to "Nintendo Store",
        "39" to "Tokopedia",
        "40" to "2Game",
        "41" to "DLGamer",
        "42" to "Gamesrocket",
        "43" to "Green Man Gaming",
        "44" to "Amazon",
        "45" to "IndieGala",
        "46" to "Voidu",
        "48" to "itch.io",
        "49" to "Oculus Store",
        "50" to "Dealify",
        "51" to "Allyouplay",
        "52" to "Woot",
        "53" to "Nyakachan",
        "54" to "Sila Games",
        "55" to "Hooked on Games",
        "56" to "Kaleugames",
        "57" to "Playfield",
        "58" to "Shopto",
        "59" to "Gamesload",
        "60" to "2Game",
        "61" to "Dogado",
        "62" to "Gliz",
        "63" to "WinGameStore",
        "64" to "Gamesplanet",
        "65" to "Grepolis",
        "66" to "GmG",
        "67" to "Gamesload",
        "68" to "Kinguin",
        "69" to "Cdkeys",
        "70" to "Voidu"
    )

    /**
     * Get store name by store ID
     * Returns a default format if store ID not found in mapping
     */
    fun getStoreName(storeId: String?): String {
        if (storeId == null) return "Unknown Store"
        return storeMap[storeId] ?: "Store #$storeId"
    }
}
