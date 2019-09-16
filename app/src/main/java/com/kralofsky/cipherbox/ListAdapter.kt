package com.kralofsky.cipherbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

class ListAdapter(context: Context, objects: List<ListItem>) :
    ArrayAdapter<ListItem>(context, R.layout.listitemdesign, objects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val v: View = convertView ?: (context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
                                        .inflate(R.layout.listitemdesign, null)

        val item = getItem(position)!!

        v.findViewById<TextView>(R.id.title).text = item.title
        v.findViewById<TextView>(R.id.description).text = item.description
        v.findViewById<ImageView>(R.id.imageView).setImageResource(item.imageId)

        return v
    }
}