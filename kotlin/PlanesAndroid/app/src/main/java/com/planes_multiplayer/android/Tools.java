package com.planes_multiplayer.android;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;

import android.app.Activity;
import android.widget.Toast;

import java.util.List;


public class Tools {

    static void openLink(Context context, String linkString) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(linkString));
        Bundle b = new Bundle();
        b.putBoolean("new_window", true);

        intent.putExtras(b);

        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe)
            context.startActivity(intent);
        else
            Toast.makeText(context, "No app to display page!", Toast.LENGTH_SHORT).show();
    }

}
