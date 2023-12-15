package com.example.flo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "SongTable")
data class Song (
    val title : String = "",
    val singer : String = "",
    var coverImg: Int? = null,
    var second: Int = 0,
    var playTime: Int = 0,
    var isPlaying: Boolean = false,
    var music: String = "",
    var isLike: Boolean = false,
    var albumIdx: Int = 0   //songDao로 Album과 연결
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}

