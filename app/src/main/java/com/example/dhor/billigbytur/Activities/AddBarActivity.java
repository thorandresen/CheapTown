package com.example.dhor.billigbytur.Activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dhor.billigbytur.Async.AsyncJson;
import com.example.dhor.billigbytur.ItemHolders.ItemInformation;
import com.example.dhor.billigbytur.ItemHolders.JsonHolder;
import com.example.dhor.billigbytur.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Class for adding a bar to Firestore.
 *
 * @author Thor Garske Andresen
 *
 * Date: 08/02/19
 */
public class AddBarActivity extends AppCompatActivity {
    // Debug
    private static final String TAG = "AddBarActivity";

    // Views.
    private Button mTimePicker;
    private Button mAddButton;
    private EditText mNameText;
    private EditText mAddressText;
    private EditText mPostalCodeText;
    private EditText mAddressNumberText;
    private EditText mCityText;
    private PopupWindow mPopupWindow;
    private ConstraintLayout mConstraint;
    private Context mContext;
    private View customView;
    private LayoutInflater inflater;
    private HashMap<String, String> mOpeningHour = new HashMap<>();
    private HashMap<String, String> mClosingHour = new HashMap<>();
    private String mTimeOfDay;
    private final FirebaseFirestore mDatabase = FirebaseFirestore.getInstance();
    DatabaseReference mFirebase = FirebaseDatabase.getInstance().getReference();
    private int mCorrectAdded;
    private Toolbar mToolbar;

    // Pick EditTexts
    private EditText mMondayPick;
    private EditText mTuesdayPick;
    private EditText mWednesdayPick;
    private EditText mThursdayPick;
    private EditText mFridayPick;
    private EditText mSaturdayPick;
    private EditText mSundayPick;

    // Checkboxes
    private CheckBox mMondayClose;
    private CheckBox mTuesdayClose;
    private CheckBox mWednesdayClose;
    private CheckBox mThursdayClose;
    private CheckBox mFridayClose;
    private CheckBox mSaturdayClose;
    private CheckBox mSundayClose;

    // Strings for gathering data.
    private String mName;
    private String mAddress;
    private String mCity;
    private int mAddressNumber;
    private int mPostalCode;
    private int mAmountOfErrors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bar);

        // Assign variables
        mTimePicker = findViewById(R.id.openButton);
        mNameText = findViewById(R.id.name);
        mAddressText = findViewById(R.id.streetname);
        mAddressNumberText = findViewById(R.id.address_number);
        mPostalCodeText = findViewById(R.id.postal);
        mConstraint = findViewById(R.id.constraintAdd);
        mAddButton = findViewById(R.id.addBarButton);
        mCityText = findViewById(R.id.city);
        mToolbar = findViewById(R.id.toolbar_add_offer);

        // Get the application context and assign the views for the popupwindow.
        mContext = getApplicationContext();
        inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.activity_time_picker,null);

        // Methods
        timePickerListener();
        assignVariablesForPopup(customView);
        addListenersToCheckboxes(mMondayPick, "monday", mMondayClose);
        addListenersToCheckboxes(mTuesdayPick, "tuesday", mTuesdayClose);
        addListenersToCheckboxes(mWednesdayPick, "wednesday", mWednesdayClose);
        addListenersToCheckboxes(mThursdayPick, "thursday", mThursdayClose);
        addListenersToCheckboxes(mFridayPick, "friday", mFridayClose);
        addListenersToCheckboxes(mSaturdayPick, "saturday", mSaturdayClose);
        addListenersToCheckboxes(mSundayPick, "sunday", mSundayClose);
        addListenersToTimePickers();
        toolbarOnClickListener();
        addbarListener();
        updateTownAndPostalCode();

        // Run the async.
        new AsyncJson(mContext).execute();
    }

    /**
     * Method when pressing the back button on the toolbar.
     */
    public void toolbarOnClickListener() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildAlertDialog();
            }
        });
    }

    /**
     * Method for building an alert dialog for asking permission when exiting the activity.
     */
    public void buildAlertDialog(){
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you wish to exit?")
                .setCancelable(false)
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();


    }

    /**
     * Method for adding a listener to the timepicker button and making a popup window appear when pressed.
     */
    public void timePickerListener(){
        mTimePicker.setOnClickListener(new View.OnClickListener() {
            // Local variables
            @Override
            public void onClick(View view) {
                mPopupWindow = new PopupWindow(
                        customView,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT,
                        ConstraintLayout.LayoutParams.WRAP_CONTENT
                );

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    mPopupWindow.setElevation(30);
                }

                mPopupWindow.showAtLocation(mConstraint, Gravity.CENTER, 0,0);

                ImageButton closeButton = customView.findViewById(R.id.closePopup);
                closeButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View customView) {
                        mPopupWindow.dismiss();
                    }
                });
            }
        });
    }

    /**
     * Method for verifying and gathering all the relevant data that the user types in this activity.
     */
    public Boolean informationCheckAndGather(){
        // Make sure the name is correct then saves it.
        mAmountOfErrors = 0;
        checkName();
        checkAddress();
        checkPostalCode();
        checkAddressNumber();
        checkOpeningHours();
        if(mAmountOfErrors > 0){
            Log.d(TAG, "informationCheckAndGather: Amount of errors: " + mAmountOfErrors);
            return false;
        }

        // Set the variables to be the text fields if it follows the text.
        mName = mNameText.getText().toString();
        mAddress = mAddressText.getText().toString();
        mAddressNumber = Integer.parseInt(mAddressNumberText.getText().toString());
        mPostalCode = Integer.parseInt(mPostalCodeText.getText().toString());
        mCity = mCityText.getText().toString();


        Log.d(TAG, "informationCheckAndGather: " + mName + " " + mAddress + " " + mAddressNumber + " " + mPostalCode);

        return true;
    }

    /**
     * Method for checking wether the postal code meets the requirements the danish postal codes.
     */
    public void checkPostalCode(){
        if(mPostalCodeText.getText().toString().length() == 0){
            mPostalCodeText.setError(getString(R.string.postalEmpty));
            mAmountOfErrors += 1;
            return;
        }
        if(JsonHolder.getInstance().getmPostalMap().get(mPostalCodeText.getText().toString()) == null){
            mPostalCodeText.setError("Please choose valid postal code.");
            mAmountOfErrors += 1;
            return;
        }
        int parseInt = Integer.parseInt(mPostalCodeText.getText().toString());
        if(parseInt < 1000 || parseInt > 3799 && parseInt < 4000 || parseInt > 9999){
            mPostalCodeText.setError(getString(R.string.postalError));
            mAmountOfErrors += 1;
        }
    }

    /**
     * Method for checking if the name meets the requirements.
     */
    public void checkName(){
        if(mNameText.length() < 1){
            mNameText.setError(getString(R.string.mNameEmpty));
            mAmountOfErrors += 1;
            return;
        }
        if(mNameText.length() > 64){
            mNameText.setError(getString(R.string.name_too_long));
            mAmountOfErrors += 1;
        }
    }

    /**
     * Method for checking if the address meets the requirements.
     * TODO: !BUG! NEEDS FORMATTING - NO #¤&/()=?= FX.
     */
    public void checkAddress(){
        if(mAddressText.length() < 1){
            mAddressText.setError(getString(R.string.addressEmpty));
            mAmountOfErrors += 1;
            return;
        }
        /*if(!Pattern.matches("[a-zA-Z]+",mAddressText.getText().toString())){
            mAddressText.setError(getString(R.string.only_letters_address));
            mAmountOfErrors += 1;
            return;
        }*/
        if(mAddressText.length() > 64){
            mAddressText.setError(getString(R.string.address_too_long));
            mAmountOfErrors += 1;
        }
    }

    /**
     * Method for checking whether the address number meets requirements.
     */
    public void checkAddressNumber(){
        if(mAddressNumberText.getText().toString().length() == 0){
            mAddressNumberText.setError("Please enter number of address");
            mAmountOfErrors += 1;
            return;
        }
        if(Integer.parseInt(mAddressNumberText.getText().toString()) > 1000){
            mAddressNumberText.setError("Number must be smaller than 1000");
            mAmountOfErrors += 1;
        }
    }

    /**
     * Method for checking that the opening hours meets requirements.
     */
    public void checkOpeningHours(){
        if(mOpeningHour.size() != 7 || mClosingHour.size() != 7){
            Toast.makeText(mContext, "Remember to pick opening and closing hours for all days!", Toast.LENGTH_SHORT).show();
            mAmountOfErrors += 1;
            return;
        }
    }

    /**
     * Adds a listener on the add button.
     */
    public void addbarListener(){
        mAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!informationCheckAndGather()){
                    return;
                }
                addBars();
                // Makes a snackbar and adds to the singleton for use when in the homefragment.
                ItemInformation.getInstance().setmName(mName);
                ItemInformation.getInstance().getmBarList().clear();
                Intent intent = new Intent(getApplicationContext(), HomeScreenActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Method for adding bars to firestore from this activity.
     */
    public void addBars(){
        mCorrectAdded = 0;
        // Bar hashmap.
        Map<String, Object> Bar = new HashMap<>();

        // Name
        Bar.put("Name", mName);

        // Address
        Bar.put("Address", mAddress + ", " + mPostalCode + " " + mCity);

        // People pics
        HashMap<String, Integer> People = new HashMap<>();
        People.put("1", 2131165310);
        People.put("2", 2131165312);
        People.put("3", 2131165311);
        People.put("4", 2131165309);
        Bar.put("People",People);

        // Add the vote closing to firebase
        mFirebase.child("Bars").child(mName).setValue(1);

        // Opening Hours
        HashMap<String, String> testMap = new HashMap<>();
        Iterator it = mOpeningHour.entrySet().iterator();
        Iterator it2 = mClosingHour.entrySet().iterator();
        while(it.hasNext() && it2.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            Map.Entry pair2 = (Map.Entry)it2.next();
            testMap.put(pair.getKey().toString(), pair.getValue().toString() + "-" + pair2.getValue());
        }
        Bar.put("OpeningHours", testMap);


        // Offers
        HashMap<String, Integer> Offers = new HashMap<>();
        Bar.put("Offers", Offers);

        // Timestamp of release.
        Bar.put("Release", Timestamp.now());

        // Makes sure that the name remove name is set to false, as we are now adding an item. This will be set to true again when item is either removed or snackbar is gone.
        ItemInformation.getInstance().setRemoveName(false);

        // Put into firestore.
        mDatabase.collection("Bars")
                .add(Bar)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        Toast.makeText(mContext, "Failed to add bar, please try again", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Method for assigning the variables in the popupwindow.
     * @param mView The view that there is assigned to the popup window.
     */
    public void assignVariablesForPopup(View mView){
        mMondayPick = mView.findViewById(R.id.mondayPick);
        mTuesdayPick = mView.findViewById(R.id.tuesdayPick);
        mWednesdayPick = mView.findViewById(R.id.wednesdayPick);
        mThursdayPick = mView.findViewById(R.id.thursdayPick);
        mFridayPick = mView.findViewById(R.id.fridayPick);
        mSaturdayPick = mView.findViewById(R.id.saturdayPick);
        mSundayPick = mView.findViewById(R.id.sundayPick);

        mMondayClose = mView.findViewById(R.id.mondayClose);
        mTuesdayClose = mView.findViewById(R.id.tuesdayClose);
        mWednesdayClose = mView.findViewById(R.id.wednesdayClose);
        mThursdayClose = mView.findViewById(R.id.thursdayClose);
        mFridayClose = mView.findViewById(R.id.fridayClose);
        mSaturdayClose = mView.findViewById(R.id.saturdayClose);
        mSundayClose = mView.findViewById(R.id.sundayClose);
    }

    /**
     * Method for adding listeners to all the time pickers in the pop up window.
     */
    public void addListenersToTimePickers(){
        mMondayPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, "1. Pick opening hour. 2. Pick closing hour", Toast.LENGTH_LONG).show();
                mTimeOfDay = "morning";
                pickingTimeForDifferentDays("monday");
            }
        });

        mTuesdayPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimeOfDay = "morning";
                pickingTimeForDifferentDays("tuesday");
            }
        });

        mWednesdayPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimeOfDay = "morning";
                pickingTimeForDifferentDays("wednesday");
            }
        });

        mThursdayPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimeOfDay = "morning";
                pickingTimeForDifferentDays("thursday");
            }
        });

        mFridayPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimeOfDay = "morning";
                pickingTimeForDifferentDays("friday");
            }
        });

        mSaturdayPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimeOfDay = "morning";
                pickingTimeForDifferentDays("saturday");
            }
        });

        mSundayPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTimeOfDay = "morning";
                pickingTimeForDifferentDays("sunday");
            }
        });
    }

    /**
     * Method for adding listeners to all the checkboxes in the popupwindow, which lets the user choose whether a bar is open on different days.
     */
    public void addListenersToCheckboxes(final EditText picker, final String day, final CheckBox closer){
        closer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    picker.setText(R.string.bar_closed);
                    mOpeningHour.put(day, "-");
                    mClosingHour.put(day, "-");
                    picker.setOnClickListener(null);
                }
                if(!isChecked){
                    picker.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mTimeOfDay = "morning";
                            pickingTimeForDifferentDays("monday");
                        }
                    });
                }
            }
        });
    }

    /**
     * Method for picking the opening times of the different days. This shows a timepicker widget.
     * @param day The day chosen.
     */
    public void pickingTimeForDifferentDays(final String day){
        TimePickerDialog.OnTimeSetListener time = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                String iString; // Local variables for formatting
                String iString1; // Local variables for formatting
                if(i < 9){iString = "" + 0 + i;} else {iString = ""+i;} // Formatting if under 10, to put a number infront.
                if(i1 < 9){iString1 = "" + 0 + i1;} else {iString1 = ""+i1;} // Formatting if under 10, to put a number infront.
                Log.d(TAG, "onTimeSet: " + mTimeOfDay + " " + i + " " + i1);
                // If picking closing hours this if statement is run.
                if(mTimeOfDay == "evening"){
                    mClosingHour.put(day, iString + ":" + iString1);
                    Log.d(TAG, "onTimeSet: put closing hours ");
                }
                // If picking opening hours this is run, and then running the method again, so that closing hours can be chosen.
                if(mTimeOfDay == "morning"){
                    mOpeningHour.put(day, iString + ":" + iString1);
                    Log.d(TAG, "onTimeSet: put opening hours");
                    pickingTimeForDifferentDays(day);
                    mTimeOfDay = "evening";
                }
                updateHints(day); // Updates the hint.
            }
        };
        // Openings the timepicker.
        TimePickerDialog dialog = new TimePickerDialog(this, time, 12, 0, true);
        dialog.show();
    }

    /**
     * Method for updating the hints.
     * @param day the day for updating.
     */
    public void updateHints(String day){
        switch(day) {
            case "monday":
                mMondayPick.setText(mOpeningHour.get(day) + "-" + mClosingHour.get(day));
                break;
            case "tuesday":
                mTuesdayPick.setText(mOpeningHour.get(day) + "-" + mClosingHour.get(day));
                break;
            case "wednesday":
                mWednesdayPick.setText(mOpeningHour.get(day) + "-" + mClosingHour.get(day));
                break;
            case "thursday":
                mThursdayPick.setText(mOpeningHour.get(day) + "-" + mClosingHour.get(day));
                break;
            case "friday":
                mFridayPick.setText(mOpeningHour.get(day) + "-" + mClosingHour.get(day));
                break;
            case "saturday":
                mSaturdayPick.setText(mOpeningHour.get(day) + "-" + mClosingHour.get(day));
                break;
            case "sunday":
                mSundayPick.setText(mOpeningHour.get(day) + "-" + mClosingHour.get(day));
                break;
        }
    }

    /**
     * Method for updating the town and postal code depending on the user input.
     */
    public void updateTownAndPostalCode(){
        // Postal code reader
        mPostalCodeText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    if(JsonHolder.getInstance().getmPostalMap().get(mPostalCodeText.getText().toString()) == null){
                        mPostalCodeText.setError("Please choose valid postal code or city instead.");
                        return;
                    }
                    mCityText.setText(JsonHolder.getInstance().getmPostalMap().get(mPostalCodeText.getText().toString()));
                }
            }
        });

        mCityText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (!hasFocus) {
                    if(JsonHolder.getInstance().getmCityMap().get(mCityText.getText().toString()) == null){
                        mCityText.setError("Please choose valid city or postal code instead.");
                        return;
                    }
                    mPostalCodeText.setText(JsonHolder.getInstance().getmCityMap().get(mCityText.getText().toString()));
                }
            }
        });
    }
}
