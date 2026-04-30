package com.example.gameapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gameapp.data.ThemeManager

/**
 * BaseActivity that handles applying the user's selected theme.
 * All other activities in the app should extend this activity.
 */
abstract class BaseActivity : AppCompatActivity() {
    
    protected lateinit var themeManager: ThemeManager
    private var currentThemeName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        themeManager = ThemeManager(this)
        
        // Apply theme before super.onCreate
        val themeName = themeManager.getSelectedThemeName()
        currentThemeName = themeName
        setTheme(themeManager.getThemeResId())
        
        super.onCreate(savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        // Recreate the activity if the theme has changed while it was in the background
        val latestThemeName = themeManager.getSelectedThemeName()
        if (currentThemeName != null && currentThemeName != latestThemeName) {
            // Update the tracked theme name and recreate
            currentThemeName = latestThemeName
            recreate()
        }
    }
}
