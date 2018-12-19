package com.inspiregeniussquad.handstogether.appData;

import android.net.Uri;

public class NewsFeedItems {

    private String nfId;
    private String tName, eName, eDesc, eDate, eTime, pTime, vName, vidUrl, pstrUrl, eVenue, postedBy;
    private String likes;
    private String pDate;
    private Comments comments;
    private String commentCount;
    private Uri posterUri;

    private boolean isLiked, isBookmarked;

    public NewsFeedItems() {

    }

    public boolean isLiked() {
        return isLiked;
    }

    public void setLiked(boolean liked) {
        isLiked = liked;
    }

    public String getpDate() {
        return pDate;
    }

    public void setpDate(String pDate) {
        this.pDate = pDate;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String geteName() {
        return eName;
    }

    public void seteName(String eName) {
        this.eName = eName;
    }

    public String geteDesc() {
        return eDesc;
    }

    public void seteDesc(String eDesc) {
        this.eDesc = eDesc;
    }

    public String geteDate() {
        return eDate;
    }

    public void seteDate(String eDate) {
        this.eDate = eDate;
    }


    public String geteTime() {
        return eTime;
    }

    public void seteTime(String eTime) {
        this.eTime = eTime;
    }

    public String getpTime() {
        return pTime;
    }

    public void setpTime(String pTime) {
        this.pTime = pTime;
    }

    public String getvName() {
        return vName;
    }

    public void setvName(String vName) {
        this.vName = vName;
    }

    public String getVidUrl() {
        return vidUrl;
    }

    public void setVidUrl(String vidUrl) {
        this.vidUrl = vidUrl;
    }

    public String getPstrUrl() {
        return pstrUrl;
    }

    public void setPstrUrl(String pstrUrl) {
        this.pstrUrl = pstrUrl;
    }

    public String geteVenue() {
        return eVenue;
    }

    public void seteVenue(String eVenue) {
        this.eVenue = eVenue;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }

    public Comments getComments() {
        return comments;
    }

    public void setComments(Comments comments) {
        this.comments = comments;
    }

    public String getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(String commentCount) {
        this.commentCount = commentCount;
    }

    public String getNfId() {
        return nfId;
    }

    public void setNfId(String nfId) {
        this.nfId = nfId;
    }

    public boolean isBookmarked() {
        return isBookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        isBookmarked = bookmarked;
    }

    public Uri getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(Uri posterUri) {
        this.posterUri = posterUri;
    }
}
