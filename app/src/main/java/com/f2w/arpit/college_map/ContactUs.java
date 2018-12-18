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
        mContactus_detailslist.add(new Contactus_details(1,"JUHI TIWARI","juhi@infotsav.in","Coordinator","9461155105",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(2,"MUKUL SIKKA","mukul@infotsav.in","Coordinator","7470485414",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(3,"PRATYUSH RANJAN","pratyush@infotsav.in","Coordinator","9598319698",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(4,"SHUBHANKER SRIVASTAVA","shubhanker@infotsav.in","Coordinator","9454932137",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(5,"AVIHARSH SHUKLA","sidvaish4@gmail.com","Head App Developer","8765114937",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(6,"SIDDHARTH VAISH","sidvaish97@gmail.com","Head App Developer","9149386335",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(7,"HIMADRI SINGH","himsingh1612@gmail.com","App Developer","8449569368",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(8,"DEEPAK PALIWAL","deepakkpaliwal@gmail.com","App Developer","8769449558",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(9,"ABHISHEK CHAURASIA","abhi.chaurasia14@gmail.com","App Developer","9919098817",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(10,"ADITYA SHANKAR MISHRA","adityashankarmishra3@gmail.com","App Developer","9260970643",R.drawable.barry_allen));
        mContactus_detailslist.add(new Contactus_details(11,"SHUBHAM BHAWSAR","bhawsarshubham741@gmail.com","App Developer","8878674076",R.drawable.barry_allen));

        adapter=new ContactusAdapter(getApplicationContext(), mContactus_detailslist);
        lvcontactus.setAdapter(adapter);

        lvcontactus.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Item clicked",Toast.LENGTH_LONG).show();
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
