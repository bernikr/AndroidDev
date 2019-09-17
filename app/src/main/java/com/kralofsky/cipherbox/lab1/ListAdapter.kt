package com.kralofsky.cipherbox.lab1

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.kralofsky.cipherbox.R

class ListAdapter(context: Context, objects: List<ListItem>) :
    ArrayAdapter<ListItem>(context,
        R.layout.listitemdesign, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: View = convertView ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                                        .inflate(R.layout.listitemdesign, null)

        val item = getItem(position)!!

        v.findViewById<TextView>(R.id.listitemtitle).text = item.title
        v.findViewById<TextView>(R.id.listitemdescription).text = item.description
        v.findViewById<ImageView>(R.id.listitemimg).setImageResource(item.imageId)

        v.setOnClickListener {
            val intent = Intent(context, DisplayActivity::class.java)
            intent.putExtra("item", item)
            startActivity(context, intent, Bundle.EMPTY)
        }

        return v
    }
}