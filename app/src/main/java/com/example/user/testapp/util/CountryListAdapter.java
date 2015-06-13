package com.example.user.testapp.util;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.testapp.R;
import com.example.user.testapp.model.Comment;
import com.example.user.testapp.model.Country;

import java.util.List;

public class CountryListAdapter extends ArrayAdapter<Country> {
    public CountryListAdapter(Context context, List<Country> list) {

        super(context, R.layout.country_spinner_item,list);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Country country = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.country_spinner_item, null);
        }
        ((TextView) convertView.findViewById(R.id.spinnerTxt))
                .setText(country.getName());

        return convertView;
    }
}
