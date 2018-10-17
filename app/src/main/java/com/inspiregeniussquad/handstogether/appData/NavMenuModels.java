package com.inspiregeniussquad.handstogether.appData;
public class NavMenuModels {

    public String menuTag;
    public int menuIconId;
    public boolean isGroup;
    public boolean hasChildren;
    public boolean isSelected;

    public NavMenuModels(int menuIconId, String menuTag, boolean isGroup, boolean hasChildren) {
        this.menuIconId = menuIconId;
        this.menuTag = menuTag;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
