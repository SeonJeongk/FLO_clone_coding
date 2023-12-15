package com.example.flo.ui.song

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.flo.R
import kotlinx.coroutines.*

class Foreground: Service() {
    val CHANNEL_ID = "FGS153"
    val NOTI_ID = 153

    // 카운트 최댓값 지정
    val PROGRESS_MAX = 100

    //-------------------- 코루틴 스코프와 작업자 선언 -----------------------
    // Coroutine이 어떤 스레드에서 실행될지 지정한다
    // Dispatcher : 스레드의 부하 상태에 맞춰 코루틴을 분배해준다
    private var notificationJob: Job? = null
    val scope = CoroutineScope(Dispatchers.Default)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //내가 쓸 채널을 알림
        createNotificationChannel()

        //백그라운드에서 진행
        runBackground()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent): IBinder {
        return Binder()
    }
    override fun onDestroy() {
        super.onDestroy()
        notificationJob?.cancel()
    }

    //----------- 알림채널 생성 --------------
    fun createNotificationChannel(){
        //버전 체크
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //서비스 채널 생성
            val serviceChannel = NotificationChannel(CHANNEL_ID, "FOREGROUND", NotificationManager.IMPORTANCE_LOW)
            //서비스가 위의 채널을 사용한다고 알림
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(serviceChannel)
        }
    }

    //------------ 공통된 알림 빌드 로직 -------------
    private fun updateNotificationProgress(progress: Int) {

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("노래 재생중")
            .setContentText("가수 이름")
            .setSmallIcon(R.drawable.ic_bottom_look_select)
            .setProgress(PROGRESS_MAX, progress, false)
            .build()

        // 클릭시 SongActivity로 이동하는 Intent 생성
        val intent = Intent(this, SongActivity::class.java)
        // 호출된 액티비티가 현재 태스크의 top 이미 존재할 경우 재실행되지 않음 (기존 top 재사용)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE)
        notification.contentIntent = pendingIntent

        //알림 업데이트
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTI_ID, notification)

        //사용하는 현 서비스가 foreground로 동작함을 알려줌
        startForeground(NOTI_ID, notification)
    }

    private fun runBackground() {
        // job이 이미 실행 중인지 확인하고, 실행 중이 아닌 경우에만 호출
        if (notificationJob == null || notificationJob?.isCancelled == true) {
            notificationJob = scope.launch {
                try {
                    repeat(PROGRESS_MAX) { i ->
                        // progress bar 업데이트
                        updateNotificationProgress(i)
                        delay(1000)
                    }
                } finally {
                    //
                }
            }
        }
    }
}