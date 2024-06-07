package com.reapmind.toasty

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.reapmind.toasty.utils.SocketHandler
import org.json.JSONException
import org.json.JSONObject

object ToastyToast {


    fun initialize(application: Application){

        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, p1: Bundle?) {

                trackViews(activity)
            }

            override fun onActivityStarted(activity: Activity) {
                /*Log.i("APPLICATION_LIFECYCLE", "onActivityStarted${activity.localClassName}")
                Log.i("APPLICATION_LIFECYCLE", "onActivityStarted${activity.localClassName}")*/

                /*val jsonObject = JSONObject()
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
                )*/
                logEvent(activity,"Activity Started: ${activity.localClassName}")
                //Toast.makeText(activity, "EventAdded", Toast.LENGTH_SHORT).show()
            }

            override fun onActivityResumed(activity: Activity) {
                /*Log.i(
                    "APPLICATION_LIFECYCLE",
                    "onActivityResumed${activity.callingActivity.toString()}"
                )*/
                logEvent(activity,"Activity Resumed: ${activity.localClassName}")
            }

            override fun onActivityPaused(activity: Activity) {
                /*Log.i(
                    "APPLICATION_LIFECYCLE",
                    "onActivityPaused${activity.callingActivity.toString()}"
                )*/
                logEvent(activity,"Activity Paused: ${activity.localClassName}")
            }

            override fun onActivityStopped(activity: Activity) {
                logEvent(activity,"Activity Stopped: ${activity.localClassName}")
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
                logEvent(activity,"Activity Destroyed: ${activity.localClassName}")
            }
        })

    }
    private fun logEvent(activity: Activity,event: String) {
        //SocketHandler.sendEvent(event)
        Toast.makeText(activity,event,Toast.LENGTH_SHORT).show()
    }
    private fun trackViews(activity: Activity) {
        val rootView = activity.window.decorView.rootView
        traverseViews(activity,rootView)
    }

    private fun traverseViews(activity: Activity,view: View) {
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                traverseViews(activity,view.getChildAt(i))
            }
        }
        view.setOnClickListener {
            logEvent(activity,"View Clicked: ${view.javaClass.simpleName} with id ${view.id}")
        }
    }

}