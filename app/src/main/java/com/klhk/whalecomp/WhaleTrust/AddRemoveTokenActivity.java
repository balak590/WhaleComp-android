package com.klhk.whalecomp.WhaleTrust;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static com.klhk.whalecomp.utilities.Constants.ETH_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_LOCK_FINGERPRINT;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ACTIVE;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_DIGIT;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_ICON;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_NAME;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_SYMBOL;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;
import static com.klhk.whalecomp.utilities.TokenList.tokenList;

public class AddRemoveTokenActivity extends AppCompatActivity {

    ImageView newTokenButton;
    LinearLayout activeTokenList,inActiveTokenList;
    AutoCompleteTextView searchTokenText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_remove_token);
        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        searchTokenText = findViewById(R.id.searchTokenText);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(AddRemoveTokenActivity.this, android.R.layout.simple_list_item_1,tokenList);
        searchTokenText.setThreshold(1);
        searchTokenText.setAdapter(adapter);
        newTokenButton = findViewById(R.id.newTokenButton);

        searchTokenText.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(AddRemoveTokenActivity.this, adapter.getItem(position).toString(), Toast.LENGTH_SHORT).show();
                String tokenSelected = adapter.getItem(position).toString();
                findToken(tokenSelected);
            }
        });

        newTokenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddRemoveTokenActivity.this,NewTokenActivity.class));
                finish();
            }
        });

        activeTokenList = findViewById(R.id.activeTokenList);
        inActiveTokenList = findViewById(R.id.inActiveTokenList);

        setupActiveTokens();

    }

    public boolean checkToken(String tokenAddr){
        boolean tokenAvialable = false;
        try{
            JSONArray checkArray = new JSONArray();
            if(!Preference.getInstance().returnValue(WHALETRUST_TOKEN_LIST).isEmpty()){
                checkArray =  new JSONArray(Preference.getInstance().returnValue(WHALETRUST_TOKEN_LIST));
            }
            if(checkArray.length()>0){
                for(int i =0;i<checkArray.length();i++){
                    JSONObject obj = new JSONObject(checkArray.get(i).toString());
                    if(obj.getString(WHALETRUST_TOKEN_ADDRESS).equalsIgnoreCase(tokenAddr)){
                        tokenAvialable=true;
                    }

                }
            }
        }catch (Exception e){
            e.printStackTrace();

        }
        return tokenAvialable;
    }

    public void InitialTokens(String tokenName,String tokenAddr,String tokenSymbol,String imagePath){


        if(!checkToken(tokenAddr)){
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
                setupActiveTokens();


            }catch(Exception e){
                e.printStackTrace();
            }
        }else{
            Toast.makeText(this, "You have already added this token to the list.", Toast.LENGTH_SHORT).show();
        }

    }

    public void findToken(String tokenName){
        try {
            JSONArray array = new JSONArray(loadJSONFromAsset());
            //JSONObject tokenObj = new JSONObject(obj.getString(tokenAddress.toLowerCase()));
            for(int i=0;i<array.length();i++){
                JSONObject obj = new JSONObject(array.get(i).toString());
                if(obj.getString("symbol").equalsIgnoreCase(tokenName)||obj.getString("name").equalsIgnoreCase(tokenName)){
                    String tokenAddr = new JSONObject(obj.getString("platforms")).getString("binance-smart-chain");
                    String tokenName1 = obj.getString("name").toUpperCase();
                    String tokenSymbol = obj.getString("symbol").toUpperCase();
                    String imagePath= obj.getString("image");
                    InitialTokens(tokenName1,tokenAddr,tokenSymbol,imagePath);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getApplication().getAssets().open("bsc_tokens_logo.json");
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

    public void setupActiveTokens(){
        try{
            JSONArray array = new JSONArray();
            if(!Preference.getInstance().returnValue(WHALETRUST_TOKEN_LIST).isEmpty()){
                array =  new JSONArray(Preference.getInstance().returnValue(WHALETRUST_TOKEN_LIST));
            }
            if(activeTokenList.getChildCount()>0){
                activeTokenList.removeAllViews();
            }
            if(inActiveTokenList.getChildCount()>0){
                inActiveTokenList.removeAllViews();
            }
            for(int i =0;i<array.length();i++){
                View view = getLayoutInflater().inflate(R.layout.template_token_active,null,false);
                ImageView tokenImage = view.findViewById(R.id.tokenImage);
                ImageView toggleToken = view.findViewById(R.id.toggleToken);
                TextView tokenName = view.findViewById(R.id.tokenName);
                JSONObject obj = new JSONObject(array.get(i).toString());
                tokenName.setText(obj.getString(WHALETRUST_TOKEN_SYMBOL));
                Picasso.get().load(obj.getString(WHALETRUST_TOKEN_ICON)).into(tokenImage);
                int counter =i;
                JSONArray finalArray = array;
                toggleToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{
                            String updateValue="";
                            if(toggleToken.getDrawable().getConstantState()==getResources().getDrawable(R.drawable.pass_toggle_on).getConstantState()){
                                toggleToken.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
                                updateValue="false";
                            }else{
                                toggleToken.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
                                updateValue="true";
                            }

                            JSONArray updatedArray = new JSONArray();
                            for(int j=0; j<finalArray.length();j++){
                                JSONObject obj1 = new JSONObject(finalArray.get(j).toString());
                                if(j==counter){
                                    obj1.put(WHALETRUST_TOKEN_ACTIVE,updateValue);
                                }
                                updatedArray.put(obj1);
                            }
                            Preference.getInstance().writePreference(WHALETRUST_TOKEN_LIST,updatedArray.toString());
                            setupActiveTokens();
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                });
                if(obj.getString(WHALETRUST_TOKEN_ACTIVE).equalsIgnoreCase("true")){
                    toggleToken.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_on));
                    activeTokenList.addView(view);
                }else if(obj.getString(WHALETRUST_TOKEN_ACTIVE).equalsIgnoreCase("false")){
                    toggleToken.setImageDrawable(getResources().getDrawable(R.drawable.pass_toggle_off));
                    inActiveTokenList.addView(view);
                }


            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}