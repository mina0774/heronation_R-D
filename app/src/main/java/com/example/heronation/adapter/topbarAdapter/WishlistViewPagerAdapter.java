package com.example.heronation.adapter.topbarAdapter;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.heronation.main.MainActivity;
import com.example.heronation.wishlist.topbarFragment.WishlistClosetFragment;
import com.example.heronation.wishlist.topbarFragment.WishlistClosetNotLoginFragment;
import com.example.heronation.wishlist.topbarFragment.WishlistItemFragment;
import com.example.heronation.wishlist.topbarFragment.WishlistItemNotLoginFragment;
import com.example.heronation.wishlist.topbarFragment.WishlistRecentlyViewedItemFragment;
import com.example.heronation.wishlist.topbarFragment.WishlistRecentlyViewedItemNotLoginFragment;
import com.example.heronation.wishlist.topbarFragment.WishlistShopFragment;
import com.example.heronation.wishlist.topbarFragment.WishlistShopNotLoginFragment;

public class WishlistViewPagerAdapter extends FragmentPagerAdapter {
    private int mPageCount;

    /*
     * 생성자를 통해서 프래그먼트 관리를 도와주는 FragmentManager와
     * 페이지의 개수를 탭의 개수와 맞춰주기 위해 Page Count를 받아온다.
     */
    public WishlistViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mPageCount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                if(MainActivity.access_token.matches("null")) {
                    WishlistClosetNotLoginFragment wishlistClosetNotLoginFragment = new WishlistClosetNotLoginFragment();
                    return wishlistClosetNotLoginFragment;
                }else{
                    WishlistClosetFragment wishlistClosetFragment = new WishlistClosetFragment();
                    return wishlistClosetFragment; }
            case 1:
                if(MainActivity.access_token.matches("null")) {
                    WishlistItemNotLoginFragment wishlistItemNotLoginFragment = new WishlistItemNotLoginFragment();
                    return wishlistItemNotLoginFragment;
                }else {
                    WishlistItemFragment wishlistItemFragment = new WishlistItemFragment();
                    return wishlistItemFragment;
                }
            case 2:
                if(MainActivity.access_token.matches("null")) {
                    WishlistShopNotLoginFragment wishlistShopNotLoginFragment = new WishlistShopNotLoginFragment();
                    return wishlistShopNotLoginFragment;
                }else {
                    WishlistShopFragment wishlistShopFragment = new WishlistShopFragment();
                    return wishlistShopFragment;
                }
            case 3:
                if(MainActivity.access_token.matches("null")) {
                    WishlistRecentlyViewedItemNotLoginFragment wishlisttRecentlyViewedItemNotLoginFragment = new WishlistRecentlyViewedItemNotLoginFragment();
                    return wishlisttRecentlyViewedItemNotLoginFragment;
                }else {
                    WishlistRecentlyViewedItemFragment wishlistRecentlyViewedItemFragment = new WishlistRecentlyViewedItemFragment();
                    return wishlistRecentlyViewedItemFragment;
                }
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
