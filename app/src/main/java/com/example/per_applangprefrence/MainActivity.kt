package com.example.per_applangprefrence

import android.app.LocaleManager
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.os.LocaleList
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.per_applangprefrence.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags("fa-IR")

    private var localeManager: LocaleManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        // Call this on the main thread as it may require Activity.restart()
//        AppCompatDelegate.setApplicationLocales(appLocale)

        // Call this to get the selected locale and display it in your App
        val selectedLocale = AppCompatDelegate.getApplicationLocales()[1]

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            localeManager =
                getSystemService(Context.LOCALE_SERVICE) as LocaleManager
        }

        binding.fab.setOnClickListener {
                val localeList = LocaleListCompat.forLanguageTags("fa-IR")
                AppCompatDelegate.setApplicationLocales(localeList)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    // Specify the constants to be used in the below code snippets

    companion object {

        // Constants for SharedPreference File
        const val PREFERENCE_NAME = "shared_preference"
        const val PREFERENCE_MODE = Context.MODE_PRIVATE

        // Constants for SharedPreference Keys
        const val FIRST_TIME_MIGRATION = "first_time_migration"
        const val SELECTED_LANGUAGE = "selected_language"

        // Constants for SharedPreference Values
        const val STATUS_DONE = "status_done"
    }

    // Utility method to put a string in a SharedPreferenceprivate
    fun putString(key: String, value: String) {
        val editor = getSharedPreferences(
            PREFERENCE_NAME,
            PREFERENCE_MODE
        ).edit()
        editor.putString(key, value)
        editor.apply()
    }

    // Utility method to get a string from a SharedPreferenceprivate
    fun getString(key: String): String? {
        val preference = getSharedPreferences(PREFERENCE_NAME, PREFERENCE_MODE)
        return preference.getString(
            key,
            null
        )
    }

    fun hasMigrated() {
        // Check if the migration has already been done or not
        if (getString(FIRST_TIME_MIGRATION) != STATUS_DONE) {

            // Fetch the selected language from wherever it was stored. In this case it’s SharedPref

            // In this case let’s assume that it was stored in a key named SELECTED_LANGUAGE
            getString(SELECTED_LANGUAGE)?.let { it ->

                // Set this locale using the AndroidX library that will handle the storage itself
                val localeList = LocaleListCompat.forLanguageTags(it)
                AppCompatDelegate.setApplicationLocales(localeList)

                // Set the migration flag to ensure that this is executed only once
                putString(FIRST_TIME_MIGRATION, STATUS_DONE)
            }
        }
    }
}