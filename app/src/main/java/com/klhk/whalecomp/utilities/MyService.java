package com.klhk.whalecomp.utilities;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import static com.klhk.whalecomp.utilities.Constants.BNB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.BUSD_ADDRESS;

public class MyService extends Service {
    public MyService() {
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        onTaskRemoved(intent);

        double amountOut = FunctionCall.getSwapAmount(BNB_ADDRESS,BUSD_ADDRESS,String.valueOf(1),String.valueOf(0.0001));
        Toast.makeText(getApplicationContext(),String.valueOf(amountOut),
                Toast.LENGTH_SHORT).show();
        return START_STICKY;
    }
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }
}
