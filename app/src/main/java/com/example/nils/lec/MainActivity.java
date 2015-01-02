package com.example.nils.lec;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends ListActivity {


    private ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

       listview = getListView();


        ArrayList<MyItem> myItems= new ArrayList<MyItem>();

        myItems.add(new MyItem("Proximity Sensor", "Evaluate distance between your phone and a beacon"));
        myItems.add(new MyItem(R.drawable.mole, "Titre 2", "Description 2"));
        myItems.add(new MyItem("Titre 3", "Description 3"));


        MyItemsAdapter myItemsAdapter =
                new MyItemsAdapter(this, myItems);

        setListAdapter(myItemsAdapter);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d("Debug", "Position = " + position);
    }

}
