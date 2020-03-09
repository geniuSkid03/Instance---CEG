package com.instance.ceg.appViews;


import com.instance.ceg.appData.NavigationItem;

public class NavigationTwo extends NavigationItem {

    private int imgResId;
    private String name;
    private boolean isSelected = false;

    public NavigationTwo(int imgResId, String name){
        setViewType(2);

        this.imgResId = imgResId;
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getImgResId() {
        return imgResId;
    }

    public String getName() {
        return name;
    }

}
