package com.kralofsky.cipherbox

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.kralofsky.cipherbox.lab1.Lab1Activity
import kotlin.reflect.KClass

class MainMenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mainmenulayout)

        val items = listOf(
            *ciphers.toTypedArray(),
            Lab1Activity,
            Lab2Activity
        )


        findViewById<ListView>(R.id.mainmenulist).adapter = MainMenuAdapter(this, items)
    }
}


interface MainMenuEntry {
    val name: String
    val description: String
    val imageId: Int
    val activity: KClass<out AppCompatActivity>
}


class MainMenuAdapter(context: Context, objects: List<MainMenuEntry>) :
    ArrayAdapter<MainMenuEntry>(context, R.layout.listitemdesign, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: View = convertView ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.listitemdesign, null)

        val item = getItem(position)!!

        v.findViewById<TextView>(R.id.listitemtitle).text = item.name
        v.findViewById<TextView>(R.id.listitemdescription).text = item.description
        v.findViewById<ImageView>(R.id.listitemimg).setImageResource(item.imageId)

        v.setOnClickListener {
            val intent = Intent(context, item.activity.java)
            if (item is Cipher)
                intent.putExtra("cipherID", ciphers.indexOf(item))
            startActivity(context, intent, Bundle.EMPTY)
        }

        return v
    }
}