package com.kralofsky.cipherbox

import android.content.Context
import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.abs


class Lab3Activity : SensorEventListener, LocationListener, AppCompatActivity() {
    companion object : MainMenuEntry {
        override val name = "Lab 3"
        override val description = ""
        override val imageId = R.drawable.lab_flask
        override val activity = Lab3Activity::class
    }

    private val xValueTextView by lazy { findViewById<TextView>(R.id.lab3_x_valuse) }
    private val yValueTextView by lazy { findViewById<TextView>(R.id.lab3_y_valuse) }
    private val zValueTextView by lazy { findViewById<TextView>(R.id.lab3_z_valuse) }
    private val orientationTextView by lazy { findViewById<TextView>(R.id.lab3_orientation_text) }
    private val coordinatesTextView by lazy { findViewById<TextView>(R.id.lab3_coordinates) }
    private val coordinatesNetworkTextView by lazy { findViewById<TextView>(R.id.lab3_coordinates_network) }
    private val compassView by lazy { findViewById<CompassView>(R.id.lab3_compass) }

    private val senSensorManager by lazy { getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val senAccelerometer by lazy { senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }
    private val senOrientation by lazy { senSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION) }

    private val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    private var informationObtained = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab3activitylayout)

        senSensorManager.registerListener(this, senOrientation, SensorManager.SENSOR_DELAY_NORMAL)

        val startAndStop = findViewById<Button>(R.id.lab3_start_and_stop)
        startAndStop.setOnClickListener {
            if(senAccelerometer == null){
                Toast.makeText(this, "No Sensor", Toast.LENGTH_LONG).show()
            }

            if(informationObtained) {
                startAndStop.text = "Start"
                senSensorManager.unregisterListener(this, senAccelerometer)
                informationObtained = false
            } else {
                senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
                startAndStop.text = "Stop"
                informationObtained = true
            }
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !Settings.System.canWrite(this)
        ){
            val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS)
            startActivity(intent)
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        val mySensor = (event?: return).sensor
        val values = event.values
        when (mySensor?.type) {
            Sensor.TYPE_ACCELEROMETER -> {
                xValueTextView.text = values[0].toString()
                yValueTextView.text = values[1].toString()
                zValueTextView.text = values[2].toString()

                if (values.map { it * it }.sum() > 15 * 15) {
                    Toast.makeText(this, "please dont shake me!!!", Toast.LENGTH_SHORT).show()
                }

                val absoluteValues = values.map { abs(it) }

                var index = absoluteValues.indexOf(absoluteValues.max())
                if (values[index] < 0) index += 3

                val string = listOf(
                    "Left side down",
                    "Upright",
                    "Screen Up",
                    "Right side down",
                    "uʍop ǝpᴉsd∩",
                    "Overhead"
                )[index]
                orientationTextView.text = string
            }
            Sensor.TYPE_ORIENTATION -> {
                compassView.baring = values[0]
                compassView.invalidate()

                var tilt = -values[1]/90
                if(tilt < 0) tilt = 0f
                if(tilt > 1) tilt = 1f

                try {
                    Settings.System.putInt(
                        this.contentResolver,
                        Settings.System.SCREEN_BRIGHTNESS,
                        (tilt*255).toInt()
                    )
                } catch (e: SecurityException){
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPause() {
        super.onPause()
        senSensorManager.unregisterListener(this, senAccelerometer)
        senSensorManager.unregisterListener(this, senOrientation)
        locationManager.removeUpdates(this)
    }

    override fun onResume() {
        super.onResume()
        if (informationObtained) {
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }

        senSensorManager.registerListener(this, senOrientation, SensorManager.SENSOR_DELAY_NORMAL)

        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1f, this)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 400, 1f, this)
        } catch (e: SecurityException) {

        }
    }

    override fun onLocationChanged(location: Location?) {
        val text = "lat: ${location?.latitude} lon: ${location?.longitude}"
        when(location?.provider){
            LocationManager.GPS_PROVIDER -> coordinatesTextView.text = text
            LocationManager.NETWORK_PROVIDER -> coordinatesNetworkTextView.text = text
        }
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}
}

class CompassView: View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    var baring = 0f

    private val paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val circleSize = width.toFloat()*0.7f
        val arcSize = 20f

        paint.color = Color.BLACK
        paint.strokeWidth = 20f
        paint.style = Paint.Style.STROKE
        paint.pathEffect = null

        canvas?.drawArc(
            (width-circleSize)/2,
            (height-circleSize)/2,
            (width+circleSize)/2,
            (height+circleSize)/2,
            -baring-90-arcSize/2, arcSize, false, paint)
    }
}