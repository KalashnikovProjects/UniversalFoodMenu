package com.example.ufmcontroller.data.service

import android.app.UiModeManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import com.example.ufmcontroller.domain.repository.EventsServiceRepository
import com.example.ufmcontroller.services.EventsForegroundService
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class AndroidEventsServiceController @Inject constructor(
    @ApplicationContext private val context: Context
) : EventsServiceRepository {

    private val intent = Intent(context, EventsForegroundService::class.java)

    override fun startService() {
        Log.d("UFM", "create events service controller")

        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as UiModeManager
        val isTv = uiModeManager.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION

        if (isTv) {
            context.startService(intent)
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent)
            } else {
                context.startService(intent)
            }
        }
    }

    override fun stopService() {
        context.stopService(intent)
    }
}