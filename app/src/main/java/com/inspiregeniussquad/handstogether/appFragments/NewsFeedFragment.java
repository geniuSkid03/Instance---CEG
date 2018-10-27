package com.inspiregeniussquad.handstogether.appFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.LinearLayout;

import com.inspiregeniussquad.handstogether.R;
import com.inspiregeniussquad.handstogether.appActivities.NewsItemViewActivity;
import com.inspiregeniussquad.handstogether.appAdapters.NewsFeedAdapter;
import com.inspiregeniussquad.handstogether.appData.Keys;
import com.inspiregeniussquad.handstogether.appData.NewsFeedItems;
import com.inspiregeniussquad.handstogether.appUtils.AppHelper;

import java.util.ArrayList;

public class NewsFeedFragment extends SuperFragment implements SearchView.OnQueryTextListener {

    private RecyclerView newsFeedRv;
    private LinearLayout newsLoadingView;

    private boolean isRefreshing;

    private NewsFeedAdapter newsFeedAdapter;
    private ArrayList<NewsFeedItems> newsFeedItemsArrayList;

    private NewsFeedItems itemOne, itemTwo, itemThree, itemFour;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        //news items initializations
        newsFeedItemsArrayList = new ArrayList<>();

        newsFeedAdapter = new NewsFeedAdapter(getContext(), newsFeedItemsArrayList);
        newsFeedAdapter.setClickListener(new NewsFeedAdapter.onViewClickedListener() {
            @Override
            public void onViewClicked(int position) {
                onNewsItemClicked(newsFeedItemsArrayList.get(position));
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.home_search, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_filter_search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        searchView.setQueryHint(getString(R.string.search_event));

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    protected View inflateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return initNewsFeedView(inflater.inflate(R.layout.fragment_newsfeed, container, false));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        newsFeedRv.setAdapter(newsFeedAdapter);
    }

    private View initNewsFeedView(View view) {

        newsFeedRv = view.findViewById(R.id.news_feed_recycler_view);
        newsLoadingView = view.findViewById(R.id.news_loading_view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        newsFeedRv.setLayoutManager(linearLayoutManager);

        return view;
    }

    private void onNewsItemClicked(NewsFeedItems newsFeedItem) {
        goTo(getActivity(), NewsItemViewActivity.class, false, Keys.NEWS_ITEM, gson.toJson(newsFeedItem));
    }

    public void refreshNewsFeed() {
        //todo - get values from firebase and add it here in arraylist and notifyDataSetChanged

        newsFeedItemsArrayList.clear();

        AppHelper.print("Refreshing newsfeed");

        newsLoadingView.setVisibility(View.VISIBLE);
        newsFeedRv.setVisibility(View.GONE);

        itemOne = new NewsFeedItems();
        itemOne.setId(0);
        itemOne.setName("Intra variety competition");
        itemOne.setPostedDate("10-08-2018");
        itemOne.setEventDate("12.10.2018");
        itemOne.setPostedTime("15:00");
        itemOne.setEventDesc("We are happy to announce you this Intra variety show competition, gonna take place today in association with CEG Spartanz" + "The essence of the beauty is unity in Variety!!! \n" +
                "Illuminate the stage with colors!! \n" +
                "Reach out to the people on Arena with zest and fervour!! \n" +
                "Here's a platform to showcase your team's Uniqueness and talents!!\n" +
                "Get on the stage of Agni 2k18 and make the crowd go crazy !!\n" +
                "RULE LIST\n" +
                "1. Total time limit for the performance is 12 minutes including stage setup. \n" +
                "Time will be calculated from the moment, the first person of the team steps on \n" +
                "the stage. Time limit will be strictly followed.\n" +
                "2. ID cards of all performers need to be submitted during team registration.\n" +
                "3. Properties required for the performance should be brought by the team.\n" +
                "4. Slots will be lot based and no change of slot there-by.\n" +
                "5. Track should be sent to cegspartanz@gmail.com 1 day prior to the \n" +
                "performance. If any team fails to do so, they will be disqualified.\n" +
                "6. Judges decision is final. Argument leads to disqualification.\n" +
                "7. Vulgarity at any cost leads to disqualification.\n" +
                "8. Use of any flammable substances, fluids (Water ,Ink , etc ) and stage discrepancy is strictly condemned.\n" +
                "9. Track should not contain any political traces.\n" +
                "10. For any special performances like UV lightings etc please inform us 1 hour \n" +
                "before the event starts");
        itemOne.setTitle("Agni Intra Vareity show Competition");
        itemOne.setVenue("Vivek Auditorium");

        itemTwo = new NewsFeedItems();
        itemTwo.setId(1);
        itemTwo.setName("Informals");
        itemTwo.setPostedDate("10-08-2018");
        itemTwo.setEventDate("12/13-10-2018");
        itemTwo.setPostedTime("14:00");
        itemTwo.setEventDesc("Agni proudly presents Informals, where you can expose your extraordinary talent, " +
                "and it takes place in association with CEG Spartanz" + "Do you think 'Agni is special'? What is so great about Agni???\n" +
                "\n" +
                "Agni is filled with moments that make our friendship more and more special, more close to our hearts. There are numerous events trademarking Agni....but this is something that is more special ♥\n" +
                "\n" +
                "Come as a group along with your friends, get on to the stage, laugh harder, shout louder.....because this is.........'THE INFORMALS'.");
        itemTwo.setTitle("Expose your extraordinary");
        itemTwo.setVenue("Near Chemistry lab(SNH)");

        itemThree = new NewsFeedItems();
        itemThree.setId(2);
        itemThree.setName("CEG Spartanz");
        itemThree.setPostedDate("17-08-2018");
        itemThree.setPostedTime("06.30");
        itemThree.setEventDesc("Here we are celebrating the twelve years of our community, " +
                "till we have been emerging as one of the best teams with in and outside of our college" +
                "We started as a small group of variety show enthusiasts, lead by the team founders Ananth R and jagadeesan P, " +
                "the dynamic duo from Electrical and Mechanical Engineering departments. Spartanz as a team was moulded because of Vijay TV's EQ2 in which we won\n" + "THIRD Prize (Inter Variety) in REVIVALS'16 at MMC\n" +
                "WINNERS Intra Dance Competition Agni'16\n" +
                "RUNNERS (Inter Variety) in PRADHARSHINI'16 at KMC\n" +
                "THIRD PRIZE (Mime) in DEEP WOODS'17 at MCC\n" +
                "WINNERS (Inter Variety) in MITAFEST'17 at MIT\n" +
                "THIRD PRIZE (Intra Dance) in TECHOFES'17\n" +
                "WINNERS (Mime) in CIPOFEST'17 at CIPET\n" +
                "RUNNERS (Mime) in SAMGATHA'17 at IIIT-DM");
        itemThree.setTitle("12 Years of CEG Spartans - Celebration");
        itemThree.setVenue("Ceg spartans hall");

        itemFour = new NewsFeedItems();
        itemFour.setId(4);
        itemFour.setName("CEG Spartanz - Play");
        itemFour.setPostedDate("08-08-2018");
        itemFour.setPostedTime("06.30");
        itemFour.setEventDesc("Bravery in women is unleashed with the role play of First Women Warrior - Rani Velu Nachiyar in a play by Warrior from CEG Spartarz"+"Velu Nachiyar was the princess of Ramanathapuram and the only child of Raja Chellamuthu vijayaragunatha Sethupathy and Rani Sakandhimuthal of the Ramnad kingdom.\n" +
                "\n" +
                "She was a scion of suryavamsam and kasyapagotram like cholas. Ramnad sethupathis are suryavamsi kshatriyas and their predecessors were crowned in sethu(Rameswaram) by lord Vishnu during his incarnation as Sree Rama.She was known to be an extremely beautiful lady like draupadi the crown queen of pandavas. Some copperplates call her as virtuous as mangayarkkarasiyar the consort of king ninraseer nedumaran of pandyan dynasty who was patron of saint tirugnanasambandar.She was also trained in saivite and vaishnavite studies of both tamil,and Sanskrit traditions.She was also trained in works of ancient jurist karnisuta etc for criminal investigation and penal code formation.She proposed and implemented several reforms in trade like sangam cholas that led to development of democratic councils.As a result ancient nanadesi multinationals like narpattiyennayiravar, disai ayirattu elunnurruvar, manigramattar etc who find mention and praise in earliest of sangam works (12th century B.C.E), set up firms in Singapore, Malaya, Vietnam, and also in western sea.Other development works included laying roads, irrigation systems, , waterways, canals, building other transport infrastructure like shipping, libraries,schools , colleges,improving land defense and navy etc.\n" +
                "\n" +
                "Rani velu nachiyar was a great devotee of Lord Ayyanar or Sevugan, who is son of Lord Vishnu and Lord Shiva. Whenever she was in distress she visited the temple of Ayyanar in madappuram near sivagangai, to offer worship and other services. This temple finds mention and praise in earliest of sangam works and is supposed to be a place where Chera, Chola,Pandya and Velir kings offered worship prior to setting off to war fields. Ayyanar is supposed to have appeared before her and solved several of her problems. The sthala puranam or place history of this temple also says that The queen attained his beatitude here.");
        itemFour.setTitle("Play in Stage");
        itemFour.setVenue("");

        if (newsFeedItemsArrayList.size() < 4) {
            newsFeedItemsArrayList.add(itemOne);
            newsFeedItemsArrayList.add(itemTwo);
            newsFeedItemsArrayList.add(itemThree);
            newsFeedItemsArrayList.add(itemFour);

            newsFeedAdapter.notifyDataSetChanged();

            newsLoadingView.setVisibility(View.GONE);
            newsFeedRv.setVisibility(View.VISIBLE);

            animateWithData(newsFeedRv);
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

    @Override
    public void onDestroy() {
        super.onDestroy();

        newsFeedItemsArrayList.clear();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(TextUtils.isEmpty(newText)) {
            resetSearch();
            return false;
        }

        AppHelper.print("Search Text: "+newText);

        ArrayList<NewsFeedItems> filteredNewsItem = new ArrayList<>(newsFeedItemsArrayList);
        for (NewsFeedItems newsFeedItems : newsFeedItemsArrayList) {
            if(!newsFeedItems.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredNewsItem.remove(newsFeedItems);
            }
        }

        newsFeedAdapter = new NewsFeedAdapter(getActivity(), filteredNewsItem);
        newsFeedAdapter.notifyDataSetChanged();
        newsFeedRv.setAdapter(newsFeedAdapter);

        animateWithData(newsFeedRv);

        return false;
    }

    private void resetSearch() {
        refreshNewsFeed();
    }
}
