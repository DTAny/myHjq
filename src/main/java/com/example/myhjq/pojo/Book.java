package com.example.myhjq.pojo;

public class Book {
    private int bid;
    private String bname;
    private String author;
    private boolean state;

    public Book(){}

    public int getBid(){
        return this.bid;
    }
    public void setBid(int bid){
        this.bid = bid;
    }
    public String getBname(){
        return this.bname;
    }
    public void setBname(String bname){
        this.bname = bname;
    }
    public String getAuthor(){
        return this.author;
    }
    public void setAuthor(String author){
        this.author = author;
    }
    public boolean getState(){
        return this.state;
    }
    public void setState(boolean state){
        this.state = state;
    }
}
