package com.kralofsky.cipherbox

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity


val ciphers = listOf<Cipher>(
    CaesarCipher
)

class CipherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cipheractivitylayout)

        val cipher = ciphers[intent.getIntExtra("cipherID",-1)]

        val controlLayout = cipher.controlLayout
        if(controlLayout != null) {
            val mainLayout = findViewById<LinearLayout>(R.id.cipherview_controllcontainerlayout)

            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = inflater.inflate(controlLayout, null)

            mainLayout.removeAllViews()
            mainLayout.addView(layout)
        }

        val ciphertext = findViewById<EditText>(R.id.cipherview_ciphertext)
        val cleartext = findViewById<EditText>(R.id.cipherview_cleartext)

        cleartext.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (currentFocus == cleartext) ciphertext.setText(cipher.encode(p0.toString()))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })

        ciphertext.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                if (currentFocus == ciphertext) cleartext.setText(cipher.decode(p0.toString()))
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }
}

abstract class Cipher : MainMenuEntry {
    override val activity = CipherActivity::class
    open val controlLayout: Int? = null

    abstract fun encode(cleartext: String) : String
    abstract fun decode(ciphertext: String) : String
}


