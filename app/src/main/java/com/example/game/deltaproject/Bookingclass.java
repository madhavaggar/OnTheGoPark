package com.example.game.deltaproject;

public class Bookingclass {
    int status;
    Double latitude;
    Double longitude;
    String username;
    String booktime;
    String intime;
    String outtime;
    String id;

    public Bookingclass(){

    }

    public Bookingclass( String username,int status, Double latitude, Double longitude,String booktime,String intime,String outtime,String id){
        this.status=status;
        this.latitude=latitude;
        this.longitude=longitude;
        this.username=username;
        this.booktime= booktime;
        this.intime=intime;
        this.outtime=outtime;
        this.id=id;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public int getStatus() {
        return status;
    }

    public String getUsername() {
        return username;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getBooktime() {
        return booktime;
    }

    public String getIntime() {
        return intime;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setBooktime(String booktime) {
        this.booktime = booktime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
