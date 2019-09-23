package com.kralofsky.cipherbox

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

val ciphers = listOf<Cipher>(
    CaesarCipher
)

class CipherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cipheractivitylayout)

        val cipher = ciphers[intent.getIntExtra("cipherID",-1)]

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

    abstract fun encode(cleartext: String) : String
    abstract fun decode(ciphertext: String) : String
}


