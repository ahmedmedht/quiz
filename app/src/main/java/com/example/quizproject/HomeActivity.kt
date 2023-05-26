package com.example.quizproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.core.view.GravityCompat
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_home.drawer_layout
import kotlinx.android.synthetic.main.activity_home.img_menu_drawer
import kotlinx.android.synthetic.main.activity_home.nav_view

class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        img_menu_drawer.setOnClickListener {
            drawer_layout.openDrawer(GravityCompat.START)
        }

        nav_view.setNavigationItemSelectedListener (this)

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.item_nav_about -> {
                // Handle click for Item 1
                // Perform necessary actions
                Toast.makeText(this,"zayed",Toast.LENGTH_SHORT).show()
                true
            }
            R.id.item_nav_profile -> {
                // Handle click for Item 2
                // Perform necessary actions
                Toast.makeText(this,"zayed",Toast.LENGTH_SHORT).show()
                true
            }
            // Handle other items as needed
            else -> false
        }

    }
}