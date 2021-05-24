package com.klhk.whalecomp.homefragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.klhk.whalecomp.DocFragment.DocActivity.Desc2Activity;
import com.klhk.whalecomp.DocFragment.DocActivity.DescActivity;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WalletFragment.WalletFragment;

import java.util.Locale;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static androidx.core.content.ContextCompat.getSystemService;

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

    TextView amountText;
    RelativeLayout navigateDesc1,navigateWallet;
    ImageView navigateDesc2,navigateWallet2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        amountText = view.findViewById(R.id.amountText);
        navigateDesc1 = view.findViewById(R.id.navigateDesc1);
        navigateDesc2 = view.findViewById(R.id.navigateDesc2);
        navigateWallet = view.findViewById(R.id.navigateWallet);
        navigateWallet2 = view.findViewById(R.id.navigateWallet2);

        EditText calculateText =  view.findViewById(R.id.calculateText);
        ImageView calculateButton = view.findViewById(R.id.calculateButton);
        TextView profit140Text =  view.findViewById(R.id.profit140Text);
        TextView profit20Text = view.findViewById(R.id.profit20Text);


        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!calculateText.getText().toString().trim().isEmpty()){
                    int amount = Integer.parseInt(calculateText.getText().toString());
                    double profit20 = amount+(amount*.2);
                    double profit140 = amount+(amount*1.4);

                    profit140Text.setText("$"+String.format("%.2f",profit140));
                    profit20Text.setText("$"+String.format("%.2f",profit20));
                    try {
                        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }
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

        navigateDesc1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), DescActivity.class));
            }
        });
        navigateDesc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), Desc2Activity.class));
            }
        });
        navigateWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WalletFragment fragment = new WalletFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            }
        });
        navigateWallet2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WalletFragment fragment = new WalletFragment();
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
            }
        });


        return view;
    }
}