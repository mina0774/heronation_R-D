package com.example.heronation.wishlist.topbarFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.heronation.R;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.RecentlyViewedItem;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.FavoriteItemAdapter;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.RecentlyViewedItemAdapter;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass.FavoriteItem;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.VISIBLE;


public class WishlistRecentlyViewedItemFragment extends Fragment {
    private ArrayList<RecentlyViewedItem> item_list;
    private RecentlyViewedItemAdapter recentlyViewedItemAdapter;
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

        if(sharedPreferences.getAll().isEmpty())
        { // 최근 본 상품이 없을 때
            recycler_view_recently_viewed_item.setVisibility(View.INVISIBLE);
            have_not_recently_viewed_item.setVisibility(VISIBLE);
        }
        else if(!sharedPreferences.getAll().isEmpty())
        { // 최근 본 상품이 있을 때
            have_not_recently_viewed_item.setVisibility(View.INVISIBLE);
            recycler_view_recently_viewed_item.setVisibility(VISIBLE);

            Map<String,?> keys= sharedPreferences.getAll();
            Gson gson=new GsonBuilder().create();

            for(Map.Entry<String,?> entry : keys.entrySet()){
                Log.d("shared",entry.getValue().toString()+"");
                RecentlyViewedItem recentlyViewedItem=gson.fromJson(entry.getValue().toString(),RecentlyViewedItem.class); //각 데이터를 shared preference로부터 모두 받아와서
                item_list.add(recentlyViewedItem); // 아이템 리스트에 추가함
                recycler_view_recently_viewed_item.setLayoutManager(new GridLayoutManager(getActivity(),2, GridLayoutManager.VERTICAL,false));
                recentlyViewedItemAdapter=new RecentlyViewedItemAdapter(item_list,getActivity()); //Adapter 안에 horizontal adapter를 선언하여 이에 대한 레이아웃을 Grid로 지정
                /* 레이아웃 매니저 수직으로 지정 */
                recycler_view_recently_viewed_item.setAdapter(recentlyViewedItemAdapter);
            }

        }


        return rootView;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
