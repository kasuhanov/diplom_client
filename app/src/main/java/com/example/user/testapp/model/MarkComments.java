package com.example.user.testapp.model;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class MarkComments {
    @SerializedName("comments")
    private List<String> comments;
    @SerializedName("users")
    private List<String> users;

    public List<Comment> toComennts(){
        List<Comment> list= new ArrayList<Comment>();
        for(int i=0;i<comments.size();i++){
            list.add(new Comment(comments.get(i),0,users.get(i)));
        }
        return list;
    }
    public MarkComments(List<String> comments, List<String> users) {
        this.comments = comments;
        this.users = users;
    }

    public List<String> getComments() {
        return comments;
    }

    public void setComments(List<String> comments) {
        this.comments = comments;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
