package com.example.heronation.shop;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heronation.R;
import com.example.heronation.adapter.topbarAdapter.ShopViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ShopFragment extends Fragment {

    @BindView(R.id.shop_tab_layout) TabLayout shop_tabLayout;

    /* 프래그먼트 나타낼때, 프래그먼트를 담는 뷰페이저, 뷰페이저를 도와주는 어댑터 */
    @BindView(R.id.shop_fragment_container) ViewPager viewPager;
    private ShopViewPagerAdapter shopViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_shop,container,false);
        ButterKnife.bind(this,rootView);
        /* 뷰페이져 어댑터 객체를 생성하고,
         * 생성자를 통해서 프래그먼트 관리를 도와주는 FragmentManager와
         * 페이지의 개수를 탭의 개수와 맞춰주기 위해 Page Count를 받아온다.
         * 뷰페이저에 어댑터를 설정한다.
         * 그 후, tabLayout과 viewPager 연결
         */
        shopViewPagerAdapter=new ShopViewPagerAdapter(getChildFragmentManager(),shop_tabLayout.getTabCount());
        viewPager.setAdapter(shopViewPagerAdapter);

        /*
         상단탭이 선택되었을 때, 상단탭의 선택된 현재 위치를 얻어 Fragment를 이동시킨다.
         */
        shop_tabLayout.addOnTabSelectedListener(new ShopTopItemSelectedListener());

        /* ViewPager의 페이지가 변경될 때 알려주는 리스너*/
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(shop_tabLayout));

        return rootView;
    }

    /*
     * Shop 상단 탭에 있는 특정 값을 선택하였을 때
     * Switch문으로 경우를 나누어
     * Shop Ranking 버튼을  눌렀을 때, ShopRankingFragment로 이동
     * 즐겨찾기 버튼을 눌렀을 때, ShopFavoritesFragment로 이동
     */
    class ShopTopItemSelectedListener implements TabLayout.OnTabSelectedListener{
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            viewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {

        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }


    /*
     * onFragmenInteraciton~ 를 한 이유
     * Acitivity와 Fragment가 통신할 때, OnFragmentInteractionListener를 사용함.
     * 프래그먼트에서 액티비티로 통신(데이터 주고 받는 것)이 있을 수도 있기 때문에
     * MainActivity 에서 이를 implement한 후 오버라이딩 (임시로)
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
