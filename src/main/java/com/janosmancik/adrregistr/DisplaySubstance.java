package com.janosmancik.adrregistr;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
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

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            //ziskam ID ktere se ma editovat/zobrazit/mazat poslane z main aktivity
            int value = extras.getInt("id");
            if (value >0)
            {
                //z database vytahnu zaznam pod hledanym ID a ulozim do id_To_Update
                Cursor rs = mydb.getData(value);
                id_To_Update = value;
                rs.moveToFirst();

                //vytahnu zaznam se jmenem
                String nam = rs.getString(rs.getColumnIndex(DBHelper.KEY_NAME));

                if (!rs.isClosed())
                {
                    rs.close();
                }

                name.setText(nam);
                name.setEnabled(false);
                name.setFocusable(false);
                name.setClickable(false);

            }
        }
    }

}
