package com.example.game.deltaproject;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Context context;
    LayoutInflater inflater;
    View v;
    TextView parking;
    int found=0;
    FirebaseDatabase database= FirebaseDatabase.getInstance();
    DatabaseReference parkdb=database.getReference("Parking");


    public MarkerInfoWindowAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public View getInfoWindow(Marker arg0){
        return null;
    }

    @Override
    public View getInfoContents(final Marker arg0) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v =  inflater.inflate(R.layout.info_window_marker, null);
        parkdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d : dataSnapshot.getChildren()){
                    Parking p = d.getValue(Parking.class);
                    if(p.getLatitude()==arg0.getPosition().latitude && p.getLongitude()==arg0.getPosition().longitude){
                        parking =(TextView) v.findViewById(R.id.availableparking);
                        parking.setText("Spaces: " + p.getAvailablepark() + "");
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "onCancelled", databaseError.toException());

            }
        });
        LatLng latLng = arg0.getPosition();
        TextView tvLat = (TextView) v.findViewById(R.id.lat);
        TextView tvLng = (TextView) v.findViewById(R.id.lng);
        TextView book=v.findViewById(R.id.book);
        tvLat.setText("Latitude:" + latLng.latitude);
        tvLng.setText("Longitude:"+ latLng.longitude);
        return v;
    }
}