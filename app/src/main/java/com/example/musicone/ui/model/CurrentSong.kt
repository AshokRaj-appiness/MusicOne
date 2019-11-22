package com.example.musicone.ui.model

class CurrentSong {
    var songArtist: String ?= null
    var songTitile:String ?= null
    var songPath:String ?= null
    var songId:Long ?= null
    var currentPosition:Int = 0
    var isPlaying:Boolean = false
    var isLoop:Boolean = false
    var isSuffle:Boolean = false
    var trackPosition:Int = 0
}