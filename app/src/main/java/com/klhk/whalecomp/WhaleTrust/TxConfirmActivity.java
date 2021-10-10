package com.klhk.whalecomp.WhaleTrust;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.CutomKeypad.CustomKeypadActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.SettingFragment.PasswordSettingActivity;
import com.klhk.whalecomp.utilities.FunctionCall;
import com.klhk.whalecomp.utilities.LockOut;

import org.jetbrains.annotations.NotNull;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigInteger;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

import wallet.core.jni.CoinType;
import wallet.core.jni.HDWallet;
import wallet.core.jni.PrivateKey;

import static com.klhk.whalecomp.utilities.Constants.BNB_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.LAST_DATE_MILLI;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_PATTERN;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_LOCK_FINGERPRINT;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_LOCK_TIMEOUT;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;
import static com.klhk.whalecomp.utilities.Constants.shortAddress;

public class TxConfirmActivity extends AppCompatActivity {

    TextView transferAmount,transferAmountUSD,tokenName,senderAddress,gasAmountText,totalAmount;
    ImageView continueButton;
    Web3j web3;
    HDWallet wallet;
    PrivateKey pk;
    Credentials credentials;

    String tokenName1,tokenAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tx_confirm);
        System.loadLibrary("TrustWalletCore");
        wallet = new HDWallet(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_PATTERN), "");
        web3 = Web3j.build(new HttpService("https://bsc-dataseed1.binance.org:443"));
        pk = wallet.getKeyForCoin(CoinType.SMARTCHAIN);
        credentials = Credentials.create(ECKeyPair.create(pk.data()));

        continueButton = findViewById(R.id.continueButton);
        transferAmount = findViewById(R.id.transferAmount);
        transferAmountUSD = findViewById(R.id.transferAmountUSD);
        tokenName = findViewById(R.id.tokenName);
        senderAddress = findViewById(R.id.senderAddress);
        gasAmountText = findViewById(R.id.gasAmountText);
        totalAmount = findViewById(R.id.totalAmount);
        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        Intent i = getIntent();

        tokenName.setText(i.getStringExtra("tokenName"));
        tokenName1 = getIntent().getStringExtra("tokenName");
        tokenAddress = getIntent().getStringExtra("tokenAddr");
        senderAddress.setText(shortAddress(getIntent().getStringExtra("address")));
        transferAmount.setText("-"+getIntent().getStringExtra("amount")+" "+tokenName1);


        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptPassword(TxConfirmActivity.this);
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
                    if(tokenAddress.equalsIgnoreCase(BNB_ADDRESS)){
                        SendFunds sendFunds = new SendFunds();
                        sendFunds.execute(getIntent().getStringExtra("address"),getIntent().getStringExtra("amount"));
                    }else{
                        String tx= FunctionCall.transferTokens(getIntent().getStringExtra("address"),tokenAddress,Double.parseDouble(getIntent().getStringExtra("amount")));
                        Intent intent = new Intent(getApplicationContext(),TxSuccessActivity.class);
                        intent.putExtra("txHash",tx);
                        intent.putExtra("tokenName",tokenName1);
                        intent.putExtra("tokenAddr",tokenAddress);
                        intent.putExtra("amount",getIntent().getStringExtra("amount"));
                        intent.putExtra("address",getIntent().getStringExtra("address"));
                        startActivity(intent);
                        finish();
                    }
//                        SendFunds sendFunds = new SendFunds();
//                        sendFunds.execute(getIntent().getStringExtra("address"),getIntent().getStringExtra("amount"));
                }

                @Override
                public void onAuthenticationFailed() {
                    super.onAuthenticationFailed();
                    Toast.makeText(context, "Finger Print authentication failed", Toast.LENGTH_SHORT).show();
                    ((FragmentActivity) context).finish();
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){

            if(tokenAddress.equalsIgnoreCase(BNB_ADDRESS)){
                SendFunds sendFunds = new SendFunds();
                sendFunds.execute(getIntent().getStringExtra("address"),getIntent().getStringExtra("amount"));
            }else{
                String tx= FunctionCall.transferTokens(getIntent().getStringExtra("address"),tokenAddress,Double.parseDouble(getIntent().getStringExtra("amount")));
                Intent intent = new Intent(getApplicationContext(),TxSuccessActivity.class);
                intent.putExtra("txHash",tx);
                intent.putExtra("tokenName",tokenName1);
                intent.putExtra("tokenAddr",tokenAddress);
                intent.putExtra("amount",getIntent().getStringExtra("amount"));
                intent.putExtra("address",getIntent().getStringExtra("address"));
                startActivity(intent);
                finish();
            }

        }
    }

    class SendFunds extends AsyncTask<String, Void, String> {


        protected String doInBackground(String... args) {
            String tx="";
            try{

                BigInteger nonce =  web3.ethGetTransactionCount(credentials.getAddress(), DefaultBlockParameterName.LATEST).sendAsync().get().getTransactionCount();
                BigInteger gasPrice = web3.ethGasPrice().sendAsync().get().getGasPrice();

                BigInteger value = Convert.toWei(args[1], Convert.Unit.ETHER).toBigInteger();

                RawTransaction rawTransaction = RawTransaction.createEtherTransaction(nonce, //new BigInteger(String.valueOf(2)),//nonce
                        gasPrice, //gasprice
                        BigInteger.valueOf(500000), //gaslimit
                        args[0], //to
                        value);//amount

                byte[] signedMessage = TransactionEncoder.signMessage(rawTransaction, 56 , credentials);

                String hexValue = Numeric.toHexString(signedMessage);
                Log.e("Signed Tx",hexValue);
                EthSendTransaction transactionResponse = web3.ethSendRawTransaction(hexValue).sendAsync().get(600, TimeUnit.SECONDS);
                if(transactionResponse.hasError()){
                    tx = transactionResponse.getError().getMessage();
                }else{
                    tx = transactionResponse.getTransactionHash();
                }

            }catch (Exception e){
                e.printStackTrace();
            }
            return tx;
        }

        protected void onPostExecute(String message) {
            //Log.e("Tx","trans "+ message);

            Toast.makeText(TxConfirmActivity.this, message, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(),TxSuccessActivity.class);
            intent.putExtra("txHash",message);
            intent.putExtra("tokenName",tokenName1);
            intent.putExtra("tokenAddr",tokenAddress);
            intent.putExtra("amount",getIntent().getStringExtra("amount"));
            intent.putExtra("address",getIntent().getStringExtra("address"));
            startActivity(intent);
            TxConfirmActivity.this.finish();
        }
    }
}