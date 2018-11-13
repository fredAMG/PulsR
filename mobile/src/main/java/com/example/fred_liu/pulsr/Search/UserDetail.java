package com.example.fred_liu.pulsr.Search;


public class UserDetail {

    private String d_name;
    private String d_email;
    private String d_date;


    public UserDetail(String d_name, String d_email, String d_date) {
        super();
        this.d_name = d_name;
        this.d_email = d_email;
        this.d_date = d_date;

    }




    public String getName() {
        return d_name;
    }

    public void setName(String name) {
        this.d_name = name;
    }

    public String getEmail() {
        return d_email;
    }

    public void setEmail(String email) {
        this.d_email = email;
    }

    public String getDate() {
        return d_date;
    }

    public void setDate(String date) {
        this.d_date = date;
    }

}
