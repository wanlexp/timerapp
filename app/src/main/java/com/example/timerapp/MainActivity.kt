package com.example.timerapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var inputMinutes: EditText
    private lateinit var inputSeconds: EditText
    private lateinit var startTimerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        inputMinutes = findViewById(R.id.inputMinutes)
        inputSeconds = findViewById(R.id.inputSeconds)
        startTimerButton = findViewById(R.id.startTimerButton)

        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }

        startTimerButton.setOnClickListener {
            val minutesStr = inputMinutes.text.toString()
            val secondsStr = inputSeconds.text.toString()

            // 입력값 없으면 0으로 처리
            val minutes = minutesStr.toLongOrNull() ?: 0L
            val seconds = secondsStr.toLongOrNull() ?: 0L

            if (minutes == 0L && seconds == 0L) {
                Toast.makeText(this, "분 또는 초 단위로 시간을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (seconds !in 0..59) {
                Toast.makeText(this, "초는 0~59 사이의 값이어야 합니다", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val duration = (minutes * 60 + seconds) * 1000L  // 밀리초 변환

            val serviceIntent = Intent(this, TimerService::class.java).apply {
                putExtra("durationMillis", duration)
            }
            ContextCompat.startForegroundService(this, serviceIntent)

            Toast.makeText(this, "타이머 시작: ${minutes}분 ${seconds}초", Toast.LENGTH_SHORT).show()
        }
    }
}
