package com.klhk.whalecomp.WhaleTrust;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.sshadkany.neo;
import com.google.protobuf.ByteString;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.utilities.FunctionCall;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthLog;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.TransactionManager;
import org.web3j.utils.Convert;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.concurrent.ExecutionException;

import wallet.core.jni.HDWallet;
import wallet.core.jni.proto.Ethereum;

import static com.klhk.whalecomp.utilities.Constants.BNB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.BSC_SCAN_API_KEY;
import static com.klhk.whalecomp.utilities.Constants.BUSD_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ICON;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_SYMBOL;
import static com.klhk.whalecomp.utilities.Constants.getBalance;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;
import static com.klhk.whalecomp.utilities.Constants.shortAddress;

public class TokenDetailActivity extends AppCompatActivity {

    ImageView sendFunds,receiveFunds,swapFunds,tokenImage;
    TextView tokenBalance,approxAmountUSD;
    LinearLayout todayTxList,olderTxList;
    String tokenName,tokenAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_detail);

        sendFunds= findViewById(R.id.sendFunds);
        receiveFunds= findViewById(R.id.receiveFunds);
        swapFunds= findViewById(R.id.swapFunds);
        todayTxList= findViewById(R.id.todayTxList);
        olderTxList= findViewById(R.id.olderTxList);
        tokenImage = findViewById(R.id.tokenImage);
        tokenBalance = findViewById(R.id.tokenBalance);
        approxAmountUSD = findViewById(R.id.approxAmountUSD);



        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);
        tokenName = getIntent().getStringExtra("tokenName");
        tokenAddress = getIntent().getStringExtra("tokenAddr");
        Picasso.get().load(getIntent().getStringExtra("tokenImage")).into(tokenImage);

        //getBalance(tokenAddress,tokenBalance, tokenName);

        new Thread(new Runnable() {
            @Override
            public void run() {
                String amount = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), tokenAddress);
                double amountOut = FunctionCall.getSwapAmount(tokenAddress,BUSD_ADDRESS,String.valueOf(1),String.valueOf(0.0001));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tokenBalance.setText(String.format("%.4f", Double.parseDouble(amount)) + " " + tokenName.toUpperCase());
                        double amountUSD1 = Double.parseDouble(amount) * amountOut;
                        if(tokenAddress.equalsIgnoreCase(BUSD_ADDRESS)){
                            approxAmountUSD.setText("≈$"+String.format("%.2f", Double.parseDouble(amount)));
                        }else{
                            approxAmountUSD.setText("≈$"+String.format("%.2f", Double.parseDouble(String.valueOf(amountUSD1))));
                        }
                    }
                });
            }
        }).start();;



//        Web3j web3 = Web3j.build(new HttpService("https://bsc-dataseed1.binance.org:443"));
//        EthFilter filter = new EthFilter(DefaultBlockParameterName.EARLIEST,DefaultBlockParameterName.LATEST,"0x4649119AAd1DC5893E7EA6A23b2f3bfE4D46c7DE");
//        try {
//            EthLog log = web3.ethGetLogs(filter).sendAsync().get();
//            Log.e("log", String.valueOf(log.getRawResponse()));
////            for(int i =0; i<log.getLogs().size();i++){
////                Log.e("log",log.getLogs().get(i).toString());
////            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//        https://api.bscscan.com/api?module=account&action=txlist&startblock=1&endblock=99999999&sort=asc&apikey=BM7AH2VYW3XB2HIUSUPHV2PZCI5751R14P&address=0x4649119AAd1DC5893E7EA6A23b2f3bfE4D46c7DE

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "https://api.bscscan.com/api?module=account&action=txlist&startblock=1&endblock=99999999&sort=asc&apikey="+BSC_SCAN_API_KEY+"&address="+ Preference.getInstance().returnValue(WHALETRUST_ADDRESS);
        JsonObjectRequest
                jsonObjectRequest
                = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        Log.e("onResponse: ",response.toString() );
                        JSONObject jsonObject = (JSONObject) response;
                        try {

                            if(jsonObject.getString("status").equalsIgnoreCase("1")){
                                JSONArray dataArray = new JSONArray(jsonObject.getString("result"));
                                if(todayTxList.getChildCount()>0){
                                    todayTxList.removeAllViews();
                                }
                                if(tokenAddress.equalsIgnoreCase(BNB_ADDRESS)){
                                    for(int i=0;i<dataArray.length();i++){
                                        View view = getLayoutInflater().inflate(R.layout.tx_list_layout,null);
                                        ImageView txStatusImage = view.findViewById(R.id.txStatusImage);
                                        neo txCardDetail = view.findViewById(R.id.txCardDetail);
                                        TextView statusText = view.findViewById(R.id.statusText);
                                        TextView toAddressText = view.findViewById(R.id.toAddressText);
                                        TextView bnbGasFee = view.findViewById(R.id.bnbGasFee);
                                        JSONObject obj = new JSONObject(dataArray.get(i).toString());
                                        if(!obj.getString("value").equalsIgnoreCase("0")){
                                            if(!obj.getString("to").equalsIgnoreCase(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS))){
                                                txStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.sent_funds_tx));
                                                toAddressText.setText("to: "+shortAddress(obj.getString("to")));
                                            }else{
                                                txStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.receive_funds_tx));
                                                toAddressText.setText("from: "+shortAddress(obj.getString("from")));
                                            }
                                            bnbGasFee.setText(String.valueOf(Convert.fromWei(obj.getString("value"), Convert.Unit.ETHER))+" BNB");
                                            int counter =i;

                                            txCardDetail.setOnTouchListener(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View v, MotionEvent event) {
                                                    ViewGroup viewGroupC1 = v.findViewById(R.id.txCardDetail);
                                                    final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                                                    try{
                                                        if (txCardDetail.isShapeContainsPoint(event.getX(), event.getY())) {
                                                            switch (event.getAction()) {
                                                                case MotionEvent.ACTION_DOWN:

                                                                    txCardDetail.setStyle(neo.small_inner_shadow);
                                                                    textView.setScaleX(textView.getScaleX() * 0.95f);
                                                                    textView.setScaleY(textView.getScaleY() * 0.95f);
                                                                    return true; // if you want to handle the touch event
                                                                case MotionEvent.ACTION_UP:
                                                                case MotionEvent.ACTION_CANCEL:
                                                                    // RELEASED
                                                                    txCardDetail.setStyle(neo.drop_shadow);
                                                                    textView.setScaleX(1);
                                                                    textView.setScaleY(1);
                                                                    Thread.sleep(100);
                                                                    try{
                                                                        Intent intent = new Intent(TokenDetailActivity.this,TxDetailActivity.class);
                                                                        intent.putExtra("data",dataArray.get(counter).toString());
                                                                        startActivity(intent);
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

                                            todayTxList.addView(view);
                                        }
                                    }
                                }else{
                                    for(int i=0;i<dataArray.length();i++){
                                        View view = getLayoutInflater().inflate(R.layout.tx_list_layout,null);
                                        ImageView txStatusImage = view.findViewById(R.id.txStatusImage);
                                        TextView statusText = view.findViewById(R.id.statusText);
                                        TextView toAddressText = view.findViewById(R.id.toAddressText);
                                        TextView bnbGasFee = view.findViewById(R.id.bnbGasFee);
                                        neo txCardDetail = view.findViewById(R.id.txCardDetail);
                                        JSONObject obj = new JSONObject(dataArray.get(i).toString());
                                        if(obj.getString("to").equalsIgnoreCase(tokenAddress)||obj.getString("from").equalsIgnoreCase(tokenAddress)){
                                            if(!obj.getString("to").equalsIgnoreCase(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS))){
                                                txStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.sent_funds_tx));
                                                toAddressText.setText("to: "+shortAddress(obj.getString("to")));
                                            }else{
                                                txStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.receive_funds_tx));
                                                toAddressText.setText("from: "+shortAddress(obj.getString("from")));
                                            }
                                            int counter =i;
                                            txCardDetail.setOnTouchListener(new View.OnTouchListener() {
                                                @Override
                                                public boolean onTouch(View v, MotionEvent event) {
                                                    ViewGroup viewGroupC1 = v.findViewById(R.id.txCardDetail);
                                                    final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                                                    try{
                                                        if (txCardDetail.isShapeContainsPoint(event.getX(), event.getY())) {
                                                            switch (event.getAction()) {
                                                                case MotionEvent.ACTION_DOWN:

                                                                    txCardDetail.setStyle(neo.small_inner_shadow);
                                                                    textView.setScaleX(textView.getScaleX() * 0.95f);
                                                                    textView.setScaleY(textView.getScaleY() * 0.95f);
                                                                    return true; // if you want to handle the touch event
                                                                case MotionEvent.ACTION_UP:
                                                                case MotionEvent.ACTION_CANCEL:
                                                                    // RELEASED
                                                                    txCardDetail.setStyle(neo.drop_shadow);
                                                                    textView.setScaleX(1);
                                                                    textView.setScaleY(1);
                                                                    Thread.sleep(100);
                                                                    try{
                                                                        Intent intent = new Intent(TokenDetailActivity.this,TxDetailActivity.class);
                                                                        intent.putExtra("data",dataArray.get(counter).toString());
                                                                        startActivity(intent);
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

                                            todayTxList.addView(view);
                                        }

                                    }
                                }

                                }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                    }
                });
        queue.add(jsonObjectRequest);
        sendFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(),SendToAddressActivity.class);
                i.putExtra("tokenName",tokenName);
                i.putExtra("tokenAddr",tokenAddress);
                startActivity(i);
            }
        });

        receiveFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplication(),ReceiveFundsActivity.class));
            }
        });
        swapFunds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplication(),SwapWhaleTrustActivity.class);
                i.putExtra("tokenName",tokenName);
                i.putExtra("tokenAddr",tokenAddress);
                i.putExtra("isSecondToken","false");
                i.putExtra("tokenImage",getIntent().getStringExtra("tokenImage"));
                startActivity(i);
            }
        });

    }

    public void getTransactionHistory(){

    }
}