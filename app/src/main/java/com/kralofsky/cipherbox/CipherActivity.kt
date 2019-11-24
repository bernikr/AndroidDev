package com.kralofsky.cipherbox

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.BaseColumns
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.SimpleCursorAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.kralofsky.cipherbox.CipherBoxDbContract.HistoryEntry


val ciphers = listOf(
    CaesarCipher,
    VigenereCipher
)

class CipherActivity : AppCompatActivity() {
    private lateinit var dbHelper: CipherboxDbHelper
    private lateinit var cipher: Cipher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.cipheractivitylayout)

        cipher = ciphers[intent.getIntExtra("cipherID",-1)]
        dbHelper = CipherboxDbHelper(this)

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
            saveToHistory(clearText.text.toString(), cipherText.text.toString())
        }

        findViewById<Button>(R.id.cipherview_btn_decode).setOnClickListener {
            clearText.setText(cipher.decode(cipherText.text.toString()))
            saveToHistory(clearText.text.toString(), cipherText.text.toString())
        }

        cipher.init(this)
    }

    override fun onDestroy() {
        dbHelper.close()
        super.onDestroy()
    }

    private fun saveToHistory(cleartext: String, ciphertext: String) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(HistoryEntry.COLUMN_NAME_CLEARTEXT, cleartext)
            put(HistoryEntry.COLUMN_NAME_CIPHERTEXT, ciphertext)
            put(HistoryEntry.COLUMN_NAME_CIPHER, cipher.name)
        }

        db.insert(HistoryEntry.TABLE_NAME, null, values)
    }

    private fun loadFromHistory(id: Long){
        val projection = arrayOf(HistoryEntry.COLUMN_NAME_CLEARTEXT, HistoryEntry.COLUMN_NAME_CIPHERTEXT)

        val cursor = dbHelper.readableDatabase.query(
            HistoryEntry.TABLE_NAME,
            projection,
            "${BaseColumns._ID} = ?",
            arrayOf(id.toString()),
            null,
            null,
            null
        )

        with(cursor) {
            moveToFirst()

            findViewById<EditText>(R.id.cipherview_cleartext).setText(
                getString(getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_CLEARTEXT))
            )
            findViewById<EditText>(R.id.cipherview_ciphertext).setText(
                getString(getColumnIndexOrThrow(HistoryEntry.COLUMN_NAME_CIPHERTEXT))
            )
        }

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
        R.id.action_history -> {
            val db = dbHelper.readableDatabase

            val fromColumns = arrayOf(
                HistoryEntry.COLUMN_NAME_CLEARTEXT,
                HistoryEntry.COLUMN_NAME_CIPHERTEXT,
                HistoryEntry.COLUMN_NAME_CIPHER
            )

            val toViews = intArrayOf(
                R.id.listitem_history_cleartext,
                R.id.listitem_history_ciphertext,
                R.id.listitem_history_cipher
            )

            val projection = fromColumns + BaseColumns._ID


            // How you want the results sorted in the resulting Cursor
            val sortOrder = "${BaseColumns._ID} DESC"

            val cursor = db.query(
                HistoryEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                sortOrder
            )

            val adapter = SimpleCursorAdapter(this, R.layout.listitem_history, cursor, fromColumns, toViews)


            val builder = AlertDialog.Builder(this)

            builder.apply{
                setTitle(R.string.history)
                setAdapter(adapter) { _, i ->
                    adapter.getItem(i)
                    loadFromHistory(adapter.getItemId(i))
                }
            }

            val dialog = builder.create()
            dialog.show()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }
}

abstract class Cipher : MainMenuEntry {
    override val activity = CipherActivity::class
    open val controlLayout: Int? = null
    open val youtube: String? = null

    abstract fun encode(cleartext: String) : String
    abstract fun decode(ciphertext: String) : String

    abstract fun init(context: AppCompatActivity)
}
