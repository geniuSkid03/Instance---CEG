package com.inspiregeniussquad.handstogether.appAdapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appData.NavMenuModels;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpandableListAdapter extends BaseExpandableListAdapter {


    private Context context;
    private List<NavMenuModels> headerList;
    private HashMap<NavMenuModels, List<NavMenuModels>> childList;
    private LayoutInflater layoutInflater;
    private ImageView dropDownIv;
    private View headerView, childView;
    private int selectedGroupPosition = 0, selectedChildPosition = 0;

    public ExpandableListAdapter(Context context, List<NavMenuModels> headerList,
                                 HashMap<NavMenuModels, List<NavMenuModels>> childList) {
        this.context = context;
        this.headerList = headerList;
        this.childList = childList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < headerList.size(); i++) {
            AppHelper.print("Header:" + headerList.get(i).menuTag);
        }

        for (Map.Entry<NavMenuModels, List<NavMenuModels>> navMenuModelsListEntry : childList.entrySet()) {
            AppHelper.print("Child key: " + navMenuModelsListEntry.getKey().menuTag);
        }
    }

    @Override
    public NavMenuModels getChild(int groupPosition, int childPosition) {
        return this.childList.get(this.headerList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        String childName = getChild(groupPosition, childPosition).menuTag;
        int childIcon = getChild(groupPosition, childPosition).menuIconId;
        boolean isGroup = getChild(groupPosition, childPosition).isGroup;
        boolean isSelected = getChild(groupPosition, childPosition).isSelected;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_child, null);
        }

        ImageView childIconIv = convertView.findViewById(R.id.list_child_icon);
        TextView childTagTv = convertView.findViewById(R.id.list_child_tag);
        dropDownIv = convertView.findViewById(R.id.drop_down_view);

        childIconIv.setImageDrawable(ContextCompat.getDrawable(context, childIcon));
        childTagTv.setText(childName);

        childView = convertView.findViewById(R.id.selected_child_view);
        childView.setVisibility(childPosition == selectedChildPosition ? View.VISIBLE : View.INVISIBLE);

        return convertView;
    }

    public void setSelectedGroupAs(int selectedGroupPosition) {
        this.selectedGroupPosition = selectedGroupPosition;
    }

    public void setSelectedChildAs(int selectedChildPosition) {
        this.selectedChildPosition = selectedChildPosition;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        int childernCount;
        if (this.childList.get(this.headerList.get(groupPosition)) == null)
            childernCount = 0;
        else childernCount = this.childList.get(this.headerList.get(groupPosition)).size();

        AppHelper.print("Childern count: " + childernCount);

        return childernCount;
    }

    @Override
    public int getGroupCount() {
        return headerList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public NavMenuModels getGroup(int groupPosition) {
        return headerList.get(groupPosition);
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerName = getGroup(groupPosition).menuTag;
        int headerIcon = getGroup(groupPosition).menuIconId;
        boolean isGroup = getGroup(groupPosition).isGroup;
        boolean hasChildren = getGroup(groupPosition).hasChildren;
        boolean isSelected = getGroup(groupPosition).isSelected;

        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_group_header, null);
        }

        ((TextView) convertView.findViewById(R.id.list_header_tag)).setText(headerName);
        ((TextView) convertView.findViewById(R.id.list_header_tag)).setTypeface(null, Typeface.BOLD); //for bold text
        ((ImageView) convertView.findViewById(R.id.list_header_icon)).setImageDrawable(ContextCompat.getDrawable(context, headerIcon));

        dropDownIv = convertView.findViewById(R.id.drop_down_view);
        dropDownIv.setVisibility(hasChildren ? View.VISIBLE : View.GONE);
        dropDownIv.setImageDrawable(isExpanded ?
                ContextCompat.getDrawable(context, R.drawable.ic_arrow_up) :
                ContextCompat.getDrawable(context, R.drawable.ic_arrow_down));

        headerView = convertView.findViewById(R.id.selected_header_view);
        headerView.setVisibility(groupPosition == selectedGroupPosition ? View.VISIBLE : View.INVISIBLE);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}