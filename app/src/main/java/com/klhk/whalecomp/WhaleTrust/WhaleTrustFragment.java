package com.klhk.whalecomp.WhaleTrust;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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
import com.github.sshadkany.neo;
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.SettingFragment.AccountQr;
import com.klhk.whalecomp.SettingFragment.PickAddressActivity;
import com.klhk.whalecomp.utilities.AppController;
import com.klhk.whalecomp.utilities.FunctionCall;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import java8.util.concurrent.CompletableFuture;

import static android.app.Activity.RESULT_OK;
import static com.klhk.whalecomp.utilities.Constants.BNB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.BUSD_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.ETH_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_METHOD;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_PATTERN;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_PASSWORD;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ACTIVE;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_DIGIT;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ICON;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_NAME;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_SYMBOL;
import static com.klhk.whalecomp.utilities.Constants.getBalance;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WhaleTrustFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WhaleTrustFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WhaleTrustFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WhaleTrustFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WhaleTrustFragment newInstance(String param1, String param2) {
        WhaleTrustFragment fragment = new WhaleTrustFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    ImageView walletButton,nftButton,bnbTokenDetails,showQRButton,scanQRButton;
    TextView walletButtonText,nftButtonText,bnbBalance,ethBalance;
    RelativeLayout addTokenButton;
    LinearLayout newTokenLayout;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1001&&resultCode==RESULT_OK){
            Log.e("Reached","activityResult1");
            getFragmentManager()
                    .beginTransaction()
                    .detach(this)
                    .attach(this)
                    .commit();
            ((MainActivity)getActivity()).disconnectFromExtAPP(false);
        }

        if(requestCode==1001&&resultCode!=RESULT_OK){
            Log.e("Reached","activityResult2");
            ((MainActivity)getActivity()).clickHomeScreen();
        }

        if(requestCode==4000&&resultCode==RESULT_OK){
            Toast.makeText(getContext(), data.getStringExtra("address"), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_LIST).isEmpty()){

            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialog);

            View v = getLayoutInflater().inflate(R.layout.pick_add_address_whaletrust_new,null);
            ImageView closeButton = v.findViewById(R.id.closeButton);
            neo newButton = v.findViewById(R.id.newButton);
            neo importButton = v.findViewById(R.id.importButton);
            builder.setCancelable(false);

            builder.setView(v);
            AlertDialog dialog = builder.show();

            newButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try{
                        ViewGroup viewGroupC1 = v.findViewById(R.id.newButton);
                        final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                        if (newButton.isShapeContainsPoint(event.getX(), event.getY())) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    newButton.setStyle(neo.small_inner_shadow);
                                    textView.setScaleX(textView.getScaleX() * 0.96f);
                                    textView.setScaleY(textView.getScaleY() * 0.96f);
                                    return true; // if you want to handle the touch event
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    // RELEASED
                                    newButton.setStyle(neo.drop_shadow);
                                    textView.setScaleX(1);
                                    textView.setScaleY(1);
                                    Thread.sleep(60);
                                    dialog.cancel();
                                    startActivityForResult(new Intent((MainActivity)getActivity(), CreateMnemonicActivity.class),1001);
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

            importButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try{
                        ViewGroup viewGroupC1 = v.findViewById(R.id.importButton);
                        final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                        if (importButton.isShapeContainsPoint(event.getX(), event.getY())) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    importButton.setStyle(neo.small_inner_shadow);
                                    textView.setScaleX(textView.getScaleX() * 0.96f);
                                    textView.setScaleY(textView.getScaleY() * 0.96f);
                                    return true; // if you want to handle the touch event
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    // RELEASED
                                    importButton.setStyle(neo.drop_shadow);
                                    textView.setScaleX(1);
                                    textView.setScaleY(1);
                                    Thread.sleep(60);
                                    dialog.cancel();
                                    startActivityForResult(new Intent((MainActivity)getActivity(), ImportAddressActivity.class),1001);
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



            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    ((MainActivity)getActivity()).clickHomeScreen();
                }
            });
            return null;
        }else if(!Preference.getInstance().returnValue(WALLET_SELECTED).equalsIgnoreCase(WHALETRUST)){
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialog);

            View v = getLayoutInflater().inflate(R.layout.whale_trust_dialog,null);
            ImageView closeButton = v.findViewById(R.id.closeButton);
            neo whaleTrustButton = v.findViewById(R.id.whaleTrustButton);

            builder.setCancelable(false);

            builder.setView(v);
            AlertDialog dialog = builder.show();

            whaleTrustButton.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    try{
                        ViewGroup viewGroupC1 = v.findViewById(R.id.whaleTrustButton);
                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                        if (whaleTrustButton.isShapeContainsPoint(event.getX(), event.getY())) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:

                                    whaleTrustButton.setStyle(neo.small_inner_shadow);
                                    textView.setScaleX(textView.getScaleX() * 0.96f);
                                    textView.setScaleY(textView.getScaleY() * 0.96f);
                                    return true; // if you want to handle the touch event
                                case MotionEvent.ACTION_UP:
                                case MotionEvent.ACTION_CANCEL:
                                    // RELEASED
                                    whaleTrustButton.setStyle(neo.drop_shadow);
                                    textView.setScaleX(1);
                                    textView.setScaleY(1);
                                    Thread.sleep(60);
                                    ((MainActivity)getActivity()).disconnectFromExtAPP(false);
                                    Preference.getInstance().writePreference(WALLET_SELECTED,WHALETRUST);
                                    Preference.getInstance().writePreference(WALLET_SELECTED_ADDRESS,Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED));
                                    ((MainActivity)getActivity()).refreshWithFragmnet();
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



            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.cancel();
                    ((MainActivity)getActivity()).clickHomeScreen();

                }
            });
            return null;
        }
        else{
            try{
                JSONArray array = new JSONArray(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_LIST));
                for(int i=0;i<array.length();i++){
                    JSONObject obj = new JSONObject(array.get(i).toString());
                    if(obj.get(WHALETRUST_ADDRESS).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))){
                        Preference.getInstance().writePreference(WALLET_SELECTED_ADDRESS,obj.getString(WHALETRUST_ADDRESS));
                        Preference.getInstance().writePreference(WHALETRUST_ADDRESS,obj.getString(WHALETRUST_ADDRESS));
                        Preference.getInstance().writePreference(WHALETRUST_ADDRESS_PATTERN,obj.getString(WHALETRUST_ADDRESS_PATTERN));
                        Preference.getInstance().writePreference(WHALETRUST_ADDRESS_METHOD,obj.getString(WHALETRUST_ADDRESS_METHOD));
                        Preference.getInstance().writePreference(WHALETRUST_PASSWORD,obj.getString(WHALETRUST_PASSWORD));
                        if(obj.has(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME)){
                            Preference.getInstance().writePreference(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME,obj.getString(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME));
                        }else{
                            Preference.getInstance().writePreference(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME,"");
                        }
                    }
                }
            }catch(Exception e){
                e.printStackTrace();
            }


            return setupView();
        }


    }

    public void InitialTokens(String tokenName,String tokenAddr,String tokenSymbol,String imagePath){
        JSONObject object = new JSONObject();
        try{

                object.put(WHALETRUST_TOKEN_NAME,tokenName);
                object.put(WHALETRUST_TOKEN_ADDRESS,tokenAddr);
                object.put(WHALETRUST_TOKEN_DIGIT,"18");
                object.put(WHALETRUST_TOKEN_SYMBOL,tokenSymbol);
                object.put(WHALETRUST_TOKEN_ACTIVE,"true");
                object.put(WHALETRUST_TOKEN_ICON,imagePath);

                Log.e("reahced",object.toString());
                JSONArray array = new JSONArray();
                if(!Preference.getInstance().returnValue(WHALETRUST_TOKEN_LIST).isEmpty()){
                    array =  new JSONArray(Preference.getInstance().returnValue(WHALETRUST_TOKEN_LIST));
                }

                array.put(object);
                Preference.getInstance().writePreference(WHALETRUST_TOKEN_LIST,array.toString());

                if(array.length()==1){
                    findToken(ETH_ADDRESS);
                }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void findToken(String tokenAddress){
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONObject tokenObj = new JSONObject(obj.getString(tokenAddress.toLowerCase()));

            if(tokenObj!=null){
                String tokenAddr = tokenAddress;
                String tokenName = tokenObj.getString("name").toUpperCase();
                String tokenSymbol = tokenObj.getString("symbol").toUpperCase();
                String imagePath= tokenObj.getString("image");
                boolean foundToken=true;

                InitialTokens(tokenName,tokenAddr,tokenSymbol,imagePath);
            }else{
                //Toast.makeText(NewTokenActivity.this, "Paste a valid address", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getActivity().getAssets().open("Tokens.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    JSONArray array;

    public View setupView(){
        View view= getLayoutInflater().inflate(R.layout.fragment_whale_trust, null, false);
        walletButton = view.findViewById(R.id.walletButton);
        nftButton = view.findViewById(R.id.nftButton);
        walletButtonText = view.findViewById(R.id.walletButtonText);
        nftButtonText = view.findViewById(R.id.nftButtonText);
        addTokenButton = view.findViewById(R.id.addTokenButton);
        bnbTokenDetails = view.findViewById(R.id.bnbTokenDetails);
        ethBalance = view.findViewById(R.id.ethBalance);
        bnbBalance = view.findViewById(R.id.bnbBalance);
        showQRButton = view.findViewById(R.id.showQRButton);
        scanQRButton = view.findViewById(R.id.scanQRButton);
        newTokenLayout = view.findViewById(R.id.newTokenLayout);
        //getBalance(BNB_ADDRESS,bnbBalance, "BNB");

        try{
           array = new JSONArray();
        if(!Preference.getInstance().returnValue(WHALETRUST_TOKEN_LIST).isEmpty()){
            array =  new JSONArray(Preference.getInstance().returnValue(WHALETRUST_TOKEN_LIST));
        }else{
            findToken(BNB_ADDRESS);
        }
        if(newTokenLayout.getChildCount()>0){
            newTokenLayout.removeAllViews();
        }
        for(int i=0;i<array.length();i++){
            JSONObject obj = new JSONObject(array.get(i).toString());
            if(obj.getString(WHALETRUST_TOKEN_ACTIVE).equalsIgnoreCase("true")){
                View view1 = getLayoutInflater().inflate(R.layout.new_token_template, null, false);
                neo tokenDetails = view1.findViewById(R.id.tokenDetails);
                TextView tokenBalance = view1.findViewById(R.id.tokenBalance);
                TextView tokenBalanceUSD = view1.findViewById(R.id.tokenBalanceUSD);
                ImageView tokenImage = view1.findViewById(R.id.tokenImage);
                Picasso.get().load(obj.getString(WHALETRUST_TOKEN_ICON)).into(tokenImage);
                tokenBalance.setText("0.0000 "+ obj.getString(WHALETRUST_TOKEN_SYMBOL).toUpperCase());


                int counter = i;
                JSONArray finalArray = array;
                tokenDetails.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        try{
                            ViewGroup viewGroupC1 = v.findViewById(R.id.tokenDetails);
                            final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                            if (tokenDetails.isShapeContainsPoint(event.getX(), event.getY())) {
                                switch (event.getAction()) {
                                    case MotionEvent.ACTION_DOWN:

                                        tokenDetails.setStyle(neo.small_inner_shadow);
                                        textView.setScaleX(textView.getScaleX() * 0.96f);
                                        textView.setScaleY(textView.getScaleY() * 0.96f);
                                        return true; // if you want to handle the touch event
                                    case MotionEvent.ACTION_UP:
                                    case MotionEvent.ACTION_CANCEL:
                                        // RELEASED
                                        tokenDetails.setStyle(neo.drop_shadow);
                                        textView.setScaleX(1);
                                        textView.setScaleY(1);
                                        Thread.sleep(80);
                                        try{
                                            JSONObject obj1 = new JSONObject(finalArray.get(counter).toString());
                                            Intent i = new Intent(getActivity(),TokenDetailActivity.class);
                                            i.putExtra("tokenName",obj1.getString(WHALETRUST_TOKEN_SYMBOL).toUpperCase());
                                            i.putExtra("tokenAddr",obj1.getString(WHALETRUST_TOKEN_ADDRESS));
                                            i.putExtra("tokenImage",obj1.getString(WHALETRUST_TOKEN_ICON));
                                            startActivity(i);
                                        }catch(Exception e){
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

                newTokenLayout.addView(view1);


            }


        }
        }catch (Exception e){
            e.printStackTrace();
        }


        showQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AccountQr.class));
            }
        });

        scanQRButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(),ScanActivity.class),4000);
            }
        });

//        bnbTokenDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getActivity(),TokenDetailActivity.class);
//                i.putExtra("tokenName","BNB");
//                i.putExtra("tokenAddr",BNB_ADDRESS);
//                startActivity(i);
//            }
//        });

        addTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),AddRemoveTokenActivity.class));
            }
        });

        walletButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nftButton.setImageDrawable(getResources().getDrawable(R.drawable.whaletrust_button_white));
                nftButtonText.setTextColor(getResources().getColor(R.color.black));
                walletButton.setImageDrawable(getResources().getDrawable(R.drawable.whaletrust_button_blue));
                walletButtonText.setTextColor(getResources().getColor(R.color.bg_color));
            }
        });

        nftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nftButton.setImageDrawable(getResources().getDrawable(R.drawable.whaletrust_button_blue));
                nftButtonText.setTextColor(getResources().getColor(R.color.bg_color));
                walletButton.setImageDrawable(getResources().getDrawable(R.drawable.whaletrust_button_white));
                walletButtonText.setTextColor(getResources().getColor(R.color.black));
            }
        });
        int finalI = 0;
        for(int i=0;i<array.length();i++){
            try{
                JSONObject obj = new JSONObject(array.get(i).toString());
                if(obj.getString(WHALETRUST_TOKEN_ACTIVE).equalsIgnoreCase("true")) {

                    int finalI1 = finalI;
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                View view1 = newTokenLayout.getChildAt(finalI1);
                                TextView tokenBalance = view1.findViewById(R.id.tokenBalance);
                                try {

                                    String amount = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), obj.getString(WHALETRUST_TOKEN_ADDRESS));

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                tokenBalance.setText(String.format("%.4f", Double.parseDouble(amount)) + " " + obj.getString(WHALETRUST_TOKEN_SYMBOL).toUpperCase());

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                    finalI++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }
        finalI = 0;
        for(int i=0;i<array.length();i++){
            try{
                JSONObject obj = new JSONObject(array.get(i).toString());
                if(obj.getString(WHALETRUST_TOKEN_ACTIVE).equalsIgnoreCase("true")) {

                    int finalI1 = finalI;
                    Thread thread = new Thread() {
                        @Override
                        public void run() {
                            try {
                                View view1 = newTokenLayout.getChildAt(finalI1);
                                TextView tokenBalanceUSD = view1.findViewById(R.id.tokenBalanceUSD);
                                try {

                                    String amount = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), obj.getString(WHALETRUST_TOKEN_ADDRESS));
                                    double amountOut = FunctionCall.getSwapAmount(obj.getString(WHALETRUST_TOKEN_ADDRESS),BUSD_ADDRESS,String.valueOf(1),String.valueOf(0.0001));

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try {
                                                double amountUSD1 = Double.parseDouble(amount) * amountOut;
                                                if(obj.getString(WHALETRUST_TOKEN_ADDRESS).equalsIgnoreCase(BUSD_ADDRESS)){
                                                    tokenBalanceUSD.setText("≈$"+String.format("%.2f", Double.parseDouble(amount)));
                                                }else{
                                                    tokenBalanceUSD.setText("≈$"+String.format("%.2f", Double.parseDouble(String.valueOf(amountUSD1))));
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                        }
                                    });

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    thread.start();
                    finalI++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return view;




    }



}