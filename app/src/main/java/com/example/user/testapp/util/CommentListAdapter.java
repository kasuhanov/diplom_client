package com.example.user.testapp.util;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.user.testapp.R;
import com.example.user.testapp.model.Comment;
import com.example.user.testapp.model.Mark;

import java.util.List;

public class CommentListAdapter extends ArrayAdapter<Comment>{
    public CommentListAdapter(Context context, List<Comment> list) {

        super(context, R.layout.comment_listview_item,list);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Comment comment = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.comment_listview_item, null);
        }
        ((TextView) convertView.findViewById(R.id.userTxt))
                .setText(comment.getLogin());
        ((TextView) convertView.findViewById(R.id.commentTxt))
                .setText(comment.getComment());
        return convertView;
    }
}
