package com.planes_multiplayer.android

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.net.Uri
import android.widget.Toast

public class Tools {
    companion object Links
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
    }
}