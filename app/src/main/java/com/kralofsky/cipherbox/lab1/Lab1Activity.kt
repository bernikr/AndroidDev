package com.kralofsky.cipherbox.lab1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kralofsky.cipherbox.MainMenuEntry
import com.kralofsky.cipherbox.R

class Lab1Activity : AppCompatActivity() {
    companion object : MainMenuEntry {
        override val name = "Lab 1"
        override val description = ""
        override val imageId = R.mipmap.ic_launcher
        override val activity = Lab1Activity::class
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab1activitylayout)

        val myButton: Button = findViewById(R.id.button)
        val myTextField: TextView = findViewById(R.id.textView)

        myButton.setOnClickListener {
            myTextField.text = myTextField.text.toString() + "\nNew Line!"
        }

        findViewById<Button>(R.id.remove_useless_button).setOnClickListener {
            findViewById<Button>(R.id.useless_button).visibility = View.GONE
        }

        findViewById<Button>(R.id.add_view_button).setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
    }
}
