package com.example.dhor.billigbytur.Adapters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dhor.billigbytur.Activities.HomeScreenActivity;
import com.example.dhor.billigbytur.Fragments.BarInfoFragment;
import com.example.dhor.billigbytur.ItemHolders.BarInformation;
import com.example.dhor.billigbytur.R;

import java.util.ArrayList;

public class BarAdapter extends RecyclerView.Adapter<BarAdapter.ViewHolder> {
    private static final String TAG = "BarAdapter";

    // Variables
    private Context mContext;
    private ArrayList<String> mBarNames = new ArrayList<>();
    private ArrayList<String> mBarAddresses = new ArrayList<>();
    private ArrayList<String> mBarOffers = new ArrayList<>();
    private ArrayList<Integer> mBarPeople = new ArrayList<>();

    public BarAdapter(ArrayList<String> mBarNames, ArrayList<String> mBarAddresses, ArrayList<String> mBarOffers, ArrayList<Integer> mBarPeople, Context mContext){
        this.mBarNames = mBarNames;
        this.mBarAddresses = mBarAddresses;
        this.mBarOffers = mBarOffers;
        this.mBarPeople = mBarPeople;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public BarAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bar_item_layout, parent, false);
        BarAdapter.ViewHolder holder = new BarAdapter.ViewHolder(view);
        return holder;

    }

    @Override
    public void onBindViewHolder(@NonNull BarAdapter.ViewHolder holder, final int position) {
        holder.mBarNames.setText(mBarNames.get(position));
        holder.mBarOffers.setText(mBarOffers.get(position));
        holder.mBarAddresses.setText(mBarAddresses.get(position));

        Glide.with(mContext)
                .load(mBarPeople.get(position))
                .into(holder.mBarPeople);


        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity = (AppCompatActivity) view.getContext();

                //Passes argument of the given position clicked, before changing fragment.
                Bundle posArg = new Bundle();
                posArg.putInt("posArg",position);
                Fragment barInfoFragment = new BarInfoFragment(); // Create new fragment.
                barInfoFragment.setArguments(posArg);

                // Change fragment.
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, barInfoFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return mBarNames.size();
    }


public class ViewHolder extends RecyclerView.ViewHolder{
    ImageView mBarPeople;
    TextView mBarNames;
    TextView mBarAddresses;
    TextView mBarOffers;
    ConstraintLayout parentLayout;
    public ViewHolder(View itemView) {
        super(itemView);
        mBarPeople = itemView.findViewById(R.id.people);
        mBarNames = itemView.findViewById(R.id.barName);
        mBarAddresses = itemView.findViewById(R.id.address);
        mBarOffers = itemView.findViewById(R.id.offersAmount);
        parentLayout = itemView.findViewById(R.id.parent_layout);

    }
}
}
