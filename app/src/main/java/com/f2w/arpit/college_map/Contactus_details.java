package com.f2w.arpit.college_map;

import android.content.Intent;
import android.net.Uri;

import static android.support.v4.content.ContextCompat.startActivity;

public class Contactus_details {
    private int id;
    private String name;
    private String email;
    private String dept;
    private String call;
    private int image;

    public Contactus_details(int id, String name, String email, String dept, String call,int image) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dept = dept;
        this.call=call;
        this.image=image;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getDept() {
        return dept;
    }

    public String getCall() {
        return call;

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setDept(String dept) {
        this.dept = dept;
    }

    public void setCall(String call) {
        this.call = call;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
