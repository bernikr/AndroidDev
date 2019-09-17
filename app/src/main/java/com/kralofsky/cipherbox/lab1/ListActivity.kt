package com.kralofsky.cipherbox.lab1

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.kralofsky.cipherbox.R

class ListActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab1listactivitylayout)

        val rawItems = intent.extras?.getParcelableArray("list") ?: arrayOf<Parcelable>()
        val allitems: MutableList<ListItem> = ArrayList()

        for (i in rawItems)
            if (i is ListItem)
                allitems.add(i)

        val items = ArrayList(allitems)

        val adapter = ListAdapter(this, items)
        findViewById<ListView>(R.id.lab1listView).adapter = adapter

        findViewById<Button>(R.id.lab1listactivitysortbtn).setOnClickListener {
            items.sortBy { it.title }
            allitems.sortBy { it.title }
            adapter.notifyDataSetChanged()
        }

        findViewById<EditText>(R.id.lab1listactivityfilter).addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                items.clear()
                items.addAll(allitems.filter { it.title.startsWith(p0.toString()) })
                adapter.notifyDataSetChanged()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        })
    }
}