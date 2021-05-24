package com.klhk.whalecomp.GiftFragment;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.klhk.whalecomp.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GiftFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GiftFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GiftFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GiftFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GiftFragment newInstance(String param1, String param2) {
        GiftFragment fragment = new GiftFragment();
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

    TextView amountText,text1,text2,text3,text4;
    RelativeLayout giftlayout1,giftlayout2,giftlayout3;
    ImageView giftImage1,giftImage2,giftImage3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gift, container, false);
        amountText = view.findViewById(R.id.amountText);
        text1 = view.findViewById(R.id.text1);
        text2 = view.findViewById(R.id.text2);
        text3 = view.findViewById(R.id.text3);
        text4 = view.findViewById(R.id.text4);
        giftlayout1 = view.findViewById(R.id.giftlayout1);
        giftlayout2 = view.findViewById(R.id.giftlayout2);
        giftlayout3 = view.findViewById(R.id.giftlayout3);

        giftImage1 = view.findViewById(R.id.giftImage1);
        giftImage2 = view.findViewById(R.id.giftImage2);
        giftImage3 = view.findViewById(R.id.giftImage3);

        TextPaint paint = amountText.getPaint();
        float width = paint.measureText(text1.getText().toString());

        Shader textShader = new LinearGradient(0, 0, width, amountText.getTextSize(),
                new int[]{
                        Color.parseColor("#FF5C00"),
                        Color.parseColor("#FF7A00"),
                        Color.parseColor("#FFC600"),
                }, null, Shader.TileMode.CLAMP);
        amountText.getPaint().setShader(textShader);
        text1.getPaint().setShader(textShader);
        text2.getPaint().setShader(textShader);
        text3.getPaint().setShader(textShader);
        text4.getPaint().setShader(textShader);

        giftlayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giftImage1.setImageDrawable(getResources().getDrawable(R.drawable.gift_card_yellow_glow));
                giftImage2.setImageDrawable(getResources().getDrawable(R.drawable.gift_card_white));
                giftImage3.setImageDrawable(getResources().getDrawable(R.drawable.gift_card_white));
            }
        });
        giftlayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giftImage1.setImageDrawable(getResources().getDrawable(R.drawable.gift_card_white));
                giftImage2.setImageDrawable(getResources().getDrawable(R.drawable.gift_card_yellow_glow));
                giftImage3.setImageDrawable(getResources().getDrawable(R.drawable.gift_card_white));
            }
        });
        giftlayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                giftImage1.setImageDrawable(getResources().getDrawable(R.drawable.gift_card_white));
                giftImage2.setImageDrawable(getResources().getDrawable(R.drawable.gift_card_white));
                giftImage3.setImageDrawable(getResources().getDrawable(R.drawable.gift_card_yellow_glow));
            }
        });


        return view;
    }
}