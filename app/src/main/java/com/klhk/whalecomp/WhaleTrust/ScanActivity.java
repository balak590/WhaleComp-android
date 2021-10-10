package com.klhk.whalecomp.WhaleTrust;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.klhk.whalecomp.R;

import java.util.ArrayList;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private static final int PERMISSION_REQUESTS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        mScannerView = findViewById(R.id.scannerView);
        if (allPermissionsGranted()) {
            //createCameraSource();
            mScannerView.startCamera();
        } else {
            getRuntimePermissions();
        }
    }

    public void zingCamera(){
        mScannerView.setResultHandler((ZXingScannerView.ResultHandler) this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onResume() {
        super.onResume();
        zingCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result result) {
        BarcodeFormat format = result.getBarcodeFormat();

        Log.e("format",result.getText());


        try{
            String confirmURL = "ethereum";
            String url = result.getText();
            if(url.contains(confirmURL)){
                Intent returnIntent = new Intent();
                String[] split = url.split(":");
                Log.e("Address",split[1]);
                returnIntent.putExtra("address",split[1]);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }else{
                zingCamera();
                Toast.makeText(ScanActivity.this, "This is not a valid Address", Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            zingCamera();
            Toast.makeText(ScanActivity.this, "This is not a valid Address", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean allPermissionsGranted() {
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(getApplicationContext(), permission)) {
                return false;
            }
        }
        return true;
    }
    private void getRuntimePermissions() {
        List allNeededPermissions = new ArrayList<>();
        for (String permission : getRequiredPermissions()) {
            if (!isPermissionGranted(getApplicationContext(), permission)) {
                allNeededPermissions.add(permission);
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    ScanActivity.this, (String[]) allNeededPermissions.toArray(new String[0]), PERMISSION_REQUESTS);
        }
    }
    private String[] getRequiredPermissions() {
        try {
            PackageInfo info =
                    getApplication().getPackageManager()
                            .getPackageInfo(getApplicationContext().getPackageName(), PackageManager.GET_PERMISSIONS);
            String[] ps = info.requestedPermissions;
            if (ps != null && ps.length > 0) {
                return ps;
            } else {
                return new String[0];
            }
        } catch (Exception e) {
            return new String[0];
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, String[] permissions, int[] grantResults) {
        if (allPermissionsGranted()) {
            mScannerView.startCamera();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static boolean isPermissionGranted(Context context, String permission) {
        if (ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }
}