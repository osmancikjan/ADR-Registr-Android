package com.janosmancik.adrregistr;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private DBHelper myDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openDB();
       // populateListView();
    }

    private void openDB(){
        myDB = new DBHelper(this);
        myDB.open();
    }

    private void populateListView(){
        Cursor cursor = myDB.getAllBasicRows();
        String[] fromFieldNames = new String[] {DBHelper.KEY_UN, DBHelper.KEY_KEMLER, DBHelper.KEY_NAME};
        int[] toViewIDs = new int[] {R.id.textUN, R.id.textKemler, R.id.textName};
        SimpleCursorAdapter myCursorAdapter;
        myCursorAdapter = new SimpleCursorAdapter(getBaseContext(),R.layout.row,cursor, fromFieldNames, toViewIDs,0);
        ListView myList = findViewById(R.id.listView);
        myList.setAdapter(myCursorAdapter);
    }
}
