package com.klhk.whalecomp.AlertFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.messaging.FirebaseMessaging;
import com.github.sshadkany.neo;
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WhaleTrust.SwapWhaleTrustActivity;
import com.klhk.whalecomp.utilities.AppController;
import com.klhk.whalecomp.utilities.FunctionCall;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import static com.klhk.whalecomp.utilities.Constants.BNB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.BTCB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.BUSD_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.ETH_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.USDC_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.USDT_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALECOMP_ALERT_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ACTIVE;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ICON;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_SYMBOL;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

public class AlertSetupActivity extends AppCompatActivity {

    RelativeLayout addAlertLayout1;
    LinearLayout addAlertLayout2,token1Layout;
    ImageView token1Img;
    neo alertTypeButton,frequencyButton,createButton,saveButton,deleteButton,tokenPickerButton;
    EditText alertAmount;
    TextView token1PickLayout,token1Text,alertTypeText,frequencyText,usdAmountText,targetAmount,alertMetric;
    String toke1,selectedToken,firstToken,toke1_Img;

    double currentPrice, targetPrice;
    int alertType=1;
    int frequency= 1;
    JSONObject object;

    JSONArray tokenArray = new JSONArray();

//
//    private Socket mSocket;
//    {
//        try {
//            mSocket = IO.socket("http://16.162.20.222:3000");
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//    }
//
//    Emitter.Listener onConnect = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            Log.e("Connected","yes");
//        }
//    };
//
//    Emitter.Listener updatePrice = new Emitter.Listener() {
//        @Override
//        public void call(Object... args) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        if(!selectedToken.isEmpty()){
//                            JSONArray array = new JSONArray(args[0].toString());
//
//                            for(int i=0;i<array.length();i++){
//                                JSONObject object = new JSONObject(array.getJSONObject(i).toString());
//                                if(object.getString("_id").equalsIgnoreCase(selectedToken)){
//                                    currentPrice = object.getDouble("usd");
//                                    usdAmountText.setText(String.valueOf(object.getDouble("usd"))+" USD");
//                                }
//
//                            }
//
//                        }
//
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    //Log.e("data",args[0].toString());
//                }
//            });
//
//        }
//    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_alert_setup);
        addAlertLayout1 = findViewById(R.id.addAlertLayout1);
        addAlertLayout2 = findViewById(R.id.addAlertLayout2);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        createButton = findViewById(R.id.createButton);
        alertTypeButton = findViewById(R.id.alertTypeButton);
        frequencyButton = findViewById(R.id.frequencyButton);
        token1PickLayout = findViewById(R.id.token1PickLayout);
        token1Img = findViewById(R.id.token1Img);
        token1Layout = findViewById(R.id.token1Layout);
        token1Text = findViewById(R.id.token1Text);
        alertTypeText = findViewById(R.id.alertTypeText);
        frequencyText = findViewById(R.id.frequencyText);
        usdAmountText = findViewById(R.id.usdAmountText);
        targetAmount = findViewById(R.id.targetAmount);
        alertAmount = findViewById(R.id.alertAmount);
        alertMetric = findViewById(R.id.alertMetric);
        tokenPickerButton = findViewById(R.id.tokenPickerButton);
        selectedToken = "";
        firstToken="";
        toke1_Img="";
        toke1="";
        generateTokenList();

        if(getIntent().getStringExtra("data")==null){
            Log.e("Data", "empty");
            addAlertLayout1.setVisibility(View.VISIBLE);
            addAlertLayout2.setVisibility(View.GONE);
        }else {
            Log.e("Data", getIntent().getStringExtra("data").toString());
            addAlertLayout1.setVisibility(View.GONE);
            addAlertLayout2.setVisibility(View.VISIBLE);
            try {
                object = new JSONObject(getIntent().getStringExtra("data").toString());
                toke1=object.getString("symbol");
                //selectedToken=object.getString("coin");
                alertType = object.getInt("condition");
                frequency = object.getInt("frequency");
                toke1_Img = object.getString("icon");
                targetAmount.setText("≈ "+String.valueOf(object.getDouble("cost")));
                firstToken= object.getString("address");
                targetPrice = object.getDouble("cost");


                if(object.getInt("condition")==1){
                    alertTypeText.setText("價格超過");
                    alertMetric.setText("USD");
                    alertAmount.setText(object.getString("cost"));
                }
                else if(object.getInt("condition")==2){
                    alertTypeText.setText("價格低於");
                    alertMetric.setText("USD");
                    alertAmount.setText(object.getString("cost"));
                }
                else if(object.getInt("condition")==3){
                    alertTypeText.setText("漲幅達到");
                    alertMetric.setText("%");
                    alertAmount.setText(object.getString("percent"));
                }else if(object.getInt("condition")==4){
                    alertTypeText.setText("跌幅達到");
                    alertMetric.setText("%");
                    alertAmount.setText(object.getString("percent"));
                }
                setToken1();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        alertAmount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(alertAmount.getText().toString().isEmpty()){
                    targetPrice=0;
                    targetAmount.setText("");
                }else{
                    if(alertType==1){
                        targetPrice = Double.parseDouble(alertAmount.getText().toString());
                        targetAmount.setText("≈ "+String.format("%.2f",targetPrice)+" USD");
                    }
                    else if(alertType==2){
                        targetPrice = Double.parseDouble(alertAmount.getText().toString());
                        targetAmount.setText("≈ "+String.format("%.2f",targetPrice)+" USD");
                    }
                    else if(alertType==3){
                        targetPrice = currentPrice + (currentPrice*Double.parseDouble(alertAmount.getText().toString())/100);
                        targetAmount.setText("≈ "+String.format("%.2f",targetPrice)+" USD");
                    }
                    else if(alertType==4){
                        targetPrice = currentPrice - (currentPrice*Double.parseDouble(alertAmount.getText().toString())/100);
                        targetAmount.setText("≈ "+String.format("%.2f",targetPrice)+" USD");
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        saveButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.saveButton);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                try{
                    if (saveButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                saveButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                saveButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(100);
                                if(!toke1.isEmpty()&&!firstToken.isEmpty()&&!String.valueOf(targetPrice).isEmpty()){
                                    try{
                                        JSONArray array = new JSONArray();
                                        if(!Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST).toString().isEmpty()) {
                                            array = new JSONArray(Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST));
                                        }

                                        JSONArray newArray = new JSONArray();

                                        for(int i=0;i<array.length();i++){
                                            JSONObject obj = new JSONObject(array.get(i).toString());
                                            if(obj.getInt("alertId")==object.getInt("alertId")){
                                                obj.put("cost",String.format("%.2f",targetPrice));
                                                obj.put("address",firstToken);
                                                obj.put("condition",String.valueOf(alertType));
                                                obj.put("frequency",String.valueOf(frequency));
                                                obj.put("symbol",toke1);
                                                obj.put("icon",toke1_Img);
                                                obj.put("alertId",obj.getInt("alertId"));
                                                if(alertType==3||alertType==4){
                                                    obj.put("percent",alertAmount.getText().toString());
                                                }
                                            }
                                            newArray.put(obj);

                                        }

                                        Preference.getInstance().writePreference(WHALECOMP_ALERT_LIST,newArray.toString());
                                        finish();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }else{
                                    Toast.makeText(AlertSetupActivity.this, "Please setup alert correctly", Toast.LENGTH_SHORT).show();
                                }
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

        deleteButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.deleteButton);
                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                try{
                    if (deleteButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                deleteButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                deleteButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(100);
                                try{
                                    JSONArray array = new JSONArray();
                                    if(!Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST).toString().isEmpty()) {
                                        array = new JSONArray(Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST));
                                    }

                                    JSONArray newArray = new JSONArray();
                                    int counter=0;

                                    for(int i=0;i<array.length();i++){
                                        JSONObject obj = new JSONObject(array.get(i).toString());
                                        Log.e("alertID",String.valueOf(obj.getInt("alertId")));
                                        if(obj.getInt("alertId")!=object.getInt("alertId")){
                                            counter++;
                                            obj.put("alertId",counter);
                                            newArray.put(obj);
                                        }


                                    }

                                    Log.e("array",newArray.toString());

                                    Preference.getInstance().writePreference(WHALECOMP_ALERT_LIST,newArray.toString());
                                    finish();
                                }catch (Exception e){
                                    e.printStackTrace();
                                }
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




        createButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.createButton);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                try{
                    if (createButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                createButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                createButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(100);
                                if(!toke1.isEmpty()&&!firstToken.isEmpty()&&!String.valueOf(targetPrice).isEmpty()){
                                    try{
                                        JSONArray array = new JSONArray();
                                        if(!Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST).toString().isEmpty()) {
                                            array = new JSONArray(Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST));
                                        }
                                        JSONObject obj = new JSONObject();
                                        obj.put("cost",String.format("%.2f",targetPrice));
                                        obj.put("address",firstToken);
                                        obj.put("condition",String.valueOf(alertType));
                                        obj.put("frequency",String.valueOf(frequency));
                                        obj.put("symbol",toke1);
                                        obj.put("icon",toke1_Img);
                                        obj.put("alertId",array.length()+1);
                                        if(alertType==3||alertType==4){
                                            obj.put("percent",alertAmount.getText().toString());
                                        }


                                        array.put(obj);
                                        Preference.getInstance().writePreference(WHALECOMP_ALERT_LIST,array.toString());
                                        finish();
                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }else{
                                    Toast.makeText(AlertSetupActivity.this, "Please setup alert correctly", Toast.LENGTH_SHORT).show();
                                }
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

        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        alertTypeButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.alertTypeButton);
                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                try{
                    if (alertTypeButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                alertTypeButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                alertTypeButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(60);
                                AlertDialog.Builder builder = new AlertDialog.Builder(AlertSetupActivity.this,R.style.CustomDialog);

                                View view = getLayoutInflater().inflate(R.layout.pick_alert_type,null);
                                neo closeButton = view.findViewById(R.id.closeButton);
                                neo greaterUSDButton = view.findViewById(R.id.greaterUSDButton);
                                neo lesserUSDButton = view.findViewById(R.id.lesserUSDButton);
                                neo increasedAlertButton = view.findViewById(R.id.increasedAlertButton);
                                neo priceDroppedButton = view.findViewById(R.id.priceDroppedButton);
                                builder.setCancelable(false);

                                builder.setView(view);
                                AlertDialog dialog = builder.show();
                                try {
                                    greaterUSDButton.setOnTouchListener(new View.OnTouchListener() {
                                        @Override
                                        public boolean onTouch(View v, MotionEvent event) {
                                            ViewGroup viewGroupC1 = v.findViewById(R.id.greaterUSDButton);
                                            final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                                            try{
                                                runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                    }
                                                });
                                                if (greaterUSDButton.isShapeContainsPoint(event.getX(), event.getY())) {
                                                    switch (event.getAction()) {
                                                        case MotionEvent.ACTION_DOWN:

                                                            greaterUSDButton.setStyle(neo.small_inner_shadow);
                                                            textView.setScaleX(textView.getScaleX() * 0.95f);
                                                            textView.setScaleY(textView.getScaleY() * 0.95f);
                                                            return true; // if you want to handle the touch event
                                                        case MotionEvent.ACTION_UP:
                                                        case MotionEvent.ACTION_CANCEL:
                                                            // RELEASED
                                                            greaterUSDButton.setStyle(neo.drop_shadow);
                                                            textView.setScaleX(1);
                                                            textView.setScaleY(1);
                                                            Thread.sleep(60);
                                                            alertTypeText.setText("價格超過");
                                                            alertMetric.setText("USD");
                                                            alertType = 1;
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
                                }catch (Exception e){
                                    e.printStackTrace();
                                }



                                lesserUSDButton.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        ViewGroup viewGroupC1 = v.findViewById(R.id.lesserUSDButton);
                                        final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                                        try{
                                            if (lesserUSDButton.isShapeContainsPoint(event.getX(), event.getY())) {
                                                switch (event.getAction()) {
                                                    case MotionEvent.ACTION_DOWN:

                                                        lesserUSDButton.setStyle(neo.small_inner_shadow);
                                                        textView.setScaleX(textView.getScaleX() * 0.95f);
                                                        textView.setScaleY(textView.getScaleY() * 0.95f);
                                                        return true; // if you want to handle the touch event
                                                    case MotionEvent.ACTION_UP:
                                                    case MotionEvent.ACTION_CANCEL:
                                                        // RELEASED
                                                        lesserUSDButton.setStyle(neo.drop_shadow);
                                                        textView.setScaleX(1);
                                                        textView.setScaleY(1);
                                                        Thread.sleep(100);
                                                        alertTypeText.setText("價格低於");
                                                        alertMetric.setText("USD");
                                                        alertType = 2;
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

                                increasedAlertButton.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        ViewGroup viewGroupC1 = v.findViewById(R.id.increasedAlertButton);
                                        final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                                        try{
                                            if (increasedAlertButton.isShapeContainsPoint(event.getX(), event.getY())) {
                                                switch (event.getAction()) {
                                                    case MotionEvent.ACTION_DOWN:

                                                        increasedAlertButton.setStyle(neo.small_inner_shadow);
                                                        textView.setScaleX(textView.getScaleX() * 0.95f);
                                                        textView.setScaleY(textView.getScaleY() * 0.95f);
                                                        return true; // if you want to handle the touch event
                                                    case MotionEvent.ACTION_UP:
                                                    case MotionEvent.ACTION_CANCEL:
                                                        // RELEASED
                                                        increasedAlertButton.setStyle(neo.drop_shadow);
                                                        textView.setScaleX(1);
                                                        textView.setScaleY(1);
                                                        Thread.sleep(100);
                                                        alertTypeText.setText("漲幅達到");
                                                        alertMetric.setText("%");
                                                        alertType = 3;
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
                                priceDroppedButton.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        ViewGroup viewGroupC1 = v.findViewById(R.id.priceDroppedButton);
                                        final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                                        try{
                                            if (priceDroppedButton.isShapeContainsPoint(event.getX(), event.getY())) {
                                                switch (event.getAction()) {
                                                    case MotionEvent.ACTION_DOWN:

                                                        priceDroppedButton.setStyle(neo.small_inner_shadow);
                                                        textView.setScaleX(textView.getScaleX() * 0.95f);
                                                        textView.setScaleY(textView.getScaleY() * 0.95f);
                                                        return true; // if you want to handle the touch event
                                                    case MotionEvent.ACTION_UP:
                                                    case MotionEvent.ACTION_CANCEL:
                                                        // RELEASED
                                                        priceDroppedButton.setStyle(neo.drop_shadow);
                                                        textView.setScaleX(1);
                                                        textView.setScaleY(1);
                                                        Thread.sleep(100);
                                                        alertTypeText.setText("跌幅達到");
                                                        alertMetric.setText("%");
                                                        alertType = 4;
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

                                ViewGroup viewGroupC2 = view.findViewById(R.id.closeButton);
                                final ImageView textView1 = (ImageView) viewGroupC2.getChildAt(0);
                                roundButtonPressed(closeButton,textView1, AlertSetupActivity.this,dialog);

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


        frequencyButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.frequencyButton);
                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                try{
                    if (frequencyButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                frequencyButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                frequencyButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(60);
                                AlertDialog.Builder builder = new AlertDialog.Builder(AlertSetupActivity.this,R.style.CustomDialog);

                                View view = getLayoutInflater().inflate(R.layout.pick_alert_frequency,null);
                                neo closeButton = view.findViewById(R.id.closeButton);
                                neo onlyOnceButton = view.findViewById(R.id.onlyOnceButton);
                                neo oncePerDayButton = view.findViewById(R.id.oncePerDayButton);
                                neo alwaysButton = view.findViewById(R.id.alwaysButton);
                                builder.setCancelable(false);

                                builder.setView(view);
                                AlertDialog dialog = builder.show();

                                onlyOnceButton.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        ViewGroup viewGroupC1 = v.findViewById(R.id.onlyOnceButton);
                                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                                        try{
                                            if (onlyOnceButton.isShapeContainsPoint(event.getX(), event.getY())) {
                                                switch (event.getAction()) {
                                                    case MotionEvent.ACTION_DOWN:

                                                        onlyOnceButton.setStyle(neo.small_inner_shadow);
                                                        textView.setScaleX(textView.getScaleX() * 0.95f);
                                                        textView.setScaleY(textView.getScaleY() * 0.95f);
                                                        return true; // if you want to handle the touch event
                                                    case MotionEvent.ACTION_UP:
                                                    case MotionEvent.ACTION_CANCEL:
                                                        // RELEASED
                                                        onlyOnceButton.setStyle(neo.drop_shadow);
                                                        textView.setScaleX(1);
                                                        textView.setScaleY(1);
                                                        Thread.sleep(100);
                                                        frequencyText.setText("僅提醒一次");
                                                        frequency=1;
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

                                oncePerDayButton.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {

                                        ViewGroup viewGroupC1 = v.findViewById(R.id.oncePerDayButton);
                                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                                        try{
                                            if (oncePerDayButton.isShapeContainsPoint(event.getX(), event.getY())) {
                                                switch (event.getAction()) {
                                                    case MotionEvent.ACTION_DOWN:

                                                        oncePerDayButton.setStyle(neo.small_inner_shadow);
                                                        textView.setScaleX(textView.getScaleX() * 0.95f);
                                                        textView.setScaleY(textView.getScaleY() * 0.95f);
                                                        return true; // if you want to handle the touch event
                                                    case MotionEvent.ACTION_UP:
                                                    case MotionEvent.ACTION_CANCEL:
                                                        // RELEASED
                                                        oncePerDayButton.setStyle(neo.drop_shadow);
                                                        textView.setScaleX(1);
                                                        textView.setScaleY(1);
                                                        Thread.sleep(100);
                                                        frequencyText.setText("每日提醒一次");
                                                        frequency=2;
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
                                alwaysButton.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {

                                        ViewGroup viewGroupC1 = v.findViewById(R.id.alwaysButton);
                                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                                        try{
                                            if (alwaysButton.isShapeContainsPoint(event.getX(), event.getY())) {
                                                switch (event.getAction()) {
                                                    case MotionEvent.ACTION_DOWN:

                                                        alwaysButton.setStyle(neo.small_inner_shadow);
                                                        textView.setScaleX(textView.getScaleX() * 0.95f);
                                                        textView.setScaleY(textView.getScaleY() * 0.95f);
                                                        return true; // if you want to handle the touch event
                                                    case MotionEvent.ACTION_UP:
                                                    case MotionEvent.ACTION_CANCEL:
                                                        // RELEASED
                                                        alwaysButton.setStyle(neo.drop_shadow);
                                                        textView.setScaleX(1);
                                                        textView.setScaleY(1);
                                                        Thread.sleep(100);
                                                        frequencyText.setText("持續提醒");
                                                        frequency=3;
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

                                ViewGroup viewGroupC2 = view.findViewById(R.id.closeButton);
                                final ImageView textView1 = (ImageView) viewGroupC2.getChildAt(0);
                                roundButtonPressed(closeButton,textView1, AlertSetupActivity.this,dialog);

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

        tokenPickerButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.tokenPickerButton);
                final RelativeLayout textView = (RelativeLayout) viewGroupC1.getChildAt(0);
                try{
                    if (tokenPickerButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                tokenPickerButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.98f);
                                textView.setScaleY(textView.getScaleY() * 0.98f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                tokenPickerButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(60);
                                AlertDialog.Builder builder = new AlertDialog.Builder(AlertSetupActivity.this,R.style.CustomDialog);
                                View v1 = getLayoutInflater().inflate(R.layout.pick_token_swap,null);
                                LinearLayout tokenList = v1.findViewById(R.id.tokenList);
                                neo closeButton = v1.findViewById(R.id.closeButton);
                                builder.setCancelable(false);

                                builder.setView(v1);
                                AlertDialog dialog = builder.show();

                                ViewGroup viewGroupC2 = v1.findViewById(R.id.closeButton);
                                final ImageView textView1 = (ImageView) viewGroupC2.getChildAt(0);
                                roundButtonPressed(closeButton,textView1, AlertSetupActivity.this,dialog);

                                if(tokenList.getChildCount()>0){
                                    tokenList.removeAllViews();
                                }

                                for(int i =0;i<tokenArray.length();i++){
                                    try{
                                        JSONObject obj = new JSONObject(tokenArray.get(i).toString());
                                        View view1 = getLayoutInflater().inflate(R.layout.token_item_template,null);
                                        ImageView tokenImage = view1.findViewById(R.id.tokenImage);
                                        TextView tokenName = view1.findViewById(R.id.tokenName);
                                        ImageView tokenSelected = view1.findViewById(R.id.tokenSelected);

                                        Picasso.get().load(obj.getString(WHALETRUST_TOKEN_ICON)).into(tokenImage);
                                        tokenName.setText(obj.getString(WHALETRUST_TOKEN_SYMBOL).toUpperCase());
                                        int counter =i;
                                        tokenSelected.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog.cancel();
                                                try {
                                                    toke1=obj.getString(WHALETRUST_TOKEN_SYMBOL);
                                                    firstToken= obj.getString(WHALETRUST_TOKEN_ADDRESS);
                                                    toke1_Img = obj.getString(WHALETRUST_TOKEN_ICON);
                                                    setToken1();

                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }


                                            }
                                        });

                                        tokenList.addView(view1);

                                    }catch (Exception e){
                                        e.printStackTrace();
                                    }
                                }
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

    public void generateTokenList(){
        tokenArray = new JSONArray();
        try{
            tokenArray.put(returnTokenObj("BNB","https://assets.coingecko.com/coins/images/825/large/binance-coin-logo.png?1547034615",BNB_ADDRESS));
            tokenArray.put(returnTokenObj("BTCB","https://assets.coingecko.com/coins/images/14108/large/Binance-bitcoin.png?1617332330",BTCB_ADDRESS));
            tokenArray.put(returnTokenObj("ETH","https://assets.coingecko.com/coins/images/279/large/ethereum.png?1595348880",ETH_ADDRESS));
            tokenArray.put(returnTokenObj("BUSD","https://assets.coingecko.com/coins/images/9576/large/BUSD.png?1568947766",BUSD_ADDRESS));
            tokenArray.put(returnTokenObj("USDT","https://assets.coingecko.com/coins/images/325/large/Tether-logo.png?1598003707",USDT_ADDRESS));
            tokenArray.put(returnTokenObj("USDC","https://assets.coingecko.com/coins/images/6319/large/USD_Coin_icon.png?1547042389",USDC_ADDRESS));
            JSONArray array = new JSONArray();
            if(!Preference.getInstance().returnValue(WHALETRUST_TOKEN_LIST).isEmpty()){
                array =  new JSONArray(Preference.getInstance().returnValue(WHALETRUST_TOKEN_LIST));
            }

            for(int i=0;i<array.length();i++){
                JSONObject obj = new JSONObject(array.get(i).toString());
                if(!obj.getString(WHALETRUST_TOKEN_ADDRESS).equalsIgnoreCase(BNB_ADDRESS)&&!obj.getString(WHALETRUST_TOKEN_ADDRESS).equalsIgnoreCase(ETH_ADDRESS)
                        &&!obj.getString(WHALETRUST_TOKEN_ADDRESS).equalsIgnoreCase(BTCB_ADDRESS)&&!obj.getString(WHALETRUST_TOKEN_ADDRESS).equalsIgnoreCase(BUSD_ADDRESS)
                        &&!obj.getString(WHALETRUST_TOKEN_ADDRESS).equalsIgnoreCase(USDT_ADDRESS)&&!obj.getString(WHALETRUST_TOKEN_ADDRESS).equalsIgnoreCase(USDC_ADDRESS)){
                    if(obj.getString(WHALETRUST_TOKEN_ACTIVE).equalsIgnoreCase("true")){
                        tokenArray.put(returnTokenObj(obj.getString(WHALETRUST_TOKEN_SYMBOL),obj.getString(WHALETRUST_TOKEN_ICON),obj.getString(WHALETRUST_TOKEN_ADDRESS)));
                    }
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public JSONObject returnTokenObj(String symbol, String icon, String address){
        JSONObject obj = new JSONObject();
        try{
            obj.put(WHALETRUST_TOKEN_SYMBOL,symbol);
            obj.put(WHALETRUST_TOKEN_ICON,icon);
            obj.put(WHALETRUST_TOKEN_ADDRESS,address);
        }catch (Exception e){
            e.printStackTrace();
        }
        return obj;

    }

    public void setToken1(){
        token1PickLayout.setText("");
        token1Layout.setVisibility(View.VISIBLE);
//        token1Text.setVisibility(View.VISIBLE);
        if(toke1.isEmpty()){
            token1PickLayout.setText("選擇幣種");
            token1Layout.setVisibility(View.GONE);

        }else {
            token1Text.setText(toke1);
            Picasso.get().load(toke1_Img).into(token1Img);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    currentPrice = FunctionCall.getSwapAmount(firstToken,BUSD_ADDRESS,String.valueOf(1),String.valueOf(0.0001));

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            usdAmountText.setText(String.format("%.2f", currentPrice)+" USD");
                        }
                    });
                }
            }).start();

        }

    }
}
