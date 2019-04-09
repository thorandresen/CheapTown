package com.example.dhor.billigbytur.Activities;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.dhor.billigbytur.R;

import java.util.HashMap;

/**
 * Class for adding a offer to Firestore.
 *
 * @author Thor Garske Andresen
 *
 * Date: 18/02/19
 */
public class AddOfferActivity extends AppCompatActivity {
    private static final String TAG = "AddOfferActivity";
    // Variables
    private Toolbar mToolbar;
    private int mAmountOfErrors;
    private Button mAddOffer;
    private Button mHoursAndDays;
    private PopupWindow mPopupWindow;
    private Context mContext;
    private View customView;
    private LayoutInflater inflater;
    private ConstraintLayout mConstraint;
    private HashMap<String, String> mOpeningHour = new HashMap<>();
    private HashMap<String, String> mClosingHour = new HashMap<>();
    private String mTimeOfDay;
    private DatePicker mStartDatePicker;
    private DatePicker mEndDatePicker;
        //EditText Variables
    private EditText mOfferName;
    private EditText mOfferDesc;
    private EditText mOfferPrice;
    private EditText mStartDate;
    private EditText mEndDate;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);

        // Inits
        mToolbar = findViewById(R.id.toolbar_add_offer);
        mOfferName = findViewById(R.id.offer_name);
        mOfferDesc = findViewById(R.id.short_desc_offer);
        mOfferPrice = findViewById(R.id.price_offer);
        mStartDate = findViewById(R.id.start_date_offer);
        mEndDate = findViewById(R.id.end_date_offer);
        mAddOffer = findViewById(R.id.add_offer);
        mHoursAndDays = findViewById(R.id.days_button);
        mConstraint = findViewById(R.id.offer_layout);

        // Get the application context and assign the views for the popupwindow.
        mContext = getApplicationContext();
        inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        customView = inflater.inflate(R.layout.activity_time_picker,null);

        // Methods
        assignVariablesForPopup(customView);
        toolbarOnClickListener();
        addOfferListener();
        timePickerListener();
        addListenersToTimePickers();
        addListenersToCheckboxes(mMondayPick, "monday", mMondayClose);
        addListenersToCheckboxes(mTuesdayPick, "tuesday", mTuesdayClose);
        addListenersToCheckboxes(mWednesdayPick, "wednesday", mWednesdayClose);
        addListenersToCheckboxes(mThursdayPick, "thursday", mThursdayClose);
        addListenersToCheckboxes(mFridayPick, "friday", mFridayClose);
        addListenersToCheckboxes(mSaturdayPick, "saturday", mSaturdayClose);
        addListenersToCheckboxes(mSundayPick, "sunday", mSundayClose);
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
     * Method for adding a listener to the timepicker button and making a popup window appear when pressed.
     */
    public void timePickerListener(){
        mHoursAndDays.setOnClickListener(new View.OnClickListener() {
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
     * TODO: SET DATE PICKERS
     */
    public void datePickerListener(DatePicker dp){

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
     * Method for verifying and gathering all the relevant data that the user types in this activity.
     */
    public Boolean informationCheckAndGather(){
        mAmountOfErrors = 0;
        checkName();
        checkDesc();
        checkPrice();
        checkEndDate();
        checkStartDate();
        checkOpeningHours();

        if(mAmountOfErrors > 0){
            Log.d(TAG, "informationCheckAndGather: Amount of errors: " + mAmountOfErrors);
            return false;
        }
        return true;
    }

    /**
     * Method for checking that name is put in correctly.
     */
    public void checkName(){
        if(mOfferName.getText().toString().length() == 0){
            mOfferName.setError("Please enter a offer name");
            mAmountOfErrors++;
            return;
        }
        if(mOfferName.getText().toString().length() > 32){
            mOfferName.setError("Please enter a name shorter than 32 chars");
            mAmountOfErrors++;
        }
    }

    /**
     * Method for checking that offerdesc is put correctly.
     */
    public void checkDesc(){
        if(mOfferDesc.getText().toString().length() > 64){
            mOfferDesc.setError("Short desc should be lower than 64 chars");
            mAmountOfErrors++;
        }
    }

    /**
     * Method for checking that price is within a reasonable amount
     */
    public void checkPrice(){
        if(mOfferPrice.getText().toString().length() == 0){
            mOfferPrice.setError("Please enter price");
            mAmountOfErrors += 1;
            return;
        }
        if(Integer.parseInt(mOfferPrice.getText().toString()) < 99999 && Integer.parseInt(mOfferPrice.getText().toString()) > 0){
            mOfferPrice.setError("Price should be between 0 and 99999,-");
            mAmountOfErrors++;
        }
    }

    /**
     * Method for checking that start date is valid.
     */
    public void checkStartDate(){
        if(mStartDate.getText().toString().length() == 0){
            mStartDate.setError("Please chose start date");
            mAmountOfErrors++;
        }
    }

    /**
     * Method for checking that end date is valid.
     */
    public void checkEndDate(){
        if(mEndDate.getText().toString().length() == 0){
            mEndDate.setError("Please chose end date");
            mAmountOfErrors++;
        }
    }

    /**
     * Method for adding listener to the add button of our activity.
     */
    public void addOfferListener(){
        mAddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(informationCheckAndGather()){
                    finish(); // This is where it changes activity and goes back when the offer is added
                }
            }
        });
    }
}
