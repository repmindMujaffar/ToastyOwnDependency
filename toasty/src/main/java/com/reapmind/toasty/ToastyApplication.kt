package com.reapmind.toasty

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
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
                trackViews(activity)
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
                logEvent("Activity Started: ${activity.localClassName}")
                Toast.makeText(activity, "EventAdded", Toast.LENGTH_SHORT).show()
            }

            override fun onActivityResumed(activity: Activity) {
                /*Log.i(
                    "APPLICATION_LIFECYCLE",
                    "onActivityResumed${activity.callingActivity.toString()}"
                )*/
                logEvent("Activity Resumed: ${activity.localClassName}")
            }

            override fun onActivityPaused(activity: Activity) {
                /*Log.i(
                    "APPLICATION_LIFECYCLE",
                    "onActivityPaused${activity.callingActivity.toString()}"
                )*/
                logEvent("Activity Paused: ${activity.localClassName}")
            }

            override fun onActivityStopped(activity: Activity) {
                logEvent("Activity Stopped: ${activity.localClassName}")
                /*activityCounter--
                if (activityCounter == 0) {
                    SocketHandler.socketDisconnect();
                }*/

            }

            override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
                //Log.i("APPLICATION_LIFECYCLE", "onActivitySaveInstanceState")
            }

            override fun onActivityDestroyed(activity: Activity) {
                //Log.i("APPLICATION_LIFECYCLE", "onActivityDestroyed")
                logEvent("Activity Destroyed: ${activity.localClassName}")
            }
        })


    }
    private fun logEvent(event: String) {
        SocketHandler.sendEvent(event)
    }
    private fun trackViews(activity: Activity) {
        val rootView = activity.window.decorView.rootView
        traverseViews(rootView)
    }

    private fun traverseViews(view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                traverseViews(view.getChildAt(i))
            }
        }
        view.setOnClickListener {
            logEvent("View Clicked: ${view.javaClass.simpleName} with id ${view.id}")
        }
    }

}