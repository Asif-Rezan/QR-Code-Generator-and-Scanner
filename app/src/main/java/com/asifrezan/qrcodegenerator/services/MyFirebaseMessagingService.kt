package com.asifrezan.qrcodegenerator.services

import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.asifrezan.qrcodegenerator.MainActivity
import com.asifrezan.qrcodegenerator.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

const val channelId = "notification_channel"
const val channelName = "com.asifrezan.qrcodegenerator"
@SuppressLint("MissingFirebaseInstanceTokenRefresh")
class MyFirebaseMessagingService : FirebaseMessagingService(){


    override fun onMessageReceived(message: RemoteMessage) {
        Log.e("eeee",message.notification!!.title!!)
        generateNotification(message.notification!!.title!!,message.notification!!.body!!)

//        if(message.notification != null)
//        {
//
//           generateNotification(message.notification!!.title!!,message.notification!!.body!!)
//        }

    }


    @SuppressLint("RemoteViewLayout")
    fun getRemoteView(title: String, message: String):RemoteViews
    {
        val remoteView = RemoteViews("com.asifrezan.qrcodegenerator",R.layout.notification)
        remoteView.setTextViewText(R.id.notification_title, title)
        remoteView.setTextViewText(R.id.message,message)
        remoteView.setImageViewResource(R.id.notification_image,R.drawable.add_icon)

        return remoteView
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title: String, message: String)
    {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_ONE_SHOT)

        //channel id, channel name

        var builder : NotificationCompat.Builder = NotificationCompat.Builder(applicationContext,
            channelId)
            .setSmallIcon(R.drawable.add_icon)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title,message))

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationChannel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)

        notificationManager.createNotificationChannel(notificationChannel)

        notificationManager.notify(0,builder.build())


    }

}