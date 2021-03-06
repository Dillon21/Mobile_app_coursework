package com.example.foodsafety;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsafety.json.business;
import com.example.foodsafety.json.businessRepository;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static androidx.core.content.ContextCompat.startActivity;

//Adapter for Display business activity

public class BusinessAdapterView extends RecyclerView.Adapter<BusinessAdapterView.BusinessViewHolder> {

    private Context context;

    private ArrayList<business> businesses;

    public BusinessAdapterView(Context context, ArrayList<business> businesses){
        this.context = context;
        this.businesses = businesses;

    }

    @NonNull
    @NotNull
    @Override
    public BusinessViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View businessView = LayoutInflater.from(context).inflate(R.layout.list_card_view, parent, false);
        BusinessViewHolder viewHolder = new BusinessViewHolder(businessView, this);
        return viewHolder;
    }


    //implement list to recyclerview
    @Override
    public void onBindViewHolder(@NonNull @NotNull BusinessViewHolder holder, int position) {
        business business = this.businesses.get(position);


        TextView tv_rating = holder.BusinessView.findViewById(R.id.tv_businessRating);
        TextView tv_businessName = holder.BusinessView.findViewById(R.id.tv_businessName);
        TextView tv_lat = holder.BusinessView.findViewById(R.id.tv_latitude);
        TextView tv_lon = holder.BusinessView.findViewById(R.id.tv_longitude);
        TextView tv_ID = holder.BusinessView.findViewById(R.id.tv_ID);

        //input business values into textviews
        tv_rating.setText(business.getRating());
        tv_businessName.setText(business.getBusiness_name());
        tv_lat.setText(business.getLatitude());
        tv_lon.setText(business.getLongitude());

        //set card value id
        String ID = String.valueOf(business.getID());
        tv_ID.setText(ID);

    }

    @Override
    public int getItemCount(){
        return businesses.size();
    }

    public class BusinessViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View BusinessView;
        private BusinessAdapterView adapter;
        public TextView lat;
        public TextView lon;
        public TextView ID;
        public TextView Name;
        public TextView rating;


        public BusinessViewHolder(View BusinessView, BusinessAdapterView adapter){
            super(BusinessView);
            this.BusinessView = BusinessView;
            this.adapter = adapter;
            this.BusinessView.setOnClickListener(this);




            BusinessView.findViewById(R.id.btn_location).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lat = itemView.findViewById(R.id.tv_latitude);
                    lon = itemView.findViewById(R.id.tv_longitude);

                    String Lat = (String) lat.getText();
                    String Lon = (String) lon.getText();

                    String cordinates = "geo:" + Lat + "," + Lon + "?z=20&q=restaurants";
                    Log.d("cordds", cordinates);
                    Uri mapsUri = Uri.parse(cordinates);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, mapsUri);
                    mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(context,mapIntent, null);

                }
            });

            BusinessView.findViewById(R.id.btn_favourite).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    lat = itemView.findViewById(R.id.tv_latitude);
                    lon = itemView.findViewById(R.id.tv_longitude);
                    ID = itemView.findViewById(R.id.tv_ID);
                    Name = itemView.findViewById(R.id.tv_businessName);
                    rating =itemView.findViewById(R.id.tv_businessRating);

                    String Lat = (String) lat.getText();
                    String Lon = (String) lon.getText();


                    business business = new business();
                    business.setID(Integer.valueOf((String)ID.getText()));
                    business.setBusiness_name((String)Name.getText());
                    business.setRating((String)rating.getText());
                    business.setLatitude((String)lat.getText());
                    business.setLongitude((String)lon.getText());
                    //test if text grab was successful
                    Log.d("favTest", business.getBusiness_name());
                    //store business into database
                    businessRepository.getRepository(context).storeBusiness(business);
                }
            });





        }

        @Override
        public void onClick(View view){
            int position = getAbsoluteAdapterPosition();

            business business = businesses.get(position);





        }
    }
}
