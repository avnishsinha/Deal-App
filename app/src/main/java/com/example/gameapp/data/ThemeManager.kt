package com.example.gameapp.data

import android.content.Context
import android.content.SharedPreferences
import com.example.gameapp.R

class ThemeManager(context: Context) {
    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getThemeResId(): Int {
        return when (getSelectedThemeName()) {
            "Neon" -> R.style.Theme_GameApp_Neon
            "Frost" -> R.style.Theme_GameApp_Frost
            "Contrast" -> R.style.Theme_GameApp_Contrast
            "Sunset" -> R.style.Theme_GameApp_Sunset
            "Forest" -> R.style.Theme_GameApp_Forest
            "Royal" -> R.style.Theme_GameApp_Royal
            "Crimson" -> R.style.Theme_GameApp_Crimson
            else -> R.style.Theme_GameApp_Classic
        }
    }

    fun getSelectedThemeName(): String {
        return prefs.getString(KEY_THEME, "Classic") ?: "Classic"
    }

    fun setTheme(themeName: String) {
        // Use commit() to ensure it's saved immediately before recreation
        prefs.edit().putString(KEY_THEME, themeName).commit()
    }

    companion object {
        private const val PREFS_NAME = "app_theme_prefs"
        private const val KEY_THEME = "selected_theme"
    }
}
