package com.reapmind.toasty.utils

import android.content.Context
import android.os.Build
import android.util.Log
import com.google.gson.JsonParser
import com.reapmind.toasty.utils.Constants.DATE_TIME_FORMAT
import com.reapmind.toasty.utils.Constants.DEVICE
import com.reapmind.toasty.utils.Constants.END_TIME
import com.reapmind.toasty.utils.Constants.EVENT
import com.reapmind.toasty.utils.Constants.EVENT_NAME
import com.reapmind.toasty.utils.Constants.PROPERTIES
import com.reapmind.toasty.utils.Constants.SCREEN_NAME
import com.reapmind.toasty.utils.Constants.SESSION_ID
import com.reapmind.toasty.utils.Constants.SOCKET_URL
import com.reapmind.toasty.utils.Constants.START_TIME
import com.reapmind.toasty.utils.Constants.TIME_SPENT
import com.reapmind.toasty.utils.Constants.TRACK
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONException
import org.json.JSONObject
import java.io.OutputStream
import java.net.URISyntaxException
import java.net.Socket as JavaSocket
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

const val NEW_USER_KEY: String = ""
const val TEST_USER_KEY: String = "TEST_USER_KEY"
const val TEST_SESSION_ID: String = "TEST_SESSION_ID"

const val APPLICATION: String = "Application"
const val APPLICATION_OPEN: String = "ApplicationOpen"
const val APPLICATION_CLOSE: String = "ApplicationClose"

const val MODEL: String = "model"
const val BRAND: String = "brand"
const val ID: String = "id"
const val SERIAL: String = "serial"
const val MANUFACTURER: String = "manufacturer"
const val ANDROID_VERSION: String = "androidVersion"
const val ANDROID_VERSION_NAME: String = "androidVersionName"
const val PLATFORM: String = "platform"
const val PLATFORM_ANDROID: String = "Android"

object SocketHandler {


    private lateinit var mSocket: Socket
    private lateinit var socketStartTime: Date
    private lateinit var outputStream: OutputStream
    private lateinit var javaSocket: JavaSocket
    private var KEY: String = "KEY"//connection establish key
    private lateinit var sessionManager: SessionManager


    fun establishConnection(key: String) {
        try {
            KEY = key
            val opts = IO.Options()
            opts.path = "socket.io"
            mSocket = IO.socket("$SOCKET_URL?userId=${TEST_USER_KEY}")
            /*javaSocket = JavaSocket(SOCKET_URL, 3000)
            outputStream = javaSocket.getOutputStream()*/
            socketStartTime = Calendar.getInstance().time
            emitDevice()
            mSocket.connect()
            this.sessionManager.saveKey(key)
            onTrack("session")
            Log.i("SocketConnect123","Connected")
        }catch (e: URISyntaxException) {
            e.printStackTrace()
            Log.i("SocketConnect123",e.message.toString())
        } catch (e: Exception) {
            e.printStackTrace()
            Log.i("SocketConnect1234",e.message.toString())
        }

    }
    fun initialize(context: Context) {
        this.sessionManager = SessionManager(context)

    }

    fun socketDisconnect() {
        val endTime = Calendar.getInstance().time
        emitTrack(
            APPLICATION,
            APPLICATION_CLOSE,
            findDifference(socketStartTime.time, endTime.time).toInt(),
            getDateInStringFormat(socketStartTime).toLong(),
            System.currentTimeMillis(),
            getDeviceInfo()
        )
        mSocket.disconnect()
    }

    fun emitTrack(
        screenName: String,
        eventName: String,
        timeSpent: Int,
        startTime: Long,
        endTime: Long,
        properties: JSONObject
    ) {
        val jsonObject = JSONObject()
        jsonObject.put(SESSION_ID, TEST_SESSION_ID)
        val innerJSONObject = JSONObject()
        innerJSONObject.put(SCREEN_NAME, screenName)
        innerJSONObject.put(EVENT_NAME, screenName)
        innerJSONObject.put(TIME_SPENT, timeSpent)
        innerJSONObject.put(START_TIME, startTime)
        innerJSONObject.put(END_TIME, endTime)
        innerJSONObject.put(PROPERTIES, properties)
        jsonObject.put(EVENT, innerJSONObject)
        mSocket.emit(TRACK, jsonObject)
    }

    private fun emitDevice() {
        val jsonObject = JSONObject()
        jsonObject.put(SESSION_ID, TEST_SESSION_ID)
        jsonObject.put(DEVICE, getDeviceInfo())
        mSocket.emit(DEVICE, jsonObject)
    }

    private fun getDeviceInfo(): JSONObject {
        val deviceModel = Build.MODEL
        val deviceBrand = Build.BRAND
        val deviceID = Build.ID
        val deviceSerial = Build.SERIAL
        val deviceManufacturer = Build.MANUFACTURER
        val androidVersion = Build.VERSION.SDK_INT
        val androidVersionName = Build.VERSION.RELEASE
        val jsonObject = JSONObject()
        try {
            jsonObject.put(MODEL, deviceModel)
            jsonObject.put(BRAND, deviceBrand)
            jsonObject.put(ID, deviceID)
            jsonObject.put(SERIAL, deviceSerial)
            jsonObject.put(MANUFACTURER, deviceManufacturer)
            jsonObject.put(ANDROID_VERSION, androidVersion)
            jsonObject.put(ANDROID_VERSION_NAME, androidVersionName)
            jsonObject.put(PLATFORM, PLATFORM_ANDROID)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        return jsonObject
    }


    fun getDateInStringFormat(startDate: Date): String {
        val sdf = SimpleDateFormat(DATE_TIME_FORMAT, Locale.getDefault())
        return sdf.format(startDate)
    }

    fun findDifference(
        startDate: Long,
        endDate: Long
    ): String {
        val different = endDate - startDate
        val secondsInMilli: Long = 1000
        val elapsedSeconds = different / secondsInMilli
        return elapsedSeconds.toString()
    }

    fun sendEvent(event: String) {
        Thread {
            try {
                outputStream.write(event.toByteArray())
                outputStream.flush()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
    fun onTrack(eventName: String) {
        mSocket.on(eventName) {
            try {
                val jsonObject = JSONObject(JsonParser.parseString(it[0].toString()).toString())
                this.sessionManager.saveSessionId(jsonObject.getString("sessionId"))
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }.on(Socket.EVENT_CONNECT_ERROR) {
            println("Connection error: ${it[0]}")
        }
    }
    fun getSessionId() : String?{
        return this.sessionManager.getSessionId()
    }
    fun getKey():String?{
        return this.sessionManager.getKey()
    }

}