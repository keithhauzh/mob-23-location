package com.team.moblocation.service

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.location.Location
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.team.moblocation.R

class LocationService: Service() {
    private lateinit var fusedClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private val channelId = "mob_tracking_location"

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        fusedClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object: LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val loc: Location = result.lastLocation ?: return
                Log.d("location", "${loc.latitude}, ${loc.longitude}")
            }
        }
        createNotificationChannel()
        startForeground(1,createNotification())
        startLocationTracking()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.POST_NOTIFICATIONS])
    private fun startLocationTracking() {
        val request = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            10000L
        ).setMinUpdateIntervalMillis(5000L).build()

        fusedClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            channelId,
            "Location Tracking",
            NotificationManager.IMPORTANCE_LOW
        )

        getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    private fun createNotification(): Notification {
        return Notification.Builder(this, channelId)
            .setContentTitle("Location Tracking")
            .setContentText("Mob Location Service is running")
            .setSmallIcon(R.drawable.outline_my_location_24)
            .setOngoing(true)
            .build()
    }

    override fun onDestroy() {
        fusedClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }
}