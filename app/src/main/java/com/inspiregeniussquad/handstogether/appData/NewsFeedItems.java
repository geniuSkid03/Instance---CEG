package com.inspiregeniussquad.handstogether.appData;

public class NewsFeedItems {

    private String name, logoUrl, posterImageUrl, title, eventDate, venue, eventDesc, postedDate, postedTime;
    private byte[] logoDrawable, posterDrawable;
    private int id;

    public NewsFeedItems() {

    }

    public NewsFeedItems(String name, String title, String eventDesc, String eventDate, String postedDate, String postedTime, String posterImageUrl) {
        this.name = name;
        this.title = title;
        this.eventDate = eventDate;
        this.eventDesc = eventDesc;
        this.postedDate = postedDate;
        this.postedTime = postedTime;
        this.posterImageUrl = posterImageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getPosterImageUrl() {
        return posterImageUrl;
    }

    public void setPosterImageUrl(String posterImageUrl) {
        this.posterImageUrl = posterImageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getEventDesc() {
        return eventDesc;
    }

    public void setEventDesc(String eventDesc) {
        this.eventDesc = eventDesc;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getPostedTime() {
        return postedTime;
    }

    public void setPostedTime(String postedTime) {
        this.postedTime = postedTime;
    }

    public byte[] getLogoDrawable() {
        return logoDrawable;
    }

    public void setLogoDrawable(byte[] logoDrawable) {
        this.logoDrawable = logoDrawable;
    }

    public byte[] getPosterDrawable() {
        return posterDrawable;
    }

    public void setPosterDrawable(byte[] posterDrawable) {
        this.posterDrawable = posterDrawable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
