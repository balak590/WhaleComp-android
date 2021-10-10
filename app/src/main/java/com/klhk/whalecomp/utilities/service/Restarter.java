package com.klhk.whalecomp.utilities.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.klhk.whalecomp.utilities.MyJobService;

public class Restarter extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //Log.i("Broadcast Listened", "Service tried to stop");
        //Toast.makeText(context, "Service restarted", Toast.LENGTH_SHORT).show();
//        context.stopService(new Intent(context, CompounderService.class));
//        context.stopService(new Intent(context, MyJobService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            context.startForegroundService(new Intent(context, CompounderService.class));
            context.startForegroundService(new Intent(context, MyJobService.class));
        } else {
            context.startService(new Intent(context, CompounderService.class));
            context.startService(new Intent(context, MyJobService.class));
        }
    }
}
