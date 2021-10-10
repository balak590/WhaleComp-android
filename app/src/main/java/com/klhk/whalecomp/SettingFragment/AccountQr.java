package com.klhk.whalecomp.SettingFragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.sshadkany.neo;
import com.google.zxing.WriterException;
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_METHOD;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_PATTERN;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_PASSWORD;
import static com.klhk.whalecomp.utilities.Constants.buttonPressedEffectCircle;
import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

public class AccountQr extends AppCompatActivity {

    ImageView qrImage,copyButton;
    TextView addressText,accountName;
    EditText accountNameEdit;
    neo editButton;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    Log.d("focus", "touchevent");
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_qr);
        qrImage = findViewById(R.id.qrImage);
        editButton = findViewById(R.id.editButton);
        copyButton = findViewById(R.id.copyButton);
        addressText = findViewById(R.id.addressText);
        accountName = findViewById(R.id.accountName);
        accountNameEdit = findViewById(R.id.accountNameEdit);

        addressText.setText(Preference.getInstance().returnValue(WHALETRUST_ADDRESS));
        if(!Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME).isEmpty()){
            accountName.setText(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME));
        }else{
            try{
                JSONArray array = new JSONArray(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_LIST));
                int counter = 1;
                for(int i=0;i<array.length();i++){
                    JSONObject obj = new JSONObject(array.get(i).toString());
                    if(obj.get(WHALETRUST_ADDRESS).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))){
                        accountName.setText("我的賬戶 "+ counter);
                    }
                    counter++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);


        editButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = findViewById(R.id.editButton);
                final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
                boolean output = buttonPressedEffectCircle(editButton,event,imageviewC1);

                accountNameEdit.setText(accountName.getText().toString());
                accountName.setVisibility(View.GONE);
                accountNameEdit.setVisibility(View.VISIBLE);
                accountNameEdit.setEnabled(true);
                accountNameEdit.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);


                return output;
            }
        });

//        editButton.setOnTouchListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                accountNameEdit.setText(accountName.getText().toString());
//
//                accountName.setVisibility(View.GONE);
//                accountNameEdit.setVisibility(View.VISIBLE);
//                accountNameEdit.setEnabled(true);
//                accountNameEdit.requestFocus();
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
//            }
//        });
        accountNameEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    if(!accountNameEdit.getText().toString().isEmpty() && !accountNameEdit.getText().toString().equalsIgnoreCase(accountName.getText().toString())){

                        accountName.setText(accountNameEdit.getText().toString());
                        accountName.setVisibility(View.VISIBLE);
                        accountNameEdit.setVisibility(View.GONE);
                        accountNameEdit.setText("");
                        try{
                            JSONArray array = new JSONArray(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_LIST));
                            JSONArray updatedArray = new JSONArray();

                            for(int i=0;i<array.length();i++){
                                JSONObject obj = new JSONObject(array.get(i).toString());
                                JSONObject object = new JSONObject();

                                if(obj.get(WHALETRUST_ADDRESS).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))){

                                    object.put(WHALETRUST_ADDRESS,obj.getString(WHALETRUST_ADDRESS));
                                    object.put(WHALETRUST_ADDRESS_PATTERN,obj.getString(WHALETRUST_ADDRESS_PATTERN));
                                    object.put(WHALETRUST_PASSWORD,obj.getString(WHALETRUST_PASSWORD));
                                    object.put(WHALETRUST_ADDRESS_METHOD,obj.getString(WHALETRUST_ADDRESS_METHOD));
                                    object.put(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME,accountName.getText().toString());
                                    Preference.getInstance().writePreference(WHALETRUST_ADDRESS_SELECTED_ACCOUNTNAME,accountName.getText().toString());
                                }else{
                                    object.put(WHALETRUST_ADDRESS,obj.getString(WHALETRUST_ADDRESS));
                                    object.put(WHALETRUST_ADDRESS_PATTERN,obj.getString(WHALETRUST_ADDRESS_PATTERN));
                                    object.put(WHALETRUST_PASSWORD,obj.getString(WHALETRUST_PASSWORD));
                                    object.put(WHALETRUST_ADDRESS_METHOD,obj.getString(WHALETRUST_ADDRESS_METHOD));
                                }
                                updatedArray.put(object);
                            }
                            Preference.getInstance().writePreference(WHALETRUST_ADDRESS_LIST,updatedArray.toString());
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                    }else{
                        accountName.setVisibility(View.VISIBLE);
                        accountNameEdit.setVisibility(View.GONE);
                    }
                }
            }
        });
        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", Preference.getInstance().returnValue(WHALETRUST_ADDRESS));
                clipboard.setPrimaryClip(clip);
                Toast.makeText(AccountQr.this, "Your Wallet Address copied", Toast.LENGTH_SHORT).show();
            }
        });

        QRGEncoder qrgEncoder = new QRGEncoder(addressText.getText().toString(), null, QRGContents.Type.TEXT, 900);
        qrgEncoder.setColorBlack(Color.BLACK);
        qrgEncoder.setColorWhite(Color.WHITE);
        try {
            // Getting QR-Code as Bitmap
            Bitmap bitmap = qrgEncoder.getBitmap();
            // Setting Bitmap to ImageView
            qrImage.setImageBitmap(bitmap);
        } catch (Exception e) {
            Log.e("TAG", e.toString());
        }

    }
}