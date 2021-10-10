package com.klhk.whalecomp.WhaleTrust;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.klhk.whalecomp.utilities.Hex;
import com.klhk.whalecomp.utilities.RawTx;
//import com.klhk.whalecomp.utilities.TransactionEncoder;

import org.json.JSONObject;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Hash;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlockNumber;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.Transaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.core.methods.response.Web3ClientVersion;
import org.web3j.protocol.core.methods.response.Web3Sha3;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.FastRawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Currency;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

//import wallet.core.java.AnySigner;
//import wallet.core.jni.AnySigner;
import javax.annotation.Nullable;

import wallet.core.java.AnySigner;
import wallet.core.jni.CoinType;
import wallet.core.jni.HDWallet;
import wallet.core.jni.PrivateKey;
import wallet.core.jni.proto.Binance;
import wallet.core.jni.proto.Ethereum;

import static com.klhk.whalecomp.utilities.Constants.BUSD_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_PATTERN;
import static com.klhk.whalecomp.utilities.Constants.getBalance;
import static com.klhk.whalecomp.utilities.Constants.getFullBalance;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;
import static java.sql.DriverManager.println;

public class SendToAddressActivity extends AppCompatActivity {
    ImageView continueButton,scanQrCodeButton;
    TextView pasteAddressButton,maxAmountButton,tokenBalance,approxAmountUSD;
    EditText addressEditText,amountEditText;
    String tokenName,tokenAddress;
    String amount;
    double amountOut;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==4000&&resultCode==RESULT_OK){
            addressEditText.setText(data.getStringExtra("address"));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_to_address);

        continueButton = findViewById(R.id.continueButton);


        scanQrCodeButton = findViewById(R.id.scanQrCodeButton);
        pasteAddressButton = findViewById(R.id.pasteAddressButton);
        maxAmountButton = findViewById(R.id.maxAmountButton);
        addressEditText = findViewById(R.id.addressEditText);
        amountEditText = findViewById(R.id.amountEditText);
        tokenBalance = findViewById(R.id.tokenBalance);
        approxAmountUSD = findViewById(R.id.approxAmountUSD);

        tokenName = getIntent().getStringExtra("tokenName");
        tokenAddress = getIntent().getStringExtra("tokenAddr");
        //getBalance(tokenAddress,tokenBalance, tokenName);

        //FunctionCall.swapTokens("0xc9849e6fdb743d08faee3e34dd2d1bc69ea11a51","0xe9e7cea3dedca5984780bafc599bd69add087d56", 0.0001,0.5);

        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                amount = FunctionCall.getBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS), tokenAddress);
                amountOut = FunctionCall.getSwapAmount(tokenAddress,BUSD_ADDRESS,String.valueOf(1),String.valueOf(0.0001));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tokenBalance.setText(String.format("%.4f", Double.parseDouble(amount)) + " " + tokenName.toUpperCase());
                    }
                });
            }
        }).start();

        amountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!amountEditText.getText().toString().isEmpty()){
                    double amountUSD1 = Double.parseDouble(amountEditText.getText().toString()) * amountOut;
                    if(tokenAddress.equalsIgnoreCase(BUSD_ADDRESS)){
                        approxAmountUSD.setText("≈$"+String.format("%.2f", Double.parseDouble(amountEditText.getText().toString())));
                    }else{
                        approxAmountUSD.setText("≈$"+String.format("%.2f", Double.parseDouble(String.valueOf(amountUSD1))));
                    }
                }else{
                    approxAmountUSD.setText("≈$0.00");
                }
            }
        });
        maxAmountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                amountEditText.setText(tokenBalance.getText().toString());
                getFullBalance(tokenAddress,amountEditText);
            }
        });
        pasteAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String copiedText = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                if(copiedText.length()!=42 || !copiedText.contains("0x")){
                    Toast.makeText(SendToAddressActivity.this, "Paste a valid address", Toast.LENGTH_SHORT).show();
                }else{
                    addressEditText.setText(copiedText);
                }
            }
        });
        scanQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(SendToAddressActivity.this,ScanActivity.class),4000);
            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            @Nullable
            public void onClick(View v) {

                if(addressEditText.getText().toString().isEmpty() || amountEditText.getText().toString().isEmpty()){
                    Toast.makeText(SendToAddressActivity.this, "Please fill address and amount to be transferred", Toast.LENGTH_SHORT).show();
                }else{

                    Intent intent = new Intent(SendToAddressActivity.this,TxConfirmActivity.class);
                    intent.putExtra("tokenName",tokenName);
                    intent.putExtra("tokenAddr",tokenAddress);
                    intent.putExtra("amount",amountEditText.getText().toString());
                    intent.putExtra("address",addressEditText.getText().toString());
                    startActivity(intent);
                }
        }});

    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }
}