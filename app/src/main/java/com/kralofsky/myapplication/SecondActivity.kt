package com.kralofsky.myapplication

import android.os.Bundle
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.secondactivitylayout)

        val items: MutableList<ListItem> = ArrayList()

        items.add(ListItem("Title1", R.drawable.ic_launcher_background,"desc1"))
        items.add(ListItem("Title2", R.drawable.ic_launcher_foreground,"desc2"))

        findViewById<ListView>(R.id.listView).adapter = ListAdapter(this, items)
    }
}