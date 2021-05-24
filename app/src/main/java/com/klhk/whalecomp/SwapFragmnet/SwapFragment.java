package com.klhk.whalecomp.SwapFragmnet;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WalletFragment.StratergyScreens.CompoundActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwapFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SwapFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SwapFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SwapFragment newInstance(String param1, String param2) {
        SwapFragment fragment = new SwapFragment();
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

    TextView token2PickLayout,token1PickLayout,token1Text,token2Text;
    ImageView token1Img,token2Img;
    LinearLayout token1Layout,token2Layout;
    String toke1,toke2;

    RelativeLayout swapSummery,swapSummery2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_swap, container, false);
        token2PickLayout = view.findViewById(R.id.token2PickLayout);
        token1PickLayout = view.findViewById(R.id.token1PickLayout);
        token1Text = view.findViewById(R.id.token1Text);
        token2Text = view.findViewById(R.id.token2Text);
        token1Img = view.findViewById(R.id.token1Img);
        token2Img = view.findViewById(R.id.token2Img);
        token1Layout = view.findViewById(R.id.token1Layout);
        token2Layout = view.findViewById(R.id.token2Layout);
        toke1="";
        toke2="";
        swapSummery = view.findViewById(R.id.swapSummery);
        swapSummery2 = view.findViewById(R.id.swapSummery2);

        token1PickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialog);

                View v = getLayoutInflater().inflate(R.layout.pick_token,null);
                ImageView bnbToken = v.findViewById(R.id.bnbToken);
                ImageView btcToken = v.findViewById(R.id.btcToken);
                ImageView ethToken = v.findViewById(R.id.ethToken);
                ImageView usdcToken = v.findViewById(R.id.usdcToken);
                ImageView usdtToken = v.findViewById(R.id.usdtToken);
                ImageView busdToken = v.findViewById(R.id.busdToken);
                ImageView closeButton = v.findViewById(R.id.closeButton);
                builder.setCancelable(false);

                builder.setView(v);
                AlertDialog dialog = builder.show();

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });



                bnbToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke1="bnb";
                        setToken1();
                        dialog.cancel();
                    }
                });
                btcToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke1="btc";
                        setToken1();
                        dialog.cancel();
                    }
                });
                ethToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke1="eth";
                        setToken1();
                        dialog.cancel();
                    }
                });
                usdcToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke1="usdc";
                        setToken1();
                        dialog.cancel();
                    }
                });
                usdtToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke1="usdt";
                        setToken1();
                        dialog.cancel();
                    }
                });
                busdToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke1="busd";
                        setToken1();
                        dialog.cancel();
                    }
                });
            }
        });
        token2PickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext(),R.style.CustomDialog);

                View v = getLayoutInflater().inflate(R.layout.pick_token,null);
                ImageView bnbToken = v.findViewById(R.id.bnbToken);
                ImageView btcToken = v.findViewById(R.id.btcToken);
                ImageView ethToken = v.findViewById(R.id.ethToken);
                ImageView usdcToken = v.findViewById(R.id.usdcToken);
                ImageView usdtToken = v.findViewById(R.id.usdtToken);
                ImageView busdToken = v.findViewById(R.id.busdToken);
                ImageView closeButton = v.findViewById(R.id.closeButton);
                builder.setCancelable(false);

                builder.setView(v);
                AlertDialog dialog = builder.show();

                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.cancel();
                    }
                });



                bnbToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke2="bnb";
                        setToken2();
                        dialog.cancel();
                    }
                });
                btcToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke2="btc";
                        setToken2();
                        dialog.cancel();
                    }
                });
                ethToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke2="eth";
                        setToken2();
                        dialog.cancel();
                    }
                });
                usdcToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke2="usdc";
                        setToken2();
                        dialog.cancel();
                    }
                });
                usdtToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke2="usdt";
                        setToken2();
                        dialog.cancel();
                    }
                });
                busdToken.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        toke2="busd";
                        setToken2();
                        dialog.cancel();
                    }
                });
            }
        });

        return view;
    }

    public void setToken1(){
        token1PickLayout.setText("");
        token1Layout.setVisibility(View.VISIBLE);
//        token1Text.setVisibility(View.VISIBLE);
        if(toke1.isEmpty()){
            token1PickLayout.setText("選擇幣種");
            token1Layout.setVisibility(View.GONE);

        }else if(toke1.equalsIgnoreCase("bnb")){
            token1Text.setText("BNB");
            token1Img.setImageDrawable(getResources().getDrawable(R.drawable.bnb_token));
        }else if(toke1.equalsIgnoreCase("btc")){
            token1Text.setText("BTC");
            token1Img.setImageDrawable(getResources().getDrawable(R.drawable.btc_token));
        }else if(toke1.equalsIgnoreCase("usdc")){
            token1Text.setText("USDC");
            token1Img.setImageDrawable(getResources().getDrawable(R.drawable.usdc_token));
        }else if(toke1.equalsIgnoreCase("usdt")){
            token1Text.setText("USDT");
            token1Img.setImageDrawable(getResources().getDrawable(R.drawable.usdt_token));
        }else if(toke1.equalsIgnoreCase("eth")){
            token1Text.setText("ETH");
            token1Img.setImageDrawable(getResources().getDrawable(R.drawable.eth_token));
        }else if(toke1.equalsIgnoreCase("busd")){
            token1Text.setText("BUSD");
            token1Img.setImageDrawable(getResources().getDrawable(R.drawable.busd_token));
        }

        swapSummery.setVisibility(View.VISIBLE);
        swapSummery2.setVisibility(View.VISIBLE);
    }
    public void setToken2(){
        token2PickLayout.setText("");
        token2Layout.setVisibility(View.VISIBLE);

        if(toke2.isEmpty()){
            token2PickLayout.setText("選擇幣種");
            token2Layout.setVisibility(View.GONE);

        }else if(toke2.equalsIgnoreCase("bnb")){
            token2Text.setText("BNB");
            token2Img.setImageDrawable(getResources().getDrawable(R.drawable.bnb_token));
        }else if(toke2.equalsIgnoreCase("btc")){
            token2Text.setText("BTC");
            token2Img.setImageDrawable(getResources().getDrawable(R.drawable.btc_token));
        }else if(toke2.equalsIgnoreCase("usdc")){
            token2Text.setText("USDC");
            token2Img.setImageDrawable(getResources().getDrawable(R.drawable.usdc_token));
        }else if(toke2.equalsIgnoreCase("usdt")){
            token2Text.setText("USDT");
            token2Img.setImageDrawable(getResources().getDrawable(R.drawable.usdt_token));
        }else if(toke2.equalsIgnoreCase("eth")){
            token2Text.setText("ETH");
            token2Img.setImageDrawable(getResources().getDrawable(R.drawable.eth_token));
        }else if(toke2.equalsIgnoreCase("busd")){
            token2Text.setText("BUSD");
            token2Img.setImageDrawable(getResources().getDrawable(R.drawable.busd_token));
        }
        swapSummery.setVisibility(View.VISIBLE);
        swapSummery2.setVisibility(View.VISIBLE);
    }
}