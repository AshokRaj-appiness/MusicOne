package com.example.musicone.ui.Services

import android.app.Service
import android.content.Intent
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Binder
import android.os.IBinder
import android.os.Parcelable
import com.example.musicone.ui.model.CurrentSong
import com.example.musicone.ui.model.Songs
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList

class MusicplayerService : Service() {

    private val mBinder: IBinder =
        LocalBinder() // Variable for passing the instance of service to activity

    var currentSong: CurrentSong? = null
    //Media player

    var songs: Songs? = null
    var path: String? = null
    var songTitile: String? = null
    var songArtists: String? = null
    var songList: ArrayList<Songs>? = null
    var currentSongPosition: Int = 0
    var audioSessionId: Int = 0
    private var songInfo: ((songTitle: String, currentSong: String) -> Unit)? = null

    val mediaPlayer = MediaPlayer()


    override fun onBind(p0: Intent?): IBinder? {
        songs = p0?.getParcelableExtra("Song") as Songs
        currentSong = CurrentSong()
        currentSong?.isPlaying = true
        currentSong?.isLoop = false
        currentSong?.isSuffle = false

        path = songs?.songData
        songTitile = songs?.songTitle
        songArtists = songs?.artist

        currentSongPosition = p0?.getIntExtra("position", 0)
        songList = p0?.getParcelableArrayListExtra<Songs>("SongList") as ArrayList<Songs>
        currentSong?.songPath = path
        currentSong?.currentPosition = currentSongPosition
        currentSong?.songTitile = songTitile
        currentSong?.songArtist = songArtists

        initplayer()
        return mBinder
    }

    inner class LocalBinder : Binder() {
        fun getServiceInstance(): MusicplayerService {
            return this@MusicplayerService
        }
    }

    private fun initplayer() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        try {
            mediaPlayer.setDataSource(this, Uri.parse(path))
            mediaPlayer.prepare()
            audioSessionId = mediaPlayer.audioSessionId

        } catch (e: Exception) {
            e.printStackTrace()
        }
        mediaPlayer?.start()
        mediaPlayer?.setOnCompletionListener {
            onSongComplete()
        }

        }

        fun playNext(check: String) {
            if (check.equals("playNextNormal", true)) {
                currentSongPosition = currentSongPosition + 1
            } else if (check.equals("playNextLikeNormalShuffle", true)) {
                var random = Random()
                var randomPosition = random.nextInt(songList?.size?.plus(1) as Int)
                currentSongPosition = randomPosition
            }
            if (currentSongPosition == songList?.size)
                currentSongPosition = 0

            currentSong?.isLoop = false

            var nextSong = songList?.get(currentSongPosition)
            currentSong?.songTitile = nextSong?.songTitle
            currentSong?.songPath = nextSong?.songData
            currentSong?.currentPosition = currentSongPosition
            currentSong?.songId = nextSong?.songId as Long
            currentSong?.songArtist = nextSong?.artist
            songInfo?.invoke(currentSong?.songTitile as String, currentSong?.songArtist as String)
            mediaPlayer?.reset()
            try {
                mediaPlayer?.setDataSource(this, Uri.parse(currentSong?.songPath))
                mediaPlayer?.prepare()
                mediaPlayer?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

        fun playPrevious() {
            currentSongPosition--
            if (currentSongPosition == -1)
                songList?.let { currentSongPosition = it.lastIndex }
//        if (currentSong?.isPlaying as Boolean)
//            ib_playPause?.setBackgroundResource(R.drawable.ic_pause_circle_filled_white_24dp)
//        else
//            ib_playPause?.setBackgroundResource(R.drawable.ic_play_circle_filled_black_24dp)
            currentSong?.isLoop = false
            val nextSong = songList?.get(currentSongPosition)
            currentSong?.songTitile = nextSong?.songTitle
            currentSong?.songPath = nextSong?.songData
            currentSong?.currentPosition = currentSongPosition
            currentSong?.songId = nextSong?.songId as Long
            currentSong?.songArtist = nextSong?.artist
            songInfo?.invoke(currentSong?.songTitile as String, currentSong?.songArtist as String)
            mediaPlayer?.reset()
            try {
                mediaPlayer?.setDataSource(this, Uri.parse(currentSong?.songPath))
                mediaPlayer?.prepare()
                mediaPlayer?.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }

        fun onSongComplete() {
            if (currentSong?.isSuffle as Boolean) {
                playNext("playNextLikeNormalShuffle")
            } else {
                if (currentSong?.isLoop as Boolean) {
                    currentSong?.isPlaying = true
                    val nextSong = currentSongPosition?.let { songList?.get(it) }
                    currentSong?.songTitile = nextSong?.songTitle
                    currentSong?.songArtist = nextSong?.artist
                    currentSong?.songId = nextSong?.songId
                    currentSong?.songPath = nextSong?.songData
                    mediaPlayer?.reset()
                    try {
                        mediaPlayer?.setDataSource(this, Uri.parse(currentSong?.songPath))
                        mediaPlayer?.prepare()
                        mediaPlayer?.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                } else {
                    playNext("playNextNormal")
                }
            }
        }

        fun setSongInfoListner(SongInfo: (songTitle: String, currentSong: String) -> Unit) {
            songInfo = SongInfo
        }


    }