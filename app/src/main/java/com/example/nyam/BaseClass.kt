package com.example.nyam

import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.nyam.view.profile.ProfileActivity

open class BaseClass(private val profile : Boolean) : AppCompatActivity() {

    fun setActionBar() {
        supportActionBar?.setCustomView(R.layout.app_bar)
        supportActionBar?.setDisplayShowCustomEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

//        supportActionBar?.setBackgroundDrawable(theme.resources.getColor())
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        if(profile)
            menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        return super.onOptionsItemSelected(item)
    }
}