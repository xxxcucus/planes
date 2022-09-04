package com.planes.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import com.google.gson.Gson
import com.planes.multiplayer_engine.responses.ErrorResponse

class Tools {
    companion object Tools
    {
        fun openLink(context: Context, linkString: String?) {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(linkString))
            val b = Bundle()
            b.putBoolean("new_window", true)
            intent.putExtras(b)
            val packageManager = context.packageManager
            val activities = packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY)
            val isIntentSafe = activities.size > 0
            if (isIntentSafe) context.startActivity(intent) else Toast.makeText(context, "No app to display page!", Toast.LENGTH_SHORT).show()
        }

        fun parseJsonError(jsonErrorString: String?, generalError: String, unknownError: String): String {

            val errorString: String = if (jsonErrorString != null) {
                val gson = Gson()
                val errorResponse = gson.fromJson(jsonErrorString, ErrorResponse::class.java)

                if (errorResponse != null)
                    generalError + ":" + errorResponse.m_Message + "(" + errorResponse.m_Status + ")"
                else
                    "$generalError:$unknownError"
            } else {
                "$generalError:$unknownError"
            }
            return errorString
        }

        fun displayToast(message: String, context: Context) {
            val duration = Toast.LENGTH_SHORT
            val toast = Toast.makeText(context, message, duration)
            toast.show()
        }

    }
}