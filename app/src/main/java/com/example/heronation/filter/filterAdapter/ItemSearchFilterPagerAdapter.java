package com.example.heronation.filter.filterAdapter;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.heronation.filter.FilterCategoryFragment;
import com.example.heronation.filter.FilterColorFragment;
import com.example.heronation.filter.FilterPriceFragment;
import com.example.heronation.filter.FilterSizeFragment;

public class ItemSearchFilterPagerAdapter extends FragmentPagerAdapter{
    private int mPageCount;
    /*
     * 생성자를 통해서 프래그먼트 관리를 도와주는 FragmentManager와
     * 페이지의 개수를 탭의 개수와 맞춰주기 위해 Page Count를 받아온다.
     */
    public ItemSearchFilterPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        this.mPageCount=behavior;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                FilterPriceFragment FilterPriceFragment =new FilterPriceFragment();
                return FilterPriceFragment;
            case 1:
                FilterColorFragment FilterColorFragment =new FilterColorFragment();
                return FilterColorFragment;
            case 2:
                FilterCategoryFragment FilterCategoryFragment =new FilterCategoryFragment();
                return FilterCategoryFragment;
            case 3:
                FilterSizeFragment FilterSizeFragment =new FilterSizeFragment();
                return FilterSizeFragment;

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
