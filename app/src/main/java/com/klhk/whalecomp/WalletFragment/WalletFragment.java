package com.klhk.whalecomp.WalletFragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextPaint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WalletFragment.StratergyScreens.AutoCompoundActivity;
import com.klhk.whalecomp.WalletFragment.StratergyScreens.AutoFarmActivity;
import com.klhk.whalecomp.WalletFragment.StratergyScreens.CompoundActivity;
import com.klhk.whalecomp.WalletFragment.StratergyScreens.MyStratergyActivity;
import com.klhk.whalecomp.WalletFragment.StratergyScreens.StrategyInfoActivity;
import com.klhk.whalecomp.WalletFragment.StratergyScreens.StrategyStepsActivity;
import com.klhk.whalecomp.utilities.FunctionCall;
import com.klhk.whalecomp.utilities.Hex;
import com.klhk.whalecomp.utilities.Strategy001;
import com.klhk.whalecomp.utilities.service.CompounderService;
import com.klhk.whalecomp.utilities.service.Restarter;

import static com.klhk.whalecomp.utilities.Constants.BUSD_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.EPSSTAKED;
import static com.klhk.whalecomp.utilities.Constants.EPS_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.STAKED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WALLET_SELECTED_ADDRESS;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_SELECTED;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_LIST;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WalletFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WalletFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WalletFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WalletFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WalletFragment newInstance(String param1, String param2) {
        WalletFragment fragment = new WalletFragment();
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
    TextView amountText;

    TextView editText1,editText2,editText3,strInfoButton;
    ImageView navigateMyStr,navigateCompound,navigateAuto,navigateAutoCompound,navigateToStratergy;
    private CompounderService compounderService;
    Intent mServiceIntent;
    LinearLayout noStrAvailable;
    RelativeLayout epsStrategy;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wallet, container, false);
        amountText = view.findViewById(R.id.amountText);
        editText1 = view.findViewById(R.id.editText1);
        editText2 = view.findViewById(R.id.editText2);
        editText3 = view.findViewById(R.id.editText3);
        navigateMyStr = view.findViewById(R.id.navigateMyStr);
        navigateCompound = view.findViewById(R.id.navigateCompound);
        navigateAuto = view.findViewById(R.id.navigateAuto);
        navigateAutoCompound = view.findViewById(R.id.navigateAutoCompound);
        strInfoButton = view.findViewById(R.id.strInfoButton);
        noStrAvailable = view.findViewById(R.id.noStrAvailable);
        navigateToStratergy = view.findViewById(R.id.navigateToStratergy);
        epsStrategy = view.findViewById(R.id.epsStrategy);

        TextPaint paint = amountText.getPaint();
        float width = paint.measureText(amountText.getText().toString());

        Shader textShader = new LinearGradient(0, 0, width, amountText.getTextSize(),
                new int[]{
                        Color.parseColor("#0159FF"),
                        Color.parseColor("#01D1FF"),
                }, null, Shader.TileMode.CLAMP);
        amountText.getPaint().setShader(textShader);
        editText1.getPaint().setShader(textShader);
        editText2.getPaint().setShader(textShader);
        editText3.getPaint().setShader(textShader);
        navigateToStratergy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).clickStrgsScreen();
            }
        });


        strInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), StrategyInfoActivity.class));
            }
        });

        navigateMyStr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(),StrategyStepsActivity.class);
                i.putExtra("stepName","1");
                startActivity(i);
            }
        });

        navigateCompound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), StrategyStepsActivity.class));
            }
        });

        navigateAuto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AutoFarmActivity.class));
            }
        });
        navigateAutoCompound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AutoCompoundActivity.class));
            }
        });
        try {
            JSONArray array = new JSONArray();
            if(!Preference.getInstance().returnValue(STAKED_ADDRESS).isEmpty()){
                array =  new JSONArray(Preference.getInstance().returnValue(STAKED_ADDRESS));
            }else{
                noStrAvailable.setVisibility(View.VISIBLE);
                epsStrategy.setVisibility(View.INVISIBLE);
            }
            boolean hasStr =false;
            for(int i=0;i<array.length();i++){
                JSONObject object = new JSONObject(array.get(i).toString());
                if(object.get(WHALETRUST_ADDRESS_SELECTED).toString().equalsIgnoreCase(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_SELECTED))){
                    if(object.get(EPSSTAKED).toString().equalsIgnoreCase("yes")){
                        epsStrategy.setVisibility(View.VISIBLE);
                        noStrAvailable.setVisibility(View.INVISIBLE);
                        hasStr=true;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try{
                                    String amount = Strategy001.totalBalance(Preference.getInstance().returnValue(WALLET_SELECTED_ADDRESS));
                                    double amountOut = FunctionCall.getSwapAmount(EPS_ADDRESS,BUSD_ADDRESS,String.valueOf(1),String.valueOf(0.0001));
                                    ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            try{
                                                double amountUSD1 = Double.parseDouble(FunctionCall.divideBy18(Hex.hexToBigInteger((amount)))) * amountOut;
                                                amountText.setText("$"+String.format("%.2f", Double.parseDouble(String.valueOf(amountUSD1))));
                                                editText2.setText("$"+String.format("%.2f", Double.parseDouble(String.valueOf(amountUSD1))));
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
                    noStrAvailable.setVisibility(View.VISIBLE);
                    epsStrategy.setVisibility(View.INVISIBLE);
                }
            }
            if(array.length()>0){
                Log.e("Starting","service");
                try{
                    compounderService = new CompounderService();
                    mServiceIntent = new Intent(getActivity(), compounderService.getClass());
                    if (!isMyServiceRunning(compounderService.getClass())) {
                        getActivity().startService(mServiceIntent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }



        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("Refreshed","true");
        Fragment frg = null;
        frg = getActivity().getSupportFragmentManager().findFragmentByTag("WALLET_SCREEN_FRAGMNET");
        if(frg !=null && frg.isVisible()){
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
        }
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("Service status", "Running");
                return true;
            }
        }
        Log.i ("Service status", "Not running");
        return false;
    }




}