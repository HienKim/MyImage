package com.example.thuhuong.appmyimage;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HienKim on 21/05/2017.
 */

public class Album implements Serializable {
    private String name;
    private ArrayList<Image> listPhoto;
    private String createTime;
    public Map<String, Boolean> stars = new HashMap<>();

    public Album(String name) {
        this.name = name;
    }

    public Album(String name, ArrayList<Image> listPhoto) {
        this.name = name;
        this.listPhoto = listPhoto;
    }

    public Album() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Image> getListPhoto() {
        return listPhoto;
    }

    public void setListPhoto(ArrayList<Image> listPhoto) {
        this.listPhoto = listPhoto;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // tạo 1 đối tượng có định dạng thời gian yyyy-MM-dd HH:mm:ss
        Date date = new Date(); // lấy thời gian hệ thống
        String stringDate = dateFormat.format(date);//Định dạng thời gian theo trên
        this.createTime = stringDate;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("listPhoto", listPhoto);
        result.put("createTime", createTime);
        result.put("stars", stars);

        return result;
    }
}
