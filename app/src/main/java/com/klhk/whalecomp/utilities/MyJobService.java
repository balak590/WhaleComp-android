package com.klhk.whalecomp.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.utilities.service.Restarter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.klhk.whalecomp.utilities.Constants.BNB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.BUSD_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.LAST_EXECUTION_TIME;
import static com.klhk.whalecomp.utilities.Constants.WHALECOMP_ALERT_LIST;


public class MyJobService extends Service {

//    GetQuoteAsyncTask getQuoteAsyncTask;
    JobParameters parameters;
    final Handler handler = new Handler();
    final int delay = 20000;
    private int numMessages = 0;
    private static final String CHANNEL_NAME = "Whalecomp Channel";
    private static final String CHANNEL_DESC = "Whalecomp Channel for alerts";
    JSONArray array;
    Double toke1,toke2,toke3,toke4,toke5;
    int counter =0;

    public void triggerNotification(String body){
        Intent intent = new Intent(this, MainActivity.class);


        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, getString(R.string.notification_channel_id))
                .setContentTitle("Alert Reached")
                .setContentText(body)
//                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                //.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.win))
                .setContentIntent(pendingIntent)
//                .setContentInfo("Hello")
//                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.whale_logo))
//                .setColor(getColor(R.color.black))
//                .setLights(Color.RED, 1000, 300)
                .setDefaults(Notification.DEFAULT_VIBRATE)
//                    .setNumber(++numMessages)
                .setSmallIcon(R.drawable.whale_logo);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    getString(R.string.notification_channel_id), CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription(CHANNEL_DESC);
            channel.setShowBadge(true);
            channel.canShowBadge();
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});

            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);
        }

        assert notificationManager != null;
        notificationManager.notify(0, notificationBuilder.build());
        counter++;
        if(counter==array.length()){
            counter=0;

        }

    }

    public void checkPrice(double amount,JSONObject obj,double amountOut){
        try{
//            Log.e("Checking prices","started");
//            Log.e("AmountOut",String.valueOf(amountOut));
//            Log.e("cost",String.valueOf(obj.getDouble("cost")));

//            if(obj.getInt("alertId")==1){
//                toke1=Double.parseDouble(String.format("%.2f", amountOut));
//            }else if(obj.getInt("alertId")==2){
//                toke2=Double.parseDouble(String.format("%.2f", amountOut));
//            }else if(obj.getInt("alertId")==3){
//                toke3=Double.parseDouble(String.format("%.2f", amountOut));
//            }else if(obj.getInt("alertId")==4){
//                toke4=Double.parseDouble(String.format("%.2f", amountOut));
//            }else if(obj.getInt("alertId")==5){
//                toke5=Double.parseDouble(String.format("%.2f", amountOut));
//            }

            if(obj.getInt("frequency")==1){
                try{
                    JSONArray array = new JSONArray();
                    if(!Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST).toString().isEmpty()) {
                        array = new JSONArray(Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST));
                    }

                    JSONArray newArray = new JSONArray();
                    int counter=0;

                    for(int i=0;i<array.length();i++){
                        JSONObject obj1 = new JSONObject(array.get(i).toString());
                        //Log.e("alertID",String.valueOf(obj1.getInt("alertId")));
                        if(obj1.getInt("alertId")!=obj.getInt("alertId")){
                            counter++;
                            obj1.put("alertId",counter);
                            newArray.put(obj1);
                        }


                    }

                    //Log.e("array",newArray.toString());

                    Preference.getInstance().writePreference(WHALECOMP_ALERT_LIST,newArray.toString());
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else{
                try{
                    JSONArray array = new JSONArray();
                    if(!Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST).toString().isEmpty()) {
                        array = new JSONArray(Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST));
                    }

                    JSONArray newArray = new JSONArray();
                    int counter=0;

                    for(int i=0;i<array.length();i++){
                        JSONObject obj1 = new JSONObject(array.get(i).toString());
                        //Log.e("alertID",String.valueOf(obj1.getInt("alertId")));
                        if(obj1.getInt("alertId")==obj.getInt("alertId")){
                            counter++;
                            obj1.put("alertId",counter);
                            obj1.put("lastPriceAmount",amountOut);

                        }
                        newArray.put(obj1);
                    }
                    Preference.getInstance().writePreference(WHALECOMP_ALERT_LIST,newArray.toString());
                    if(obj.getInt("condition")==1){
                        //Log.e("Reached condition","1");
                        if(amountOut>obj.getDouble("cost")&& obj.getDouble("lastPriceAmount")<obj.getDouble("cost")){
                            triggerNotification(obj.getString("symbol") + " increased above "+ String.format("%.2f", amountOut));
                        }
                    }else if(obj.getInt("condition")==2){
                        //Log.e("Reached condition","2");
                        if(amountOut<obj.getDouble("cost")&& obj.getDouble("lastPriceAmount")>obj.getDouble("cost")){
                            triggerNotification(obj.getString("symbol") + " decreased below "+ String.format("%.2f", amountOut));
                        }
                    }
                    else if(obj.getInt("condition")==3){
                        //Log.e("Reached condition","3");
                        if(amountOut>obj.getDouble("cost") && obj.getDouble("lastPriceAmount")<obj.getDouble("cost")){
                            triggerNotification(obj.getString("symbol") + " increased above "+ String.format("%.2f", amountOut));
                        }
                    }else if(obj.getInt("condition")==4){
                        //Log.e("Reached condition","4");
                        if(amountOut<obj.getDouble("cost") && obj.getDouble("lastPriceAmount")>obj.getDouble("cost")){
                            triggerNotification(obj.getString("symbol") + " decreased below "+ String.format("%.2f", amountOut));
                        }
                    }else{
                        //Log.e("Reached condition","4");

                    }
                }catch (Exception e){
                    e.printStackTrace();
                }


            }


        }catch(Exception e){

            e.printStackTrace();
        }

    }

    public void triggerFunctionCall(JSONObject obj){
        try{
            double amountOut = FunctionCall.getSwapAmount(obj.getString("address"),BUSD_ADDRESS,String.valueOf(1),String.valueOf(0.0001));
            //Toast.makeText(getApplicationContext(),String.valueOf(amountOut), Toast.LENGTH_SHORT).show();
            //Log.e("obj1",obj.toString());
            if(obj.getInt("alertId")==1){
                checkPrice(toke1,obj,amountOut);
            }else if(obj.getInt("alertId")==2){
                checkPrice(toke2,obj,amountOut);
            }else if(obj.getInt("alertId")==3){
                checkPrice(toke3,obj,amountOut);
            }else if(obj.getInt("alertId")==4){
                checkPrice(toke4,obj,amountOut);
            }else if(obj.getInt("alertId")==5){
                checkPrice(toke5,obj,amountOut);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Preference.getInstance().Initialize(getApplicationContext());
        System.loadLibrary("TrustWalletCore");

        startTimer();



        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        stoptimertask();
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction("restartservice");
        broadcastIntent.setClass(this, Restarter.class);
        this.sendBroadcast(broadcastIntent);
    }

    private Timer timer;
    private TimerTask timerTask;
    public void startTimer() {
        try{
            timer = new Timer();
            timerTask = new TimerTask() {
                public void run() {
                    //Log.i("Count", "=========  "+ (counter++));
                    try{
                        //Log.e("Reached","serviceStart");
                        toke1 =0.0;
                        toke2 =0.0;
                        toke3 =0.0;
                        toke4 =0.0;
                        toke5 =0.0;

                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    if(!Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST).toString().isEmpty()) {
                                        array = new JSONArray(Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST));
                                        for(int i=0;i<array.length();i++){
                                            JSONObject obj = new JSONObject(array.get(i).toString());
                                            //Log.e("obj",obj.toString());
                                            triggerFunctionCall(obj);
                                        }
                                    }

                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
            };
            timer.schedule(timerTask, 1000, 10000); //

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void stoptimertask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

//    @Override
//    public boolean onStartJob(JobParameters params) {
//        this.parameters = params;
////        triggerNotification("Hello 1");
////        jobFinished(params,true);
//        Preference.getInstance().Initialize(getApplicationContext());
//        System.loadLibrary("TrustWalletCore");
//
//        Log.e("Reached","serviceStart");
//        toke1 =0.0;
//        toke2 =0.0;
//        toke3 =0.0;
//        toke4 =0.0;
//        toke5 =0.0;
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try{
//                    //triggerNotification("Hello");
//                    if(!Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST).toString().isEmpty()) {
//                        array = new JSONArray(Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST));
//                        //jsonArray = new JSONArray();
//                        for(int i=0;i<array.length();i++){
//
//                            JSONObject obj = new JSONObject(array.get(i).toString());
//
//                            Log.e("obj",obj.toString());
//
//                            triggerFunctionCall(obj);
//                        }
//                        System.out.println("myHandler: here!"); // Do your work here
//                        //handler.postDelayed(this, delay);
//                    }
//
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//        return true;
//    }

//    @Override
//    public boolean onStopJob(JobParameters params) {
//
//        Log.e("Reached","serviceStop");
//        return false;
//    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    private class GetQuoteAsyncTask extends AsyncTask<String,String,String>{
//
//        @Override
//        protected String doInBackground(String... strings) {
//            try{
//
////                JSONArray array = new JSONArray(strings[0]);
////
////                for(int i=0;i<array.length();i++){
////
////                }
//            }catch(Exception e){
//                e.printStackTrace();
//            }
//
//            return null;
//        }
//
//        @Override
//        protected void onProgressUpdate(String... values) {
//            super.onProgressUpdate(values);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            jobFinished(parameters,true);
//        }
//    }
}
