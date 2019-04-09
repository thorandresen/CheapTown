package com.example.dhor.billigbytur.Async;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.dhor.billigbytur.ItemHolders.JsonHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

/**
 * Class for retrieving data from a json file async, so it doesn't block the UI thread.
 *
 * @author Frederik Træholt Andersen
 * @author Thor Garske Andresen
 */
public class AsyncJson extends AsyncTask<String, Void, String> {
    private static final String TAG = "AsyncJson";
    private Context mContext;

    public AsyncJson(Context mContext){
        this.mContext = mContext;
    }

    /**
     * The method that does the actually work in the background.
     * @param strings the params passed to the AsyncTask.
     * @return The final result after how the operation went.
     */
    @Override
    protected String doInBackground(String... strings) {
        String json = loadJSONFromAsset();
        try {
            JSONArray arr = new JSONArray(json);
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                JsonHolder.getInstance().getmPostalMap().put(""+obj.getString("nr"), ""+obj.getString("navn"));
                JsonHolder.getInstance().getmCityMap().put(""+obj.getString("navn"), ""+obj.getString("nr"));
//              Log.e(TAG, "Nr: " + obj.getString("nr") + " " + obj.getString("navn"));
            }
        } catch(final JSONException e) {
            Log.e(TAG, "Json parsing error: " + e.getMessage());
            return "json failure";
        }
        return "worked";
    }

    /**
     * Method for loading the Json file with postal numbers and cities from the assets folder.
     * @return A string of the json file.
     */
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = mContext.getAssets().open("postnumre.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
