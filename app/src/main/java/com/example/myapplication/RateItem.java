package com.example.myapplication;

public class RateItem {
    private String cname;
    private int id;
    private String cval;
    public RateItem(){
        super();
        cname="";
        cval= "";
    }
    public RateItem(String cname, String cval) {
        super();
        this.cname = cname;
        this.cval = cval;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCval() {
        return cval;
    }

    public void setCval(String cval) {
        this.cval = cval;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    @Override
    public String toString() {
        return "RateItem{" +
                "cname='" + cname + '\'' +
                ", id=" + id +
                ", cval='" + cval + '\'' +
                '}';
    }
}