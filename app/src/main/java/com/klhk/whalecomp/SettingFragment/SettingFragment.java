package com.klhk.whalecomp.SettingFragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.sshadkany.neo;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.WhaleTrust.CreateMnemonicActivity;
import com.klhk.whalecomp.WhaleTrust.ImportAddressActivity;

import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_ADDRESS_LIST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
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

    LinearLayout whaleTrustAvailable,whaleTrustNotAvailable;

    TextView appVerison;
    neo showQRButton,changeAccountButton,showMnemonicButton,changeSlippageButton,changeDurationButton,passwordSettingButton,
            authManagementButton,notificationButton,changeNetworkButton,changaLanguageButton,communityButton,updateButton;
    ImageView newAccountButton,importAccountButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_setting, container, false);
        appVerison = view.findViewById(R.id.appVerison);

        showQRButton = view.findViewById(R.id.showQRButton);
        changeAccountButton = view.findViewById(R.id.changeAccountButton);
        showMnemonicButton = view.findViewById(R.id.showMnemonicButton);
        changeSlippageButton = view.findViewById(R.id.changeSlippageButton);
        changeDurationButton = view.findViewById(R.id.changeDurationButton);
        passwordSettingButton = view.findViewById(R.id.passwordSettingButton);
        authManagementButton = view.findViewById(R.id.authManagementButton);
        notificationButton = view.findViewById(R.id.notificationButton);
        changeNetworkButton = view.findViewById(R.id.changeNetworkButton);
        changaLanguageButton = view.findViewById(R.id.changaLanguageButton);
        communityButton = view.findViewById(R.id.communityButton);
        whaleTrustNotAvailable = view.findViewById(R.id.whaleTrustNotAvailable);
        whaleTrustAvailable = view.findViewById(R.id.whaleTrustAvailable);
        importAccountButton = view.findViewById(R.id.importAccountButton);
        newAccountButton = view.findViewById(R.id.newAccountButton);
        updateButton = view.findViewById(R.id.updateButton);

        if(Preference.getInstance().returnValue(WHALETRUST_ADDRESS_LIST).isEmpty()){
            whaleTrustNotAvailable.setVisibility(View.VISIBLE);
            whaleTrustAvailable.setVisibility(View.GONE);
        }else{
            whaleTrustNotAvailable.setVisibility(View.GONE);
            whaleTrustAvailable.setVisibility(View.VISIBLE);
        }

        updateButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.updateButton);
                return setLayout(new Intent(getActivity(), UpdateActivity.class),event,viewGroupC1,updateButton);
            }
        });



        newAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CreateMnemonicActivity.class));
            }
        });
        importAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ImportAddressActivity.class));
            }
        });

        showQRButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.showQRButton);
                return setLayout(new Intent(getActivity(), AccountQr.class),event,viewGroupC1,showQRButton);
            }
        });


        changeAccountButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.changeAccountButton);
                return setLayout(new Intent(getActivity(), PickAddressActivity.class),event,viewGroupC1,changeAccountButton);
            }
        });

        showMnemonicButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.showMnemonicButton);
                return setLayout(new Intent(getActivity(), MnemonicDescActivity.class),event,viewGroupC1,showMnemonicButton);
            }
        });

        changeSlippageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.changeSlippageButton);
                return setLayout(new Intent(getActivity(), DefaultSlippageActivity.class),event,viewGroupC1,changeSlippageButton);
            }
        });

        changeDurationButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.changeDurationButton);
                return setLayout(new Intent(getActivity(), DefaultTimeoutActivity.class),event,viewGroupC1,changeDurationButton);
            }
        });

        passwordSettingButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.passwordSettingButton);
                return setLayout(new Intent(getActivity(), PasswordSettingActivity.class),event,viewGroupC1,passwordSettingButton);
            }
        });

        authManagementButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.authManagementButton);
                return setLayout(new Intent(getActivity(), AuthManagementActivity.class),event,viewGroupC1,authManagementButton);
            }
        });

        notificationButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.notificationButton);
                return setLayout(new Intent(getActivity(), NotificationManagemntActivity.class),event,viewGroupC1,notificationButton);
            }
        });

        changeNetworkButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.changeNetworkButton);
                return setLayout(new Intent(getActivity(), ChangeSmartChainActivity.class),event,viewGroupC1,changeNetworkButton);
            }
        });

        changaLanguageButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.changaLanguageButton);
                return setLayout(new Intent(getActivity(), ChangeLanguageActivity.class),event,viewGroupC1,changaLanguageButton);
            }
        });

        communityButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                ViewGroup viewGroupC1 = v.findViewById(R.id.communityButton);
                return setLayout(new Intent(getActivity(), SocialMediaActivity.class),event,viewGroupC1,communityButton);
            }
        });



        try {
            PackageInfo pInfo = getContext().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            String version = pInfo.versionName;
            appVerison.setText("版本:"+version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return view;
    }

    public boolean setLayout(Intent intent,MotionEvent event,ViewGroup viewGroupC1,neo button){

        final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
        try{
            if (button.isShapeContainsPoint(event.getX(), event.getY())) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        button.setStyle(neo.small_inner_shadow);
                        textView.setScaleX(textView.getScaleX() * 0.98f);
                        textView.setScaleY(textView.getScaleY() * 0.98f);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // RELEASED
                        button.setStyle(neo.drop_shadow);
                        textView.setScaleX(1);
                        textView.setScaleY(1);
                        Thread.sleep(100);
                        startActivity(intent);
                        return true; // if you want to handle the touch event
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return false;
    }
}