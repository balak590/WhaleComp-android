package com.klhk.whalecomp.WhaleTrust;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
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

import com.github.sshadkany.neo;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_METHOD;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_PATTERN;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_PASSWORD;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ACTIVE;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_DIGIT;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ICON;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_NAME;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_SYMBOL;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

public class NewTokenActivity extends AppCompatActivity {

    ImageView continueButton,scanQrCodeButton;
    TextView pasteAddressButton,tokenName,tokenSymbol,tokenDigit;
    EditText addressEditText;
    boolean foundToken=false;
    String tokenAddr,imagePath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_token);
        continueButton = findViewById(R.id.continueButton);


        scanQrCodeButton = findViewById(R.id.scanQrCodeButton);
        pasteAddressButton = findViewById(R.id.pasteAddressButton);
        addressEditText = findViewById(R.id.addressEditText);

        tokenDigit = findViewById(R.id.tokenDigit);
        tokenSymbol = findViewById(R.id.tokenSymbol);
        tokenName = findViewById(R.id.tokenName);

        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        addressEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(addressEditText.getText().toString().length()==42){
                    if(!addressEditText.getText().toString().contains("0x")){
                        Toast.makeText(NewTokenActivity.this, "Paste a valid address", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        findToken(addressEditText.getText().toString());
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pasteAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String copiedText = clipboard.getPrimaryClip().getItemAt(0).getText().toString();
                if(copiedText.length()!=42 || !copiedText.contains("0x")){
                    Toast.makeText(NewTokenActivity.this, "Paste a valid address", Toast.LENGTH_SHORT).show();
                }else{
                    addressEditText.setText(copiedText);
                    findToken(copiedText);
                }
            }
        });
        scanQrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            @Nullable
            public void onClick(View v) {
                JSONObject object = new JSONObject();
                try{

                    if(foundToken){
                        object.put(WHALETRUST_TOKEN_NAME,tokenName.getText().toString());
                        object.put(WHALETRUST_TOKEN_ADDRESS,tokenAddr);
                        object.put(WHALETRUST_TOKEN_DIGIT,"18");
                        object.put(WHALETRUST_TOKEN_SYMBOL,tokenSymbol.getText().toString());
                        object.put(WHALETRUST_TOKEN_ACTIVE,"true");
                        object.put(WHALETRUST_TOKEN_ICON,imagePath);

                        Log.e("reahced",object.toString());
                        JSONArray array = new JSONArray();
                        if(!Preference.getInstance().returnValue(WHALETRUST_TOKEN_LIST).isEmpty()){
                            array =  new JSONArray(Preference.getInstance().returnValue(WHALETRUST_TOKEN_LIST));
                        }

                        array.put(object);
                        Preference.getInstance().writePreference(WHALETRUST_TOKEN_LIST,array.toString());

                        finish();
                    }else{
                        Toast.makeText(NewTokenActivity.this, "Please fill in the token details", Toast.LENGTH_SHORT).show();
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }

            }});
    }

    public void findToken(String tokenAddress){
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            JSONObject tokenObj = new JSONObject(obj.getString(tokenAddress.toLowerCase()));

            if(tokenObj!=null){
                tokenAddr = tokenAddress;
                tokenName.setText(tokenObj.getString("name").toUpperCase());
                tokenSymbol.setText(tokenObj.getString("symbol").toUpperCase());
                imagePath= tokenObj.getString("image");
                foundToken=true;
            }else{
                Toast.makeText(NewTokenActivity.this, "Paste a valid address", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplication().getAssets().open("Tokens.json");
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
}