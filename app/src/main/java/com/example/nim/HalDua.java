package com.example.nim;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

import kotlin.Suppress;


public class HalDua extends AppCompatActivity {

    private SensorManager sensorManager;
    private TextView latitude, longitude, akurasi;
    private FusedLocationProviderClient locationProviderClient;

    Spinner pilih;
    Button apply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hal_dua);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        latitude = findViewById(R.id.latitude);
        longitude = findViewById(R.id.longitude);
        akurasi = findViewById(R.id.akurasi);
        locationProviderClient = LocationServices.getFusedLocationProviderClient(HalDua.this);

        apply = (Button) findViewById(R.id.btspin);
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pilihspin();
            }
        });

        pilih = (Spinner) findViewById(R.id.spin);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantresults){
        super.onRequestPermissionsResult(requestCode, permissions, grantresults);
        if (requestCode == 10){
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED){

            }else {
                pilihspin();
            }
        }
    }

    public void pilihspin() {
        String pilihmenu = pilih.getSelectedItem().toString();
        if (pilihmenu.equals("Jelajahi")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.ump.ac.id"));
            startActivity(intent);
            Toast.makeText(this, "Tunggu..", Toast.LENGTH_SHORT).show();
        } else if (pilihmenu.equals("Hubungi")) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("smsto:085724126979"));
            intent.putExtra("sms_body", "Pesan dari aplikasi Android");
            startActivity(intent);
            Toast.makeText(this, "Mengirimkan pesan", Toast.LENGTH_SHORT).show();
        } else if (pilihmenu.equals("Baca Data")) {
            List<Sensor> deviceSensor = sensorManager.getSensorList(Sensor.TYPE_PROXIMITY);
            AlertDialog.Builder kotakpesan = new AlertDialog.Builder(HalDua.this);
            kotakpesan.setMessage(deviceSensor +"\n");
            kotakpesan.create().show();
        } else if (pilihmenu.equals("Cek Posisi")) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION}, 10);
                }
            }else {
                locationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>(){
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onSuccess(Location location){
                        if (location != null){
                            latitude.setText(String.valueOf(location.getLatitude()));
                            longitude.setText(String.valueOf(location.getLongitude()));
                            akurasi.setText(location.getAccuracy() + "%");
                            Toast.makeText(getBaseContext(), "Lokasi terbaca", Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Meminta akses lokasi", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener(){
                    @Override
                    public void onFailure(@NonNull Exception e){
                        Toast.makeText(getApplicationContext(),e .getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }


}





