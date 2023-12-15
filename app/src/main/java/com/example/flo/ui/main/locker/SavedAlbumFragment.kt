package com.example.flo.ui.main.locker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.flo.data.local.SongDatabase
import com.example.flo.databinding.FragmentLockerSavedalbumBinding
import com.example.flo.ui.main.album.AlbumLockerRVAdapter

class SavedAlbumFragment : Fragment() {

    lateinit var binding: FragmentLockerSavedalbumBinding
    lateinit var albumDB: SongDatabase

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLockerSavedalbumBinding.inflate(inflater, container, false)

        albumDB = SongDatabase.getInstance(requireContext())!!

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        initRecyclerview()
    }

    private fun initRecyclerview(){
        binding.savedalbumAlbumListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val albumRVAdapter = AlbumLockerRVAdapter()

        // 리스너 객체 생성 및 전달

        albumRVAdapter.setMyItemClickListener(object : AlbumLockerRVAdapter.MyItemClickListener{
            override fun onRemoveSong(albumId: Int) {
                albumDB.albumDao().getLikedAlbums(getJwt())
                albumDB.albumDao().disLikedAlbum(getJwt(), albumId)    // 좋아요 한 데이터를 지워준다
            }
        })


        binding.savedalbumAlbumListRv.adapter = albumRVAdapter
        albumRVAdapter.addAlbums(albumDB.albumDao().getLikedAlbums(getJwt()) as ArrayList)
    }

    // 로그인한 유저가 누군지 return
    private fun getJwt():Int{
        val spf = activity?.getSharedPreferences("auth", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("jwt", 0)   // 값이 없으면 0 반환
    }
}