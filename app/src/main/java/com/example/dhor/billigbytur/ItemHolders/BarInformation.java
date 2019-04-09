package com.example.dhor.billigbytur.ItemHolders;

import com.google.firebase.Timestamp;

import java.util.Map;

public class BarInformation {
    // Variables
    private String Address;
    private String Name;
    private Map<String, Integer> Offers;
    private Map<String, String> OpeningHours;
    private Map<String, Integer> People;
    private Map<String, Integer>  PeopleVotes;
    private String mDataId;
    private Timestamp Release;

    // Constructor
    public BarInformation(){

    }

    // Getters and setters.
    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Map<String, Integer> getOffers() {
        return Offers;
    }

    public void setOffers(Map<String, Integer> offers) {
        Offers = offers;
    }

    public Map<String, String> getOpeningHours() {
        return OpeningHours;
    }

    public void setOpeningHours(Map<String, String> openingHours) {
        OpeningHours = openingHours;
    }

    public Map<String, Integer> getPeople() {
        return People;
    }

    public void setPeople(Map<String, Integer> people) {
        People = people;
    }

    public String getmDataId() {
        return mDataId;
    }

    public void setmDataId(String mDataId) {
        this.mDataId = mDataId;
    }

    public Timestamp getRelease() {
        return Release;
    }

    public void setRelease(Timestamp release) {
        this.Release = release;
    }
}
