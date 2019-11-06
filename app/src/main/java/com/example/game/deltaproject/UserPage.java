package com.example.game.deltaproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class UserPage extends AppCompatActivity {

    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);
        Intent intent=getIntent();
        username=intent.getStringExtra("Username");
    }

    public void scanQR(View view){
        Intent intent= new Intent(UserPage.this,QRActivity.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

    public void findParkingSpace(View view){
        Intent intent = new Intent(UserPage.this,MapEngine.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

    public void SignOut(View V){
        if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
            FirebaseAuth.getInstance().signOut();
            Toast.makeText(UserPage.this,"Signed Out",Toast.LENGTH_LONG).show();
        }
        finish();
    }

    public void ViewBooking(View V){
        Intent intent = new Intent(UserPage.this,BookingInfo.class);
        intent.putExtra("Username",username);
        startActivity(intent);
    }

    public void CancelBooking(View V){
        new AlertDialog.Builder(UserPage.this)
                .setTitle("Cancel Booking")
                .setMessage("Are you sure you want to cancel your booking?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(UserPage.this,CancelBooking.class);
                        intent.putExtra("Username",username);
                        startActivity(intent);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
