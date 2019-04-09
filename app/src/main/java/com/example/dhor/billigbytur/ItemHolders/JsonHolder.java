package com.example.dhor.billigbytur.ItemHolders;

import java.util.HashMap;

public class JsonHolder {
    private static JsonHolder jsonHolder_instance;
    private HashMap<String, String> mPostalMap = new HashMap<>();
    private HashMap<String, String> mCityMap = new HashMap<>();

    // static method to create instance of Singleton class
    public static JsonHolder getInstance()
    {
        if (jsonHolder_instance == null)
            jsonHolder_instance = new JsonHolder();

        return jsonHolder_instance;
    }

    public HashMap<String, String> getmPostalMap() {
        return mPostalMap;
    }

    public void setmPostalMap(String key, String value) {
        mPostalMap.put(key,value);
    }

    public HashMap<String, String> getmCityMap() {
        return mCityMap;
    }

    public void setmCityMap(String key, String value) {
        mCityMap.put(key, value);
    }
}
