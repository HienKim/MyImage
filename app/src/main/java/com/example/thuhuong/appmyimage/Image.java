package com.example.thuhuong.appmyimage;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ThuHuong on 21/05/2017.
 */

public class Image {
    private String name;
    private String url;
    private String timeCreate;

    public Image(String name, String url, String timeCreate) {
        this.name = name;
        this.url = url;
        this.timeCreate = timeCreate;
    }

    public Image(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Image() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTimeCreate() {
        return timeCreate;
    }

    public void setCreateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // tạo 1 đối tượng có định dạng thời gian yyyy-MM-dd HH:mm:ss
        Date date = new Date(); // lấy thời gian hệ thống
        String stringDate = dateFormat.format(date);//Định dạng thời gian theo trên
        this.timeCreate = stringDate;
    }
}
