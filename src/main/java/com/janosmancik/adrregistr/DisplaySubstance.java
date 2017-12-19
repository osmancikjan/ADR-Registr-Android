package com.janosmancik.adrregistr;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by Honza on 11.12.2017.
 */

public class DisplaySubstance extends Activity {

    private DBHelper mydb;

    TextView kemler;
    TextView un;
    TextView name;

    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.row);

        name = findViewById(R.id.textName);
        kemler = findViewById(R.id.textKemler);
        un = findViewById(R.id.textUN);

        Intent intent = getIntent();
            //ziskam ID ktere se ma editovat/zobrazit/mazat poslane z main aktivity
                String value = intent.getStringExtra("un");
                Log.i("value", value);
                if (value.length() > 0) {
                    //z database vytahnu zaznam pod hledanym ID a ulozim do id_To_Update
                    String stringUN = "";
                    String stringNazev = "";
                    try (Cursor rs = mydb.getData(value)) {
                        rs.moveToFirst();

                        //vytahnu zaznam se jmenem
                        stringUN = rs.getString(mydb.COL_UN);
                        stringNazev = rs.getString(mydb.COL_NAME);

                        if (!rs.isClosed()) {
                            rs.close();
                        }
                    }

                    un.setText(stringUN);
                    name.setText(stringNazev);
                }
    }

}
