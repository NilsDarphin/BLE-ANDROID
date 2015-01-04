package com.example.nils.lec;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by nils on 02/01/15.
 */
public class ItemsAdapter extends ArrayAdapter<ItemList> {


    public ItemsAdapter(Context context, ArrayList<ItemList> appItems) {
        super(context, 0, appItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ItemList app = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_main, parent, false);

        // Lookup view for data population
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        TextView titre = (TextView) convertView.findViewById(R.id.titre);
        TextView description = (TextView) convertView.findViewById(R.id.description);

        // Populate the data into the template view using the data object
        titre.setText(app.getName());
        description.setText(app.getDescription());
        icon.setImageResource(app.getResId());

        // Return the completed view to render on screen
        return convertView;
    }
}
