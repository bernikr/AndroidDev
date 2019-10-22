package com.kralofsky.cipherbox

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class Lab2Activity : AppCompatActivity() {
    companion object : MainMenuEntry {
        override val name = "Lab 2"
        override val description = ""
        override val imageId = R.mipmap.ic_launcher
        override val activity = Lab2Activity::class
    }

    private var publication: ModelPost? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab2activitylayout)

        findViewById<Button>(R.id.send_request).setOnClickListener {
            val ro = RequestOperator()
            ro.listener = { i: Int, post: ModelPost? ->
                publication = post
                updatePublication()
            }
            ro.start()
        }
    }

    fun updatePublication(){
        runOnUiThread {
            findViewById<TextView>(R.id.lab2title).text = publication?.title
            findViewById<TextView>(R.id.lab2bodyText).text = publication?.bodyText
        }
    }

    private data class ModelPost(
        val id: Int,
        val userId: Int,
        val title: String,
        val bodyText: String
    )

    private class RequestOperator: Thread() {
        var listener: ((Int, ModelPost?) -> Unit)? = null

        var responseCode: Int = 0

        override fun run() {
            super.run()
            try {
                val publication = request()
                listener?.invoke(responseCode, publication)
            } catch (e: IOException) {
                listener?.invoke(-1, null)
            } catch (e: JSONException) {
                listener?.invoke(-2, null)
            }
        }

        private fun request(): ModelPost? {
            val obj = URL("https://jsonplaceholder.typicode.com/posts/1")
            val con = obj.openConnection() as HttpURLConnection
            con.requestMethod = "GET"
            con.setRequestProperty("Content-Type", "application/json")
            responseCode = con.responseCode
            println("Response Code: $responseCode")

            val streamReader: InputStreamReader
            streamReader = if (responseCode == 200){
                InputStreamReader(con.inputStream)
            } else {
                //InputStreamReader(con.errorStream)
                return null
            }

            val inp = BufferedReader(streamReader)
            val response = inp.readText()
            inp.close()

            println(response)

            return parsingJsonObject(response)
        }

        private fun parsingJsonObject(response: String): ModelPost {
            val obj = JSONObject(response)

            return ModelPost(
                obj.getInt("id"),
                obj.getInt("userId"),
                obj.getString("title"),
                obj.getString("body")
            )
        }
    }
}
