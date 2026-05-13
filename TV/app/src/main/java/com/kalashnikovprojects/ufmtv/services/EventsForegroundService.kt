package com.kalashnikovprojects.ufmtv.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.kalashnikovprojects.ufmtv.R
import com.kalashnikovprojects.ufmtv.data.remote.EventWebSocketService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class EventsForegroundService : Service() {

    @Inject
    lateinit var webSocketService: EventWebSocketService
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        val notification = NotificationCompat.Builder(this, "timer_channel")
            .setContentTitle("Universal food menu")
            .setContentText("Updating menu")
            .setSmallIcon(R.drawable.ufm_icon_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()

        startForeground(NOTIFICATION_ID, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        webSocketService.connect(serviceScope)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketService.disconnect()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    companion object {
        private const val NOTIFICATION_ID = 101
    }
}