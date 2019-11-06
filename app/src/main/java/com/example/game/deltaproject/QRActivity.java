package com.example.game.deltaproject;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class QRActivity extends AppCompatActivity {

    SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String intentData = "";
    FirebaseDatabase database;
    Bookingclass b;
    DatabaseReference bookdb;
    String username;
    SimpleDateFormat mdformat = new SimpleDateFormat("hhmmss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr);
        bookdb=FirebaseDatabase.getInstance().getReference("Booked");
        Intent intent = getIntent();
        username = intent.getStringExtra("Username");
        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (intentData.length() > 0) {
                    bookdb.addValueEventListener(new ValueEventListener() {
                        String[] split = intentData.split(",");
                        String latitude = split[0];
                        String longitude = split[1];

                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot d : dataSnapshot.getChildren()) {
                                String key = d.getKey();
                                b = retrievebook(key);
                                if (b.getUsername().equals(username) && b.getLatitude() == Double.parseDouble(latitude)
                                        && b.getLongitude() == Double.parseDouble(longitude) && b.getStatus() == 0) {
                                    Date date = new Date();
                                    int diff = getTimeDiff(b.getBooktime(), mdformat.format(date));
                                    if (diff > 003000) {
                                        Toast.makeText(getApplicationContext(), "Time Limit Exceeded, Booking Cancelled", Toast.LENGTH_SHORT).show();
                                        bookdb.child(b.getId()).child("status").setValue(3);
                                    } else {
                                        bookdb.child(b.getId()).child("status").setValue(1);
                                        Toast.makeText(getApplicationContext(), "RESERVED", Toast.LENGTH_SHORT).show();
                                        bookdb.child(b.getId()).child("intime").setValue(mdformat.format(date));
                                    }
                                }
                                if (b.getUsername().equals(username) && b.getStatus() == 1) {
                                    Date date = new Date();
                                    int diff = getTimeDiff(b.getIntime(), mdformat.format(date));
                                    bookdb.child(b.getId()).child("outtime").setValue(mdformat.format(date));
                                    bookdb.child(b.getId()).child("status").setValue(2);
                                    Intent intent = new Intent(QRActivity.this, BillGenerator.class);
                                    intent.putExtra("Timedifference", diff);
                                    startActivity(intent);
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
        });
    }


    private Bookingclass retrievebook(String key){
        bookdb.child(key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot d : dataSnapshot.getChildren()) {
                    b = d.getValue(Bookingclass.class);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error", "onCancelled", databaseError.toException());
            }
        });
        return b;
    }

    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true) //you should add this feature
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(QRActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(QRActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {
                            if (barcodes.valueAt(0).rawValue != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);
                                btnAction.setText("SCAN Complete");
                            }
                        }
                    });

                }
            }
        });
    }

    public int getTimeDiff(String time1, String time2) {
        int i = 0;
        int hr2 = Integer.parseInt(time2) / 10000;
        int min2 = (Integer.parseInt(time2) / 100) % 100;
        int sec2 = Integer.parseInt(time2) % 100;
        int hr1 = Integer.parseInt(time1) / 10000;
        int min1 = (Integer.parseInt(time1) / 100) % 100;
        int sec1 = Integer.parseInt(time1) % 100;
        i = (hr2 - hr1) * 10000;
        if (hr2 - hr1 < 0) {
            i = i + (24) * 10000;
        }
        i = i + (min2 - min1) * 100;
        if (min2 - min1 < 0) {
            i = i + (60) * 100;
            i = i - 10000;
        }
        i = i + sec2 - sec1;
        if (sec2 - sec1 < 0) {
            i = i + 60;
            i = i - 100;
        }
        return i;
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }
}
