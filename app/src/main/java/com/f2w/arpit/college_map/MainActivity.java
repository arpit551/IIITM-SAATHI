package com.f2w.arpit.college_map;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

import ir.mirrajabi.searchdialog.SimpleSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;

public class MainActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

//    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        final String[] destination = new String[1];
        final String[] source = new String[1];

        findViewById(R.id.currentlocation).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v) {
                new SimpleSearchDialogCompat(MainActivity.this, "Search...", "Where are you now ?",
                        null, initData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        Toast.makeText(MainActivity.this,""+searchable.getTitle(),Toast.LENGTH_SHORT).show();
                        baseSearchDialogCompat.dismiss();
                        source[0] =searchable.getTitle();

                    }
                }).show();
            }
        });
        findViewById(R.id.destinationlocation).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick (View v) {
                new SimpleSearchDialogCompat(MainActivity.this, "Search...", "Where you want to go ?",
                        null, initData(), new SearchResultListener<Searchable>() {
                    @Override
                    public void onSelected(BaseSearchDialogCompat baseSearchDialogCompat, Searchable searchable, int i) {
                        Toast.makeText(MainActivity.this,""+searchable.getTitle(),Toast.LENGTH_SHORT).show();
                        baseSearchDialogCompat.dismiss();
                        destination[0] =searchable.getTitle();

                    }
                }).show();
            }
        });
        findViewById(R.id.go).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
              intent.putExtra("Destination", destination[0]);
              intent.putExtra("Origin", source[0]);
                startActivity(intent);
            }


        });
    }

    private ArrayList<SearchModel> initData() {
        ArrayList<SearchModel> items = new ArrayList<>();
        items.add(new SearchModel("Main Gate "));
        items.add(new SearchModel("Administrative Block"));
        items.add(new SearchModel("Learning Resource Center"));
        items.add(new SearchModel("New Auditorium"));
        items.add(new SearchModel("B-Block/LT-1"));
        items.add(new SearchModel("A-Block"));
        items.add(new SearchModel("C-Block"));
        items.add(new SearchModel("D-Block"));
        items.add(new SearchModel("E-Block"));
        items.add(new SearchModel("F-Block"));
        items.add(new SearchModel("Hospital"));
        items.add(new SearchModel("Sports Complex"));
        return items;
    }
}