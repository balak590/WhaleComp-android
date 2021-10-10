package com.klhk.whalecomp.WhaleTrust;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WebBrowser.WebBrowserActivity;

import org.json.JSONObject;
import org.web3j.utils.Convert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.klhk.whalecomp.utilities.Constants.BSC_SCAN_API_KEY;
import static com.klhk.whalecomp.utilities.Constants.URL_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;
import static com.klhk.whalecomp.utilities.Constants.shortAddress;
import static com.klhk.whalecomp.utilities.Hex.hexToInteger;

public class TxDetailActivity extends AppCompatActivity {

    TextView transferAmountText,transferUsdAmountText,senderAddress,txHashText,blockNumberText,confirmText,gasUsageText,gasUnitPriceText,networkFeeText,stateText,dateText;
    ImageView continueButton;
    String txHash="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_detail);
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
        dateText = findViewById(R.id.dateText);


//        txHashText.setText(shortAddress(getIntent().getStringExtra("txHash")));
//        senderAddress.setText(shortAddress(getIntent().getStringExtra("address")));
//        transferAmountText.setText("-"+getIntent().getStringExtra("amount")+" "+getIntent().getStringExtra("tokenName"));


        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);
        try{
            JSONObject obj = new JSONObject(getIntent().getStringExtra("data"));
            blockNumberText.setText(obj.getString("blockNumber"));
            dateText.setText(convertDate(obj.getString("timeStamp")));
            if(!obj.getString("to").equalsIgnoreCase(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS))){
                //txStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.sent_funds_tx));
                senderAddress.setText(shortAddress(obj.getString("to")));
            }else{
                //txStatusImage.setImageDrawable(getResources().getDrawable(R.drawable.receive_funds_tx));
                senderAddress.setText(shortAddress(obj.getString("from")));
            }

            txHashText.setText(shortAddress(obj.getString("hash")));
            txHash= obj.getString("hash");
//            stateText.setText(obj.getString("已完成"));
            stateText.setText("已完成");
            confirmText.setText(obj.getString("confirmations"));
            gasUsageText.setText(Convert.fromWei(obj.getString("gasPrice"), Convert.Unit.GWEI).toString()+" Gwei");
            gasUnitPriceText.setText(obj.getString("gas")+" Gwei");
            networkFeeText.setText((CharSequence) Convert.fromWei(obj.getString("cumulativeGasUsed"), Convert.Unit.GWEI).toString()+" BNB");

        }catch (Exception e){
            e.printStackTrace();
        }

        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://bscscan.com/tx/"+txHash;
                Intent intent = new Intent(TxDetailActivity.this, WebBrowserActivity.class);
                intent.putExtra(URL_ADDRESS,url);
                startActivity(intent);
            }
        });

    }

    public String convertDate(String timeMills){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        long milliSeconds= Long.parseLong(timeMills)*1000;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }


}