package com.kralofsky.cipherbox.ciphers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.kralofsky.cipherbox.*

object VigenereCipher : Cipher() {
    override val name = "Vigenere Cipher"
    override val imageId = R.drawable.vigenere_portrait
    override val controlLayout = R.layout.cipher_single_key
    override val link = "https://en.wikipedia.org/wiki/Vigen%C3%A8re_cipher"
    override val youtube = "SkJcmCaHqS0"

    private var key: String = "A"

    override fun encode(cleartext: String): String = cleartext.mapCharByCharIndexed { i, c -> c + key[i%key.length] }

    override fun decode(ciphertext: String): String = ciphertext.mapCharByCharIndexed { i, c -> c + key[i%key.length].inverse() }

    override fun init(context: AppCompatActivity) {
        context.findViewById<EditText>(R.id.cipher_single_key).addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                val tempKey = p0.toString().mapCharByChar {
                    if (!it.isLetter()) null
                    else it.toUpperCase()
                }
                key = if(tempKey.isNotEmpty()) tempKey else "A"
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }
}
