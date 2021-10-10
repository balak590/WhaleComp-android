package com.klhk.whalecomp.homefragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.sshadkany.neo;
import com.klhk.whalecomp.DocFragment.DocActivity.Desc2Activity;
import com.klhk.whalecomp.DocFragment.DocActivity.DescActivity;
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WalletFragment.StratergyScreens.StrategyInfoActivity;
import com.klhk.whalecomp.WalletFragment.StratergyScreens.StrategyStepsActivity;
import com.klhk.whalecomp.WalletFragment.WalletFragment;
import com.klhk.whalecomp.utilities.FunctionCall;
import com.klhk.whalecomp.utilities.Hex;
import com.klhk.whalecomp.utilities.Strategy001;
import com.klhk.whalecomp.utilities.service.CompounderService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;
import static com.klhk.whalecomp.utilities.Constants.APY_CALCULATED_EPS;
import static com.klhk.whalecomp.utilities.Constants.APY_CALCULATED_EPS_NON_COMPOUND;
import static com.klhk.whalecomp.utilities.Constants.BUSD_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.CAN_EXECUTE_COMPOUNDING;
import static com.klhk.whalecomp.utilities.Constants.EPSSTAKED;
import static com.klhk.whalecomp.utilities.Constants.EPS_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.NEXT_EXECUTION_TIME;
import static com.klhk.whalecomp.utilities.Constants.STAKED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.diffInHour;
import static com.klhk.whalecomp.utilities.Constants.diffInMins;
import static com.klhk.whalecomp.utilities.Constants.setupUI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView amountText,profit20TextSmall,profit140TextSmall;
    RelativeLayout epsStakingDetail;
    LinearLayout pendingTimeLayout;
    TextView pendingTime;

    neo navigateWallet,navigateWallet2,navigateDesc1,calculateButton,navigateDesc2;

    TextView top_coin_defi_dominance,top_coin_name,trading_volume_24h,defi_dominance,defi_eth_ratio,defi_marketcap,apy1Text,apy2Text,apy2TextSmall,apy1TextSmall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupUI(view,getActivity());
        amountText = view.findViewById(R.id.amountText);
        navigateDesc1 = view.findViewById(R.id.navigateDesc1);
        navigateDesc2 = view.findViewById(R.id.navigateDesc2);
        navigateWallet = view.findViewById(R.id.navigateWallet);
        navigateWallet2 = view.findViewById(R.id.navigateWallet2);
        epsStakingDetail = view.findViewById(R.id.epsStakingDetail);
        profit140TextSmall = view.findViewById(R.id.profit140TextSmall);
        profit20TextSmall = view.findViewById(R.id.profit20TextSmall);
        EditText calculateText =  view.findViewById(R.id.calculateText);
        calculateButton = view.findViewById(R.id.calculateButton);
        TextView profit140Text =  view.findViewById(R.id.profit140Text);
        TextView profit20Text = view.findViewById(R.id.profit20Text);

        defi_marketcap = view.findViewById(R.id.defi_marketcap);
        defi_eth_ratio = view.findViewById(R.id.defi_eth_ratio);
        defi_dominance = view.findViewById(R.id.defi_dominance);
        trading_volume_24h = view.findViewById(R.id.trading_volume_24h);
        top_coin_name = view.findViewById(R.id.top_coin_name);
        top_coin_defi_dominance = view.findViewById(R.id.top_coin_defi_dominance);
        apy1Text = view.findViewById(R.id.apy1Text);
        apy2Text = view.findViewById(R.id.apy2Text);
        apy1TextSmall = view.findViewById(R.id.apy1TextSmall);
        apy2TextSmall = view.findViewById(R.id.apy2TextSmall);

        pendingTime = view.findViewById(R.id.pendingTime);
        pendingTimeLayout = view.findViewById(R.id.pendingTimeLayout);

        try {
            JSONArray array = new JSONArray();
            if(!Preference.getInstance().returnValue(STAKED_ADDRESS).isEmpty()){
                array =  new JSONArray(Preference.getInstance().returnValue(STAKED_ADDRESS));
            }else{
                epsStakingDetail.setVisibility(View.GONE);
            }
            boolean hasStr =false;
            for(int i=0;i<array.length();i++){
                Log.e("updating", "amount1");
                JSONObject object = new JSONObject(array.get(i).toString());
                if(object.get(WHALETRUST_ADDRESS_SELECTED).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))){

                    if(object.get(EPSSTAKED).toString().equalsIgnoreCase("yes")){
                        epsStakingDetail.setVisibility(View.VISIBLE);
                        hasStr=true;
                        try{
                            Log.e("APY calc", String.format("%.2f", Double.parseDouble(object.get(APY_CALCULATED_EPS).toString())));
                            String apyCalc = String.format("%.2f", Double.parseDouble(object.get(APY_CALCULATED_EPS).toString()));
                            String[] apyString = apyCalc.split("\\.");


                            apy1Text.setText(apyString[0]);
                            apy1TextSmall.setText("."+apyString[1]);
                            profit140TextSmall.setText("("+apyString[0]+"."+apyString[1]+"%)");
                            Log.e("APY calc No", String.format("%.2f", Double.parseDouble(object.get(APY_CALCULATED_EPS_NON_COMPOUND).toString())));
                            String apyNonCompCalc = String.format("%.2f", Double.parseDouble(object.get(APY_CALCULATED_EPS_NON_COMPOUND).toString()));
                            String[] apyNonCompString = apyNonCompCalc.split("\\.");


                            apy2Text.setText(apyNonCompString[0]);
                            apy2TextSmall.setText("."+apyNonCompString[1]);
                            profit20TextSmall.setText("("+apyNonCompString[0]+"."+apyNonCompString[1]+"%)");
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        try{
                            if(object.getBoolean(CAN_EXECUTE_COMPOUNDING)){
//                                複利滾動
                                pendingTime.setText("複利滾動");
                                navigateWallet2.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        ViewGroup viewGroupC1 = v.findViewById(R.id.navigateWallet2);
                                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                                        try{
                                            if (navigateWallet2.isShapeContainsPoint(event.getX(), event.getY())) {
                                                switch (event.getAction()) {
                                                    case MotionEvent.ACTION_DOWN:
                                                        navigateWallet2.setStyle(neo.small_inner_shadow);
//                                textView.setScaleX(textView.getScaleX() * 0.95f);
//                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                                        return true; // if you want to handle the touch event

                                                    case MotionEvent.ACTION_UP:
                                                    case MotionEvent.ACTION_CANCEL:
                                                        // RELEASED
                                                        navigateWallet2.setStyle(neo.drop_shadow);
                                                        textView.setScaleX(1);
                                                        textView.setScaleY(1);
                                                        Thread.sleep(80);
                                                        Intent i = new Intent(getContext(), StrategyStepsActivity.class);
                                                        i.putExtra("stepName","1");
                                                        startActivity(i);
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
                            }else{
                                Date date = new Date(Long.parseLong(object.get(NEXT_EXECUTION_TIME).toString()));
                                Date date2 = new Date(System.currentTimeMillis());
                                long millis = date.getTime() - date2.getTime();
                                int diffInhr = (int) (millis / (1000 * 60 * 60));
                                int diffInmin = (int) ((millis / (1000 * 60)) % 60);
                                pendingTime.setText("下次滾動操作預計在"+String.valueOf(diffInhr)+"小時"+String.valueOf(diffInmin)+"分鐘后");
                                pendingTime.setTextColor(getResources().getColor(R.color.bg_color));
                                pendingTimeLayout.setBackground(getResources().getDrawable(R.drawable.grey_gradient));
                                navigateWallet2.setDark_color(getResources().getColor(R.color.dark_shadow));
                                navigateWallet2.setOnTouchListener(new View.OnTouchListener() {
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event) {
                                        ViewGroup viewGroupC1 = v.findViewById(R.id.navigateWallet2);
                                        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                                        try{
                                            if (navigateWallet2.isShapeContainsPoint(event.getX(), event.getY())) {
                                                switch (event.getAction()) {
                                                    case MotionEvent.ACTION_DOWN:

                                                        navigateWallet2.setStyle(neo.small_inner_shadow);
//                                                        textView.setScaleX(textView.getScaleX() * 0.95f);
//                                                        textView.setScaleY(textView.getScaleY() * 0.95f);
                                                        return true; // if you want to handle the touch event
                                                    case MotionEvent.ACTION_UP:
                                                    case MotionEvent.ACTION_CANCEL:
                                                        // RELEASED
                                                        navigateWallet2.setStyle(neo.drop_shadow);
                                                        textView.setScaleX(1);
                                                        textView.setScaleY(1);
                                                        Thread.sleep(40);
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

                            }


                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    Log.e("updating", "amount");
                                    String amount = Strategy001.totalBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));
                                    double amountOut = FunctionCall.getSwapAmount(EPS_ADDRESS,BUSD_ADDRESS,String.valueOf(1),String.valueOf(0.0001));
                                    (getActivity()).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                double amountUSD1 = Double.parseDouble(FunctionCall.divideBy18(Hex.hexToBigInteger((amount)))) * amountOut;
                                                amountText.setText("$"+String.format("%.2f", Double.parseDouble(String.valueOf(amountUSD1))));



                                            }catch (Exception e){
                                                e.printStackTrace();
                                            }

                                        }
                                    });
                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                            }
                        }).start();
                        break;
                    }

                }
                if(!hasStr){
                    epsStakingDetail.setVisibility(View.GONE);
                }
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://api.coingecko.com/api/v3/global/decentralized_finance_defi";
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

                            if(jsonObject.getString("data")!=null){
                                JSONObject dataObj = new JSONObject(jsonObject.getString("data"));
                                NumberFormat format = NumberFormat.getCurrencyInstance();
                                format.setMaximumFractionDigits(0);
                                format.setCurrency(Currency.getInstance("USD"));
                                defi_marketcap.setText("$"+currencyFormat(String.format("%.2f", Double.parseDouble(dataObj.getString("defi_market_cap")))));
                                defi_eth_ratio.setText(String.format("%.2f", Double.parseDouble(dataObj.getString("defi_to_eth_ratio")))+ " %");
                                defi_dominance.setText(String.format("%.2f", Double.parseDouble(dataObj.getString("defi_dominance")))+ " %");
                                trading_volume_24h.setText("$"+currencyFormat(String.format("%.2f", Double.parseDouble(dataObj.getString("trading_volume_24h")))));
                                top_coin_name.setText(dataObj.getString("top_coin_name"));
                                top_coin_defi_dominance.setText(String.format("%.2f", Double.parseDouble(dataObj.getString("top_coin_defi_dominance")))+ " %");
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

        calculateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.calculateButton);
                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                try{
                    if (calculateButton.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                calculateButton.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                calculateButton.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(60);
                                if(!calculateText.getText().toString().trim().isEmpty()){
                                    int amount = Integer.parseInt(calculateText.getText().toString());
                                    double profit20 = (amount*Double.parseDouble(apy1Text.getText().toString()+apy1TextSmall.getText().toString())/100);
                                    double profit140 = (amount*Double.parseDouble(apy2Text.getText().toString()+apy2TextSmall.getText().toString())/100);

                                    profit140Text.setText("$"+String.format("%.2f",profit140));
                                    profit20Text.setText("$"+String.format("%.2f",profit20));
                                    try {
                                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                                    } catch (Exception e) {
                                        // TODO: handle exception
                                    }
                                }
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


        TextPaint paint = amountText.getPaint();
        float width = paint.measureText(amountText.getText().toString());

        Shader textShader = new LinearGradient(0, 0, width, amountText.getTextSize(),
                new int[]{
                        Color.parseColor("#0159FF"),
                        Color.parseColor("#01D1FF"),
                }, null, Shader.TileMode.CLAMP);
        amountText.getPaint().setShader(textShader);

        navigateDesc1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.navigateDesc1);
                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                try{
                    if (navigateDesc1.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                navigateDesc1.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                navigateDesc1.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(80);
                                startActivity(new Intent(getContext(), DescActivity.class));
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

        navigateDesc2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.navigateDesc2);
                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                try{
                    if (navigateDesc2.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                navigateDesc2.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                navigateDesc2.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(80);
                                startActivity(new Intent(getContext(), Desc2Activity.class));
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

        navigateWallet.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.navigateWallet);
                final TextView textView = (TextView) viewGroupC1.getChildAt(0);
                try{
                    if (navigateWallet.isShapeContainsPoint(event.getX(), event.getY())) {
                        switch (event.getAction()) {
                            case MotionEvent.ACTION_DOWN:

                                navigateWallet.setStyle(neo.small_inner_shadow);
                                textView.setScaleX(textView.getScaleX() * 0.95f);
                                textView.setScaleY(textView.getScaleY() * 0.95f);
                                return true; // if you want to handle the touch event
                            case MotionEvent.ACTION_UP:
                            case MotionEvent.ACTION_CANCEL:
                                // RELEASED
                                navigateWallet.setStyle(neo.drop_shadow);
                                textView.setScaleX(1);
                                textView.setScaleY(1);
                                Thread.sleep(80);
                                startActivity(new Intent(getContext(), StrategyInfoActivity.class));
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





        return view;
    }

    public static String currencyFormat(String amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(Double.parseDouble(amount));
    }
}