package com.example.heronation.home;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heronation.adapter.topbarAdapter.ItemViewPagerAdapter;
import com.example.heronation.R;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ItemFragment extends Fragment {
    @BindView(R.id.item_tab_layout) TabLayout item_tabLayout;

    /* 프래그먼트 나타낼때, 프래그먼트를 담는 뷰페이저, 뷰페이저를 도와주는 어댑터 */
    @BindView(R.id.item_fragment_container) ViewPager viewPager;
    private ItemViewPagerAdapter itemViewPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_item,container,false);
        ButterKnife.bind(this,rootView);

        /* 뷰페이져 어댑터 객체를 생성하고,
         * 생성자를 통해서 프래그먼트 관리를 도와주는 FragmentManager와
         * 페이지의 개수를 탭의 개수와 맞춰주기 위해 Page Count를 받아온다.
         * 뷰페이저에 어댑터를 설정한다.
         * 그 후, tabLayout과 viewPager 연결
         */
        itemViewPagerAdapter=new ItemViewPagerAdapter(getChildFragmentManager(),item_tabLayout.getTabCount());
        viewPager.setAdapter(itemViewPagerAdapter);

        /*
         상단탭이 선택되었을 때, 상단탭의 선택된 현재 위치를 얻어 Fragment를 이동시킨다.
         */
        item_tabLayout.addOnTabSelectedListener(new ItemTopItemSelectedListener());

        /* ViewPager의 페이지가 변경될 때 알려주는 리스너*/
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(item_tabLayout));

        return rootView;
    }


    /*
     * Item 상단 탭에 있는 특정 값을 선택하였을 때
     * Switch문으로 경우를 나누어
     * 홈 버튼을  눌렀을 때, ItemHomeFragment로 이동
     * 신상 버튼을 눌렀을 때, ItemNewFragment로 이동
     * 베스트 버튼을 눌렀을 때, ItemBestFragment로 이동
     * AI 버튼을 눌렀을 때, ItemAiFragment로 이동
     * 세일 버튼을 눌렀을 때, ItemSaleFragment로 이동*/
    class ItemTopItemSelectedListener implements TabLayout.OnTabSelectedListener{
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
