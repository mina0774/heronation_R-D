package com.example.heronation.wishlist.topbarFragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.heronation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.MODE_PRIVATE;
import static android.view.View.VISIBLE;


public class WishlistRecentlyViewedItemFragment extends Fragment {
    @BindView(R.id.have_not_item) RelativeLayout have_not_recently_viewed_item;
    @BindView(R.id.recycler_view_recently_viewed_item) RecyclerView recycler_view_recently_viewed_item;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_wishlist_recently_viewed_item, container, false);
        ButterKnife.bind(this,rootView);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("RecentlyViewedItem",MODE_PRIVATE);

        if(sharedPreferences.getAll().isEmpty()){ // 최근 본 상품이 없을 때
            recycler_view_recently_viewed_item.setVisibility(View.INVISIBLE);
            have_not_recently_viewed_item.setVisibility(VISIBLE);
        }else if(!sharedPreferences.getAll().isEmpty()){ // 최근 본 상품이 있을 때
            have_not_recently_viewed_item.setVisibility(View.INVISIBLE);
            recycler_view_recently_viewed_item.setVisibility(VISIBLE);

        }


        return rootView;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
