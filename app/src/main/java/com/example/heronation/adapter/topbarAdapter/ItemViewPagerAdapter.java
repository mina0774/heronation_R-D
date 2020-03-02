package com.example.heronation.adapter.topbarAdapter;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.heronation.home.topbarFragment.ItemAiFragment;
import com.example.heronation.home.topbarFragment.ItemBestFragment;
import com.example.heronation.home.topbarFragment.ItemHomeFragment;
import com.example.heronation.home.topbarFragment.ItemNewFragment;
import com.example.heronation.home.topbarFragment.ItemSaleFragment;

public class ItemViewPagerAdapter extends FragmentPagerAdapter {
    private int mPageCount;

    /*
     * 생성자를 통해서 프래그먼트 관리를 도와주는 FragmentManager와
     * 페이지의 개수를 탭의 개수와 맞춰주기 위해 Page Count를 받아온다.
     */
    public ItemViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mPageCount=behavior;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                ItemHomeFragment itemHomeFragment=new ItemHomeFragment();
                return itemHomeFragment;
            case 1:
                ItemNewFragment itemNewFragment=new ItemNewFragment();
                return itemNewFragment;
            case 2:
                ItemBestFragment itemBestFragment=new ItemBestFragment();
                return itemBestFragment;
            case 3:
                ItemAiFragment itemAiFragment=new ItemAiFragment();
                return itemAiFragment;
            case 4:
                ItemSaleFragment itemSaleFragment=new ItemSaleFragment();
                return itemSaleFragment;
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
