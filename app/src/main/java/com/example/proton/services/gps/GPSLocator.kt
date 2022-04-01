package com.example.proton.services.gps

import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.util.Log
import kotlin.text.toByteArray
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.apache.commons.lang3.SerializationUtils
import java.io.Serializable
import java.nio.charset.Charset
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*


class GPSLocator(private val context: Context): LocationListener {

    override fun onLocationChanged(location: Location) {
        val latitude = location.latitude
        val altitude = location.longitude

        Log.d("API", "Location changed: $location")
        sendPostRequest(latitude,altitude)

    }

    fun sendPostRequest(latitude: Double, longitude: Double) {
        val queue = Volley.newRequestQueue(context)
        val url = "http://192.168.1.68:8080/api/device/inform-location"

        val requestBody = Device(UUID.fromString("de835735-e150-41ee-8dfc-525926adb0f6"),
        UUID.fromString("de835735-e150-41ee-8dfc-525926adb0f6"),
        DeviceLocation(latitude, longitude)
        )

        val stringReq: StringRequest =
            object : StringRequest(Method.POST, url,
            Response.Listener { response ->
                val strResp = response.toString()
                Log.d("API", strResp)
            },
            Response.ErrorListener { error ->
                Log.d("API", "error => $error")
            }) {
                override fun getBody(): ByteArray {
                    return SerializationUtils.serialize(requestBody)
                }
            }
        Log.d("API", "Post request done")

        queue.add(stringReq)

    }


}

data class Device(
    val id: UUID,
    val privateKey: UUID,
    val location: DeviceLocation,
    val lastUpdate: LocalDateTime? = null,
    val tag: String? = null
) : Serializable

data class DeviceLocation(
    val latitude: Double,
    val longitude: Double
)