package com.inspiregeniussquad.handstogether.appData;

import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;

public class Users {

    private String name, email, mobile, gender, imgUrl;
    private ArrayList<String> likedPosts, commentedPosts, bookmarkedPosts;

    public Users() {

    }

    public Users(String name, String email, String mobile, String gender, ArrayList<String> likedPosts,
                 ArrayList<String> commentedPosts, ArrayList<String> bookmarkedPosts) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
        this.likedPosts = likedPosts;
        this.commentedPosts = commentedPosts;
        this.bookmarkedPosts = bookmarkedPosts;

        AppHelper.print("Users Model: "+this.name+"\t"+this.email+"\t"+this.mobile+"\n\tthis.gender"+"\t"+this.commentedPosts+"\t"+this.bookmarkedPosts);
    }

    public Users(String name, String email, String mobile, String gender) {
//        this.StudentID = StudentID;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.gender = gender;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public ArrayList<String> getLikedPosts() {
        return likedPosts;
    }

    public void setLikedPosts(ArrayList<String> likedPosts) {
        this.likedPosts = likedPosts;
    }

    public ArrayList<String> getCommentedPosts() {
        return commentedPosts;
    }

    public void setCommentedPosts(ArrayList<String> commentedPosts) {
        this.commentedPosts = commentedPosts;
    }

    public ArrayList<String> getBookmarkedPosts() {
        return bookmarkedPosts;
    }

    public void setBookmarkedPosts(ArrayList<String> bookmarkedPosts) {
        this.bookmarkedPosts = bookmarkedPosts;
    }
}
