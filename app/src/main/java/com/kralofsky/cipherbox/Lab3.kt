package com.kralofsky.cipherbox

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


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
    private val coordinatesTextView by lazy { findViewById<TextView>(R.id.lab3_coordinates) }

    private val senSensorManager by lazy { getSystemService(Context.SENSOR_SERVICE) as SensorManager }
    private val senAccelerometer: Sensor? by lazy { senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) }

    private val locationManager by lazy { getSystemService(Context.LOCATION_SERVICE) as LocationManager }

    private var informationObtained = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.lab3activitylayout)

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
    }


    override fun onSensorChanged(event: SensorEvent?) {
        val mySensor = event?.sensor
        if (mySensor?.type == Sensor.TYPE_ACCELEROMETER) {
            xValueTextView.text = event.values[0].toString()
            yValueTextView.text = event.values[1].toString()
            zValueTextView.text = event.values[2].toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPause() {
        super.onPause()
        senSensorManager.unregisterListener(this, senAccelerometer)
    }

    override fun onResume() {
        super.onResume()
        if (informationObtained) {
            senSensorManager.registerListener(this, senAccelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 400, 1f, this)
        } catch (e: SecurityException) {

        }
    }

    override fun onLocationChanged(location: Location?) {
        coordinatesTextView.text = "lat: ${location?.latitude} lon: ${location?.longitude}"
        locationManager.removeUpdates(this)
    }

    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}

    override fun onProviderEnabled(provider: String?) {}

    override fun onProviderDisabled(provider: String?) {}
}