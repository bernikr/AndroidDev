package com.kralofsky.cipherbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kralofsky.cipherbox.ciphers.*


val ciphers = listOf(
    Rot13,
    Atbash,
    CaesarCipher,
    MixedAlphabet,
    VigenereCipher,
    AffineCipher,
    RailfenceCipher,
    PolybusSquare,
    Autokey
)

class CipherActivity : AppCompatActivity() {
    private lateinit var cipher: Cipher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cipheractivitylayout)

        cipher = ciphers[intent.getIntExtra("cipherID",-1)]

        val controlLayout = cipher.controlLayout
        if(controlLayout != null) {
            val mainLayout = findViewById<LinearLayout>(R.id.cipherview_controllcontainerlayout)

            val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val layout = inflater.inflate(controlLayout, null)

            mainLayout.removeAllViews()
            mainLayout.addView(layout)
        }

        val clearText = findViewById<EditText>(R.id.cipherview_cleartext)
        val cipherText = findViewById<EditText>(R.id.cipherview_ciphertext)

        findViewById<Button>(R.id.cipherview_btn_encode).setOnClickListener {
            cipherText.setText(cipher.encode(clearText.text.toString()))
        }

        findViewById<Button>(R.id.cipherview_btn_decode).setOnClickListener {
            clearText.setText(cipher.decode(cipherText.text.toString()))
        }

        cipher.init(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.cipher_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_info -> {
            val intent = Intent(this, CipherTheoryActivity::class.java)
            intent.putExtra("cipherID", ciphers.indexOf(cipher))
            ContextCompat.startActivity(this, intent, Bundle.EMPTY)
            true
        }
        R.id.action_share -> {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, findViewById<EditText>(R.id.cipherview_ciphertext).text.toString())
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}

abstract class Cipher : MainMenuEntry {
    override val activity = CipherActivity::class
    override val description = ""
    open val controlLayout: Int? = null
    open val youtube: String? = null
    open val link: String? = null

    abstract fun encode(cleartext: String) : String
    abstract fun decode(ciphertext: String) : String

    abstract fun init(context: AppCompatActivity)
}
