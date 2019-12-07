package com.kralofsky.cipherbox.ciphers

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.kralofsky.cipherbox.*

object AffineCipher : Cipher() {
    override val name = "Affine Cipher"
    override val imageId = R.drawable.affine
    override val controlLayout = R.layout.cipher_affine
    override val youtube = "HWJGEwCEeyk"
    override val link = "https://en.wikipedia.org/wiki/Affine_cipher"

    private var a = 1
    private var b = 0

    override fun encode(cleartext: String): String = cleartext.mapLetters {
        ((it.toAlphabetInt() * a + b) % 26).toAlphabetChar()
    }

    override fun decode(ciphertext: String): String = ciphertext.mapLetters {
        (((it.toAlphabetInt() - b + 26)* inverse(a)) % 26).toAlphabetChar()
    }

    private fun inverse(n: Int): Int = (0..25).find { (it*n)%26 == 1 }!!

    override fun init(context: AppCompatActivity) {
        val spinnerA = context.findViewById<Spinner>(R.id.cipher_affine_a)
        val spinnerB = context.findViewById<Spinner>(R.id.cipher_affine_b)

        spinnerA.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item,
            listOf(1,3,5,7,9,11,15,17,19,21,23,25)
        )
        spinnerB.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, (0..25).toList())

        spinnerA.onItemSelectedListener = object:AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                a = parent?.getItemAtPosition(position) as Int
            }
        }

        spinnerB.onItemSelectedListener = object:AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                b = parent?.getItemAtPosition(position) as Int
            }
        }
    }
}
