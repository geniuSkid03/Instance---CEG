package com.instance.ceg.appFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.instance.ceg.R;
import com.instance.ceg.appActivities.TeamsActivity;
import com.instance.ceg.appAdapters.ClubsRecyclerAdapter;
import com.instance.ceg.appData.Clubs;
import com.instance.ceg.appData.Keys;
import com.instance.ceg.appInterfaces.FragmentInterfaceListener;
import com.instance.ceg.appUtils.AppHelper;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ClubsFragment extends SuperFragment {

    private RecyclerView clubsRv;
    private LinearLayout noClubsView;

    private FragmentInterfaceListener fragmentRefreshListener;

    private ArrayList<Clubs> clubsArrayList;
    private ClubsRecyclerAdapter clubsRecyclerAdapter;


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
        return initView(inflater.inflate(R.layout.fragment_clubs, container, false));
    }

    private View initView(View view) {

        clubsRv = view.findViewById(R.id.clubs_recycler);
        noClubsView = view.findViewById(R.id.no_clubs_view);

//        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
//        clubsRv.setLayoutManager(staggeredGridLayoutManager);

        clubsRv.setLayoutManager(new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false));

        clubsArrayList = new ArrayList<>();
        clubsRecyclerAdapter = new ClubsRecyclerAdapter(getContext(), clubsArrayList, new ClubsRecyclerAdapter.ClubClickListener() {
            @Override
            public void onClicked(int position) {
                goTo(getContext(), TeamsActivity.class, false, Keys.CLUBS_ID, gson.toJson(clubsArrayList.get(position)));
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        clubsRv.setAdapter(clubsRecyclerAdapter);
        refreshClubsFragment();
    }

    public void refreshClubsFragment() {
        showLoadingView();

        clubDbReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    AppHelper.print("News exists and trying to retrive!");
                    retriveClubsFromDb(dataSnapshot);
                } else {
                    AppHelper.print("No Clubs found!");
                    updateUi();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                AppHelper.print("Database error: " + databaseError);
                updateUi();
            }
        });
    }

    private void retriveClubsFromDb(DataSnapshot dataSnapshot) {
        Map<String, Clubs> clubsMap = (Map<String, Clubs>) dataSnapshot.getValue();

        clubsArrayList.clear();

        for (Map.Entry<String, Clubs> teamEntry : clubsMap.entrySet()) {
            Map map = (Map) teamEntry.getValue();

            Clubs clubs = new Clubs((String) map.get("clubsName"), (String) map.get("clubsImgUrl"));
            clubs.setClubsId((String) map.get("clubsId"));
            clubsArrayList.add(clubs);
        }

        updateUi();
    }

    private void showLoadingView() {
        clubsRv.setVisibility(View.GONE);
        noClubsView.setVisibility(View.GONE);
    }

    private void hideLoadingView() {
    }

    private void updateUi() {
        hideLoadingView();

        clubsRv.setVisibility(clubsArrayList.size() > 0 ? View.VISIBLE : View.GONE);
        noClubsView.setVisibility(clubsArrayList.size() > 0 ? View.GONE : View.VISIBLE);

        if (clubsArrayList.size() > 0) {
            animateWithData(clubsRv);
        }
    }

    private void animateWithData(RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.fall_down_layout_anim);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }

    public void setFragmentRefreshListener(FragmentInterfaceListener fragmentRefreshListener) {
        this.fragmentRefreshListener = fragmentRefreshListener;
    }

    public FragmentInterfaceListener getFragmentRefreshListener() {
        return fragmentRefreshListener;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            if (getFragmentRefreshListener() != null) {
                getFragmentRefreshListener().refreshFragments();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        clubsArrayList.clear();
    }
}
