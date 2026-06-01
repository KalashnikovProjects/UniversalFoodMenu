package com.kalashnikovprojects.ufmtv.services

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.ServiceInfo
import android.content.res.Configuration
import android.graphics.drawable.Icon
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.kalashnikovprojects.ufmtv.R
import com.kalashnikovprojects.ufmtv.data.remote.EventsWebSocketService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@AndroidEntryPoint
class EventsForegroundService : Service() {
    @Inject
    lateinit var webSocketService: EventsWebSocketService
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        Log.d("UFM", "create events foreground service")
        val isAndroidTv: Boolean = resources.configuration.uiMode == Configuration.UI_MODE_TYPE_TELEVISION

        if (!isAndroidTv) {
            createNotificationChannel()
            val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Universal food menu")
                .setContentText("Updating menu")
                .setLargeIcon(Icon.createWithResource(
                    this,
                    R.drawable.ufm_icon_foreground
                ))
                .setSmallIcon(R.drawable.ufm_icon_small)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true)
                .setOnlyAlertOnce(true)
                .build()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14
                startForeground(
                    NOTIFICATION_ID,
                    notification,
                    ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
                )
            } else {
                startForeground(NOTIFICATION_ID, notification)
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Universal food menu",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Channel for Universal Food Menu app"
            }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
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
        private const val NOTIFICATION_ID = 102
        private const val CHANNEL_ID = "ufm_notification"
    }
}