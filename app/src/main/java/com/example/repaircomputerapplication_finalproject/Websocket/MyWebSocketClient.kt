package com.example.repaircomputerapplication_finalproject.Websocket

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.example.repaircomputerapplication_finalproject.R
import com.example.repaircomputerapplication_finalproject.model.NotificationMessage
import com.example.repaircomputerapplication_finalproject.viewModel.ContextDataStore.dataStore
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

class MyWebSocketClient(
    serverUri: URI,
    private val context: Context
) : WebSocketClient(serverUri), LifecycleObserver {

    override fun onOpen(handshakedata: ServerHandshake) {
        Log.d("WebSocket", "Opened")
    }

    override fun onMessage(message: String) {
        Log.d("WebSocket", "Message received: $message")
        CoroutineScope(Dispatchers.Main).launch {
            val role = context.dataStore.data.map { items ->
                items[stringPreferencesKey("role")]
            }.first()
            val userId = context.dataStore.data.map { items ->
                items[stringPreferencesKey("userId")]
            }.first()
            val notificationMessage = parseMessage(message)
            if(role == notificationMessage.role && userId == notificationMessage.user_id){
                showNotification(context, notificationMessage.title,notificationMessage.message)
            }
        }
    }

    override fun onClose(code: Int, reason: String, remote: Boolean) {
        Log.d("WebSocket", "Closed")
    }

    override fun onError(ex: Exception) {
        Log.e("WebSocket", "Error", ex)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun connectWebSocket() {
        this.connect()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun disconnectWebSocket() {
        this.close()
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
                // ถ้าสิทธิ์ไม่ได้รับการอนุญาต เราก็ไม่สามารถแสดงการแจ้งเตือนได้
                Log.e("WebSocket", "Permission not granted to post notifications")
                return
            }
            notify(1, builder.build())
        }
    }
}
