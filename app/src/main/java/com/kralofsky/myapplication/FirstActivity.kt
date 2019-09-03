package com.kralofsky.myapplication

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class FirstActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.firstactivitylayout)

        val myButton: Button = findViewById(R.id.button)
        val myTextField: TextView = findViewById(R.id.textView)

        myButton.setOnClickListener {
            myTextField.text = myTextField.text.toString() + "\nNew Line!"
        }
    }
}
