package com.example.heronation.adapter.topbarAdapter;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.heronation.main.MainActivity;
import com.example.heronation.shop.topbarFragment.ShopFavoritesFragment;
import com.example.heronation.shop.topbarFragment.ShopFavoritesNotLoginFragment;
import com.example.heronation.shop.topbarFragment.ShopRankingFragment;
import com.example.heronation.wishlist.topbarFragment.WishlistClosetFragment;
import com.example.heronation.wishlist.topbarFragment.WishlistClosetNotLoginFragment;

public class ShopViewPagerAdapter extends FragmentPagerAdapter {
    private int mPageCount;

    /*
     * 생성자를 통해서 프래그먼트 관리를 도와주는 FragmentManager와
     * 페이지의 개수를 탭의 개수와 맞춰주기 위해 Page Count를 받아온다.
     */
    public ShopViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mPageCount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                ShopRankingFragment shopRankingFragment=new ShopRankingFragment();
                return shopRankingFragment;
            case 1:
                if(MainActivity.access_token.matches("null")) {
                    ShopFavoritesNotLoginFragment shopFavoritesNotLoginFragment = new ShopFavoritesNotLoginFragment();
                    return shopFavoritesNotLoginFragment;
                }else{
                    ShopFavoritesFragment shopFavoritesFragment=new ShopFavoritesFragment();
                    return shopFavoritesFragment; }
        }
        return null;
    }


    @Override
    public int getCount() {
        return mPageCount;
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
