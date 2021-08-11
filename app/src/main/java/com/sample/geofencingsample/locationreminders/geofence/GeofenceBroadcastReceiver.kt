package com.sample.geofencingsample.locationreminders.geofence


import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import com.sample.geofencingsample.R
import com.sample.geofencingsample.locationreminders.geofence.GeofenceTransitionsJobIntentService.Companion.enqueueWork

/**
 * Triggered by the Geofence.  Since we can have many Geofences at once, we pull the request
 * ID from the first Geofence, and locate it within the cached data in our Room DB
 *
 * Or users can add the reminders and then close the app, So our app has to run in the background
 * and handle the geofencing in the background.
 * To do that you can use https://developer.android.com/reference/android/support/v4/app/JobIntentService to do that.
 *
 */

class GeofenceBroadcastReceiver : BroadcastReceiver() {
    private val NOTIFICATION_CHANNEL_NAME: CharSequence ="XYZ"
    private val TAG = "GeofenceBroadcastReceiver"
    private val NOTIFICATION_CHANNEL_ID = "geofence_channel" // The id of the channel.

    override fun onReceive(context: Context, intent: Intent) {
        //DONE: implement the onReceive method to receive the geofencing events at the background
        val geofenceEvent = GeofencingEvent.fromIntent(intent)
        if (geofenceEvent.hasError()) {
            Log.d(TAG, "Error on receive !")
            return
        }

        when (geofenceEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                enqueueWork(context, intent)
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Log.d("BroadcastReceiver", "Geofence EXIT")
                displayNotification(context, "Geofence EXIT")
            }
            Geofence.GEOFENCE_TRANSITION_DWELL -> {
                Log.d("BroadcastReceiver", "Geofence DWELL")
                displayNotification(context, "Geofence DWELL")
            }
            else -> {
                Log.d("BroadcastReceiver", "Invalid Type")
                displayNotification(context, "Geofence INVALID TYPE")
            }
        }
    }

    private fun displayNotification(context: Context, geofenceTransition: String){
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        createNotificationChannel(notificationManager)

        val notification = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle("Geofence")
            .setContentText(geofenceTransition)
        notificationManager.notify(2, notification.build())
    }

    private fun createNotificationChannel(notificationManager: NotificationManager){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                NOTIFICATION_CHANNEL_NAME,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

}