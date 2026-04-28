package com.example.gameapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.gameapp.data.DealAlarmManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.materialswitch.MaterialSwitch

class SettingsActivity : AppCompatActivity() {

    private lateinit var alarmManager: DealAlarmManager
    private lateinit var ninetyOffAlarmSwitch: MaterialSwitch
    private lateinit var toolbar: MaterialToolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_settings)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.settingsToolbar).parent as android.view.View) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        alarmManager = DealAlarmManager(this)
        
        toolbar = findViewById(R.id.settingsToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        ninetyOffAlarmSwitch = findViewById(R.id.settingsNinetyOffAlarmSwitch)
        setupAlarmSwitch()
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
}
