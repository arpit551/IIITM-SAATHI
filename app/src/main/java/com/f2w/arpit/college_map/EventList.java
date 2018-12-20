package com.f2w.arpit.college_map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EventList extends AppCompatActivity {

    private ListView lvcontactus;
    private EventListAdapter adapter;
    private List<Contactus_details> mContactus_detailslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);
        lvcontactus=(ListView)findViewById(R.id.listview_event);
        mContactus_detailslist =new ArrayList<>();

        mContactus_detailslist.add(new Contactus_details(1,"Registration","Convention Center","#","#",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(2,"Inauguration Session","Administrative Block","#","#",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(3,"Case Study Presentation","LT-1","#","#",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(4,"Quiz","Main Pandaal","#","#",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(5,"Food Court","Main Pandaal","#","#",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(6,"Knowledge Test","Main Pandaal","#","#",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(7,"Kit Distribution","Convention Center","#","#",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(8,"Award Distribution","Administrative Block","#","#",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(9,"Refresher Program","Convention Center","#","#",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(10,"Judges Meet","Administrative Block","#","#",R.drawable.barry_allen));

        adapter=new EventListAdapter(getApplicationContext(), mContactus_detailslist);
        lvcontactus.setAdapter(adapter);

        lvcontactus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Click on marker to locate Destination",Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        Intent intent = new Intent(Photo.this, MapsActivity.class);
//        startActivity(intent);
        finish();

//        Intent intent = new Intent(EventList.this, MapsActivity.class);
//        startActivity(intent);
//        finish();
    }
}