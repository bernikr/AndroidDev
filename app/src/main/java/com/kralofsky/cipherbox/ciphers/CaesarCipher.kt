package com.kralofsky.cipherbox.ciphers

import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.kralofsky.cipherbox.*

object CaesarCipher : Cipher() {
    override val name = "Caesar Cipher"
    override val imageId = R.drawable.ceasar_portrait
    override val controlLayout = R.layout.cipher_caesar
    override val youtube = "o6TPx1Co_wg"
    override val link = "https://en.wikipedia.org/wiki/Caesar_cipher"

    private var currentLetter: Char = 'B'

    override fun encode(cleartext: String): String = cleartext.mapCharByChar { it + currentLetter }

    override fun decode(ciphertext: String): String = ciphertext.mapCharByChar { it + currentLetter.inverse() }

    override fun init(context: AppCompatActivity) {
        context.findViewById<SeekBar>(R.id.cipher_caesar_seekbar).setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                currentLetter = p1.toAlphabetChar()
                context.findViewById<TextView>(R.id.cipher_caesar_currentLetter).text = currentLetter.toString()
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}
            override fun onStopTrackingTouch(p0: SeekBar?) {}
        })
    }
}
