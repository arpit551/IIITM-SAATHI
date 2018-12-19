package com.f2w.arpit.college_map;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

public class ContactUs extends AppCompatActivity {

    private ListView lvcontactus;
    private ContactusAdapter adapter;
    private List<Contactus_details> mContactus_detailslist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        lvcontactus=(ListView)findViewById(R.id.listview_contactus);
        mContactus_detailslist =new ArrayList<>();

        mContactus_detailslist.add(new Contactus_details(1,"Control Room","7512449606","Security","+917512449606",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(2,"Ambulance","108","Medical","108",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(3,"Mr S M Tahir","7869571718","Hall Management","+917869571718",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(4,"Mr Thomas Mathew","9993095996","Registration","+919993095996",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(5,"Mr Ajay Sharma","9009522168","Transportation","+919009522168",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(6,"Mr Devendra Singh","9926288778","Catering","+919926288778",R.drawable.barry_allen));

        adapter=new ContactusAdapter(getApplicationContext(), mContactus_detailslist);
        lvcontactus.setAdapter(adapter);

        lvcontactus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Toast.makeText(getApplicationContext(),"Press call icon to make the call",Toast.LENGTH_LONG).show();
            }
        });

    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(ContactUs.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }
}
