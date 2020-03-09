package com.instance.ceg.appData;

import java.util.ArrayList;

public class NewsFeedItems {

    private String nfId;
    private String tName, eName, eDesc, eDate, eTime, pTime, vName, vidUrl, pstrUrl, eVenue, postedBy;
    private String pDate;
    private Comments comments;
    private String posterUri;
    private ArrayList<String> likedUsers;
    private String tLogo;
    private long timeStamp;

    private long likesCount, commentCount;

    private boolean isLiked, isBookmarked;

    public NewsFeedItems() {

    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
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

    public String getPosterUri() {
        return posterUri;
    }

    public void setPosterUri(String posterUri) {
        this.posterUri = posterUri;
    }

    public long getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(long likesCount) {
        this.likesCount = likesCount;
    }

    public long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(long commentCount) {
        this.commentCount = commentCount;
    }

    public ArrayList<String> getLikedUsers() {
        return likedUsers;
    }

    public void setLikedUsers(ArrayList<String> likedUsers) {
        this.likedUsers = likedUsers;
    }


    public String gettLogo() {
        return tLogo;
    }

    public void settLogo(String tLogo) {
        this.tLogo = tLogo;
    }
}
