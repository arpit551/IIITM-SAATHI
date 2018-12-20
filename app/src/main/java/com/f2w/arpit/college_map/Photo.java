package com.f2w.arpit.college_map;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

public class Photo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Presentation Hall",
                        "Block-I",
                        "Block-II",
                        "Block-III",
                        "Block-IV",
                        "Block-V",
                        "Block-VI",
                        "LT-1",
                        "LT-2"
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                        .commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_photo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }
            ImageView imageView =(ImageView)view.findViewById(android.R.id.text1);
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }
    @Override
    public void onBackPressed() {

        Intent intent = new Intent(Photo.this, MapsActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);

            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_photo, container, false);
//            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
//            ImageView imageView=(ImageView)rootView.findViewById(R.id.pic) ;
            SubsamplingScaleImageView imageView1 = (SubsamplingScaleImageView)rootView.findViewById(R.id.imageView);

// ... or ...
            if(getArguments().getInt(ARG_SECTION_NUMBER)==1)
                imageView1.setImage(ImageSource.resource(R.drawable.plan));
            if(getArguments().getInt(ARG_SECTION_NUMBER)==2)
                imageView1.setImage(ImageSource.resource(R.drawable.p1));
//            imageView1.setOrientation();
            if(getArguments().getInt(ARG_SECTION_NUMBER)==3)
                imageView1.setImage(ImageSource.resource(R.drawable.p2));
            if(getArguments().getInt(ARG_SECTION_NUMBER)==4)
                imageView1.setImage(ImageSource.resource(R.drawable.p3));
//            imageView1.setOrientation();
            if(getArguments().getInt(ARG_SECTION_NUMBER)==5)
                imageView1.setImage(ImageSource.resource(R.drawable.p4));
            if(getArguments().getInt(ARG_SECTION_NUMBER)==6)
                imageView1.setImage(ImageSource.resource(R.drawable.p5));
//            imageView1.setOrientation();
            if(getArguments().getInt(ARG_SECTION_NUMBER)==7)
                imageView1.setImage(ImageSource.resource(R.drawable.p6));
            if(getArguments().getInt(ARG_SECTION_NUMBER)==8)
                imageView1.setImage(ImageSource.resource(R.drawable.lt1));
            if(getArguments().getInt(ARG_SECTION_NUMBER)==9)
                imageView1.setImage(ImageSource.resource(R.drawable.lt2));
//            imageView1.setOrientation();


//
//            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }
}