package com.example.flo.data.local

import androidx.room.*
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Like

@Dao
interface AlbumDao {
    @Insert
    fun insert(album: Album)

    @Insert
    fun likeAlbum(like: Like)

    @Update
    fun update(album: Album)

    @Delete
    fun delete(album: Album)

    @Query("SELECT * FROM AlbumTable")
    fun getAlbums(): List<Album>

    @Query("SELECT * FROM AlbumTable WHERE id = :id")
    fun getAlbum(id: Int): Album

    @Query("SELECT id FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun isLikedAlbum(userId:Int, albumId:Int) : Int?

    @Query("DELETE FROM LikeTable WHERE userId = :userId AND albumId = :albumId")
    fun disLikedAlbum(userId: Int, albumId: Int)

    // as를 이용해 LikeTable 대신 LT, AlbumTable 대신 AT 사용
    // LikeTable의 앨범 아이디와 AlbumTable의 아이디가 같은 것끼리 붙여준다
    @Query("SELECT AT.* FROM LikeTable as LT LEFT JOIN AlbumTable as AT ON LT.albumId = AT.id WHERE LT.userId = :userId")
    fun getLikedAlbums(userId: Int) : List<Album>
}
