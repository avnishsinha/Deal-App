package com.example.gameapp

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gameapp.data.DealAlarmManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsActivity : BaseActivity() {

    private lateinit var alarmManager: DealAlarmManager
    private lateinit var ninetyOffAlarmSwitch: MaterialSwitch
    private lateinit var selectThemeButton: MaterialButton
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        toolbar = findViewById<MaterialToolbar>(R.id.settingsToolbar)
        val settingsContainer = toolbar.parent as View
        ViewCompat.setOnApplyWindowInsetsListener(settingsContainer) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        alarmManager = DealAlarmManager(this)
        
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        ninetyOffAlarmSwitch = findViewById<MaterialSwitch>(R.id.settingsNinetyOffAlarmSwitch)
        selectThemeButton = findViewById<MaterialButton>(R.id.selectThemeButton)

        setupAlarmSwitch()
        setupThemeButton()
    }

    private fun setupAlarmSwitch() {
        ninetyOffAlarmSwitch.isChecked = alarmManager.isNinetyPercentAlarmEnabled()
        ninetyOffAlarmSwitch.setOnCheckedChangeListener { _, isChecked ->
            alarmManager.setNinetyPercentAlarmEnabled(isChecked)
            val message = if (isChecked) {
                getString(R.string.alarm_90_off_enabled)
            } else {
                getString(R.string.alarm_90_off_disabled)
            }
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupThemeButton() {
        selectThemeButton.text = "Theme: ${themeManager.getSelectedThemeName()}"
        selectThemeButton.setOnClickListener {
            showThemeSelectionDialog()
        }
    }

    private fun showThemeSelectionDialog() {
        val themes = arrayOf("Classic", "Neon", "Frost", "Contrast", "Sunset", "Forest", "Royal", "Crimson")
        val currentTheme = themeManager.getSelectedThemeName()
        val checkedItem = themes.indexOf(currentTheme)

        AlertDialog.Builder(this)
            .setTitle("Select Theme")
            .setSingleChoiceItems(themes, checkedItem) { dialog, which ->
                val selectedTheme = themes[which]
                if (selectedTheme != currentTheme) {
                    themeManager.setTheme(selectedTheme)
                    dialog.dismiss()
                    // recreate is handled by BaseActivity onResume, but for immediate change:
                    recreate()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
