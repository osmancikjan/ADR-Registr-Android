package com.janosmancik.adrregistr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Honza on 11.12.2017.
 */

public class SubstanceAdapter extends ArrayAdapter<SubstanceObjectModel> {

    public SubstanceAdapter(Context context, ArrayList<SubstanceObjectModel> substances) {
        super(context, 0, substances);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        SubstanceObjectModel substance = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);
        }
        // Lookup view for data population
        TextView textName = convertView.findViewById(R.id.textName);
        TextView textKemler = convertView.findViewById(R.id.textKemler);
        TextView textUN = convertView.findViewById(R.id.textUN);
        // Populate the data into the template view using the data object
        textName.setText(substance.getLatka());
        textKemler.setText(substance.getKemler());
        textUN.setText(substance.getUn());
        // Return the completed view to render on screen
        return convertView;
    }
}
