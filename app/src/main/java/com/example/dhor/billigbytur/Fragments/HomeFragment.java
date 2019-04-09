package com.example.dhor.billigbytur.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.example.dhor.billigbytur.Activities.AddBarActivity;
import com.example.dhor.billigbytur.Adapters.BarAdapter;
import com.example.dhor.billigbytur.ItemHolders.BarInformation;
import com.example.dhor.billigbytur.ItemHolders.ItemInformation;
import com.example.dhor.billigbytur.Listeners.MyUndoListener;
import com.example.dhor.billigbytur.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

/**
 * Our HomeFragment class, that holds the fragment with all the bars within a recyclerView.
 *
 * @author Thor Garske Andresen
 */
public class HomeFragment extends Fragment {
    // Debug
    private static final String TAG = "HomeFragment";

    // Variables.
    private final FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    FirebaseDatabase mFirebase = FirebaseDatabase.getInstance();
    DatabaseReference myRef;
    private View mView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private FloatingActionButton fab;

    // RecyclerView Variables
    private ArrayList<String> mBarNames = new ArrayList<>();
    private ArrayList<String> mBarAddresses = new ArrayList<>();
    private ArrayList<String> mBarOffers = new ArrayList<>();
    private ArrayList<Integer> mBarPeople = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        // Variables instantiated.
        mSwipeRefreshLayout = mView.findViewById(R.id.swipe_container);
        fab = mView.findViewById(R.id.fab);

        // Methods
        retrieveFireStoreData();
        setSwipeRefresherListener();
        setFabListener();

        // Return the view.
        return mView;
    }

    /**
     * A method for checking whether there is an item to be removed by the snackbar.
     */
    public void checkIfItemIsRemoved(){
        if(ItemInformation.getInstance().isRemoveName()){
            ItemInformation.getInstance().setmName(null);
        }
    }

    /**
     * Inits the recyclerview with new offers.
     */
    public void initRecyclerView(){
        if (mView != null) {
            mView.findViewById(R.id.loadingPanel).setVisibility(View.GONE);
            RecyclerView recyclerView = mView.findViewById(R.id.recycler_bars);
            BarAdapter adapter = new BarAdapter(mBarNames, mBarAddresses,mBarOffers,mBarPeople,getContext());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            runLayoutAnimation(recyclerView);
        }
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    /**
     * Method for undoing action when added a bar.
     */
    public void undoAction(){
        if(ItemInformation.getInstance().getmName() != null){
            Snackbar mySnackbar = Snackbar.make(mView, "You succesfully added: " + ItemInformation.getInstance().getmName(), Snackbar.LENGTH_LONG);
            mySnackbar.setAction("Undo", new MyUndoListener());
            mySnackbar.show();
            ItemInformation.getInstance().setRemoveName(true);
        }
    }

    /**
     * Method for loading all of FireStore items into recyclerview and create as objects.
     */
    public void retrieveFireStoreData(){
        mDatabase.collection("Bars")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Add information to informationholder.
                                BarInformation mBarInformation;
                                mBarInformation = document.toObject(BarInformation.class);
                                mBarInformation.setmDataId(document.getId());
                                ItemInformation.getInstance().getmBarList().add(mBarInformation);
                                ItemInformation.getInstance().getmBarMap().put(mBarInformation.getName(),mBarInformation);

                                // Add bars into the recyclerview.
                                mBarNames.add(mBarInformation.getName());
                                mBarOffers.add("Offers: " + mBarInformation.getOffers().size());
                                mBarAddresses.add(mBarInformation.getAddress());

                                // Sets the picture to the correct one. (TO BE CHANGED WITH SOMETHING ELSE!
                                mBarPeople.add(mBarInformation.getPeople().get("3"));
                            }
                            Log.d(TAG, "onComplete: Retrieved information!");
                            // Stops the refreshing when data is retrieved correctly.
                            mSwipeRefreshLayout.setRefreshing(false);
                        }
                        else{
                            Log.d(TAG, "onComplete: Failed in retrieving!");
                        }
                        checkIfItemIsRemoved(); // Should item be removed?
                        undoAction(); // Is there an action to be undone?
                        initRecyclerView(); // inits recyclerview
                    }
                });
    }

    /**
     * Method for adding a swipe refresh listener to the recyclerview, making it possible to update the recyclerview with all the bars by swiping down.
     */
    public void setSwipeRefresherListener(){
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mBarNames = new ArrayList<>();
                mBarAddresses = new ArrayList<>();
                mBarOffers = new ArrayList<>();
                mBarPeople = new ArrayList<>();
                ItemInformation.getInstance().getmBarList().clear();
                retrieveFireStoreData();
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
                Intent intent = new Intent(getContext(), AddBarActivity.class);
                startActivity(intent);
            }
        });
    }
}


