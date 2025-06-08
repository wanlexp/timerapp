package com.example.timerapp

import android.app.*
import android.content.Intent
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat

class TimerService : Service() {

    private var countDownTimer: CountDownTimer? = null
    private var durationMillis: Long = 0L

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        durationMillis = intent?.getLongExtra("durationMillis", 0L) ?: 0L
        startForeground(1, createNotification())
        startRepeatingTimer(durationMillis)
        return START_STICKY
    }

    private fun startRepeatingTimer(duration: Long) {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(duration, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                Log.d("TimerService", "남은 시간: $millisUntilFinished")
            }

            override fun onFinish() {
                OverlayUtil.showOverlayView(applicationContext)

                Handler(Looper.getMainLooper()).postDelayed({
                    startRepeatingTimer(durationMillis)  // 반복 시작
                }, 1000)
            }
        }.start()
    }

    private fun createNotification(): Notification {
        val channelId = "timer_channel"
        val channelName = "Timer Service"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chan = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            getSystemService(NotificationManager::class.java).createNotificationChannel(chan)
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle("타이머 실행 중")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
