package com.example.dhor.billigbytur.Listeners;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.dhor.billigbytur.ItemHolders.BarInformation;
import com.example.dhor.billigbytur.ItemHolders.ItemInformation;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class MyUndoListener implements View.OnClickListener {
    private static final String TAG = "MyUndoListener";
    private final FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();

    @Override
    public void onClick(View view) {
        BarInformation bar = ItemInformation.getInstance().getmBarMap().get(ItemInformation.getInstance().getmName());

        mDatabase.collection("Bars").document(bar.getmDataId())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Succesfully removed");
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.d(TAG, "onCanceled: Failed to remove");
                    }
                });


    }
}
