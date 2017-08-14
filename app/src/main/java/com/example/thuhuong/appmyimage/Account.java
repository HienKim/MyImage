package com.example.thuhuong.appmyimage;

import java.io.Serializable;

/**
 * Created by HienKim on 21/05/2017.
 */

public class Account implements Serializable {

    private String id;
    private Album album;
    private String username;
    private String email;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Account(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
