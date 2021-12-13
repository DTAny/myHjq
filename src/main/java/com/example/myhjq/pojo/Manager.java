package com.example.myhjq.pojo;

public class Manager {
    private int mid;
    private String macc;
    private String mpwd;
    private String mname;

    public Manager(){

    }

    public Manager(int mid, String macc, String mpwd, String mname) {
        this.mid = mid;
        this.macc = macc;
        this.mpwd = mpwd;
        this.mname = mname;
    }

    public int getMid(){
        return mid;
    }
    public void setMid(int mid){
        this.mid = mid;
    }
    public String getMacc(){
        return macc;
    }
    public void setMacc(String macc){
        this.macc = macc;
    }
    public String getMpwd(){
        return mpwd;
    }
    public void setMpwd(String mpwd){
        this.mpwd = mpwd;
    }
    public String getMname(){
        return mname;
    }
    public void setMname(String mname){
        this.mname = mname;
    }

}
