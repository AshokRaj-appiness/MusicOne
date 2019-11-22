package com.example.musicone.ui.fragments


import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.cleveroad.audiovisualization.AudioVisualization
import com.cleveroad.audiovisualization.DbmHandler
import com.cleveroad.audiovisualization.GLAudioVisualizationView

import com.example.musicone.R
import com.example.musicone.ui.model.CurrentSong
import com.example.musicone.ui.model.Songs
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_song_playing.*
import kotlinx.android.synthetic.main.fragment_song_playing.tv_song_artist
import kotlinx.android.synthetic.main.song_item.*
import kotlinx.coroutines.delay
import java.lang.NullPointerException
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class SongPlayingFragment : Fragment() {

    lateinit var myActivity:Context
    var mediaPlayer:MediaPlayer?=null

    var currentSongPosition :Int= 0
    var songList:ArrayList<Songs>?=null

    var currentSong:CurrentSong?=null

    var audioVisualization: AudioVisualization ?= null

    var updateSongTime = object :Runnable{
        override fun run() {
            val getCurrent = mediaPlayer?.currentPosition
            tv_startTime.setText(String.format("%d:%d",TimeUnit.MILLISECONDS.toMinutes(getCurrent?.toLong() as Long),TimeUnit.MILLISECONDS.toSeconds(getCurrent.toLong() as Long) - TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getCurrent.toLong() as Long))))
            Handler().postDelayed(this, 1000)
        }

    }


    object Statistic{
        var SHARED_PREF_SHUFFLE = "Shuffle feature"
        var SHARED_PREF_LOOP = "Shuffle feature"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_song_playing, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        audioVisualization = glAudioVisualizationView as AudioVisualization

    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        myActivity = context as Activity
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        myActivity = activity
    }





    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        currentSong = CurrentSong()
        currentSong?.isPlaying = true
        currentSong?.isLoop = false
        currentSong?.isSuffle = false


        val songs = arguments?.getParcelable<Parcelable>("Song") as Songs
        val path = songs.songData
        val songTitile = songs.songTitle
        val songArtists = songs.artist
        val songId = songs.songId
        currentSongPosition = arguments!!.getInt("position")
        songList = arguments?.getParcelableArrayList("SongList")
        currentSong?.songPath = path
        currentSong?.currentPosition = currentSongPosition
        currentSong?.songTitile = songTitile
        currentSong?.songArtist = songArtists

        updateTextView(currentSong?.songTitile as String,currentSong?.songArtist as String)

        mediaPlayer = MediaPlayer()
        mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try{
            myActivity?.let { mediaPlayer?.setDataSource(it, Uri.parse(path)) }
            mediaPlayer?.prepare()

        }catch (e:Exception){
            e.printStackTrace()
        }
        mediaPlayer?.start()
        processInformation(mediaPlayer as MediaPlayer)

        if(currentSong?.isPlaying as Boolean){
            ib_playPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_white_24dp)
        }else{
            ib_playPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp)
        }

        mediaPlayer?.setOnCompletionListener {
            onSongComplete()
        }

        clickHandler()

        val vizualizerHandler = DbmHandler.Factory.newVisualizerHandler(myActivity,0)
        audioVisualization?.linkTo(vizualizerHandler)
        setDefaultShuffleMode()
    }

    fun setDefaultShuffleMode(){
        val shuffleModePref = myActivity.getSharedPreferences(Statistic.SHARED_PREF_SHUFFLE,Context.MODE_PRIVATE)
        val isSuffled = shuffleModePref?.getBoolean("feature",false)
        if(isSuffled as Boolean){
            currentSong?.isSuffle = true
            currentSong?.isLoop = false
            ib_suffle.setBackgroundResource(R.drawable.ic_shuffle_shaded_24dp)
            ib_loop.setBackgroundResource(R.drawable.ic_replay_black_24dp)
        }else{
            currentSong?.isSuffle = false
            ib_suffle.setBackgroundResource(R.drawable.ic_shuffle_black_24dp)
        }
        val shuffleLoopPref = myActivity.getSharedPreferences(Statistic.SHARED_PREF_LOOP,Context.MODE_PRIVATE)
        val isLoopAllowed = shuffleLoopPref?.getBoolean("feature",false)
        if(isLoopAllowed as Boolean){
            currentSong?.isSuffle = false
            currentSong?.isLoop = true
            ib_suffle.setBackgroundResource(R.drawable.ic_shuffle_black_24dp)
            ib_loop.setBackgroundResource(R.drawable.ic_replay_shade_24dp)
        }else{
            currentSong?.isLoop = false
            ib_loop.setBackgroundResource(R.drawable.ic_replay_black_24dp)
        }
    }
    fun clickHandler(){
        ib_suffle.setOnClickListener {
            val editorShuffle = myActivity.getSharedPreferences(Statistic.SHARED_PREF_SHUFFLE,Context.MODE_PRIVATE).edit()
            val editorLoop = myActivity.getSharedPreferences(Statistic.SHARED_PREF_LOOP,Context.MODE_PRIVATE).edit()

            if(currentSong?.isSuffle as Boolean){
                ib_suffle.setBackgroundResource(R.drawable.ic_shuffle_black_24dp)
                currentSong?.isSuffle =false
                editorShuffle.putBoolean("feature",false)
                editorShuffle.apply()
            }else{
                currentSong?.isSuffle = true
                currentSong?.isLoop = false
                ib_suffle.setBackgroundResource(R.drawable.ic_shuffle_shaded_24dp)
                ib_loop.setBackgroundResource(R.drawable.ic_replay_black_24dp)

                editorLoop.putBoolean("feature",false)
                editorLoop.apply()
                editorShuffle.putBoolean("feature",true)
                editorShuffle.apply()
            }
        }
        ib_next.setOnClickListener {
            currentSong?.isPlaying = true
            if(currentSong?.isSuffle as Boolean)
                playNext("playNextLikeNormalShuffle")
            else
                playNext("playNextNormal")
        }
        ib_previuos.setOnClickListener {
            currentSong?.isPlaying = true
            if(currentSong?.isLoop as Boolean){
                ib_loop?.setBackgroundResource(R.drawable.ic_replay_black_24dp)
            }
            playPrevious()
        }
        ib_loop.setOnClickListener {
            val editorShuffle = myActivity.getSharedPreferences(Statistic.SHARED_PREF_SHUFFLE,Context.MODE_PRIVATE).edit()
            val editorLoop = myActivity.getSharedPreferences(Statistic.SHARED_PREF_LOOP,Context.MODE_PRIVATE).edit()
            if(currentSong?.isLoop as Boolean){
                currentSong?.isLoop = false
                ib_loop?.setBackgroundResource(R.drawable.ic_replay_black_24dp)
                editorLoop.putBoolean("feature",false)
                editorLoop.apply()
            }else{
                currentSong?.isLoop = true
                currentSong?.isSuffle = false
                ib_loop.setBackgroundResource(R.drawable.ic_replay_shade_24dp)
                ib_suffle.setBackgroundResource(R.drawable.ic_shuffle_black_24dp)
                editorShuffle.putBoolean("feature",false)
                editorShuffle.apply()
                editorLoop.putBoolean("feature",true)
                editorLoop.apply()
            }
        }
        ib_playPause.setOnClickListener {
            if(mediaPlayer?.isPlaying as Boolean){
                mediaPlayer?.pause()
                currentSong?.isPlaying =false
                ib_playPause.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp)
            }else{
                mediaPlayer?.start()
                currentSong?.isPlaying =true
                ib_playPause.setBackgroundResource(R.drawable.ic_pause_circle_filled_white_24dp)
            }
        }
    }

    fun playNext(check:String){
        if(check.equals("playNextNormal",true)){
            currentSongPosition = currentSongPosition+1
        }else if(check.equals("playNextLikeNormalShuffle",true)){
            var random = Random()
            var randomPosition = random.nextInt(songList?.size?.plus(1) as Int)
            currentSongPosition = randomPosition
        }
        if(currentSongPosition == songList?.size)
            currentSongPosition = 0

        currentSong?.isLoop = false

        var nextSong = songList?.get(currentSongPosition)
        currentSong?.songTitile = nextSong?.songTitle
        currentSong?.songPath = nextSong?.songData
        currentSong?.currentPosition = currentSongPosition
        currentSong?.songId = nextSong?.songId as Long
        currentSong?.songArtist = nextSong?.artist
        updateTextView(currentSong?.songTitile as String,currentSong?.songArtist as String)
        mediaPlayer?.reset()
        try{
            mediaPlayer?.setDataSource(myActivity,Uri.parse(currentSong?.songPath))
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            processInformation(mediaPlayer as MediaPlayer)
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }

    }

    fun playPrevious(){
        currentSongPosition = currentSongPosition - 1
        if(currentSongPosition == -1)
            currentSongPosition = 0
        if(currentSong?.isPlaying as Boolean)
            ib_playPause?.setBackgroundResource(R.drawable.ic_pause_circle_filled_white_24dp)
        else
            ib_playPause?.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp)
        currentSong?.isLoop = false
        val nextSong = songList?.get(currentSongPosition)
        currentSong?.songTitile = nextSong?.songTitle
        currentSong?.songPath = nextSong?.songData
        currentSong?.currentPosition = currentSongPosition
        currentSong?.songId = nextSong?.songId as Long
        currentSong?.songArtist = nextSong?.artist
        updateTextView(currentSong?.songTitile as String,currentSong?.songArtist as String)
        mediaPlayer?.reset()
        try{
            mediaPlayer?.setDataSource(myActivity,Uri.parse(currentSong?.songPath))
            mediaPlayer?.prepare()
            mediaPlayer?.start()
            processInformation(mediaPlayer as MediaPlayer)
        }catch (e:java.lang.Exception){
            e.printStackTrace()
        }


    }

    fun onSongComplete(){
        if(currentSong?.isSuffle as Boolean){
            playNext("playNextLikeNormalShuffle")
        }else{
            if(currentSong?.isLoop as Boolean){
                currentSong?.isPlaying = true
                val nextSong = songList?.get(currentSongPosition)
                currentSong?.songTitile = nextSong?.songTitle
                currentSong?.songArtist = nextSong?.artist
                currentSong?.songId = nextSong?.songId
                currentSong?.songPath = nextSong?.songData
                mediaPlayer?.reset()
                try{
                    mediaPlayer?.setDataSource(myActivity,Uri.parse(currentSong?.songPath))
                    mediaPlayer?.prepare()
                    mediaPlayer?.start()
                    processInformation(mediaPlayer as MediaPlayer)
                }catch (e:java.lang.Exception){
                    e.printStackTrace()
                }

            }else{
                playNext("playNextNormal")
            }
        }
    }

    fun updateTextView(songTitle:String,songArtist:String){
        tv_songTitle.text = songTitle
        tv_song_artist.text = songArtist
    }

    fun processInformation(mediaPlayer:MediaPlayer){
        val finalTime = mediaPlayer.duration
        val startTime = mediaPlayer.currentPosition
        sb_songSeekBar.max = finalTime
        tv_startTime.setText(String.format("%d:%d",TimeUnit.MILLISECONDS.toMinutes(startTime?.toLong() as Long),TimeUnit.MILLISECONDS.toSeconds(startTime.toLong() as Long) - TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(startTime.toLong() as Long))))
        tv_endTime.setText(String.format("%d:%d",TimeUnit.MILLISECONDS.toMinutes(finalTime?.toLong() as Long),TimeUnit.MILLISECONDS.toSeconds(finalTime.toLong() as Long) - TimeUnit.MILLISECONDS.toSeconds(TimeUnit.MILLISECONDS.toMinutes(finalTime.toLong() as Long))))
        sb_songSeekBar.setProgress(startTime)
        Handler().postDelayed(updateSongTime,1000)

    }

    override fun onResume() {
        super.onResume()
        audioVisualization?.onResume()
    }

    override fun onPause() {
        audioVisualization?.onPause()
        super.onPause()

    }

    override fun onDestroy() {
        audioVisualization?.release()
        super.onDestroy()

    }

}
