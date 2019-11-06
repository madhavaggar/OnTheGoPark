package com.example.game.deltaproject;

public class Parking {
    private double latitude;
    private double longitude;
    private int totalparking;
    private int availablepark;
    private String ID;

    public Parking() {

    }

    public Parking(double latitude,double longitude,int totalparking,int availablepark,String ID){

        this.latitude=latitude;
        this.longitude=longitude;
        this.ID=ID;
        this.totalparking=totalparking;
        this.availablepark=availablepark;
    }

    public int getAvailablepark() {
        return availablepark;
    }

    public int getTotalparking() {
        return totalparking;
    }

    public void setAvailablepark(int availablepark) {
        this.availablepark = availablepark;
    }

    public void setTotalparking(int totalparking) {
        this.totalparking = totalparking;
    }

    public double getLongitude(){
        return longitude;
    }

    public double getLatitude(){
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setID(String ID){
        this.ID=ID;
    }

    public String getID() {
        return ID;
    }
}
