package com.example.heronation.wishlist.topbarFragment;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.heronation.R;
import com.example.heronation.home.dataClass.RecentlyViewedItem;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.RecentlyViewedItemAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.VISIBLE;


public class WishlistRecentlyViewedItemFragment extends Fragment {
    public static ArrayList<RecentlyViewedItem> item_list;
    public static RecentlyViewedItemAdapter recentlyViewedItemAdapter;
    @BindView(R.id.have_not_item) RelativeLayout have_not_recently_viewed_item;
    @BindView(R.id.recycler_view_recently_viewed_item) RecyclerView recycler_view_recently_viewed_item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_wishlist_recently_viewed_item, container, false);
        ButterKnife.bind(this,rootView);

        item_list=new ArrayList<>();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RecentlyViewedItem",MODE_PRIVATE);

        if(sharedPreferences.getString("items", null)==null||sharedPreferences.getString("items", null).equals("{}")) { // 최근 본 상품이 없을 때
            recycler_view_recently_viewed_item.setVisibility(View.GONE);
            have_not_recently_viewed_item.setVisibility(VISIBLE);
        }
        else { // 최근 본 상품이 있을 때
            have_not_recently_viewed_item.setVisibility(View.GONE);
            recycler_view_recently_viewed_item.setVisibility(VISIBLE);

            Gson gson=new GsonBuilder().create();
            LinkedHashMap linkedHashMap=gson.fromJson(sharedPreferences.getString("items",""),LinkedHashMap.class);
            Iterator iterator=linkedHashMap.values().iterator();

            while(iterator.hasNext()){
                RecentlyViewedItem recentlyViewedItem = gson.fromJson(iterator.next().toString(),RecentlyViewedItem.class);
                item_list.add(recentlyViewedItem);
            }

            recycler_view_recently_viewed_item.setLayoutManager(new GridLayoutManager(getActivity(),2, GridLayoutManager.VERTICAL,false));
            recentlyViewedItemAdapter=new RecentlyViewedItemAdapter(item_list,getActivity()); //Adapter 안에 horizontal adapter를 선언하여 이에 대한 레이아웃을 Grid로 지정
            recycler_view_recently_viewed_item.setAdapter(recentlyViewedItemAdapter);
        }

        return rootView;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
