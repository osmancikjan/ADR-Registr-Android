package com.janosmancik.adrregistr;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by Honza on 11.12.2017.
 */

public class DisplaySubstance extends Activity {

    private DBHelper mydb;

    TextView kemler;
    TextView un;
    TextView name;
    TextView ochrana;
    TextView ohrozeni;
    Button callButton;
    Location loc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.substance_detail);

        name = findViewById(R.id.nameText);
        kemler = findViewById(R.id.kemlerText);
        un = findViewById(R.id.unText);
        ochrana = findViewById(R.id.text1);
        ohrozeni = findViewById(R.id.text2);

        Intent intent = getIntent();

        String value = intent.getStringExtra("un");

        String stringUN = value;
        String stringNazev = "";

        SubstanceObjectModel latka = new SubstanceObjectModel();

        if (value.length() > 0) {
            try {
                mydb = new DBHelper(getApplicationContext());
                latka = mydb.getData(value);
            } catch (Exception e) {
                Log.w("Error", e.getMessage());
            } finally {
                Log.i("Finally", "");
            }
        }

        un.setText(latka.getUn());
        name.setText(latka.getLatka());
        kemler.setText(latka.getKemler());
        ochrana.setText(latka.getOchrana());
        ohrozeni.setText(latka.getOhrozeni());

        Button btn = findViewById(R.id.callBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);

                Intent intent1 = intent.setData(Uri.parse("tel:123"));
                startActivity(intent1);
            }
        });

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                loc = location;
                Log.i("location", String.valueOf(loc.getLatitude()));
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        };
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
            requestPermissions(new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.INTERNET}, 10);
     else return;

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 10:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    configureButton();
                return;

        }
    }

    public void configureButton() {
        Button callButton = findViewById(R.id.callBtn);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager.requestLocationUpdates("gps", 10000, 20, locationListener);
            }
        });
    }
}


