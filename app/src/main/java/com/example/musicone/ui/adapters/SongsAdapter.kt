package com.example.musicone.ui.adapters

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.musicone.R
import com.example.musicone.ui.activities.MainActivity
import com.example.musicone.ui.fragments.SongPlayingFragment
import com.example.musicone.ui.model.Songs

class SongsAdapter(val songsList:ArrayList<Songs>,val context: Context):RecyclerView.Adapter<SongsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.song_item,parent,false))
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tv_song_title.text = songsList.get(position).songTitle
        holder.tv_song_artist.text = songsList.get(position).artist
        holder.lv_song_item.setOnClickListener {
            val fragment = SongPlayingFragment()
            val args = Bundle()
            args.putInt("position",position)
            args.putParcelable("Song",songsList.get(position))
            args.putParcelableArrayList("SongList",songsList)
            fragment.arguments = args
            (context as FragmentActivity).supportFragmentManager.beginTransaction().replace(R.id.nav_host_fragment,fragment).commit()
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tv_song_title = itemView.findViewById<TextView>(R.id.tv_song_title)
        val tv_song_artist = itemView.findViewById<TextView>(R.id.tv_song_artist)
        val lv_song_item = itemView.findViewById<ConstraintLayout>(R.id.lv_song_item)
    }

}