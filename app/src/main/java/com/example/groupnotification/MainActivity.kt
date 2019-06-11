package com.example.groupnotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.v4.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Created by Nizar Assegaf on 11,June,2019
 */

class MainActivity : AppCompatActivity() {

    companion object {
        const val NOTIFICATION_ID = 237
        const val NOTIFICATION_ID2 = 238
    }

    private var value = 0
    private val inboxStyle = NotificationCompat.InboxStyle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button_show_notif.setOnClickListener {
            showNotification(NOTIFICATION_ID)
        }

        button_show_notif2.setOnClickListener {
            showNotification(NOTIFICATION_ID2)
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    fun makeNotificationChannel(id: String, name: String, importance: Int) {
        val channel = NotificationChannel(id, name, importance)
        channel.setShowBadge(true) // set false to disable badges, Oreo exclusive

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.createNotificationChannel(channel)
    }

    fun showNotification(id: Int) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            makeNotificationChannel("CHANNEL$id", "Example channel$id", NotificationManager.IMPORTANCE_DEFAULT)
        }

        value++

        val resultIntent = Intent(this@MainActivity, MainActivity::class.java)
        resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val resultPendingIntent = PendingIntent.getActivity(
            this@MainActivity,
            0 /* Request code */, resultIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        inboxStyle.setBigContentTitle("Content Text$id")
        inboxStyle.setSummaryText("summary Text$id")
        inboxStyle.addLine("line$value")

        val notification = NotificationCompat.Builder(this, "CHANNEL$id")

        notification
            .setSmallIcon(R.mipmap.ic_launcher) // can use any other icon
            .setGroup(id.toString())
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.notification_icon_background))
            .setAutoCancel(true)
            .setStyle(inboxStyle)
            .setGroupSummary(false)
            .setContentIntent(resultPendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify("app Name", id, notification.build())
    }

}
