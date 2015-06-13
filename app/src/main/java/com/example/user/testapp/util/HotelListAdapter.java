package com.example.user.testapp.util;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.testapp.R;
import com.example.user.testapp.model.Comment;
import com.example.user.testapp.model.Hotel;

import java.util.List;

public class HotelListAdapter extends ArrayAdapter<Hotel> {
    public HotelListAdapter(Context context, List<Hotel> list) {

        super(context, R.layout.hotel_listiew_item,list);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Hotel hotel = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.hotel_listiew_item, null);
        }
        ((TextView) convertView.findViewById(R.id.hotelNameTxt))
                .setText(hotel.getName());
        ((TextView) convertView.findViewById(R.id.HotelDescTxt))
                .setText(hotel.getDescription());
        ((TextView) convertView.findViewById(R.id.hotelStarTxt))
                .setText("Stars: "+hotel.getStar());

        return convertView;
    }
}
