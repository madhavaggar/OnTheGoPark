package com.example.game.deltaproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CancelBooking extends AppCompatActivity {
    FirebaseDatabase database;
    Bookingclass b;
    Parking p;
    DatabaseReference parkdb = database.getReference("Parking");
    DatabaseReference bookdb = database.getReference("Booked");
    String username;
    TextView message;
    Cursor parkrow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_booking);
        Intent intent = getIntent();
        message = findViewById(R.id.cancelopt);
        username = intent.getStringExtra("Username");
        bookdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    b = d.getValue(Bookingclass.class);
                    if (b.getUsername().equals(username) && b.getStatus() == 0) {
                        parkdb.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot f : dataSnapshot.getChildren()) {
                                    p = f.getValue(Parking.class);
                                    if (p.getLongitude() == b.getLongitude() && p.getLatitude() == b.getLatitude()) {
                                        parkdb.child(p.getID()).child("availablepark").setValue(p.getAvailablepark() + 1);
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Log.e("Error", "onCancelled", databaseError.toException());
                            }
                        });
                        bookdb.child(b.getId()).child("status").setValue(3);
                        message.setText("Your booking has been cancelled");
                    } else {
                        Toast.makeText(getApplicationContext(), "Your booking cannot be cancelled", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "onCancelled", databaseError.toException());
            }
        });
    }
}