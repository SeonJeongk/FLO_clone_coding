package com.example.flo.ui.main.album

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.flo.ui.main.home.HomeFragment
import com.example.flo.R
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Like
import com.example.flo.databinding.FragmentAlbumBinding
import com.example.flo.ui.main.MainActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class AlbumFragment : Fragment() {
    lateinit var binding : FragmentAlbumBinding
    private var gson: Gson = Gson()

    private val information = arrayListOf("수록곡", "상세정보", "영상")

    private var isLiked : Boolean = false   // 좋아요 여부

    lateinit var albumDB: SongDatabase
    lateinit var album: Album

    var nowPos = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAlbumBinding.inflate(inflater, container, false)

        var albumId = arguments?.getInt("albumId")

        if(albumId == 0){
            setServerAlbum()   // api 데이터 적용
        }else{
            setAlbum()
        }

        isLiked = isLikedAlbum(album.id)

        setInit(album)  //Home에서 넘어온 데이터 반영
        setOnClickListeners(album)

        //HomeFragment로 돌아간다
        binding.albumBtnBackIv.setOnClickListener{
            (context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_frm, HomeFragment())
                .commitAllowingStateLoss()
        }

        //Album tab 바꾸기 위해 Adater와 연결
        val albumIdx = arguments?.getInt("server_albumId")
        val albumAdapter = AlbumVPAdapter(this, albumIdx!!)
        binding.albumContentVp.adapter = albumAdapter

        TabLayoutMediator(binding.albumContentTb, binding.albumContentVp){  //tablayout을 viewPager2와 연결해준다
            tab, position ->
            tab.text = information[position]
        }.attach()


        //Imageview radius 적용
        binding.albumSongCoverIv.clipToOutline = true

        return binding.root
    }


    // api 없는 위의 앨범 초기화
    private fun setAlbum(){
        albumDB = SongDatabase.getInstance(requireContext())!!
        var albumId = arguments?.getInt("albumId")
        nowPos = albumId!!

        album = albumDB.albumDao().getAlbum(nowPos)
    }

    // api값을 반영해 팟캐스트 내용으로 초기화
    private fun setServerAlbum() {
        // api로 받아온 데이터를 적용한다
        var albumTitle = arguments?.getString("server_title")
        var albumSinger = arguments?.getString("server_singer")
        var albumImg = arguments?.getString("server_imgUrl")

        Glide.with(this).load(albumImg).into(binding.albumSongCoverIv)
        binding.albumTitleTv.text = albumTitle
        binding.albumSingerTv.text = albumSinger

        album = Album(0, albumTitle, albumSinger, null) //album 초기화
    }

    // 기존의 초기화
    private fun setInit(album: Album){
        if (album.coverImg != null) {
            binding.albumSongCoverIv.setImageResource(album.coverImg!!)
        }
        binding.albumTitleTv.text = album.title.toString()
        binding.albumSingerTv.text = album.singer.toString()

        // 좋아요 설정
        if(isLiked){
            binding.albumBtnLikeIv.setImageResource(R.drawable.ic_my_like_on)
            Log.d("setInit", "title : "+ album.title.toString() + " 좋아요 true")
        } else {
            binding.albumBtnLikeIv.setImageResource(R.drawable.ic_my_like_off)
            Log.d("setInit", "title : "+ album.title.toString() + " 좋아요 false")
        }
    }

    // 로그인한 유저가 누군지 return
    private fun getJwt():Int{
        val spf = activity?.getSharedPreferences("auth2", AppCompatActivity.MODE_PRIVATE)
        return spf!!.getInt("userIdx", 0)   // 값이 없으면 0 반환
    }
    //--------------------------------------------------------------------- 수정했음 userIdx로------------------

    // 로그인 한 유저가 좋아한 곡들을 likeAlbum에 넣어준다
    private fun likedAlbum(userId : Int, albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val like = Like(userId, albumId)

        songDB.albumDao().likeAlbum(like)
    }

    // 홈에서 Album을 눌렀을 때 좋아요를 눌렀나 파악
    private fun isLikedAlbum(albumId: Int): Boolean{
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        val likeId : Int? = songDB.albumDao().isLikedAlbum(userId, albumId)    // 어떤 앨범을 좋아했는지 알려주는 변수

        return likeId != null   //좋아요를 한 게 없으면(null) false return
    }

    // 유저가 좋아요를 해제하면 LikedTable에서 유저 아이디와 앨범 아이디가 같은 데이터를 지워준다
    private fun disLikedAlbum(albumId: Int){
        val songDB = SongDatabase.getInstance(requireContext())!!
        val userId = getJwt()

        songDB.albumDao().disLikedAlbum(userId, albumId)    // 좋아요 한 데이터를 지워준다
    }

    private fun setOnClickListeners(album: Album){
        val userId = getJwt()
        binding.albumBtnLikeIv.setOnClickListener {
            if(isLiked){    //좋아요 취소
                binding.albumBtnLikeIv.setImageResource(R.drawable.ic_my_like_off)
                disLikedAlbum(album.id)
                Log.d("리스너", "앨범 : " + album.id + " 좋아요 취소")
            } else {    //좋아요 추가
                binding.albumBtnLikeIv.setImageResource(R.drawable.ic_my_like_on)
                likedAlbum(userId, album.id)
                Log.d("리스너", "앨범 : " + album.id + " 좋아요 추가")
            }
        }
    }
}