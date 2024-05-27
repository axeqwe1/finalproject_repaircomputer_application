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
import com.example.repaircomputerapplication_finalproject.Websocket.MyWebSocketClient
import java.net.URI

class WebSocketService : Service() {
    private lateinit var webSocketClient: MyWebSocketClient

    override fun onCreate() {
        super.onCreate()
        val uri = URI("ws://192.168.1.100:8001")
        webSocketClient = MyWebSocketClient(uri, this)
        webSocketClient.connect()
        startForegroundService()
    }

    private fun startForegroundService() {
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

        startForeground(1, notification)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        webSocketClient.close()
        super.onDestroy()
    }
}
