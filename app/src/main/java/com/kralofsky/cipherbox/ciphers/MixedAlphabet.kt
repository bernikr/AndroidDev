package com.kralofsky.cipherbox.ciphers

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.kralofsky.cipherbox.*

object MixedAlphabet : Cipher() {
    override val name = "Mixed Alphabet"
    override val imageId = R.drawable.alphabet
    override val controlLayout = R.layout.cipher_single_key
    override val link = "https://crypto.interactive-maths.com/mixed-alphabet-cipher.html"
    override val youtube: String = "H2vLu_vvAMA"

    private var alphabet: List<Char> = ('A'..'Z').toList()

    override fun encode(cleartext: String): String = cleartext.mapLetters { alphabet[it.toAlphabetInt()] }

    override fun decode(ciphertext: String): String = ciphertext.mapLetters { alphabet.indexOf(it).toAlphabetChar() }

    override fun init(context: AppCompatActivity) {
        context.findViewById<EditText>(R.id.cipher_single_key).addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                val key = p0.toString().mapCharByChar {
                    if (!it.isLetter()) null
                    else it.toUpperCase()
                }
                alphabet =  (key.toCharArray() + ('A'..'Z').toList()).distinct()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }
}
