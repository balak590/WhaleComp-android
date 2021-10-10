package com.klhk.whalecomp.WhaleTrust;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.sshadkany.neo;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WalletFragment.WalletFragment;

import org.json.JSONObject;

import static com.klhk.whalecomp.utilities.Constants.BSC_SCAN_API_KEY;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;
import static com.klhk.whalecomp.utilities.Constants.shortAddress;
import static com.klhk.whalecomp.utilities.Hex.hexToInteger;

public class TxSuccessActivity extends AppCompatActivity {

    TextView transferAmountText,transferUsdAmountText,senderAddress,txHashText,blockNumberText,confirmText,gasUsageText,gasUnitPriceText,networkFeeText,stateText;
    ImageView continueButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_success);

        transferAmountText = findViewById(R.id.transferAmountText);
        transferUsdAmountText = findViewById(R.id.transferUsdAmountText);
        senderAddress = findViewById(R.id.senderAddress);
        txHashText = findViewById(R.id.txHashText);
        blockNumberText = findViewById(R.id.blockNumberText);
        confirmText = findViewById(R.id.confirmText);
        gasUsageText = findViewById(R.id.gasUsageText);
        gasUnitPriceText = findViewById(R.id.gasUnitPriceText);
        networkFeeText = findViewById(R.id.networkFeeText);
        continueButton = findViewById(R.id.continueButton);
        stateText = findViewById(R.id.stateText);


        txHashText.setText(shortAddress(getIntent().getStringExtra("txHash")));
        senderAddress.setText(shortAddress(getIntent().getStringExtra("address")));
        transferAmountText.setText("-"+getIntent().getStringExtra("amount")+" "+getIntent().getStringExtra("tokenName"));


        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                getTxDetails(getIntent().getStringExtra("txHash"));
            }
        }, 2000);
    }

    public void getTxDetails(String txHash){
        RequestQueue queue = Volley.newRequestQueue(TxSuccessActivity.this);

        String url = "https://api.bscscan.com/api?module=proxy&action=eth_getTransactionReceipt&txhash="+txHash+"&apikey="+BSC_SCAN_API_KEY;
        Log.e( "getTxDetails: ",url );
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

                            if(jsonObject.getString("result")!=null){
                                JSONObject object = new JSONObject(jsonObject.getString("result").toString());
                                blockNumberText.setText(String.valueOf(hexToInteger(object.getString("blockNumber"))));
                                if(object.getString("blockNumber").equalsIgnoreCase("0x1")){
                                    stateText.setText("已完成");
                                }
                                gasUsageText.setText(String.valueOf(hexToInteger(object.getString("gasUsed"))));
                                double amountUsedBnb = hexToInteger(object.getString("gasUsed"))* 0.000000005;
                                networkFeeText.setText("-"+ String.valueOf(amountUsedBnb)+" BNB");


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
    }
}