package com.example.flo.ui.main

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.flo.*
import com.example.flo.data.entities.Album
import com.example.flo.data.entities.Song
import com.example.flo.data.local.SongDatabase
import com.example.flo.databinding.ActivityMainBinding
import com.example.flo.ui.main.home.HomeFragment
import com.example.flo.ui.main.locker.LockerFragment
import com.example.flo.ui.main.look.LookFragment
import com.example.flo.ui.main.search.SearchFragment
import com.example.flo.ui.song.SongActivity

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null

    var songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0  //지금 보여지는 song

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setTheme(R.style.Theme_FLO) //splach에서 다시 main theme으로 돌아와준다

        binding = ActivityMainBinding.inflate(layoutInflater)   //--> binding으로 코드 가독성 높아짐
        setContentView(binding.root)

        inputDumySongs()    //db값 불러오기
        initSong()
        initBottomNavigation()

        initClickListener()

        // id정보를 sharedPreference에 저장해 intent로 넘겨준다
        binding.mainPlayerCl.setOnClickListener {
            val sharedPref = getSharedPreferences("song", MODE_PRIVATE)
            val editor = sharedPref.edit()

            editor.putInt("songId", songs[nowPos].id)
            editor.apply()

            val intent = Intent(this, SongActivity::class.java)
            startActivity(intent)
        }

        Log.d("MAIN/JWT_TO_SERVER", getJwt().toString())
    }

    private fun getJwt(): String? {
        val spf = this.getSharedPreferences("auth2", AppCompatActivity.MODE_PRIVATE)

        return spf!!.getString("jwt", "")
    }

    private fun initClickListener(){
        binding.mainMiniplayerPlayBtn.setOnClickListener {
            setPlayerStatus(true)
        }
        binding.mainMiniplayerPauseBtn.setOnClickListener {
            setPlayerStatus(false)
        }
        binding.mainMiniplayerPreviousBtn.setOnClickListener {
            moveSong(-1)    //이전곡
            setPlayerStatus(true)
        }
        binding.mainMiniplayerNextBtn.setOnClickListener {
            moveSong(+1)    //다음곡
            setPlayerStatus(true)
        }
    }

    //------------ miniplayer 값 바꾸기 --------------
    private  fun setMiniPlayer(song: Song){
        val sharedPref = getSharedPreferences("song", MODE_PRIVATE)
        val second = sharedPref.getInt("songSec", 0)    //SongActivity의 progress 반영

        binding.mainMiniplayerTitileTv.text = song.title
        binding.mainMiniplayerSingerTv.text = song.singer
        binding.mainProgressSb.progress = (second * 100000 / song.playTime)    //십만은 seekbar의 max값
        val music =resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)
    }

    //액티비티가 화면에 보여지기 직전에 호출
    //--------- sharedPreference에 저장된 id로 데이터 불러온다 -----------
    override fun onStart() {
        super.onStart()

        //id값으로 데이터 불러오기
        val sharedPref = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPref.getInt("songId", 1)

        nowPos = getPlayingSongPosition(songId)  //현재 재생중인 노래 id값 (index)

        songDB = SongDatabase.getInstance(this)!!

        songs[nowPos] = if(songId == 0){
            //songId가 null일 때 넣을 값 지정
            songDB.songDao().getSong(1)
        } else{
            //songId에 해당하는 데이터가 존재할 때 저장된 값 가져옴
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", songs[nowPos].id.toString())

        //song값 miniplayer에 반영하기
        setMiniPlayer(songs[nowPos])
    }

    override fun onPause() {
        super.onPause()

        songs[nowPos].second = ((binding.mainProgressSb.progress * songs[nowPos].playTime)/100)/1000
        songs[nowPos].isPlaying = false

        val sharedPref = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPref.edit()

        // 현재 재생 중인 노래의 second 값을 SharedPreferences에 저장
        editor.putInt("songSec", songs[nowPos].second)
        editor.putInt("songId", songs[nowPos].id)

        Log.d("main 저장", "songid : " + songs[nowPos].id)

        editor.apply()
    }
    override fun onResume() {
        super.onResume()
        val sharedPref = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPref.getInt("songId", 1)
        val second = sharedPref.getInt("songSec", 0)

        nowPos = getPlayingSongPosition(songId)  //현재 재생중인 노래 id값 (index)

        binding.mainProgressSb.progress = (second * 100000) / songs[nowPos].playTime

        songs[nowPos] = if(songId == 0){
            //songId가 null일 때 넣을 값 지정
            songDB.songDao().getSong(1)
        } else{
            //songId에 해당하는 데이터가 존재할 때 저장된 값 가져옴
            songDB.songDao().getSong(songId)
        }

        Log.d("song ID", songs[nowPos].id.toString())

        //song값 miniplayer에 반영하기
        setMiniPlayer(songs[nowPos])
    }

    //----------- MainActivity의 Miniplayer값 --------------
    fun handleAlbumClick(album: Album){
        val sharedPref = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPref.getInt("songId", 1)

        nowPos = getPlayingSongPosition(songId)  //현재 재생중인 노래 id값 (index)

        songs = songDB.songDao().getSongsInAlbum(album.id) as ArrayList<Song>   //수록곡

        //songs[nowPos]에 첫 번째 수록곡을 넣어준다
        songs[nowPos] = songs[0]

        Log.d("main 저장 앨범", "songid : " + songs[nowPos].id)

        // 노래가 바뀌면 second값을 0으로 바꿔준다
        val editor = sharedPref.edit()
        editor.putInt("songSec", 0)
        editor.apply()

        // Miniplayer 업데이트
        setMiniPlayer(songs[nowPos])
    }

    //---------- 노래 넘기기 ------------
    private fun moveSong(direct: Int){
        // 범위 외의 값
        if(nowPos + direct <0){
            Toast.makeText(this, "first song", Toast.LENGTH_SHORT).show()
            return
        }
        if(nowPos + direct >= songs.size){
            Toast.makeText(this, "last song", Toast.LENGTH_SHORT).show()
            return
        }

        nowPos += direct

        timer.interrupt()   //thread 정지
        startTimer()    //다음곡 시작

        mediaPlayer?.release()
        mediaPlayer = null

        setMiniPlayer(songs[nowPos])
    }

    //------------- 노래제목 돌려받기 ---------------
//    companion object { const val STRING_INTENT_KEY = "song_title"}
//    private val getResultText = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ){ result ->
//        if(result.resultCode == Activity.RESULT_OK){
//            val returnString = result.data?.getStringExtra(STRING_INTENT_KEY).toString()
//            Toast.makeText(this@MainActivity, returnString, Toast.LENGTH_SHORT).show()
//            Log.d("song_title", returnString)
//        }
//    }

    private fun initSong(){
        val sharedPref = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPref.getInt("songId", 0)

        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())

        nowPos = getPlayingSongPosition(songId)  //현재 재생중인 노래 id값 (index)
        Log.d("now Song Id", songs[nowPos].id.toString())

        startTimer()   //timer 시작
        setMiniPlayer(songs[nowPos])
    }
    private fun getPlayingSongPosition(songId : Int): Int{
        for(i in 0 until songs.size){
            if(songs[nowPos].id == songId){  //일치하는 id 있다면 해당 index return
                return i
            }
        }
        return 0
    }

    private fun startTimer(){
        timer = Timer(songs[nowPos].playTime, songs[nowPos].isPlaying)
        timer.start()
        Log.d("start timer", "timer start 호출됨")
    }

    //---------- Thread를 상속받은 Timer class - 노래 재생 관리 ------------
    inner class Timer (private val playTime: Int, var isPlaying: Boolean = true):Thread(){
        private var second : Int = 0
        private var mills: Float = 0f
        override fun run() {
            super.run()
            try {
                while(true){
                    //재생 완료
                    if(second >= playTime){
                        mediaPlayer?.stop()    //재생시간 줄여놔서 추가한 재생 중지 명령어
                        mediaPlayer?.prepare()
                        mediaPlayer?.seekTo(0) // 재생 위치 초기화

                        second = 0
                        mills = 0f

                        runOnUiThread { //seekbar 진행도 및 현재 재생시간 초기화
                            binding.mainProgressSb.progress = 0

                            setPlayerStatus(false)
                            songs[nowPos].isPlaying = false
                            startTimer()
                        }

                        break
                    }

                    if(isPlaying){  //진행 시간 관리
                        sleep(50)
                        mills +=50

                        runOnUiThread {
                            binding.mainProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }

                        if(mills % 1000 == 0f){
                            second++    //1초마다 시간 변경
                        }
                    }
                }
            } catch (e: InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }

    //재생상태 설명
    fun setPlayerStatus(isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){ //노래 재생
            binding.mainMiniplayerPlayBtn.visibility = View.GONE
            binding.mainMiniplayerPauseBtn.visibility = View.VISIBLE
            mediaPlayer?.start()
            Log.d("player status", "노래 재생")
        }
        else { //노래 정지
            binding.mainMiniplayerPlayBtn.visibility = View.VISIBLE
            binding.mainMiniplayerPauseBtn.visibility = View.GONE

            Log.d("정지버튼", "mediaplayer는 " + mediaPlayer?.isPlaying + " timer는 " + timer.isPlaying)
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
    }

    //----------- DB에 없을시 데이터 넣어주기 ---------------
    private fun inputDumySongs() {
        val songDB = SongDatabase.getInstance(this)!!
        val songs = songDB.songDao().getSongs()

        if(songs.isNotEmpty()) return   //데이터 이미 존재하면 함수 종료

        songDB.songDao().insert(
            Song(
                "Creep", "Radiohead",
                R.drawable.img_album_creep,
                0, 10, false,
                "music_creep",
                false, 1
            )
        )
        songDB.songDao().insert(
            Song(
                "Never Not", "Lauv",
                R.drawable.img_album_nevernot,
                0, 10, false,
                "music_nevernot",
                false, 2
            )
        )
        songDB.songDao().insert(
            Song(
                "I Like Me Better", "Lauv",
                R.drawable.img_album_nevernot,
                0, 10, false,
                "music_nevernot",
                false, 2
            )
        )
        songDB.songDao().insert(
            Song(
                "Kill Bill", "SZA",
                R.drawable.img_album_kilbill,
                0, 10, false,
                "music_killbill",
                false, 3
            )
        )
        songDB.songDao().insert(
            Song(
                "SOS", "SZA",
                R.drawable.img_album_kilbill,
                0, 10, false,
                "music_killbill",
                false, 3
            )
        )
        songDB.songDao().insert(
            Song(
                "Never Be the Same", "Camila Cabello",
                R.drawable.img_album_neverbethesame,
                0, 10, false,
                "music_nbts",
                false, 4
            )
        )
        songDB.songDao().insert(
            Song(
                "Havana (feat. Young Thug)", "Camila Cabello",
                R.drawable.img_album_neverbethesame,
                0, 10, false,
                "music_nbts",
                false, 4
            )
        )
        songDB.songDao().insert(
            Song(
                "golden hour", "JVKE",
                R.drawable.img_album_goldenhour,
                0, 10, false,
                "music_golden",
                false, 5
            )
        )
        songDB.songDao().insert(
            Song(
                "Lilac", "아이유 (IU)",
                R.drawable.img_album_exp2,
                0, 10, false,
                "music_lilac",
                false, 6
            )
        )
        songDB.songDao().insert(
            Song(
                "Next Level", "에스파 (AESPA)",
                R.drawable.img_album_exp3,
                0, 10, false,
                "music_next",
                false, 7
            )
        )
        songDB.songDao().insert(
            Song(
                "Butter", "방탄소년단 (BTS)",
                R.drawable.img_album_exp,
                0, 10, false,
                "music_butter",
                false, 8
            )
        )
        songDB.songDao().insert(
            Song(
                "작은 것들을 위한 시 (Boy With Luv) (Feat. Halsey)", "방탄소년단 (BTS)",
                R.drawable.img_album_exp4,
                0, 10, false,
                "music_boy",
                false, 8
            )
        )

        //데이터 잘 들어갔나 확인용 로그
        val _songs = songDB.songDao().getSongs()
        Log.d("DB data", _songs.toString())

    }
    private fun initBottomNavigation(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_frm, HomeFragment())
            .commitAllowingStateLoss()

        binding.mainBnv.setOnItemSelectedListener{ item ->
            when (item.itemId) {

                R.id.homeFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, HomeFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }

                R.id.lookFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LookFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.searchFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, SearchFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
                R.id.lockerFragment -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.main_frm, LockerFragment())
                        .commitAllowingStateLoss()
                    return@setOnItemSelectedListener true
                }
            }
            false
        }
    }
}