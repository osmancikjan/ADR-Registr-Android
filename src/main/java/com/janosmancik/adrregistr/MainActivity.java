package com.janosmancik.adrregistr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends Activity {

    private DBHelper myDB;
    private ListView obj;
    private EditText kemlerInput;
    private EditText unInput;
    private ListView listView;
    private ArrayList<SubstanceObjectModel> arrayOfSubstances;

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

        Button btn = (Button)findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RecognitionActivity.class);
                startActivity(intent);
            }
        });

        // Construct the data source
        arrayOfSubstances = new ArrayList<SubstanceObjectModel>();
       arrayOfSubstances = myDB.getAllSubstancesNames();

// Create the adapter to convert the array to views

        final SubstanceAdapter[] adapter = {new SubstanceAdapter(this, arrayOfSubstances)};
// Attach the adapter to a ListView

        listView = findViewById(R.id.listView);
        listView.setAdapter(adapter[0]);

        kemlerInput = findViewById(R.id.kemlerInput);
        kemlerInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // adapter[0] = new SubstanceAdapter(MainActivity.this, arrayOfSubstances);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length()==0){
                    arrayOfSubstances = myDB.getAllSubstancesNames();
                    listView.setAdapter(adapter[0]);
                    listView.refreshDrawableState();
                } else {
                    arrayOfSubstances = myDB.getAllSubstancesByKemler(s.toString());
                }
                adapter[0] = new SubstanceAdapter(getApplicationContext(), arrayOfSubstances);
                listView.setAdapter(adapter[0]);
                listView.refreshDrawableState();
            }
        });

        unInput = findViewById(R.id.unInput);
        unInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // adapter[0] = new SubstanceAdapter(MainActivity.this, arrayOfSubstances);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s == null || s.equals("")){
                    arrayOfSubstances = myDB.getAllSubstancesNames();
                    listView.setAdapter(adapter[0]);
                    listView.refreshDrawableState();
                } else {
                    arrayOfSubstances = myDB.getAllSubstancesByUN(s.toString());
                }
                adapter[0] = new SubstanceAdapter(getApplicationContext(), arrayOfSubstances);
                listView.setAdapter(adapter[0]);
                listView.refreshDrawableState();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                Intent intent = new Intent(getApplicationContext(), DisplaySubstance.class);
                intent.putExtra("un", arrayOfSubstances.get(arg2).getUn());
                startActivity(intent);
            }
        });
    }
}
