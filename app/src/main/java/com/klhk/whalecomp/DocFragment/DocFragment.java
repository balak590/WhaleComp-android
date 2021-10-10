package com.klhk.whalecomp.DocFragment;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.klhk.whalecomp.DocFragment.DocActivity.DescActivity;
import com.klhk.whalecomp.MainActivity;
import com.klhk.whalecomp.R;

import static com.klhk.whalecomp.utilities.Constants.setupUI;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DocFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DocFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DocFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DocFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DocFragment newInstance(String param1, String param2) {
        DocFragment fragment = new DocFragment();
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

    ImageView eps_stake_doc,cake_stake_card;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doc, container, false);
        setupUI(view,getActivity());
        eps_stake_doc=view.findViewById(R.id.eps_stake_doc);
        cake_stake_card = view.findViewById(R.id.cake_stake_card);


        eps_stake_doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getContext(), DescActivity.class),4001);
            }
        });
        cake_stake_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), DescActivity.class));
            }
        });
        return  view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==4001&&resultCode==RESULT_OK){
            ((MainActivity)getActivity()).clickMyStrgsScreen();
        }
    }
}