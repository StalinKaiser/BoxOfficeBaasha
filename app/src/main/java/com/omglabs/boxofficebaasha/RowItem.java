package com.omglabs.boxofficebaasha;

import java.io.Serializable;

/**
 * Created by anusha on 24/05/16.
 */
public class RowItem implements Serializable{
    private String title;
    private String theatre;
    private String date;
    private String time;
    private String count;
    private String postedUser;
    private String tickettype;
    private String ticketprice;

    //private int imgIcon;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTicketType() {
        return tickettype;
    }

    public void setTicketType(String tickettype) {
        this.tickettype = tickettype;
    }

    public String getTicketprice() {
        return ticketprice;
    }

    public void setTicketprice(String ticketprice) {
        this.ticketprice = ticketprice;
    }

    public String getTheatre() {
        return theatre;
    }

    public void setTheatre(String theatre) {
        this.theatre = theatre;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPostedUser() {
        return postedUser;
    }

    public void setPostedUser(String postedUser) {
        this.postedUser = postedUser;
    }

    /*public int getImgIcon() {
        return imgIcon;
    }*/

    /*public void setImgIcon(int imgIcon) {
        this.imgIcon = imgIcon;
    }*/
}

