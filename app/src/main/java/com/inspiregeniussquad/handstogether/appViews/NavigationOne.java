package com.inspiregeniussquad.handstogether.appViews;


import com.inspiregeniussquad.handstogether.appData.NavigationItem;

public class NavigationOne extends NavigationItem {


    private String name, mobileNumber;

    public NavigationOne(String name, String mobileNumber){
        setViewType(1);

        this.name = name;
        this.mobileNumber = mobileNumber;
    }

    public NavigationOne() {

    }

    public String getName() {
        return name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
