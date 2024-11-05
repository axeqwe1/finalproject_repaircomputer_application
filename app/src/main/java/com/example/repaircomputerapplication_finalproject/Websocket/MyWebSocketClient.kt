package com.example.repaircomputerapplication_finalproject.Websocket

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.repaircomputerapplication_finalproject.R
import com.example.repaircomputerapplication_finalproject.model.NotificationMessage
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString
import java.net.URI

class MyWebSocketClient(
    private val context: Context,
    private val onNewNotification: () -> Unit // Callback เพื่ออัปเดตจำนวนการแจ้งเตือนใน ViewModel
) {
    private val client = OkHttpClient()
    private lateinit var webSocket: WebSocket

    init {
        connectWebSocket()
    }

    private fun connectWebSocket() {
        val request = Request.Builder().url("ws://45.136.255.62:80").build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                Log.d("WebSocket", "Opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                Log.d("WebSocket", "Message received: $text")
                handleMessage(text)
                CoroutineScope(Dispatchers.Main).launch {
                    val role = context.dataStore.data.map { items ->
                        items[stringPreferencesKey("role")]
                    }.first()
                    val userId = context.dataStore.data.map { items ->
                        items[stringPreferencesKey("userId")]
                    }.first()
                    val notificationMessage = parseMessage(text)
                    if(role == notificationMessage.role && userId == notificationMessage.user_id){
                        showNotification(context, notificationMessage.title,notificationMessage.message)
                        onNewNotification() // เรียก Callback เมื่อมีการแจ้งเตือนใหม่
                    }
                }
            }

            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
                Log.d("WebSocket", "Binary message received")
                handleMessage(bytes.utf8())
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                Log.d("WebSocket", "Closing with reason: $reason")
                webSocket.close(1000, null)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                Log.e("WebSocket", "Error", t)
                reconnect()
            }
        })
    }

    private fun reconnect() {
        CoroutineScope(Dispatchers.IO).launch {
            delay(5000) // รอ 5 วินาทีก่อนเชื่อมต่อใหม่
            connectWebSocket()
        }
    }

    private fun handleMessage(message: String) {
        CoroutineScope(Dispatchers.Main).launch {
            val role = context.dataStore.data.map { items ->
                items[stringPreferencesKey("role")]
            }.first()
            val userId = context.dataStore.data.map { items ->
                items[stringPreferencesKey("userId")]
            }.first()
            val notificationMessage = parseMessage(message)
            if (role == notificationMessage.role && userId == notificationMessage.user_id) {
                showNotification(context, notificationMessage.title, notificationMessage.message)
                onNewNotification()
            }
        }
    }

    private fun parseMessage(message: String): NotificationMessage {
        return Gson().fromJson(message, NotificationMessage::class.java)
    }

    private fun showNotification(context: Context, title: String, message: String) {
        val channelId = "notification_channel_id"
        val channelName = "Notification Channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "Channel description"
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(context)) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Log.e("WebSocket", "Permission not granted to post notifications")
                return
            }
            notify(1, builder.build())
        }
    }

    fun disconnectWebSocket() {
        webSocket.close(1000, "Service destroyed")
    }
}
