package com.example.gameapp.data

import android.content.Context
import android.content.SharedPreferences
import java.util.Locale

/**
 * Stores user alarm preferences for game deals.
 *
 * Supports:
 * - Global alarm when any deal reaches >= 90% off.
 * - Per-game alarms based on game title.
 */
class DealAlarmManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isNinetyPercentAlarmEnabled(): Boolean {
        return prefs.getBoolean(KEY_NINETY_PERCENT_ALARM, false)
    }

    fun setNinetyPercentAlarmEnabled(enabled: Boolean) {
        prefs.edit().putBoolean(KEY_NINETY_PERCENT_ALARM, enabled).apply()
    }

    fun toggleGameAlarm(gameTitle: String): Boolean {
        val normalizedTitle = normalizeTitle(gameTitle)
        val alarms = getGameAlarms().toMutableSet()
        val isEnabled = if (alarms.contains(normalizedTitle)) {
            alarms.remove(normalizedTitle)
            false
        } else {
            alarms.add(normalizedTitle)
            true
        }
        saveGameAlarms(alarms)
        return isEnabled
    }

    fun hasAlarmForGame(gameTitle: String): Boolean {
        return getGameAlarms().contains(normalizeTitle(gameTitle))
    }

    private fun getGameAlarms(): Set<String> {
        return prefs.getStringSet(KEY_GAME_ALARMS, emptySet()) ?: emptySet()
    }

    private fun saveGameAlarms(alarms: Set<String>) {
        prefs.edit().putStringSet(KEY_GAME_ALARMS, alarms).apply()
    }

    private fun normalizeTitle(title: String): String {
        return title.trim().lowercase(Locale.US)
    }

    companion object {
        private const val PREFS_NAME = "deal_alarms"
        private const val KEY_NINETY_PERCENT_ALARM = "alarm_90_percent"
        private const val KEY_GAME_ALARMS = "game_alarms"
    }
}
