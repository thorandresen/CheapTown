package com.example.dhor.billigbytur.Fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;


import com.example.dhor.billigbytur.Activities.AddBarActivity;
import com.example.dhor.billigbytur.Activities.AddOfferActivity;
import com.example.dhor.billigbytur.Activities.HomeScreenActivity;
import com.example.dhor.billigbytur.ItemHolders.BarInformation;
import com.example.dhor.billigbytur.ItemHolders.ItemInformation;
import com.example.dhor.billigbytur.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.local.QueryData;

import java.util.HashMap;
import java.util.Map;

public class BarInfoFragment extends Fragment {
    // Debug
    private static final String TAG = "BarInfoFragment";
    // Variables
    private View mView;
    private BarInformation mBarInformation;
    private int mCount;
    private Toolbar mToolbar;
    private TextView mName;
    private TextView mAddress;
    private FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_bar_info, container, false);

        // Init variables.
        mName = mView.findViewById(R.id.barName);
        mAddress = mView.findViewById(R.id.address);
        mToolbar = mView.findViewById(R.id.toolbarId);
        fab = mView.findViewById(R.id.fab_bar_info);
        // Methods
        toolbarOnClickListener();
        setFabListener();
        overrideBackButton();

        // Inits
        mCount = getArguments().getInt("posArg"); // Gets arguments from baradapter to retrieve info in recyclerview pressed.
        mBarInformation = ItemInformation.getInstance().getmBarList().get(mCount); // Gets the correct bar pressed.
        mName.setText(mBarInformation.getName());
        mAddress.setText(mBarInformation.getAddress());

        // Inflate the layout for this fragment
        return mView;
    }

    /**
     * Method when pressing the back button on the toolbar.
     */
    public void toolbarOnClickListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create fragment and pass peoplevotes as argument.
                Fragment HomeFragment = new HomeFragment(); // Create new fragment.

                // Change fragment.
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment).addToBackStack(null).commit();
            }
        });
    }

    /**
     * Method for adding a listener to the floating action button.
     */
    public void setFabListener(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddOfferActivity.class);
                startActivity(intent);
            }
        });
    }

    public void overrideBackButton(){
        mView.setFocusableInTouchMode(true);
        mView.requestFocus();
        mView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                Log.i(TAG, "keyCode: " + keyCode);
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    // Create fragment and pass peoplevotes as argument.
                    Fragment HomeFragment = new HomeFragment(); // Create new fragment.

                    // Change fragment.
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment).addToBackStack(null).commit();
                    return true;
                }
                return false;
            }
        });
    }
}
