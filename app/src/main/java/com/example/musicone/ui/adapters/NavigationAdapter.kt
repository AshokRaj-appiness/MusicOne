package com.example.musicone.ui.adapters

import android.content.Context
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.musicone.R
import com.example.musicone.ui.activities.MainActivity
import com.example.musicone.ui.fragments.AboutUsFragment
import com.example.musicone.ui.fragments.FavoriteFragment
import com.example.musicone.ui.fragments.MainScreenFragment
import com.example.musicone.ui.model.NavItem

class NavigationAdapter(val navList:List<NavItem>,val context: Context): RecyclerView.Adapter<NavigationAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iv_nav_icon = itemView.findViewById<ImageView>(R.id.iv_nav_icon)
        val tv_nav_text = itemView.findViewById<TextView>(R.id.tv_nav_text)
        val lt_navItem = itemView.findViewById<ConstraintLayout>(R.id.lt_navItem)

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.navigation_drawer_item,parent,false))
    }

    override fun getItemCount(): Int {
        return navList.size
    }

    override fun onBindViewHolder(holder: NavigationAdapter.ViewHolder, position: Int) {
        holder.iv_nav_icon.setImageResource(navList.get(position).itemImageId)
        holder.tv_nav_text.text = navList.get(position).itemName
        holder.let {

        }
        holder.lt_navItem.setOnClickListener {
            when(position){
                0 ->(context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,MainScreenFragment()).commit()
                1->(context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,FavoriteFragment()).commit()
                else ->(context as MainActivity).supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,AboutUsFragment()).commit()
            }
        }
    }
}