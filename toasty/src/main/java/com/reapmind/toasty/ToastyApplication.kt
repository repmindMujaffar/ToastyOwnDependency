package com.reapmind.toasty

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.reapmind.toasty.utils.SocketHandler
import org.json.JSONException
import org.json.JSONObject
import java.util.Calendar

class ToastyApplication : Application() {

    private var activityCounter: Int = 0

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, p1: Bundle?) {
                if (activityCounter == 0) {
                    SocketHandler.establishConnection();
                }
                activityCounter++;
            }

            override fun onActivityStarted(activity: Activity) {
                Log.i("APPLICATION_LIFECYCLE", "onActivityStarted${activity.localClassName}")
                Log.i("APPLICATION_LIFECYCLE", "onActivityStarted${activity.localClassName}")

                val jsonObject = JSONObject()
                try {
                    jsonObject.put("data", activity.localClassName)
                    jsonObject.put("message", activity.localClassName)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                SocketHandler.emitTrack(
                    activity.localClassName,
                    activity.localClassName,
                    SocketHandler.findDifference(
                        System.currentTimeMillis(),
                        System.currentTimeMillis()
                    ).toInt(),
                    System.currentTimeMillis(),
                    System.currentTimeMillis(),
                    jsonObject
                )
                Toast.makeText(activity, "EventAdded", Toast.LENGTH_SHORT).show()
            }

            override fun onActivityResumed(activity: Activity) {
                Log.i(
                    "APPLICATION_LIFECYCLE",
                    "onActivityResumed${activity.callingActivity.toString()}"
                )
            }

            override fun onActivityPaused(activity: Activity) {
                Log.i(
                    "APPLICATION_LIFECYCLE",
                    "onActivityPaused${activity.callingActivity.toString()}"
                )
            }

            override fun onActivityStopped(activity: Activity) {
                activityCounter--
                if (activityCounter == 0) {
                    SocketHandler.socketDisconnect();
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
                Log.i("APPLICATION_LIFECYCLE", "onActivitySaveInstanceState")
            }

            override fun onActivityDestroyed(activity: Activity) {
                Log.i("APPLICATION_LIFECYCLE", "onActivityDestroyed")
            }
        })
        unregisterActivityLifecycleCallbacks(object :ActivityLifecycleCallbacks{
            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
            }
            override fun onActivityStarted(p0: Activity) {
            }
            override fun onActivityResumed(p0: Activity) {
            }
            override fun onActivityPaused(p0: Activity) {
            }
            override fun onActivityStopped(p0: Activity) {
            }
            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
            }
            override fun onActivityDestroyed(activity: Activity) {
                Log.i("APPLICATION_LIFECYCLE", "onActivityDestroyed-Event disconnect")
                Toast.makeText(activity, "Event disconnected", Toast.LENGTH_SHORT).show()
            }
        })

    }

}