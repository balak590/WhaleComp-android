package com.klhk.whalecomp.SettingFragment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WhaleTrust.CreateMnemonicActivity;
import com.klhk.whalecomp.WhaleTrust.ImportAddressActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_METHOD;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_PATTERN;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_PASSWORD;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

public class PickAddressActivity extends AppCompatActivity {


    LinearLayout addressListLayout;
    ImageView addAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_address);

        addAddress = findViewById(R.id.addAddress);
        addressListLayout = findViewById(R.id.addressListLayout);
        createView();



        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                AlertDialog.Builder builder = new AlertDialog.Builder(PickAddressActivity.this,R.style.CustomDialog);

                View v = getLayoutInflater().inflate(R.layout.pick_add_address_whaletrust_new,null);
                ImageView closeButton = v.findViewById(R.id.closeButton);
                neo newButton = v.findViewById(R.id.newButton);
                neo importButton = v.findViewById(R.id.importButton);
                builder.setCancelable(false);

                builder.setView(v);
                AlertDialog dialog = builder.show();

                newButton.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v1, MotionEvent event) {
                        try{
                            ViewGroup viewGroupC1 = v1.findViewById(R.id.newButton);
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
                                        startActivity(new Intent(PickAddressActivity.this, CreateMnemonicActivity.class));
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
                    public boolean onTouch(View v1, MotionEvent event) {
                        try{
                            ViewGroup viewGroupC1 = v1.findViewById(R.id.importButton);
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
                                        startActivity(new Intent(PickAddressActivity.this, ImportAddressActivity.class));
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
                    }
                });
            }
        });
    }

    public void createView(){
        int accountCounter =1;
        try{
            JSONArray array = new JSONArray(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_LIST));
            if(addressListLayout.getChildCount()>0){
                addressListLayout.removeAllViews();
            }
            for(int i=0; i<array.length();i++){
                View v = getLayoutInflater().inflate(R.layout.pick_address_template,null,false);
                TextView addressName = v.findViewById(R.id.addressName);
                TextView accountName = v.findViewById(R.id.accountName);
                ImageView bscSelected = v.findViewById(R.id.bscSelected);
                JSONObject obj = new JSONObject(array.get(i).toString());
                if(obj.get(WHALETRUST_ADDRESS).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))){
                    bscSelected.setImageDrawable(getResources().getDrawable(R.drawable.round_selected));
                }else{
                    bscSelected.setImageDrawable(getResources().getDrawable(R.drawable.round_unselected));
                }

                if(obj.has(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME)){
                    accountName.setText(obj.get(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME).toString());
                }else{
                    accountName.setText("我的賬戶 "+String.valueOf(accountCounter));
                    accountCounter++;
                }
                String addr = obj.get(WHALETRUST_ADDRESS).toString();
                String str1 = addr.substring(0, 6) + ".."+ addr.substring(addr.length()-4, addr.length());
                addressName.setText(str1);
                int counter =i;
                bscSelected.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try{

                            JSONObject obj1 = new JSONObject(array.get(counter).toString());

                            Preference.getInstance().writePreference(WALLET_SELECTED_ADDRESS,obj1.getString(WHALETRUST_ADDRESS));
                            Preference.getInstance().writePreference(WHALETRUST_ADDRESS_SELECTED,obj1.getString(WHALETRUST_ADDRESS));
                            Preference.getInstance().writePreference(WHALETRUST_ADDRESS,obj1.getString(WHALETRUST_ADDRESS));
                            Preference.getInstance().writePreference(WHALETRUST_ADDRESS_PATTERN,obj1.getString(WHALETRUST_ADDRESS_PATTERN));
                            Preference.getInstance().writePreference(WHALETRUST_ADDRESS_METHOD,obj1.getString(WHALETRUST_ADDRESS_METHOD));
                            Preference.getInstance().writePreference(WHALETRUST_PASSWORD,obj1.getString(WHALETRUST_PASSWORD));
                            if(obj1.has(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME)){
                                Preference.getInstance().writePreference(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME,obj1.getString(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME));
                            }else{
                                Preference.getInstance().writePreference(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME,"");
                            }
                            createView();
                        }catch(Exception e){
                            e.printStackTrace();
                        }


                    }
                });
                addressListLayout.addView(v);

            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }
}