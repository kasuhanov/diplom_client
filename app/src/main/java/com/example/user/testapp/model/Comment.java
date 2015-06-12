package com.example.user.testapp.model;


import com.google.gson.annotations.SerializedName;

public class Comment {
    @SerializedName("login")
    private String login;
    @SerializedName("comment")
    private String comment;
    @SerializedName("id")
    private long id;

    public Comment(String comment, long id, String login) {
        this.comment = comment;
        this.id = id;
        this.login = login;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
