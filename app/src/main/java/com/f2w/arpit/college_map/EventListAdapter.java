package com.f2w.arpit.college_map;


import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

public class EventListAdapter extends BaseAdapter {

    private Context mContext;
    private List<Contactus_details> mContactus_detailslist;

//    private static final EventListAdapter ourInstance = new EventListAdapter();

    public EventListAdapter (Context mContext, List<Contactus_details> mContactus_detailslist) {
        this.mContext = mContext;
        this.mContactus_detailslist = mContactus_detailslist;
    }

    @Override
    public int getCount() {
        return mContactus_detailslist.size();
    }

    @Override
    public Object getItem(int position) {
        return mContactus_detailslist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(mContext,R.layout.event_card,null);
        final TextView person_name=(TextView)view.findViewById(R.id.event_name);

        final TextView person_email=(TextView)view.findViewById(R.id.destination);

//
//        TextView person_department=(TextView)view.findViewById(R.id.person_department);

        AssetManager assetManager = mContext.getAssets();

        person_name.setTextColor(Color.BLACK);

        ImageView person_call=(ImageView) view.findViewById(R.id.go_button);



        person_name.setText(mContactus_detailslist.get(position).getName());
        person_email.setText(mContactus_detailslist.get(position).getEmail());
//        person_department.setText(mContactus_detailslist.get(position).getDept());
        final String number =mContactus_detailslist.get(position).getCall();

        person_call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MapsActivity.class);
                intent.putExtra("Destination", person_email.getText().toString());
                intent.putExtra("EventPressed", "true");
                mContext.startActivity(intent);
            }
        });

        person_call.setImageResource(R.drawable.mapicon);
        view.setTag(mContactus_detailslist.get(position).getId());
        return view;
    }

//    private EventListAdapter() {
//    }
}
