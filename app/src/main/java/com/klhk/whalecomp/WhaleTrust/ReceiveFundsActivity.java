package com.klhk.whalecomp.WhaleTrust;

import static com.klhk.whalecomp.utilities.Constants.roundButtonPressed;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class ReceiveFundsActivity extends AppCompatActivity {

    ImageView qrImage,copyButton;
    TextView addressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_funds);
        qrImage = findViewById(R.id.qrImage);
        copyButton = findViewById(R.id.copyButton);
        addressText = findViewById(R.id.addressText);
        addressText.setText(Preference.getInstance().returnValue("userWalletAddress.whaleTrust"));
        neo backButton = findViewById(R.id.backButton);
        ViewGroup viewGroupC1 = findViewById(R.id.backButton);
        final ImageView imageviewC1 = (ImageView) viewGroupC1.getChildAt(0);
        roundButtonPressed(backButton,imageviewC1,this);

        copyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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