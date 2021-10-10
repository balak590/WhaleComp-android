package com.klhk.whalecomp.utilities;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Constants {
    public final static String BNB_ADDRESS = "0xeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee";
    public final static String BUSD_ADDRESS = "0xe9e7cea3dedca5984780bafc599bd69add087d56";
    public final static String USDT_ADDRESS = "0x55d398326f99059ff775485246999027b3197955";
    public final static String USDC_ADDRESS = "0x8ac76a51cc950d9822d68b83fe1ad97b32cd580d";
    public final static String ETH_ADDRESS = "0x2170ed0880ac9a755fd29b2688956bd959f933f8";
    public final static String BTCB_ADDRESS = "0x7130d2a12b9bcbfae4f2634d864a1ee1ce3ead9c";
    public final static String WBNB_ADDRESS = "0xbb4CdB9CBd36B01bD1cBaEBF2De08d9173bc095c";
    public final static String EPS_ADDRESS = "0xa7f552078dcc247c2684336020c03648500c6d9f";

    public final static String WHALETRUST_ADDRESS = "userWalletAddress.whaleTrust";
    public final static String WHALETRUST_ADDRESS_SELECTED = "userWalletAddress.whaleTrust.selected";
    public final static String WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME = "userWalletAddress.whaleTrust.selected.AccountName";
    public final static String WHALETRUST_PASSWORD = "userWalletAddress.whaleTrust.pass_key_word_whaletrust";
    public final static String WHALETRUST_ADDRESS_LIST = "userWalletAddress.whaleTrust.list";
    public final static String WHALETRUST_ADDRESS_METHOD = "userWalletAddress.whaleTrust.method";
    public final static String WHALETRUST_ADDRESS_PATTERN = "userWalletAddress.whaleTrust.pattern";
    public final static String WHALETRUST_LOCK_TIMEOUT = "userWalletAddress.whaleTrust.timeout";
    public final static String WHALETRUST_LOCK_FINGERPRINT = "userWalletAddress.whaleTrust.fingerprint";
    public final static String BSC_SCAN_API_KEY = "BM7AH2VYW3XB2HIUSUPHV2PZCI5751R14P";
    public final static String LAST_DATE_MILLI = "whalecomp.lastlogin.time";
    public final static String WALLET_SELECTED = "whalecomp.wallet.selected";
    public final static String WALLET_SELECTED_ADDRESS = "whalecomp.wallet.selected.address";
    public final static String WHALETRUST = "whaletrust";
    public final static String METAMASK = "metamask";
    public final static String TRUSTWALLET = "trustwallet";



    public final static String STAKED_ADDRESS = "whalecomp.wallet.selected.address.staked";
    public final static String EPSSTAKED = "EpsStaked.withus";
    public final static String APY_CALCULATED_EPS = "whaleComp.APY.calculated.amount.eps";
    public final static String APY_CALCULATED_EPS_NON_COMPOUND = "whaleComp.APY.calculated.non.compounded.amount.eps";

    public final static String WHALECOMP_ID = "whalecomp.uniqueId";

    public final static String WHALECOMP_SLIPPAGE = "whalecomp.wallet.slippage";
    public final static String WHALECOMP_TIMEOUT = "whalecomp.wallet.timeout";


    public final static String WHALETRUST_TOKEN_LIST = "userWalletAddress.whaleTrust.token.list";
    public final static String WHALETRUST_TOKEN_NAME = "userWalletAddress.whaleTrust.token.name";
    public final static String WHALETRUST_TOKEN_ADDRESS = "userWalletAddress.whaleTrust.token.Address";
    public final static String WHALETRUST_TOKEN_DIGIT = "userWalletAddress.whaleTrust.token.digit";
    public final static String WHALETRUST_TOKEN_SYMBOL = "userWalletAddress.whaleTrust.token.symbol";
    public final static String WHALETRUST_TOKEN_ACTIVE = "userWalletAddress.whaleTrust.token.active";
    public final static String WHALETRUST_TOKEN_ICON = "userWalletAddress.whaleTrust.token.icon";

    public final static String WHALECOMP_ALERT_LIST = "whaleComp.token.alert.list_complete";

    public final static String LAST_EXECUTION_TIME = "whaleComp.last.execution.time.days";
    public final static String LAST_EXECUTION_BLOCK = "whaleComp.last.execution.block";
    public final static String NEXT_EXECUTION_TIME = "whaleComp.next.execution.time.days";

    public final static String CAN_EXECUTE_COMPOUNDING = "whaleComp.perform.componding.window";


    public final static String URL_ADDRESS = "url_address";

    public static String shortAddress(String address){
        String str1 = address.substring(0, 6) + ".."+ address.substring(address.length()-4, address.length());
        return str1;
    }
    public static void getBalance(String tokenAddress, TextView balaceTextView, String symbol, Context context){
        //GetBalanceAsync getBalanceAsync = new GetBalanceAsync();
        //String amount =getBalanceAsync.doInBackground(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS),tokenAddress);
        String amount = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS),tokenAddress);

        balaceTextView.setText(String.format("%.4f", Double.parseDouble(amount))+" "+ symbol);


    }


    public static void getBalance(String tokenAddress, TextView balaceTextView, String symbol){
        //GetBalanceAsync getBalanceAsync = new GetBalanceAsync();
        //String amount =getBalanceAsync.doInBackground(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS),tokenAddress);
        String amount = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS),tokenAddress);
        balaceTextView.setText(String.format("%.4f", Double.parseDouble(amount))+" "+ symbol);


    }

    public static void getBalance(String tokenAddress, TextView balaceTextView){
        String amount = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS),tokenAddress);
        balaceTextView.setText("餘額: "+String.format("%.4f", Double.parseDouble(amount)));


    }

    public static void getFullBalance(String tokenAddress ,EditText balaceTextView){

        String amount = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS),tokenAddress);
        balaceTextView.setText(amount);


    }

    public static double diffInHour(Date startDate,Date endDate){
        long duration  = endDate.getTime() - startDate.getTime();

        double diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        double diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        double diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        double diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        return diffInHours;
    }

    public static double diffInMins(Date startDate,Date endDate){
        long duration  = endDate.getTime() - startDate.getTime();

        double diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        double diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        double diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        double diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        return diffInMinutes;
    }

    public static double diffInDays(Date startDate,Date endDate){
        long duration  = endDate.getTime() - startDate.getTime();

        double diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        double diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        double diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        double diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        return diffInDays;
    }


    public static long diffTimeSecond(Date startDate,Date endDate){
        long duration  = endDate.getTime() - startDate.getTime();

        long diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration);
        long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
        long diffInHours = TimeUnit.MILLISECONDS.toHours(duration);
        long diffInDays = TimeUnit.MILLISECONDS.toDays(duration);
        return diffInSeconds;
    }

    public static  void roundButtonPressed(neo roundButton,ImageView imageviewC1,Activity activity){
        roundButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try{
                    if (roundButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                roundButton.setStyle(neo.small_inner_shadow);
                                imageviewC1.setScaleX(imageviewC1.getScaleX() * 0.85f);
                                imageviewC1.setScaleY(imageviewC1.getScaleY() * 0.85f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                roundButton.setStyle(neo.drop_shadow);
                                imageviewC1.setScaleX(1);
                                imageviewC1.setScaleY(1);
                                Thread.sleep(150);
                                activity.finish();
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }

                return false;
            }
        });
    }

    public static  void roundButtonPressed(neo roundButton, ImageView imageviewC1, Activity activity, AlertDialog dialog){
        roundButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                try{
                    if (roundButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                roundButton.setStyle(neo.small_inner_shadow);
                                imageviewC1.setScaleX(imageviewC1.getScaleX() * 0.96f);
                                imageviewC1.setScaleY(imageviewC1.getScaleY() * 0.96f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                roundButton.setStyle(neo.drop_shadow);
                                imageviewC1.setScaleX(1);
                                imageviewC1.setScaleY(1);
                                Thread.sleep(60);
                                dialog.cancel();
                                return true; // if you want to handle the touch event
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    return false;
                }

                return false;
            }
        });
    }

    public static boolean buttonPressedEffectCircle(neo circle_button,MotionEvent event,ImageView imageviewC1){
        try{
            if (circle_button.isShapeContainsPoint(event.getX(), event.getY())) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // PRESSED
                        //use only "small inner shadow" because its same size with "drop shadow" style and "big inner shadow" is bigger
                        // "small inner shadow" = "drop shadow"
                        // "big inner shadow"  > "drop shadow"
                        circle_button.setStyle(neo.small_inner_shadow);
                        imageviewC1.setScaleX(imageviewC1.getScaleX() * 0.95f);
                        imageviewC1.setScaleY(imageviewC1.getScaleY() * 0.95f);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED
                        circle_button.setStyle(neo.drop_shadow);
                        imageviewC1.setScaleX(1);
                        imageviewC1.setScaleY(1);
                        Thread.sleep(150);
                        return true; // if you want to handle the touch event
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return false;
    }

    static class GetBalanceAsync extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... args) {
            String message ="";
            try{
                message= FunctionCall.getBalance(args[0],args[1]);

            }catch (Exception e){
                e.printStackTrace();
            }
            return message;
        }

        protected void onPostExecute(String message) {

        }
    }

//    public static void getBalance(String tokenAddress, TextView balaceTextView, String symbol){
//        String tag_json_obj = "json_obj_req";
//        //value = 0.0;
//        String url = "http://api.whalecomp.com/getWalletBalance";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("userAddress", Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));
//        params.put("tokenAddress",tokenAddress);
//
//        Log.e("params",params.toString());
//
//
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                url, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("TAG", response.toString());
//                        try {
//                            if(response.getString("code").equalsIgnoreCase("0")){
//                                String amount = response.getString("data").toString();
//                                balaceTextView.setText(String.format("%.4f", Double.parseDouble(amount))+" "+ symbol);
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("Error:", error.getMessage());
//            }
//        }) {
//
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
//                Log.d("getHeaders", params.toString());
//                return params;
//            }
//
//        };
//        jsonObjReq.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//
//    }
//
//    public static void getFullBalance(String tokenAddress ,EditText balaceTextView){
//        String tag_json_obj = "json_obj_req";
//        //value = 0.0;
//        String url = "http://api.whalecomp.com/getWalletBalance";
//        Map<String, String> params = new HashMap<String, String>();
//        params.put("userAddress", Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));
//        params.put("tokenAddress",tokenAddress);
//
//        Log.e("params",params.toString());
//
//
//
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                url, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("TAG", response.toString());
//                        try {
//                            if(response.getString("code").equalsIgnoreCase("0")){
//                                String amount = response.getString("data").toString();
//                                balaceTextView.setText(amount);
//
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("Error:", error.getMessage());
//            }
//        }) {
//
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("Content-Type", "application/json");
//                Log.d("getHeaders", params.toString());
//                return params;
//            }
//
//        };
//        jsonObjReq.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//
//
//    }

    public static void hideSoftKeyboard(Activity activity) {
//        InputMethodManager inputMethodManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        InputMethodManager inputManager = (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusedView = activity.getCurrentFocus();
        if (focusedView != null) {
            inputManager.hideSoftInputFromWindow(focusedView.getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    public static void setupUI(View view, Activity activity) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView,activity);
            }
        }
    }
}
