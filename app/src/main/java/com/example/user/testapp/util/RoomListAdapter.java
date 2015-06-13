package com.example.user.testapp.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.testapp.R;
import com.example.user.testapp.model.Room;

import java.util.List;

public class RoomListAdapter  extends ArrayAdapter<Room> {
    public RoomListAdapter(Context context, List<Room> list) {

        super(context, R.layout.room_listview_item,list);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Room Room = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.room_listview_item, null);
        }
        ((TextView) convertView.findViewById(R.id.roomDateLongTxt))
                .setText("Duration: "+Room.getDateLong());
        ((TextView) convertView.findViewById(R.id.roomDateTxt))
                .setText("Start date: "+Room.getDateStart());
        ((TextView) convertView.findViewById(R.id.roomTypeTxt))
                .setText("Type: "+Room.getType());
        ((TextView) convertView.findViewById(R.id.RoomDescTxt))
                .setText(Room.getDescription());

        return convertView;
    }
}
