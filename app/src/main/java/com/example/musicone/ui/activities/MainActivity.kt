package com.example.musicone.ui.activities

import android.os.Bundle
import android.widget.Toast
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicone.R
import com.example.musicone.ui.adapters.NavigationAdapter
import com.example.musicone.ui.fragments.MainScreenFragment
import com.example.musicone.ui.model.NavItem
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.app_bar_main.*
import kotlinx.android.synthetic.main.nav_header_main.*

class MainActivity : AppCompatActivity() {
    var drawerLayout:DrawerLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        drawerLayout = findViewById(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout,toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout?.addDrawerListener(toggle)
        toggle.syncState()


        val mainScreen = MainScreenFragment()
        this.supportFragmentManager
            .beginTransaction()
            .add(R.id.nav_host_fragment,mainScreen,"Main Fragment")
            .commit()
        val navList = listOf<NavItem>(
            NavItem(R.drawable.ic_music_icon,"All Songs"),
            NavItem(R.drawable.ic_favorite_icon,"Favourites"),
            NavItem(R.drawable.ic_settings_icon,"Settings"),
            NavItem(R.drawable.ic_about_us_icon,"About Us")
        )
        val hView =  nav_view.getHeaderView(0);
        val rv_nav_list = hView.findViewById<RecyclerView>(R.id.rv_nav_list)
        rv_nav_list.adapter = NavigationAdapter(navList,this)
    }

    override fun onStart() {
        super.onStart()
    }
}
