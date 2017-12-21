package com.janosmancik.adrregistr;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
            } catch(Exception e) {
                Log.w("Error", e.getMessage());
            }
            finally {
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

                intent.setData(Uri.parse("tel:123"));
                startActivity(intent);
            }
        });
    }


}
