package com.kralofsky.cipherbox

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.Path.FillType
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONArray
import org.json.JSONException
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL


class Lab2Activity : AppCompatActivity() {
    companion object : MainMenuEntry {
        override val name = "Lab 2"
        override val description = ""
        override val imageId = R.drawable.lab_flask
        override val activity = Lab2Activity::class
    }

    private var publication: ModelPost? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab2activitylayout)

        findViewById<Button>(R.id.send_request).setOnClickListener {
            setIndicatorStatus(IndicatingView.State.LOADING)

            val ro = RequestOperator()
            ro.listener = { i: Int, posts: List<ModelPost>? ->
                publication = posts?.get(0)
                updatePublication()
                setIndicatorStatus(
                    if(i==200 && posts != null)
                        IndicatingView.State.SUCCESS
                    else
                        IndicatingView.State.FAILED
                )
                findViewById<TextView>(R.id.lab2number).text = "There are ${posts?.size ?: 0} Posts"
            }
            ro.start()
        }


        val animator = ValueAnimator.ofFloat(0f, 1f)
        animator.duration = 1000
        animator.repeatMode = ValueAnimator.RESTART
        animator.repeatCount = ValueAnimator.INFINITE
        animator.interpolator = LinearInterpolator()
        val indicator = findViewById<IndicatingView>(R.id.lab2indicatingView)
        animator.addUpdateListener {
            indicator.animation = it.animatedValue as Float
            indicator.invalidate()
        }
        animator.start()
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
        var listener: ((Int, List<ModelPost>?) -> Unit)? = null

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

        private fun request(): List<ModelPost>? {
            val obj = URL("https://jsonplaceholder.typicode.com/posts")
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

        private fun parsingJsonObject(response: String): List<ModelPost> {
            val arr = JSONArray(response)

            return (0 until arr.length()).map {
                val obj = arr.getJSONObject(it)
                ModelPost(
                    obj.getInt("id"),
                    obj.getInt("userId"),
                    obj.getString("title"),
                    obj.getString("body")
                )
            }
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
        NOTEXECUTED, LOADING, SUCCESS, FAILED
    }

    var state = State.NOTEXECUTED
    var animation = 0f

    private val paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        when(state){
            State.SUCCESS -> {
                paint.color = Color.GREEN
                paint.strokeWidth = 20f
                paint.pathEffect = null
                canvas?.drawLine(0f,0f,width/2f, height.toFloat(), paint)
                canvas?.drawLine(width/2f,height.toFloat(),width.toFloat(), height/2f, paint)
            }
            State.FAILED -> {
                paint.color = Color.RED
                paint.strokeWidth = 20f
                paint.pathEffect = null
                canvas?.drawLine(0f,0f,width.toFloat(), height.toFloat(), paint)
                canvas?.drawLine(0f, height.toFloat(), width.toFloat(), 0f, paint)
            }
            State.LOADING -> {
                paint.color = Color.YELLOW
                paint.strokeWidth = 20f
                paint.style = Paint.Style.STROKE
                paint.pathEffect = DashPathEffect(floatArrayOf(40f, 20f), 0f)

                val path = Path()

                path.fillType = FillType.EVEN_ODD
                path.moveTo(width.toFloat()/2, 0f)
                path.lineTo(0f, width.toFloat())
                path.lineTo(height.toFloat(), width.toFloat())
                path.close()

                canvas?.drawPath(path, paint)

                val size = width.toFloat()/3

                paint.color = Color.BLACK
                paint.strokeWidth = 20f
                paint.style = Paint.Style.STROKE
                paint.pathEffect = null

                canvas?.drawArc(
                    (width-size)/2,
                    (height-size)/2+height/6,
                    (width+size)/2,
                    (height+size)/2+height/6,
                    animation*360, 270f, false, paint)


                val rectNum = 5
                val currentRects = (animation*(rectNum+1)).toInt()
                paint.color = Color.GREEN
                paint.strokeWidth = 0f
                paint.style = Paint.Style.FILL_AND_STROKE

                for(i in 1 .. currentRects) {
                    paint.color = Color.rgb(0,
                        mapRange(FloatRange(1f,rectNum.toFloat()), FloatRange(180f, 40f), i.toFloat()).toInt(),
                        0)
                    val rectwidth = width.toFloat()/rectNum
                    val left = (i-1)*rectwidth
                    canvas?.drawRect(left, height-50f, left+rectwidth, height.toFloat(), paint)
                }
            }
            else -> {}
        }
    }
}

class FloatRange(override val start: Float, override val endInclusive: Float) : ClosedRange<Float>

fun mapRange(range1: FloatRange, range2: FloatRange, value: Float): Float {
    require(range1.endInclusive != range1.start) { "first range cannot be single-valued" }
    return range2.start + (value - range1.start) * (range2.endInclusive - range2.start) / (range1.endInclusive - range1.start)
}

