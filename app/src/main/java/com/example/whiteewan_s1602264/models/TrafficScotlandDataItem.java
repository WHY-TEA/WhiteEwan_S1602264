package com.example.whiteewan_s1602264.models;

import java.util.Date;

/*
 * Ewan White
 * S1602264
 * */

public class TrafficScotlandDataItem {

    private String dataTitle;
    private String dataDescription;
    private String link;
    private Date publishedDate;
    private Date startDate;
    private Date endDate;
    private long durationTime;
    private double latitude;
    private double longitude;



    public TrafficScotlandDataItem() {
        this.dataTitle = "";
        this.dataDescription = "";
        this.link = "";
        this.publishedDate = new Date();
        this.endDate = new Date();
        this.durationTime = 0;
        this.latitude = 0;
        this.longitude = 0;
    }



    public TrafficScotlandDataItem(String dataTitle, String dataDescription, String link, Date publishedDate, Date startDate, Date endDate, long durationTime, double latitude, double longitude) {
        this.dataTitle = dataTitle;
        this.dataDescription = dataDescription;
        this.link = link;
        this.publishedDate = publishedDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.durationTime = durationTime;
        this.latitude = latitude;
        this.longitude = longitude;
    }


    public String getDataTitle() {
        return dataTitle;
    }

    public String getDataDescription() {
        return dataDescription;
    }

    public String getLink() {
        return link;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public long getDurationTime() {
        return durationTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }



    public void setDataTitle(String dataTitle) {
        this.dataTitle = dataTitle;
    }

    public void setDataDescription(String dataDescription) {
        this.dataDescription = dataDescription;
    }

    public void setLink(String link) {
        this.link = link;
    }


    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setDurationTime(long durationTime) {
        this.durationTime = durationTime;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }




    @Override
    public String toString() {
        return "TrafficScotlandDataItem{" +
                "dataTitle='" + dataTitle + '\'' +
                ", dataDescription='" + dataDescription + '\'' +
                ", link='" + link + '\'' +
                ", publishedDate='" + publishedDate + '\'' +
                ", startDate='" + startDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", durationTime='" + durationTime + '\'' +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude+ '\'' +
                '}';
    }
}
