package com.example.flo.ui.song

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.flo.R
import com.example.flo.data.local.SongDatabase
import com.example.flo.data.entities.Song
import com.example.flo.databinding.ActivitySongBinding

class SongActivity : AppCompatActivity() {

    lateinit var binding : ActivitySongBinding
    lateinit var timer: Timer
    private var mediaPlayer: MediaPlayer? = null

    val songs = arrayListOf<Song>()
    lateinit var songDB: SongDatabase
    var nowPos = 0  //지금 보여지는 song

    //--------- 재생 알림창 ----------
    fun serviceStart(view: View){
        val intent = Intent(this, Foreground::class.java)
        ContextCompat.startForegroundService(this, intent)
        Log.d("service", "start")
    }
    fun serviceStop(view: View){
        val intent = Intent(this, Foreground::class.java)
        stopService(intent)
        Log.d("service", "stop")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initPlayList()
        initSong()

        initClickListener()

        binding.songAlbumImgIv.clipToOutline = true //imaveView radius 적용
    }

    //---------- 사용자가 focus를 잃었을 때 음악 중지 -----------------
    override fun onPause() {
        super.onPause()

        songs[nowPos].second = ((binding.songProgressSb.progress * songs[nowPos].playTime)/100)/1000
        songs[nowPos].isPlaying = false

        //현재 노래의 id값을 저장해둔다
        val sharedPref = getSharedPreferences("song", MODE_PRIVATE)
        val editor = sharedPref.edit()

        editor.putInt("songId", songs[nowPos].id)  //id값 저장
        editor.putInt("songSec", songs[nowPos].second)  //second값 저장

        Log.d("song저장", "songid : " + songs[nowPos].id)

        editor.apply()
    }

    override fun onDestroy() {
        super.onDestroy()
        timer.interrupt()   //thread 종료
        mediaPlayer?.release()  //미디어플레이어가 갖고있던 리소스 해제
        mediaPlayer = null //미디어플레이어 해제
    }

    //------------ DB에 저장된 데이터 리스트를 songs에 저장 --------------
    private fun initPlayList(){
        songDB = SongDatabase.getInstance(this)!!
        songs.addAll(songDB.songDao().getSongs())
    }

    //------------ 처음 보여질 song을 nowPos로 초기화 ----------------h
    private fun initSong(){
        val sharedPref = getSharedPreferences("song", MODE_PRIVATE)
        val songId = sharedPref.getInt("songId", 1)
        val second = sharedPref.getInt("songSec", 0)
        Log.d("initsong","now song : " + songId.toString())

        nowPos = getPlayingSongPosition(songId)  //현재 재생중인 노래 id값 (index)
        songs[nowPos] = songs[songId]
        Log.d("initsong", "nowPos값은?" +nowPos.toString())
        Log.d("initsong", "now Song Id" + songs[nowPos].id.toString() + " title : " + songs[nowPos].title.toString())

        songs[nowPos].second = second
        startTimer()   //timer 시작
        setPlayer(songs[nowPos])
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

        setPlayer(songs[nowPos])
    }

    //------------- 좋아요 버튼 눌렸을 때 눌린 상태 변경 --------------
    private fun setLike(isLike: Boolean){
        songs[nowPos].isLike = !isLike
        songDB.songDao().updateIsLikeById(!isLike, songs[nowPos].id)

        if(!isLike){
            binding.songBtnLikeIv.setImageResource(R.drawable.ic_my_like_on)
            CustomToast(applicationContext, "좋아요 한 곡에 담겼습니다.").show()

        } else{
            binding.songBtnLikeIv.setImageResource(R.drawable.ic_my_like_off)
            CustomToast(applicationContext, "좋아요 한 곡이 취소되었습니다.").show()
        }
    }

    private fun getPlayingSongPosition(songId : Int): Int{
        for(i in 0 until songs.size){
            if(songs[nowPos].id == songId){  //일치하는 id 있다면 해당 index return
                Log.d("find song position", "songID : " + songId.toString() + " songs[nowPos].id : " + songs[nowPos].id.toString())
                Log.d("find song position", "i값은?" + i)
                return i
            }
        }
        Log.d("find id", "못찾음")
        return 0
    }


    //----------- SongActivity 화면을 받아와 초기화된 정보로 뷰 렌더링 -----------
    //시간 단위: ms
    private fun setPlayer(song: Song){
        binding.songTitleTv.text = song.title
        binding.songSingerTv.text = song.singer
        binding.songAlbumImgIv.setImageResource(song.coverImg!!)
        binding.songProgressStartTv.text = String.format("%02d:%02d", song.second / 60, song.second % 60)
        binding.songProgressEndTv.text = String.format("%02d:%02d", song.playTime / 60, song.playTime % 60)
        binding.songProgressSb.progress = (song.second * 100000 / song.playTime)
        val music =resources.getIdentifier(song.music, "raw", this.packageName)
        mediaPlayer = MediaPlayer.create(this, music)

        if(song.isLike){
            binding.songBtnLikeIv.setImageResource(R.drawable.ic_my_like_on)
        } else{
            binding.songBtnLikeIv.setImageResource(R.drawable.ic_my_like_off)
        }
    }

    fun setPlayerStatus(isPlaying : Boolean){
        songs[nowPos].isPlaying = isPlaying
        timer.isPlaying = isPlaying

        if(isPlaying){ //노래 재생
            binding.songBtnPlayIv.visibility = View.GONE
            binding.songBtnPauseIv.visibility = View.VISIBLE
            mediaPlayer?.start()
            Log.d("player status", "노래 재생")
        }
        else { //노래 정지
            binding.songBtnPlayIv.visibility = View.VISIBLE
            binding.songBtnPauseIv.visibility = View.GONE

            Log.d("정지버튼", "mediaplayer는 " + mediaPlayer?.isPlaying + " timer는 " + timer.isPlaying)
            if(mediaPlayer?.isPlaying == true){
                mediaPlayer?.pause()
            }
        }
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
                            binding.songProgressSb.progress = 0
                            binding.songProgressStartTv.text = "00:00"

                            //한 곡 반복
                            if(binding.songBtnRepeatIv.visibility == View.VISIBLE) {
                                setPlayerStatus(true)
                                songs[nowPos].isPlaying = true
                                Log.d("repeat", "반복 재생 호출")
                            } else {
                                setPlayerStatus(false)
                                songs[nowPos].isPlaying = false
                            }
                            startTimer()
                        }

                        break
                    }

                    if(isPlaying){  //진행 시간 관리
                        sleep(50)
                        mills +=50

                        runOnUiThread {
                            binding.songProgressSb.progress = ((mills / playTime) * 100).toInt()
                        }

                        if(mills % 1000 == 0f){
                            runOnUiThread { //1초마다 시간 변경
                                binding.songProgressStartTv.text = String.format("%02d:%02d", second / 60, second % 60)
                            }
                            second++
                        }
                    }
                }
            } catch (e: InterruptedException){
                Log.d("Song", "쓰레드가 죽었습니다. ${e.message}")
            }
        }
    }
    private fun initClickListener(){
        binding.songBtnDownIv.setOnClickListener {
            finish()    //songActivity 사라지고 다시 main을 띄워준다
        }

        binding.songBtnRepeatIv.setOnClickListener{
            setRepeatStatus(false)  //반복 꺼짐
        }
        binding.songBtnUnrepeatIv.setOnClickListener{
            setRepeatStatus(true)   //반복 켜짐
        }

        binding.songBtnPlayIv.setOnClickListener {
            setPlayerStatus(true)
            serviceStart(binding.songBtnPlayIv)
        }
        binding.songBtnPauseIv.setOnClickListener {
            setPlayerStatus(false)
            serviceStop(binding.songBtnPauseIv)
        }

        binding.songBtnRandomIv.setOnClickListener{
            setRandomStatus(false)  //랜덤 꺼짐
        }
        binding.songBtnNotrandomIv.setOnClickListener{
            setRandomStatus(true)   //랜덤 켜짐
        }

        binding.songBtnNextIv.setOnClickListener{
            moveSong(+1)    //다음곡
            setPlayerStatus(true)
        }
        binding.songBtnPrevIv.setOnClickListener {
            moveSong(-1)    //이전곡
            setPlayerStatus(true)
        }

        binding.songBtnLikeIv.setOnClickListener{
            setLike(songs[nowPos].isLike)
        }

//        // down버튼 누를 때 노래 제목 main activity에 돌려주기
//        val downClick = binding.songTitleTv.text.toString()
//
//        binding.songBtnDownIv.setOnClickListener{
//            val returnIntent = Intent(this, MainActivity::class.java).apply {
//                putExtra(MainActivity.STRING_INTENT_KEY, downClick)
//            }
//            setResult(Activity.RESULT_OK, returnIntent)
//            if(!isFinishing) finish()
//        }
    }

    //---------- 버튼 설정들 --------------
    fun setRepeatStatus(isRepeat : Boolean){
        if(isRepeat){   //반복 사용
            binding.songBtnRepeatIv.visibility = View.VISIBLE
            binding.songBtnUnrepeatIv.visibility = View.GONE
        }
        else{   //반복 사용 안함
            binding.songBtnRepeatIv.visibility = View.GONE
            binding.songBtnUnrepeatIv.visibility = View.VISIBLE
        }
    }
    fun setRandomStatus(isRandom : Boolean){
        if(isRandom){   //random 켜짐
            binding.songBtnRandomIv.visibility = View.VISIBLE
            binding.songBtnNotrandomIv.visibility = View.GONE
        }
        else{   //random 꺼짐
            binding.songBtnRandomIv.visibility = View.GONE
            binding.songBtnNotrandomIv.visibility = View.VISIBLE
        }
    }
}