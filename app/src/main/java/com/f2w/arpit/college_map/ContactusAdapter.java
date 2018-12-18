package com.f2w.arpit.college_map;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ContactusAdapter extends BaseAdapter {

    private Context mContext;
    private List<Contactus_details> mContactus_detailslist;


    public ContactusAdapter(Context mContext, List<Contactus_details> mContactus_detailslist) {
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
        View view = View.inflate(mContext,R.layout.contactus_list,null);
        TextView person_name=(TextView)view.findViewById(R.id.person_name);

        TextView person_email=(TextView)view.findViewById(R.id.person_email);


        TextView person_department=(TextView)view.findViewById(R.id.person_department);

        AssetManager assetManager = mContext.getAssets();

        final Typeface tvFont = Typeface.createFromAsset(assetManager,  "fonts/Delius-Regular.ttf");
        person_name.setTypeface(tvFont);
        person_department.setTypeface(tvFont);
        person_email.setTypeface(tvFont);

        person_name.setTextColor(Color.BLACK);

        ImageView person_call=(ImageView) view.findViewById(R.id.call_person_button);
        ImageView person_image=(ImageView)view.findViewById(R.id.person_photo);



        person_name.setText(mContactus_detailslist.get(position).getName());
        person_email.setText(mContactus_detailslist.get(position).getEmail());
        person_department.setText(mContactus_detailslist.get(position).getDept());
        final String number =mContactus_detailslist.get(position).getCall();

        person_call.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) { Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:"+(number)));
                mContext.startActivity(callIntent);
            }
        });

        person_call.setImageResource(R.drawable.callbutton);
        person_image.setImageResource(mContactus_detailslist.get(position).getImage());
        view.setTag(mContactus_detailslist.get(position).getId());
        return view;
    }
}
