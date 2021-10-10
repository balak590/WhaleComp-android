package com.klhk.whalecomp.SwapFragmnet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.sshadkany.neo;
import com.klhk.whalecomp.CutomKeypad.CustomKeypadActivity;
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WalletFragment.StratergyScreens.CompoundActivity;
import com.klhk.whalecomp.WhaleTrust.SwapWhaleTrustActivity;
import com.klhk.whalecomp.utilities.AppController;
import com.klhk.whalecomp.utilities.FunctionCall;
import com.squareup.picasso.Picasso;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;

import wallet.core.jni.HDWallet;
import wallet.core.jni.PrivateKey;

import static com.klhk.whalecomp.utilities.Constants.BNB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.BTCB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.BUSD_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.ETH_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.METAMASK;
import static com.klhk.whalecomp.utilities.Constants.TRUSTWALLET;
import static com.klhk.whalecomp.utilities.Constants.USDC_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.USDT_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WBNB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALECOMP_SLIPPAGE;
import static com.klhk.whalecomp.utilities.Constants.WHALECOMP_TIMEOUT;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_LOCK_FINGERPRINT;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ACTIVE;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ICON;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_SYMBOL;
import static com.klhk.whalecomp.utilities.Constants.getBalance;
import static com.klhk.whalecomp.utilities.Constants.hideSoftKeyboard;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;
import static com.klhk.whalecomp.utilities.Constants.setupUI;
import static com.klhk.whalecomp.utilities.FunctionCall.pancakeRouterv2;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class    SwapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SwapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SwapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SwapFragment newInstance(String param1, String param2) {
        SwapFragment fragment = new SwapFragment();
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

    TextView token2PickLayout,token1PickLayout,token1Text,token2Text,exchangeRate,minimumReceived,secondToFirstText,firstToSecondText,balanceToken1,balanceToken2;
    ImageView token1Img,token2Img,settingButton,swapButton,switchToken,arrowSymbol;
    LinearLayout token1Layout,token2Layout;
    String toke1,toke2,firstToken,secondToken,toke1_Img, toke2_Img;
    EditText token1Amount;
    TextView token2Amount;
    JSONArray tokenArray = new JSONArray();

    RelativeLayout swapSummery,swapSummery2;
    double slippage,slippageNew;
    int timeOut;
    int angle =180;

    public static Handler handler = new Handler();
    Timer timer = new Timer();
    final long DELAY = 1500;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_swap, container, false);
        setupUI(view,getActivity());
        slippage =1;
        timeOut = 20;
        token2PickLayout = view.findViewById(R.id.token2PickLayout);
        token1PickLayout = view.findViewById(R.id.token1PickLayout);
        token1Text = view.findViewById(R.id.token1Text);
        token2Text = view.findViewById(R.id.token2Text);
        token1Img = view.findViewById(R.id.token1Img);
        token2Img = view.findViewById(R.id.token2Img);
        token1Layout = view.findViewById(R.id.token1Layout);
        token2Layout = view.findViewById(R.id.token2Layout);
        toke1="";
        toke2="";
        firstToken="";
        secondToken="";
        toke1_Img="";
        toke2_Img="";
        swapSummery = view.findViewById(R.id.swapSummery);
        swapSummery2 = view.findViewById(R.id.swapSummery2);
        token1Amount= view.findViewById(R.id.token1Amount);
        token2Amount= view.findViewById(R.id.token2Amount);
        settingButton = view.findViewById(R.id.settingButton);
        swapButton = view.findViewById(R.id.swapButton);

        firstToSecondText = view.findViewById(R.id.firstToSecondText);
        secondToFirstText = view.findViewById(R.id.secondToFirstText);
        minimumReceived = view.findViewById(R.id.minimumReceived);
        exchangeRate = view.findViewById(R.id.exchangeRate);

        arrowSymbol = view.findViewById(R.id.arrowSymbol);
        switchToken = view.findViewById(R.id.switchToken);

        balanceToken2 = view.findViewById(R.id.balanceToken2);
        balanceToken1 = view.findViewById(R.id.balanceToken1);
        if(!Preference.getInstance().returnValue(WHALECOMP_SLIPPAGE).isEmpty()){
            slippage = Double.parseDouble(Preference.getInstance().returnValue(WHALECOMP_SLIPPAGE));
        }else{
            slippage = 1;
        }

        slippageNew = slippage;

        swapButton.setImageDrawable(getResources().getDrawable(R.drawable.grey_square_button));

        settingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialog);

                View v1 = getLayoutInflater().inflate(R.layout.slippage_setting,null);
                ImageView slippage_01 = v1.findViewById(R.id.slippage_01);
                ImageView slippage_05 = v1.findViewById(R.id.slippage_05);
                ImageView slippage_1 = v1.findViewById(R.id.slippage_1);
                EditText customSlippage = v1.findViewById(R.id.customSlippage);
                EditText timeoutDuration = v1.findViewById(R.id.timeoutDuration);
                ImageView saveButton = v1.findViewById(R.id.saveButton);

                ImageView closeButton = v1.findViewById(R.id.closeButton);
                builder.setCancelable(false);

                builder.setView(v1);
                AlertDialog dialog = builder.show();
                if(!Preference.getInstance().returnValue(WHALECOMP_TIMEOUT).isEmpty()){
                    timeoutDuration.setText(Preference.getInstance().returnValue(WHALECOMP_TIMEOUT));
                }

                if(!Preference.getInstance().returnValue(WHALECOMP_SLIPPAGE).isEmpty()){
                    if(Preference.getInstance().returnValue(WHALECOMP_SLIPPAGE).equalsIgnoreCase("0.1")){
                        slippage_01.setImageDrawable(getResources().getDrawable(R.drawable.slippage_pressed));
                        slippage_05.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        slippage_1.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));

                    }else if(Preference.getInstance().returnValue(WHALECOMP_SLIPPAGE).equalsIgnoreCase("0.5")){
                        slippage_01.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        slippage_05.setImageDrawable(getResources().getDrawable(R.drawable.slippage_pressed));
                        slippage_1.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                    }else if(Preference.getInstance().returnValue(WHALECOMP_SLIPPAGE).equalsIgnoreCase("1")){
                        slippage_01.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        slippage_05.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        slippage_1.setImageDrawable(getResources().getDrawable(R.drawable.slippage_pressed));
                    }else{
                        slippage_01.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        slippage_05.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        slippage_1.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        customSlippage.setText(Preference.getInstance().returnValue(WHALECOMP_SLIPPAGE));


                    }
                }

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!timeoutDuration.getText().toString().isEmpty()&& Integer.parseInt(timeoutDuration.getText().toString())!=0){
                            timeOut = Integer.parseInt(timeoutDuration.getText().toString());

                        }
                        if(slippage!=slippageNew){
                            slippage = slippageNew;
                            Preference.getInstance().writePreference(WHALECOMP_SLIPPAGE,String.valueOf(slippage));
                        }
                        if(!firstToken.isEmpty()&&!secondToken.isEmpty()&&!token1Amount.getText().toString().isEmpty()){
                            swapSummery.setVisibility(View.INVISIBLE);
                            //callQuotesApiFor1Token(firstToken,secondToken,String.valueOf(slippage/100));
                            callQuotesApi(firstToken,secondToken,String.valueOf(slippage/100),token1Amount.getText().toString());
                        }
                        dialog.cancel();
                    }
                });

                customSlippage.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if(!customSlippage.getText().toString().isEmpty()){
                            slippageNew = Double.parseDouble(customSlippage.getText().toString());
                            slippage_01.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                            slippage_05.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                            slippage_1.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));

                        }else{
                            slippageNew =1;
                            slippage_01.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                            slippage_05.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                            slippage_1.setImageDrawable(getResources().getDrawable(R.drawable.slippage_pressed));
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                slippage_01.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slippageNew =0.1;
                        slippage_01.setImageDrawable(getResources().getDrawable(R.drawable.slippage_pressed));
                        slippage_05.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        slippage_1.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        //Preference.getInstance().writePreference(WHALECOMP_SLIPPAGE,"0.1");
                    }
                });

                slippage_05.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slippageNew =0.5;
                        slippage_01.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        slippage_05.setImageDrawable(getResources().getDrawable(R.drawable.slippage_pressed));
                        slippage_1.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        //Preference.getInstance().writePreference(WHALECOMP_SLIPPAGE,"0.5");
                    }
                });
                slippage_1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        slippageNew =1;
                        slippage_01.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        slippage_05.setImageDrawable(getResources().getDrawable(R.drawable.slippage_unpressed));
                        slippage_1.setImageDrawable(getResources().getDrawable(R.drawable.slippage_pressed));
                        //Preference.getInstance().writePreference(WHALECOMP_SLIPPAGE,"1");
                    }
                });

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();

                    }
                });

            }
        });

        switchToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideSoftKeyboard(getActivity());
                swapSummery.setVisibility(View.GONE);
                arrowSymbol.setRotation(angle);
                angle+=180;
                if(!toke1.isEmpty()&&toke2.isEmpty()){
                    toke2 = toke1;
                    toke1="";
                    secondToken = firstToken;
                    firstToken = "";
                    toke2_Img = toke1_Img;
                    toke1_Img = "";
                    if(!token1Amount.getText().toString().isEmpty()){
                        token2Amount.setText(token1Amount.getText().toString());
                        token1Amount.setText("");
                    }
                    setToken1();
                    setToken2();


                }else if(toke1.isEmpty()&&!toke2.isEmpty()){
                    toke1 = toke2;
                    toke2="";
                    firstToken = secondToken;
                    secondToken = "";
                    toke1_Img = toke2_Img;
                    toke2_Img="";
                    if(!token2Amount.getText().toString().isEmpty()){
                        token1Amount.setText(token2Amount.getText().toString());
                        token2Amount.setText("");
                    }
                    setToken1();
                    setToken2();

                }else if(!toke1.isEmpty()&&!toke2.isEmpty()){

                    String tempToke = toke1;
                    toke1 = toke2;
                    toke2 = tempToke;
                    tempToke = firstToken;
                    firstToken = secondToken;
                    secondToken = tempToke;
                    tempToke = token1Amount.getText().toString();
                    token1Amount.setText(token2Amount.getText().toString());
                    token2Amount.setText(tempToke);
                    tempToke = toke1_Img;
                    toke1_Img = toke2_Img;
                    toke2_Img = tempToke;
                    setToken1();
                    setToken2();

                }


            }
        });



        token1Amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }

            @Override
            public void afterTextChanged(Editable s) {
                swapSummery.setVisibility(View.INVISIBLE);
                if(token1Amount.getText().toString().isEmpty()){
                    token2Amount.setText("");
                }
                else if(!toke2.isEmpty()){
                    timer.cancel();
                    timer = new Timer();
                    timer.schedule(
                            new TimerTask() {
                                @Override
                                public void run() {

                                    getActivity().runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            callQuotesApi(firstToken,secondToken,String.valueOf(slippage/100),token1Amount.getText().toString());
                                        }
                                    });

                                }
                            },
                            DELAY
                    );

                }
            }
        });
        generateTokenList();


        token1PickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapSummery.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialog);
                View v1 = getLayoutInflater().inflate(R.layout.pick_token_swap,null);
                LinearLayout tokenList = v1.findViewById(R.id.tokenList);
                neo closeButton = v1.findViewById(R.id.closeButton);
                builder.setCancelable(false);

                builder.setView(v1);
                AlertDialog dialog = builder.show();

                ViewGroup viewGroupC1 = v.findViewById(R.id.closeButton);
                final ImageView textView = (ImageView) viewGroupC1.getChildAt(0);
                roundButtonPressed(closeButton,textView,getActivity(),dialog);

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

                                try {
                                    token2Amount.setText("");
                                    toke1=obj.getString(WHALETRUST_TOKEN_SYMBOL);
                                    firstToken= obj.getString(WHALETRUST_TOKEN_ADDRESS);
                                    toke1_Img = obj.getString(WHALETRUST_TOKEN_ICON);
                                    checkSwapButton();
                                    setToken1();
                                    dialog.cancel();
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

            }
        });
        token2PickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                swapSummery.setVisibility(View.GONE);

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialog);
                View v1 = getLayoutInflater().inflate(R.layout.pick_token_swap,null);
                LinearLayout tokenList = v1.findViewById(R.id.tokenList);
                neo closeButton = v1.findViewById(R.id.closeButton);
                builder.setCancelable(false);

                builder.setView(v1);
                AlertDialog dialog = builder.show();

                ViewGroup viewGroupC1 = v.findViewById(R.id.closeButton);
                final ImageView textView = (ImageView) viewGroupC1.getChildAt(0);
                roundButtonPressed(closeButton,textView,getActivity(),dialog);

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

                                try {
                                    token2Amount.setText("");
                                    toke2=obj.getString(WHALETRUST_TOKEN_SYMBOL);
                                    secondToken = obj.getString(WHALETRUST_TOKEN_ADDRESS);
                                    toke2_Img = obj.getString(WHALETRUST_TOKEN_ICON);
                                    checkSwapButton();
                                    setToken2();
                                    dialog.cancel();

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

            }
        });
        return view;
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

    public void checkSwapButton(){
        if(toke1.equalsIgnoreCase(toke2) ||toke1.isEmpty()||toke2.isEmpty()){
            removeSwapButton();
        }
        else{
            setSwapButton();
        }
    }
    public void setSwapButton(){
        swapButton.setImageDrawable(getResources().getDrawable(R.drawable.blue_square_button));
        swapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Preference.getInstance().returnValue(WALLET_SELECTED).equalsIgnoreCase(WHALETRUST)){
                    promptPassword(getContext());
                }else{
                    callSwapApi(firstToken,secondToken,String.valueOf(slippage/100),token1Amount.getText().toString());
                }



                //callSwapApi(firstToken,secondToken,String.valueOf(slippage),token1Amount.getText().toString());
            }
        });
    }

    public void removeSwapButton(){
        swapButton.setImageDrawable(getResources().getDrawable(R.drawable.grey_square_button));
        swapButton.setOnClickListener(null);
    }



//    public void callQuotesApiFor1Token(String fromAddress, String toAddress, String slippage){
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                double firstToSecond = FunctionCall.getSwapAmount(fromAddress,toAddress,String.valueOf(1),String.valueOf(0.00001));
//                double secondToFirst = FunctionCall.getSwapAmount(toAddress,fromAddress,String.valueOf(1),String.valueOf(0.00001));
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        firstToSecondText.setText("1 "+toke1.toUpperCase()+ " ≈ "+String.format("%.4f", firstToSecond)+" "+toke2.toUpperCase());
//                        secondToFirstText.setText("1 "+toke2.toUpperCase()+ " ≈ "+String.format("%.6f", secondToFirst)+" "+toke1.toUpperCase());
//                        exchangeRate.setText("< "+String.valueOf(Double.parseDouble(slippage)*100)+"%");
//                        if(!token1Amount.getText().toString().isEmpty()){
//                            swapSummery.setVisibility(View.VISIBLE);
//                        }else{
//                            swapSummery.setVisibility(View.INVISIBLE);
//                        }
//                    }
//                });
//            }
//        }).start();
////        String tag_json_obj = "json_obj_req";
////
////        String url = "http://api.whalecomp.com/getPairQuote";
////        Map<String, String> params = new HashMap<String, String>();
////        params.put("fromAddress", fromAddress);
////        params.put("toAddress",toAddress);
////        params.put("amount","1");
////        params.put("slippage",slippage);
////
////        Log.e("params",params.toString());
////        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
////                url, new JSONObject(params),
////                new Response.Listener<JSONObject>() {
////
////                    @Override
////                    public void onResponse(JSONObject response) {
////                        Log.e("TAG", response.toString());
////                        try {
////                            if(response.getString("code").equalsIgnoreCase("0")){
////                                JSONArray array = new JSONArray(response.getString("data"));
////                                if(array.get(0)!=null){
////                                    double firstToSecond = (double) array.get(0);
////                                    double secondToFirst = 1/ firstToSecond;
////                                    firstToSecondText.setText("1 "+toke1.toUpperCase()+ " ≈ "+String.format("%.4f", firstToSecond)+" "+toke2.toUpperCase());
////                                    secondToFirstText.setText("1 "+toke2.toUpperCase()+ " ≈ "+String.format("%.6f", secondToFirst)+" "+toke1.toUpperCase());
//////                                    minimumReceived.setText(String.format("%.2f", firstToSecond) + " "+ toke2.toUpperCase());
////                                    exchangeRate.setText("< "+String.valueOf(Double.parseDouble(slippage)*100)+"%");
////                                    if(!token1Amount.getText().toString().isEmpty()){
////                                        swapSummery.setVisibility(View.VISIBLE);
////                                    }else{
////                                        swapSummery.setVisibility(View.INVISIBLE);
////                                    }
////
////                                }else{
////                                    Toast.makeText(getContext(), "Unable to fetch details for this pair", Toast.LENGTH_SHORT).show();
////                                }
////
////                            }
////                        } catch (Exception e) {
////                            e.printStackTrace();
////                        }
////                    }
////                }, new Response.ErrorListener() {
////
////            @Override
////            public void onErrorResponse(VolleyError error) {
////                VolleyLog.d("Error:", error.getMessage());
////            }
////        }) {
////
////
////            @Override
////            public Map<String, String> getHeaders() throws AuthFailureError {
////                Map<String, String> params = new HashMap<String, String>();
////                params.put("Content-Type", "application/json");
////                Log.d("getHeaders", params.toString());
////                return params;
////            }
////
////        };
////        jsonObjReq.setShouldCache(false);
////        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//    }

    double value;
//    public void getBalance(String tokenAddress,TextView balaceTextView){
//        String tag_json_obj = "json_obj_req";
//        value = 0.0;
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
//                                balaceTextView.setText("餘額: "+String.format("%.4f", Double.parseDouble(amount)));
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
//
//                Log.d("getHeaders", params.toString());
//                return params;
//            }
//
//        };
//        jsonObjReq.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
//
//    }

    public void callQuotesApi(String fromAddress, String toAddress, String slippage , String amount){
        new Thread(new Runnable() {
            @Override
            public void run() {
                double amountOut = FunctionCall.getSwapAmount(fromAddress,toAddress,amount,slippage);
                double firstToSecond = FunctionCall.getSwapAmount(fromAddress,toAddress,String.valueOf(1),String.valueOf(0.0001));
                double secondToFirst = FunctionCall.getSwapAmount(toAddress,fromAddress,String.valueOf(1),String.valueOf(0.0001));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        firstToSecondText.setText("1 "+toke1.toUpperCase()+ " ≈ "+String.format("%.4f", firstToSecond)+" "+toke2.toUpperCase());
                        secondToFirstText.setText("1 "+toke2.toUpperCase()+ " ≈ "+String.format("%.6f", secondToFirst)+" "+toke1.toUpperCase());
                        exchangeRate.setText("< "+String.valueOf(Double.parseDouble(slippage)*100)+"%");
                        if(!token1Amount.getText().toString().isEmpty()){
                            swapSummery.setVisibility(View.VISIBLE);
                        }else{
                            swapSummery.setVisibility(View.INVISIBLE);
                        }
                        if(amountOut!=0){
                            token2Amount.setText(String.format("%.6f", amountOut));
                            minimumReceived.setText(String.format("%.4f", amountOut) + " "+ toke2.toUpperCase());
                        }else{
                            token2Amount.setText("0");
                            minimumReceived.setText("0.0000" + " "+ toke2.toUpperCase());
                            Toast.makeText(getContext(), "Unable to fetch details for this pair", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        }).start();

    }

    public void callSwapApi(String fromAddress, String toAddress, String slippage , String amount){

        if(Preference.getInstance().returnValue(WALLET_SELECTED).equalsIgnoreCase(WHALETRUST)){
            if(!fromAddress.equalsIgnoreCase(BNB_ADDRESS)){
                boolean allowanceHex = FunctionCall.getAllowance(fromAddress,pancakeRouterv2);
//            BigInteger allowance = Hex.hexToBigInteger(allowanceHex);
//            BigInteger amountBig = Convert.toWei(String.valueOf(amount), Convert.Unit.ETHER).toBigInteger();

                if(!allowanceHex){

                    Log.e("Allowence","not available");
                    String approve =FunctionCall.approveTokens(fromAddress,pancakeRouterv2);
                    Handler ha=new Handler();
                    Runnable myRunnable;
                    myRunnable =new Runnable() {

                        @Override
                        public void run() {

                            if(!FunctionCall.checkTransactionByHash(approve)){
                                Log.e("running","checkTransactionByHash(approve)");
                                ha.postDelayed(this, 500);
                            }else{
                                Log.e("running","swapTokens1");
                                String result = FunctionCall.swapTokens(fromAddress,toAddress,amount,slippage);
                                checkTxHash(result);
                            }

                        }
                    };

                    ha.postDelayed(myRunnable, 500);
                }else {
                    Log.e("running","swapTokens2");
                    String result = FunctionCall.swapTokens(fromAddress,toAddress,amount,slippage);
                    checkTxHash(result);
                }

            }else{
                Log.e("running","swapTokens3");
                String result = FunctionCall.swapTokens(fromAddress,toAddress,amount,slippage);
                checkTxHash(result);
                //Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }
        }else if(Preference.getInstance().returnValue(WALLET_SELECTED).equalsIgnoreCase(METAMASK)|| Preference.getInstance().returnValue(WALLET_SELECTED).equalsIgnoreCase(TRUSTWALLET)){
            String tag_json_obj = "json_obj_req";

            String url = "http://api.whalecomp.com/performSwap";
            Map<String, String> params = new HashMap<String, String>();
            params.put("address", Preference.getInstance().returnValue("userWalletAddress"));
            params.put("fromAddress", fromAddress);
            params.put("toAddress",toAddress);
            params.put("amount",amount);
            params.put("slippage",slippage);

            Log.e("params",params.toString());



            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    url, new JSONObject(params),
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            Log.e("TAG", response.toString());
                            try {
                                if(response.getString("code").equalsIgnoreCase("0")){
                                    JSONObject data = new JSONObject(response.getString("data"));

                                    String result = data.getString("from")+"/"+data.getString("to")+"/"+
                                            data.getString("data")+"/"+data.getString("gasLimit")+"/"+
                                            data.getString("value")+"/"+data.getString("nonce")+"/"+
                                            data.getString("gasPrice");

                                    String url = "https://whalecomp.com/signTransaction/"+result;

                                    Log.e("URL",url);

                                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialog);

                                    View v = getLayoutInflater().inflate(R.layout.webview_layout,null);
                                    WebView webView = v.findViewById(R.id.webViewLayout);
                                    ImageView closeButton = v.findViewById(R.id.closeButton);
                                    builder.setCancelable(false);

                                    builder.setView(v);
                                    AlertDialog dialog = builder.show();

                                    closeButton.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            dialog.cancel();
                                        }
                                    });
                                    webView.clearCache(true);

                                    webView.getSettings().setJavaScriptEnabled(true);
                                    webView.getSettings().setLoadsImagesAutomatically(true);
                                    webView.getSettings().setDomStorageEnabled(true);
                                    //webView.addJavascriptInterface(new MainActivity.MyJavaScriptInterface(), "HTMLOUT");
                                    webView.loadUrl(url);


                                    webView.setWebViewClient(new WebViewClient(){

                                        @Override
                                        public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                            Intent intent;
                                            Log.e("URL",url);

                                            if (url.contains("wc:")) {
                                                intent = new Intent(Intent.ACTION_VIEW);
                                                intent.setData(Uri.parse(url));
                                                startActivity(intent);

                                                return true;
                                            }
                                            return super.shouldOverrideUrlLoading(view, url);
                                        }

                                        @Override
                                        public void onPageFinished(WebView view, String url) {
                                            super.onPageFinished(view, url);


                                        }

                                        @Override
                                        public void onFormResubmission(WebView view, Message dontResend, Message resend) {
                                            super.onFormResubmission(view, dontResend, resend);
                                        }
                                    });


                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    VolleyLog.d("Error:", error.getMessage());
                }
            }) {


                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/json");

                    Log.d("getHeaders", params.toString());
                    return params;
                }

            };
            jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                    20000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            jsonObjReq.setShouldCache(false);
            AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        }else{
            Toast.makeText(getContext(), "Please pick a wallet", Toast.LENGTH_SHORT).show();
        }

    }


    public void checkTxHash(String txHash){
        try{
            Handler ha=new Handler();
        Runnable myRunnable;
        myRunnable =new Runnable() {

            @Override
            public void run() {

                if(!FunctionCall.checkTransactionByHash(txHash)){
                    Log.e("running","checkTransactionByHash(approve)");
                    ha.postDelayed(this, 500);
                }else{
                    triggerNotification("兌換已完成",false);
                }

            }
        };

        ha.postDelayed(myRunnable, 500);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void triggerNotification(String notificationText, boolean isError){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialog);

        View v = getLayoutInflater().inflate(R.layout.swap_notification,null);
        ImageView confirmButton = v.findViewById(R.id.confirmButton);

        TextView alertText = v.findViewById(R.id.alertText);
        alertText.setText(notificationText);
        if(isError){
            alertText.setTextColor(getResources().getColor(R.color.red));
        }
        builder.setCancelable(false);

        builder.setView(v);
        AlertDialog dialog = builder.show();

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.cancel();
                if(!isError){
                    swapSummery.setVisibility(View.GONE);
                    token1Amount.setText("");
                    token2Amount.setText("");
                    setToken1();
                    setToken2();
                }else{
                    //createNumberPad();
                }
            }
        });
    }



//    public void callOneInch(String amount){
//
//        if(!firstToken.isEmpty() && !secondToken.isEmpty() && !amount.isEmpty()){
//            Log.e("Reached", String.valueOf(BigInteger.valueOf((long) (Float.parseFloat(amount)*Math.pow(10, 18)))));
//            String amount1="";
//            if(toke1.equalsIgnoreCase("bnb")){
//
//                amount1 = String.valueOf(BigInteger.valueOf((long) (Float.parseFloat(amount)*Math.pow(10, 18))));
//            }else if(toke1.equalsIgnoreCase("btc")){
//                amount1 = String.valueOf(BigInteger.valueOf((long) (Float.parseFloat(amount)*Math.pow(10, 8))));
//
//            }else if(toke1.equalsIgnoreCase("usdc")){
//                amount1 = String.valueOf(BigInteger.valueOf((long) (Float.parseFloat(amount)*Math.pow(10, 6))));
//
//            }else if(toke1.equalsIgnoreCase("usdt")){
//                amount1 = String.valueOf(BigInteger.valueOf((long) (Float.parseFloat(amount)*Math.pow(10, 6))));
//
//            }else if(toke1.equalsIgnoreCase("eth")){
//                amount1 = String.valueOf(BigInteger.valueOf((long) (Float.parseFloat(amount)*Math.pow(10, 18))));
//
//            }else if(toke1.equalsIgnoreCase("busd")){
//                amount1 = String.valueOf(BigInteger.valueOf((long) (Float.parseFloat(amount)*Math.pow(10, 18))));
//
//            }
//            RequestQueue queue = Volley.newRequestQueue(getContext());
//            String url = "https://api.1inch.exchange/v3.0/1/quote?fromTokenAddress="+firstToken+"&toTokenAddress="+secondToken+"&amount="+String.valueOf(amount1);
//            JsonObjectRequest
//                    jsonObjectRequest
//                    = new JsonObjectRequest(
//                    Request.Method.GET,
//                    url,
//                    null,
//                    new Response.Listener() {
//                        @Override
//                        public void onResponse(Object response) {
//                            Log.e("onResponse: ",response.toString() );
//                            JSONObject jsonObject = (JSONObject) response;
//                            try {
//                                if(jsonObject.get("toTokenAmount").toString().isEmpty()){
//                                    Toast.makeText(getContext(), "This pair not available in 1 inch", Toast.LENGTH_SHORT).show();
//                                }else{
//                                    String amount2="";
//                                    BigDecimal bigInteger = new BigDecimal(jsonObject.getString("toTokenAmount"));
//                                    Log.e("FRok",String.valueOf(bigInteger.divide(BigDecimal.valueOf((long) Math.pow(10,18)))));
//                                    if(toke2.equalsIgnoreCase("bnb")){
//                                        amount2 = String.valueOf(bigInteger.divide(BigDecimal.valueOf((long) Math.pow(10,18))));
//
//                                    }else if(toke2.equalsIgnoreCase("btc")){
//                                        amount2 = String.valueOf(bigInteger.divide(BigDecimal.valueOf((long) Math.pow(10,8))));
//
//                                    }else if(toke2.equalsIgnoreCase("usdc")){
//                                        amount2 = String.valueOf(bigInteger.divide(BigDecimal.valueOf((long) Math.pow(10,6))));
//
//                                    }else if(toke2.equalsIgnoreCase("usdt")){
//                                        amount2 = String.valueOf(bigInteger.divide(BigDecimal.valueOf((long) Math.pow(10,6))));
//
//                                    }else if(toke2.equalsIgnoreCase("eth")){
//                                        amount2 = String.valueOf(bigInteger.divide(BigDecimal.valueOf((long) Math.pow(10,18))));
//
//                                    }else if(toke2.equalsIgnoreCase("busd")){
//                                        amount2 = String.valueOf(bigInteger.divide(BigDecimal.valueOf((long) Math.pow(10,18))));
//                                    }
//                                    Log.e("AMOUNT",String.valueOf(amount2));
//                                    token2Amount.setText(String.valueOf(amount2));
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//
//                        }
//
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error)
//                        {
//                        }
//                    });
//            queue.add(jsonObjectRequest);
//        }else{
//            Log.e("Reached","failed");
//        }
//
//
//    }

    public void setToken1(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                try{
                    token1PickLayout.setText("");
                    token1Layout.setVisibility(View.VISIBLE);
                    if(toke1.isEmpty()){
                        token1PickLayout.setText("選擇幣種");
                        token1Layout.setVisibility(View.GONE);

                    }else {
                        token1Text.setText(toke1);
                        Picasso.get().load(toke1_Img).into(token1Img);
                    }

                    if(!toke2.isEmpty() && !token1Amount.getText().toString().isEmpty()){
                        callQuotesApi(firstToken,secondToken,String.valueOf(slippage/100),token1Amount.getText().toString());
                    }

//        balanceToken1.setText(String.format("%.4f", getBalance(firstToken)));
                    getBalance(firstToken,balanceToken1);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

    }
    public void setToken2(){
        handler.post(new Runnable() {
            @Override
            public void run() {
                token2PickLayout.setText("");
                token2Layout.setVisibility(View.VISIBLE);

                if(toke2.isEmpty()){
                    token2PickLayout.setText("選擇幣種");
                    token2Layout.setVisibility(View.GONE);

                }else{
                    token2Text.setText(toke2);
                    Picasso.get().load(toke2_Img).into(token2Img);
                }
                if(!token1Amount.getText().toString().isEmpty() && !firstToken.isEmpty() && !secondToken.isEmpty()){
                    callQuotesApi(firstToken,secondToken,String.valueOf(slippage/100),token1Amount.getText().toString());
                }
//                if(!firstToken.isEmpty() && !secondToken.isEmpty()){
//                    callQuotesApiFor1Token(firstToken,secondToken,String.valueOf(slippage/100));
//                }
                //balanceToken2.setText(String.format("%.4f", getBalance(secondToken)));
                getBalance(secondToken,balanceToken2);
            }
        });

    }




    public void promptPassword(Context context){
        if(Preference.getInstance().returnBoolean(WHALETRUST_LOCK_FINGERPRINT)){
            BiometricManager biometricManager = BiometricManager.from(context);

            switch (biometricManager.canAuthenticate()){
                case BiometricManager.BIOMETRIC_SUCCESS:
                    //scannerMessage.setText("Authentication success");
                    break;

                case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                    //scannerMessage.setText("No Finger print hardware detected");
                    break;

                case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                    //scannerMessage.setText("No finger print enrolled");
                    break;

                case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                    //scannerMessage.setText("No Finger print hardware detected");
                    break;

            }

            Executor executor = ContextCompat.getMainExecutor(context);
            BiometricPrompt biometricPrompt = new BiometricPrompt((FragmentActivity) context, executor, new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull @NotNull CharSequence errString) {
                    super.onAuthenticationError(errorCode, errString);
                    Toast.makeText(context, "Finger Print authentication failed", Toast.LENGTH_SHORT).show();
                    ((FragmentActivity) context).finish();
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull @NotNull BiometricPrompt.AuthenticationResult result) {
                    super.onAuthenticationSucceeded(result);
                    Toast.makeText(context, "Finger Print authenticated", Toast.LENGTH_SHORT).show();
                    callSwapApi(firstToken,secondToken,String.valueOf(slippage/100),token1Amount.getText().toString());
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(context, "Finger Print authentication failed", Toast.LENGTH_SHORT).show();
                    //((FragmentActivity) context).finish();
                }
            });
            BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Authenticate")
                    .setDescription("Please place your finger on scanner")
                    .setNegativeButtonText("Cancel")
                    .build();

            biometricPrompt.authenticate(promptInfo);
        }else{
            Intent intent = new Intent(context, CustomKeypadActivity.class);
            intent.putExtra("inputType","6");
            startActivityForResult(intent,100);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            callSwapApi(firstToken,secondToken,String.valueOf(slippage/100),token1Amount.getText().toString());
        }
    }


}