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
        var screenCount = 0
        var firstActivityName = ""

        application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
            override fun onActivityCreated(activity: Activity, p1: Bundle?) {

                /*if (screenCount == 0) {
                    socketHandler.establishConnection();
                }
                socketHandler.onTrack("session")
                screenCount++;*/

                /*if (screenCount == 0){
                    logEvent(activity,"Socket connect ${activity.localClassName}")
                    firstActivityName = activity.localClassName
                }
                screenCount++
                logEvent(activity,"$screenCount")
                trackViews(activity)*/
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
                //logEvent(activity,"Activity Started: ${activity.localClassName}")
            }

            override fun onActivityResumed(activity: Activity) {
                //logEvent(activity,"Activity Resumed: ${activity.localClassName}")
            }

            override fun onActivityPaused(activity: Activity) {
                //logEvent(activity,"Activity Paused: ${activity.localClassName}")
                /*if (activity.localClassName == firstActivityName *//*&& screenCount == 0*//*){
                    logEvent(activity,"Destroyed Socket disconnect")
                    logEvent(activity,"Activity Destroyed: ${activity.localClassName} $screenCount")
                }*/
            }

            override fun onActivityStopped(activity: Activity) {
                //logEvent(activity,"Activity Stopped: ${activity.localClassName}")
                /*if (activity.localClassName == firstActivityName && screenCount == 0) {
                    logEvent(activity,"Stopped Socket disconnect")
                }*/

            }

            override fun onActivitySaveInstanceState(activity: Activity, p1: Bundle) {
                //Log.i("APPLICATION_LIFECYCLE", "onActivitySaveInstanceState")
            }

            override fun onActivityDestroyed(activity: Activity) {
                //logEvent(activity,"Activity Destroyed: ${activity.localClassName}")
                screenCount--
                if (activity.localClassName == firstActivityName && screenCount == 1){
                    logEvent(activity,"Destroyed Socket disconnect")
                    logEvent(activity,"Activity Destroyed: ${activity.localClassName}")
                }
                logEvent(activity,"Activity Destroyed12: ${activity.localClassName} $screenCount")
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
            val resourceId = view.id
            val resourceName = if (resourceId != View.NO_ID) {
                try {
                    view.context.resources.getResourceName(resourceId)
                } catch (e: Exception) {
                    "UNKNOWN_ID"
                }
            } else {
                "NO_ID"
            }
            val viewName = view.javaClass.simpleName
            val activityName = activity.localClassName
            clickEvent(activity,"View Clicked: $viewName with id $resourceName in activity $activityName")
        }
    }
    private fun clickEvent(activity: Activity,event: String){
        Toast.makeText(activity,event,Toast.LENGTH_SHORT).show()
    }

    fun viewClickEvent(activity: Activity, btnId:Int, userName:String,view: View){
        val resourceName = view.context.resources.getResourceName(btnId)
        Toast.makeText(activity,"User Name: $userName with resourceName $resourceName ",Toast.LENGTH_SHORT).show()
        Log.i("asdfviewType", view.toString())
        Log.i("asdfviewid", view.context.resources.getResourceName(btnId))
    }

    /*fun changeUserLoggedIn(event: String,jsonObject: JSONObject){
        //SocketHandler.changeUserLoggedIn(event,jsonObject)
        Log.i("changeUserLoggedIn","$event - $jsonObject")
    }
    fun screenChange(event: String, jsonObject: JSONObject){
        SocketHandler.screenChange(event,jsonObject)
    }
    fun viewClicked(event: String, jsonObject: JSONObject){
        SocketHandler.viewClicked(event,jsonObject)
    }*/

}