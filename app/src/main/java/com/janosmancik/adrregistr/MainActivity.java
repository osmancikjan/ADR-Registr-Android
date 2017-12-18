package com.janosmancik.adrregistr;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.io.IOException;
import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private DBHelper myDB;
    private ListView obj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myDB = new DBHelper(this);
        try {
            myDB.copyDataBase();
            myDB.setAllSubstances();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Button btn = findViewById(R.id.detectButton);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), CameraReader.class);
                startActivity(intent);
            }
        });

        // Construct the data source
        ArrayList<SubstanceObjectModel> arrayOfSubstances = myDB.getAllSubstancesNames();
// Create the adapter to convert the array to views
        SubstanceAdapter adapter = new SubstanceAdapter(this, arrayOfSubstances);
// Attach the adapter to a ListView

        ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        /*
        obj.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                int id_To_Search = arg2 + 1;

                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id", id_To_Search);

                Intent intent = new Intent(getApplicationContext(), DisplaySubstance.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });*/
    }
}
