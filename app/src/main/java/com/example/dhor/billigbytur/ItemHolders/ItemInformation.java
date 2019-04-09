package com.example.dhor.billigbytur.ItemHolders;

import android.support.design.widget.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Singleton for holding temporary data.
 *
 * @author Thor Garske Andresen
 */
public class ItemInformation {
    private static ItemInformation mItemInformation_instance;
    private ArrayList<BarInformation> mBarList = new ArrayList<>();
    private HashMap<String, BarInformation> mBarMap = new HashMap<>();
    private String mName;
    private String mId;
    private boolean removeName = false;

    // static method to create instance of Singleton class
    public static ItemInformation getInstance()
    {
        if (mItemInformation_instance == null)
            mItemInformation_instance = new ItemInformation();

        return mItemInformation_instance;
    }


    // Getters and setters
    public ArrayList<BarInformation> getmBarList() {
        return mBarList;
    }

    public void setmBarList(ArrayList<BarInformation> mBarList) {
        this.mBarList = mBarList;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public HashMap<String, BarInformation> getmBarMap() {
        return mBarMap;
    }

    public void setmBarMap(HashMap<String, BarInformation> mBarMap) {
        this.mBarMap = mBarMap;
    }

    public boolean isRemoveName() {
        return removeName;
    }

    public void setRemoveName(boolean removeName) {
        this.removeName = removeName;
    }
}
