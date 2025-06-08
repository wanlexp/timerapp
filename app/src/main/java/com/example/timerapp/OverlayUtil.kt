package com.example.timerapp

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.provider.Settings
import android.view.*
import android.widget.Button

object OverlayUtil {

    fun showOverlayView(context: Context) {
        if (!Settings.canDrawOverlays(context)) return

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val inflater = LayoutInflater.from(context)
        val overlayView = inflater.inflate(R.layout.overlay_block_screen, null)

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            else
                WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )

        wm.addView(overlayView, params)

        // 10초 후 자동 제거 (원하면 제거 코드 삭제)
        /*overlayView.postDelayed({
            try {
                wm.removeView(overlayView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }, 10000)*/

        //확인 누르면 제거
        val confirmButton = overlayView.findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener {
            try {
                wm.removeView(overlayView)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
