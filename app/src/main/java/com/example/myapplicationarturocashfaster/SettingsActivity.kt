package com.example.myapplicationarturocashfaster

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingsActivity : AppCompatActivity() {

    private lateinit var darkModeSwitch: SwitchMaterial
    private lateinit var languageButton: Button
    private lateinit var btnBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        initViews()
        setupListeners()
        loadCurrentSettings()
    }

    private fun initViews() {
        darkModeSwitch = findViewById(R.id.darkModeSwitch)
        languageButton = findViewById(R.id.languageButton)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun setupListeners() {
        darkModeSwitch.setOnCheckedChangeListener { _: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        languageButton.setOnClickListener {
            // For now, just toggle between English/Spanish
            // In a real app, you would use proper localization
            val currentLang = resources.configuration.locales[0].language
            if (currentLang == "es") {
                // Switch to English
                // This would require proper localization implementation
            } else {
                // Switch to Spanish
                // This would require proper localization implementation
            }
        }

        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun loadCurrentSettings() {
        val currentNightMode = resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK
        darkModeSwitch.isChecked = currentNightMode == android.content.res.Configuration.UI_MODE_NIGHT_YES
    }
}