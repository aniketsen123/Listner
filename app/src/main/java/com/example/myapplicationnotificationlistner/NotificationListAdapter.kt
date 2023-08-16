package com.example.myapplicationnotificationlistner

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class NotificationListAdapter(private val context: Context, private val notifications: List<NotificationData>) : BaseAdapter() {
    override fun getCount(): Int = notifications.size

    override fun getItem(position: Int): Any = notifications[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val notification = getItem(position) as NotificationData
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.item, parent, false)

        view.findViewById<TextView>(R.id.appNameTextView).text = notification.appName
        view.findViewById<TextView>(R.id.notificationTextView).text = notification.notificationText
        view.findViewById<TextView>(R.id.timestampTextView).text = SimpleDateFormat("dd/MM/yyyy HH:mm").format(
            Date(notification.timestamp)
        )

        return view
    }
}