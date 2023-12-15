package com.example.flo.ui.main.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.flo.*
import com.example.flo.data.entities.Album
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.remote.AlbumService
import com.example.flo.data.remote.FloAlbumResult
import com.example.flo.data.remote.FloAlbumSongs
import com.example.flo.data.remote.HomeView
import com.example.flo.databinding.FragmentHomeBinding
import com.example.flo.ui.main.MainActivity
import com.example.flo.ui.main.album.AlbumFragment
import com.example.flo.ui.main.album.AlbumListRVAdapter
import com.example.flo.ui.main.album.AlbumRVAdapter
import me.relex.circleindicator.CircleIndicator3

class HomeFragment : Fragment(), HomeView {

    lateinit var binding: FragmentHomeBinding
    private var albumDatas = ArrayList<Album>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        inputDumyAlbums()   //데이터 추가

        //------------ 앨범 adapter와 데이터 리스트 연결 -------------
        val albumRVAdapter = AlbumRVAdapter(albumDatas)
        binding.homeTodayMusicAlbumRv.adapter = albumRVAdapter
        //레이아웃 메니저 설정
        binding.homeTodayMusicAlbumRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //어댑터에 리스너 객체 넘겨줌 - 인터페이스 : MyItemClickListener
        albumRVAdapter.setMyItemClickListener(object: AlbumRVAdapter.MyItemClickListener{

            override fun onItemClick(album: Album) {    //클릭한 앨범 정보를 적용해줌
                changeAlbumFunction(album)
            }
            override fun onRemoveAlbum(position: Int) { //앨범 삭제
                albumRVAdapter.removeItem(position)
            }

            override fun onBtnClick(album: Album) {  //miniplayer에 값 적용
                (activity as MainActivity).handleAlbumClick(album)
            }
        })

        //--------------- pannel 전환 ---------------
        val pannelAdapter = PannelVPAdapter(this)
        val indicator: CircleIndicator3 = binding.homeIndicator
        indicator.setViewPager(binding.homePannelVp)

        pannelAdapter.addFragment(PannelFragment(R.drawable.img_first_album_default))
        pannelAdapter.addFragment(PannelFragment(R.drawable.img_second_album))
        binding.homePannelVp.adapter = pannelAdapter    //viewpager에 연결
        binding.homeIndicator.setViewPager(binding.homePannelVp)   //indicator 연결


        //-------- banner 이미지 전환 ------------
        var bannerAdapter = BannerVPAdapter(this)
        //fragment 추가
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp))
        bannerAdapter.addFragment(BannerFragment(R.drawable.img_home_viewpager_exp2))

        binding.homeBannerVp.adapter = bannerAdapter
        binding.homeBannerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL

        //imageview radius 적용
        binding.homeBannerDiscoveryIv.clipToOutline = true

        return binding.root
    }

    //----------- DB에 없을시 데이터 넣어주기 ---------------
    private fun inputDumyAlbums() {
        val albumDB = SongDatabase.getInstance(requireContext())!!
        val albums = albumDB.albumDao().getAlbums()

        if (albums.isNotEmpty()) {
            albumDatas = albums as ArrayList<Album>     //어댑터와 연결
            return   //데이터 이미 존재하면 함수 종료
        }else {
            albumDB.albumDao().insert(Album(1, "Pablo Honey", "Radiohead",
                R.drawable.img_album_creep
            ))
            albumDB.albumDao().insert(Album(2, "I met you when I was 18. (the playlist)", "Lauv",
                R.drawable.img_album_lauv
            ))
            albumDB.albumDao().insert(Album(3, "SOS", "SZA", R.drawable.img_album_kilbill))
            albumDB.albumDao().insert(Album(4, "Camila", "Camila Cabello",
                R.drawable.img_album_neverbethesame
            ))
            albumDB.albumDao().insert(Album(5, "golden hour", "JVKE",
                R.drawable.img_album_goldenhour
            ))
            albumDB.albumDao().insert(Album(6, "IU 5th Album 'LILAC'", "아이유 (IU)",
                R.drawable.img_album_exp2
            ))
            albumDB.albumDao().insert(Album(7, "Next Level", "에스파 (AESPA)",
                R.drawable.img_album_exp3
            ))
            albumDB.albumDao().insert(Album(8, "MAP OF THE SOUL : PERSONA", "방탄소년단 (BTS)",
                R.drawable.img_album_exp4
            ))

            //album에 데이터 추가
            albumDatas.addAll(albumDB.albumDao().getAlbums())
        }

        //데이터 잘 들어갔나 확인용 로그
//        val _albums = albumDB.albumDao().getAlbums()
//        Log.d("DB data", _albums.toString())
    }

    //리사이클러뷰의 item이 클릭되면 AlbumFragment로 전환되며 데이터(ArrayList)를 번들로 넘겨줌
    private fun changeAlbumFunction(album: Album) {
        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
//                    val gson = Gson()
//                    val albumJson = gson.toJson(album)
//                    putString("album", albumJson)
                    putInt("albumId", album.id)
                }
            })
            .commitAllowingStateLoss()
    }

    private fun changeServerAlbum(albums: FloAlbumSongs){
        activity?.supportFragmentManager!!.beginTransaction()
            .replace(R.id.main_frm, AlbumFragment().apply {
                arguments = Bundle().apply {
                    putString("server_title", albums.title)
                    putString("server_singer", albums.singer)
                    putString("server_imgUrl", albums.coverImgUrl)
                    putInt("server_albumId", albums.albumIdx)
                }
            })
            .commitAllowingStateLoss()
    }


    // 팟캐스트 업데이트

    override fun onStart() {
        super.onStart()
        getAlbumList()
    }

    private fun getAlbumList(){
        val albumService = AlbumService()
        albumService.setHomeView(this)

        albumService.getAlbumList()
    }

    private fun initRecyclerView(result: FloAlbumResult){
        var floAlbumAdapter = AlbumListRVAdapter(requireContext(), result)

        binding.homePotcastRv.adapter = floAlbumAdapter
        binding.homePotcastRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        floAlbumAdapter.setMyItemClickListener(object: AlbumListRVAdapter.MyItemClickListener {
            override fun onItemClick(albums: FloAlbumSongs) {
                changeServerAlbum(albums)
            }
        })
    }

    override fun onGetAlbumLoading() {
        binding.homeLoadingPb.visibility = View.VISIBLE
    }

    override fun onGetAlbumSuccess(code: Int, result: FloAlbumResult) {
        binding.homeLoadingPb.visibility = View.GONE
        initRecyclerView(result)
    }

    override fun onGetAlbumFailure(code: Int, message: String) {
        binding.homeLoadingPb.visibility = View.GONE
        Log.d("HOME-FRAG/AB-RESPONSE", message)
    }
}