package com.kalashnikovprojects.ufmtv.services

import android.app.Service
import android.content.Intent
import android.content.pm.ServiceInfo
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.kalashnikovprojects.ufmtv.R
import com.kalashnikovprojects.ufmtv.data.remote.EventsWebSocketService
import com.kalashnikovprojects.ufmtv.data.remote.LoginWebSocketService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class LoginEventsForegroundService : Service() {
    @Inject
    lateinit var webSocketService: LoginWebSocketService
    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        val notification = NotificationCompat.Builder(this, "ufm_login_notification")
            .setContentTitle("Universal food menu")
            .setContentText("Logging in")
            .setSmallIcon(R.drawable.ufm_icon_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) { // Android 14
            startForeground(
                LoginEventsForegroundService.Companion.NOTIFICATION_ID,
                notification,
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            startForeground(LoginEventsForegroundService.Companion.NOTIFICATION_ID, notification)
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
    }
}