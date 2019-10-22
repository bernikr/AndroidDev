package com.kralofsky.cipherbox

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
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
                setIndicatorStatus(
                    if(i==200 && post != null)
                        IndicatingView.State.SUCCESS
                    else
                        IndicatingView.State.FAILED
                )
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

    fun setIndicatorStatus(status: IndicatingView.State){
        runOnUiThread {
            val indicator = findViewById<IndicatingView>(R.id.lab2indicatingView)
            indicator.state = status
            indicator.invalidate()
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

class IndicatingView: View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    enum class State {
        NOTEXECUTED, SUCCESS, FAILED
    }

    var state = State.NOTEXECUTED

    private val paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        when(state){
            State.SUCCESS -> {
                paint.color = Color.GREEN
                paint.strokeWidth = 20f
                canvas?.drawLine(0f,0f,width/2f, height.toFloat(), paint)
                canvas?.drawLine(width/2f,height.toFloat(),width.toFloat(), height/2f, paint)
            }
            State.FAILED -> {
                paint.color = Color.RED
                paint.strokeWidth = 20f
                canvas?.drawLine(0f,0f,width.toFloat(), height.toFloat(), paint)
                canvas?.drawLine(0f, height.toFloat(), width.toFloat(), 0f, paint)
            }
            else -> {}
        }
    }
}
