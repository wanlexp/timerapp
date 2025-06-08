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
    private lateinit var startTimerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)  // 레이아웃 설정

        inputMinutes = findViewById(R.id.inputMinutes)
        startTimerButton = findViewById(R.id.startTimerButton)

        // 오버레이 권한 체크
        if (!Settings.canDrawOverlays(this)) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }

        startTimerButton.setOnClickListener {
            val minutesStr = inputMinutes.text.toString()
            if (minutesStr.isEmpty()) {
                Toast.makeText(this, "시간(분)을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val minutes = minutesStr.toLongOrNull()
            if (minutes == null || minutes <= 0) {
                Toast.makeText(this, "올바른 시간을 입력하세요", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val duration = minutes * 60 * 1000L  // 밀리초로 변환

            val serviceIntent = Intent(this, TimerService::class.java)
            serviceIntent.putExtra("durationMillis", duration)
            ContextCompat.startForegroundService(this, serviceIntent)

            Toast.makeText(this, "${minutes}분 타이머 시작", Toast.LENGTH_SHORT).show()
        }
    }
}
