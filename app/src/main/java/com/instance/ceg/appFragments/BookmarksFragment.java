package com.instance.ceg.appFragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.instance.ceg.R;
import com.instance.ceg.appInterfaces.FragmentInterfaceListener;

public class BookmarksFragment extends SuperFragment {

    private FragmentInterfaceListener fragmentInterfaceListener;

    private RecyclerView bookmarksRv;
    private LinearLayout noBookmarksRv;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
//        super.onPrepareOptionsMenu(menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_filter_search);
        searchMenuItem.setVisible(false);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initView(inflater.inflate(R.layout.fragment_bookmarks, container, false));
    }

    private View initView(View view) {


        return view;
    }

    public void refreshBookmarks() {

    }

    private FragmentInterfaceListener getFragmentInterfaceListener() {
        return fragmentInterfaceListener;
    }

    public void setFragmentInterfaceListener(FragmentInterfaceListener  fragmentInterfaceListener) {
        this.fragmentInterfaceListener = getFragmentInterfaceListener();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser) {
            if(getFragmentInterfaceListener() != null) {
                getFragmentInterfaceListener().refreshFragments();
            }
        }
    }
}
