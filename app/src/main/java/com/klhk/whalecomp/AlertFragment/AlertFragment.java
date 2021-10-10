package com.klhk.whalecomp.AlertFragment;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.sshadkany.neo;
import com.klhk.whalecomp.Preference;
import com.klhk.whalecomp.R;
import com.klhk.whalecomp.utilities.AppController;
import com.klhk.whalecomp.utilities.MyJobService;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.klhk.whalecomp.utilities.Constants.WHALECOMP_ALERT_LIST;
import static com.klhk.whalecomp.utilities.Constants.WHALETRUST_TOKEN_LIST;
import static com.klhk.whalecomp.utilities.Constants.setupUI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlertFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlertFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlertFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AlertFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlertFragment newInstance(String param1, String param2) {
        AlertFragment fragment = new AlertFragment();
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

    ImageView addButton;
    LinearLayout alertItems;
    public final static  int RESULT_FINISHED_ACTIVITY = 999;
    TextView noAlertsText;


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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_alert, container, false);
        setupUI(view,getActivity());
        addButton = view.findViewById(R.id.addButton);
        alertItems = view.findViewById(R.id.alertItems);
        noAlertsText = view.findViewById(R.id.noAlertsText);



        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(!Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST).toString().isEmpty()) {
                        JSONArray array = new JSONArray(Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST));
                        if(array.length()!=5){
                            getActivity().startActivityForResult(new Intent(getContext(),AlertSetupActivity.class), RESULT_FINISHED_ACTIVITY);
                        }else{
                            Toast.makeText(getContext(), "You have reached maximum alerts! please upgrade.", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        getActivity().startActivityForResult(new Intent(getContext(),AlertSetupActivity.class), RESULT_FINISHED_ACTIVITY);
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        try {
            if(!Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST).toString().isEmpty()){



                JSONArray array = new JSONArray(Preference.getInstance().returnValue(WHALECOMP_ALERT_LIST));

                if(alertItems.getChildCount()>0){
                    alertItems.removeAllViews();
                }

                if(array.length()>0){
                    noAlertsText.setVisibility(View.GONE);
                    for(int i=0;i<array.length();i++){
                        View view1 = getLayoutInflater().inflate(R.layout.individual_alerts,null,false);
                        TextView tokenSymbol = view1.findViewById(R.id.tokenSymbol);
                        TextView alertType = view1.findViewById(R.id.alertType);
                        TextView percentText = view1.findViewById(R.id.percentText);
                        neo alertTypeButton = view1.findViewById(R.id.alertTypeButton);
                        ImageView iconSymbol = view1.findViewById(R.id.iconSymbol);
                        JSONObject object = new JSONObject(array.getJSONObject(i).toString());

                        Picasso.get().load(object.getString("icon")).into(iconSymbol);

                        tokenSymbol.setText(object.getString("symbol"));
                        if(object.getInt("condition")==1){
                            alertType.setText("價格超過");
                            percentText.setText(object.getString("cost") + " USD");
                        }else if(object.getInt("condition")==2){
                            alertType.setText("價格低於");
                            percentText.setText(object.getString("cost") + " USD");
                        }
                        else if(object.getInt("condition")==3){
                            alertType.setText("漲幅達到");
                            percentText.setText(object.getString("percent") + " %");
                        }else if(object.getInt("condition")==4){
                            alertType.setText("跌幅達到");
                            percentText.setText(object.getString("percent") + " %");
                        }

                        alertTypeButton.setOnTouchListener(new View.OnTouchListener() {
                            @Override
                            public boolean onTouch(View v, MotionEvent event) {
                                ViewGroup viewGroupC1 = v.findViewById(R.id.alertTypeButton);
                                final LinearLayout textView = (LinearLayout) viewGroupC1.getChildAt(0);
                                try{
                                    if (alertTypeButton.isShapeContainsPoint(event.getX(), event.getY())) {
                                        switch (event.getAction()) {
                                            case MotionEvent.ACTION_DOWN:

                                                alertTypeButton.setStyle(neo.small_inner_shadow);
                                                textView.setScaleX(textView.getScaleX() * 0.98f);
                                                textView.setScaleY(textView.getScaleY() * 0.98f);
                                                return true; // if you want to handle the touch event
                                            case MotionEvent.ACTION_UP:
                                            case MotionEvent.ACTION_CANCEL:
                                                // RELEASED
                                                alertTypeButton.setStyle(neo.drop_shadow);
                                                textView.setScaleX(1);
                                                textView.setScaleY(1);
                                                Thread.sleep(100);
                                                Intent i = new Intent(getContext(),AlertSetupActivity.class);
                                                i.putExtra("data",object.toString());
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

                        alertItems.addView(view1);
                    }
                }else{
                    noAlertsText.setVisibility(View.VISIBLE);
                }

                try{

                    MyJobService myJobService = new MyJobService();
                    Intent mServiceIntent = new Intent(getActivity(), myJobService.getClass());
                    if (!isMyServiceRunning(myJobService.getClass())) {
                        getActivity().startService(mServiceIntent);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }else{
                noAlertsText.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        Log.e("token",Preference.getInstance().returnValue("messaging_token"));
//
//
//        String tag_json_obj = "json_obj_req";
//        String url = "http://api.whalecomp.com/getListAlerts";
//        Map<String, String> params = new HashMap<String, String>();
//
//        params.put("token", Preference.getInstance().returnValue("messaging_token").trim());
//        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
//                url, new JSONObject(params),
//                new Response.Listener<JSONObject>() {
//
//                    @Override
//                    public void onResponse(JSONObject response) {
//                        Log.e("TAG", response.toString());
//                        try {
//                            if(response.getString("code").equalsIgnoreCase("0")){
//                                JSONArray array = new JSONArray(response.getString("data"));
//
//                                if(alertItems.getChildCount()>0){
//                                    alertItems.removeAllViews();
//                                }
//
//                                if(array.length()>0){
//                                    noAlertsText.setVisibility(View.GONE);
//                                    for(int i=0;i<array.length();i++){
//                                        View view1 = getLayoutInflater().inflate(R.layout.individual_alerts,null,false);
//                                        TextView tokenSymbol = view1.findViewById(R.id.tokenSymbol);
//                                        TextView alertType = view1.findViewById(R.id.alertType);
//                                        TextView percentText = view1.findViewById(R.id.percentText);
//                                        ImageView alertTypeButton = view1.findViewById(R.id.alertTypeButton);
//                                        JSONObject object = new JSONObject(array.getJSONObject(i).toString());
//
//                                        tokenSymbol.setText(object.getString("symbol"));
//                                        if(object.getInt("condition")==1){
//                                            alertType.setText("價格等於");
//                                            percentText.setText(object.getString("cost") + " USD");
//                                        }else if(object.getInt("condition")==2){
//                                            alertType.setText("漲幅達到");
//                                            percentText.setText(object.getString("percent") + " %");
//                                        }else if(object.getInt("condition")==3){
//                                            alertType.setText("跌幅達到");
//                                            percentText.setText(object.getString("percent") + " %");
//                                        }
//
//                                        alertTypeButton.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                Intent i = new Intent(getContext(),AlertSetupActivity.class);
//                                                i.putExtra("data",object.toString());
//                                                startActivity(i);
//                                            }
//                                        });
//                                        alertItems.addView(view1);
//                                    }
//                                }else{
//                                    noAlertsText.setVisibility(View.VISIBLE);
//                                }
//
//                            }
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }, new Response.ErrorListener() {
//
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                VolleyLog.d("Error:", error.getMessage());
//                //pDialog.hide();
//            }
//        });
//        //mRequestQueue.add(jsonObjReq);
//        jsonObjReq.setShouldCache(false);
//        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);


        return view;
    }
}