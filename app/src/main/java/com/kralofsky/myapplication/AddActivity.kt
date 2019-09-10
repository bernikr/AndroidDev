package com.kralofsky.myapplication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class AddActivity : AppCompatActivity() {
    private val itemList : MutableList<ListItem> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.addactivitylayout)

        findViewById<Button>(R.id.addActivity_button).setOnClickListener {
            val element = ListItem(
                findViewById<EditText>(R.id.addActivity_title).text.toString(),
                R.mipmap.ic_launcher,
                findViewById<EditText>(R.id.addActivity_description).text.toString()
            )

            itemList.add(element)
        }

        findViewById<Button>(R.id.AddActivity_show_list_button).setOnClickListener {
            val intent = Intent(this, ListActivity::class.java)
            intent.putExtra("list", itemList.toTypedArray())
            startActivity(intent)
        }
    }
}