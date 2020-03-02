package com.example.heronation.wishlist;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heronation.R;
import com.example.heronation.adapter.topbarAdapter.WishlistViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;


public class WishlistFragment extends Fragment {

    private TabLayout wishlist_tabLayout;

    /* 프래그먼트 나타낼때, 프래그먼트를 담는 뷰페이저, 뷰페이저를 도와주는 어댑터 */
    private ViewPager viewPager;
    private WishlistViewPagerAdapter wishlistViewPagerAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_wishlist,container,false);

           /* Wishlist의 상단탭
         하단탭에서 Wishlist의 상단탭을 선택했을 시에만 보여져야 함
          */
        wishlist_tabLayout=(TabLayout)rootView.findViewById(R.id.wishlist_tab_layout);
        /* 뷰페이져 어댑터 객체를 생성하고,
         * 생성자를 통해서 프래그먼트 관리를 도와주는 FragmentManager와
         * 페이지의 개수를 탭의 개수와 맞춰주기 위해 Page Count를 받아온다.
         * 뷰페이저에 어댑터를 설정한다.
         * 그 후, tabLayout과 viewPager 연결
         */
        viewPager=(ViewPager)rootView.findViewById(R.id.wishlist_fragment_container);
        wishlistViewPagerAdapter=new WishlistViewPagerAdapter(getChildFragmentManager(),wishlist_tabLayout.getTabCount());
        viewPager.setAdapter(wishlistViewPagerAdapter);

        /*
         상단탭이 선택되었을 때, 상단탭의 선택된 현재 위치를 얻어 Fragment를 이동시킨다.
         */
        wishlist_tabLayout.addOnTabSelectedListener(new WishlistTopItemSelectedListener());

        /* ViewPager의 페이지가 변경될 때 알려주는 리스너*/
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(wishlist_tabLayout));

        return rootView;
    }

    /*
     * Wishlist 상단 탭에 있는 특정 값을 선택하였을 때
     * Switch문으로 경우를 나누어
     * 찜 버튼을  눌렀을 때, wishlistItemFragment로 이동
     * 샵 버튼을 눌렀을 때, wishlistShopFragment로 이동
     * 최근 본 상품 버튼을 눌렀을때, wishlistRecentlyViewedItemFragment로 이동
     */
    class WishlistTopItemSelectedListener implements TabLayout.OnTabSelectedListener{

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



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
