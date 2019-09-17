package com.kralofsky.cipherbox.lab1

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kralofsky.cipherbox.R

class DisplayActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab1displayactivitylayout)

        val item = intent.extras?.getParcelable<ListItem>("item")

        if (item != null) {
            findViewById<ImageView>(R.id.lab1displayimg).setImageResource(item.imageId)
            findViewById<TextView>(R.id.lab1displaytitle).text = item.title
            findViewById<TextView>(R.id.lab1displaydesc).text = item.description
        }
    }
}