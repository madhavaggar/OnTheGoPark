package com.example.game.deltaproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.game.deltaproject.BookingAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BookingInfo extends AppCompatActivity {

    private BookingAdapter mAdapter;
    private RecyclerView recyclerView;
    String username;
    FirebaseDatabase database;
    DatabaseReference bookdb = database.getReference("Booked");
    List<Bookingclass> booklist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_info);
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        bookdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot d:dataSnapshot.getChildren()){
                    Bookingclass b = d.getValue(Bookingclass.class);
                    booklist.add(b);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "onCancelled", databaseError.toException());
            }
        });

        recyclerView = findViewById(R.id.my_recycler_view);
        mAdapter = new BookingAdapter(BookingInfo.this,booklist);
        recyclerView.setAdapter(mAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(BookingInfo.this);
        recyclerView.setLayoutManager(layoutManager);
    }
}
