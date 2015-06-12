package com.example.user.testapp.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.testapp.R;
import com.example.user.testapp.model.Mark;

import java.util.List;

public class MarkListAdapter extends ArrayAdapter<Mark> {
    public MarkListAdapter(Context context, List<Mark>list) {

        super(context, R.layout.mark_listview_item,list);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Mark mark = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.mark_listview_item, null);
        }
        ((TextView) convertView.findViewById(R.id.textView2))
                .setText(mark.getName());
        ((TextView) convertView.findViewById(R.id.textView3))
                .setText(mark.getDescription());
        return convertView;
    }
}
