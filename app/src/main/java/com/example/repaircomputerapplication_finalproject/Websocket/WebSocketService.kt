package com.example.repaircomputerapplication_finalproject.Websocket

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.repaircomputerapplication_finalproject.R
import com.example.repaircomputerapplication_finalproject.repository.NotificationRepository
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

class WebSocketService : Service() {
    private lateinit var webSocket: MyWebSocketClient
    private val client = OkHttpClient()

    override fun onCreate() {
        super.onCreate()
        webSocket = MyWebSocketClient(this){
            // Callback ที่จะถูกเรียกเมื่อมีการแจ้งเตือนใหม่เข้ามา
            NotificationRepository.incrementNotificationCount() // อัปเดตจำนวนการแจ้งเตือนใน Repository
        }
        startForegroundServiceWithNotification()
    }


    private fun startForegroundServiceWithNotification() {
        val channelId = "WebSocketServiceChannel"
        val channelName = "WebSocket Service"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW).apply {
                description = "Channel for WebSocket Service"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("WebSocket Service")
            .setContentText("Running WebSocket service")
            .setSmallIcon(R.drawable.ic_notification)
            .build()

        startForeground(1, notification)  // เริ่ม Foreground Service ด้วย Notification
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        webSocket.disconnectWebSocket()
        client.dispatcher.executorService.shutdown() // ปิด OkHttpClient
        super.onDestroy()
    }
}
