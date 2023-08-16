package com.example.myapplicationnotificationlistner

import android.app.Notification
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification
import android.widget.ListView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    private lateinit var adapter: NotificationListAdapter
    private lateinit var notificationListener: MyNotificationListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        notificationListener = MyNotificationListener()


        // Check if your app's package is among the enabled notification listener services
        val enabledListeners = Settings.Secure.getString(
            contentResolver,
            "enabled_notification_listeners"
        )
        val packageName = packageName
        if (enabledListeners?.contains(packageName) != true) {
            openNotificationListenerSettings()
        }
        val listView = findViewById<ListView>(R.id.notificationListView)
        Toast.makeText(this,notificationListener.getNotificationList().size.toString(),Toast.LENGTH_SHORT).show()
        adapter = NotificationListAdapter(this, notificationListener.getNotificationList())
        listView.adapter = adapter


    }

    private fun openNotificationListenerSettings() {

        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        } else {
            TODO("VERSION.SDK_INT < LOLLIPOP_MR1")
        }
        startActivity(intent)
    }
}





class MyNotificationListener : NotificationListenerService() {


    private val notificationList = mutableListOf<NotificationData>()
    private lateinit var adapter: NotificationListAdapter

    override fun onCreate() {
        super.onCreate()
        adapter = NotificationListAdapter(this, notificationList)
    }

    override fun onNotificationPosted(sbn: StatusBarNotification) {
        super.onNotificationPosted(sbn)

        val appName = getAppNameFromPackage(sbn.packageName)
        // Handle SpannableString correctly
        val notificationText = sbn.notification?.extras?.getCharSequence(Notification.EXTRA_TEXT)
        val notificationString = notificationText?.toString() ?: ""

        val timestamp = sbn.postTime

        val notificationData = NotificationData(appName, notificationString, timestamp)
        notificationList.add(
            0,
            notificationData
        ) // Add at the beginning for most recent notification
        updateAdapter()
    }

    private fun getAppNameFromPackage(packageName: String): String {
        val packageManager = applicationContext.packageManager
        val applicationInfo = packageManager.getApplicationInfo(packageName, 0)
        return packageManager.getApplicationLabel(applicationInfo).toString()
    }

    fun getNotificationList(): List<NotificationData> {
        return notificationList
    }

    fun updateAdapter() {
        adapter.notifyDataSetChanged()
    }
}


