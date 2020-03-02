package com.example.heronation.filter;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.heronation.R;
import com.example.heronation.filter.filterAdapter.ItemSearchFilterPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DialogWindowFilterFragment extends DialogFragment{
    private TabLayout item_tabLayout;
    /* 프래그먼트 나타낼때, 프래그먼트를 담는 뷰페이저, 뷰페이저를 도와주는 어댑터 */
    @BindView(R.id.item_search_filter_fragment_container) ViewPager item_search_filter_viewpager;
    private ItemSearchFilterPagerAdapter item_search_filter_adapter;

    /* 화면 크기 조정*/
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    /* 화면 크기 조정*/
    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_dialog_window_filter,container);
        ButterKnife.bind(this,rootView);

        /* Item의 상단탭*/
        item_tabLayout=(TabLayout)rootView.findViewById(R.id.item_tab_layout);

        /* 뷰페이져 어댑터 객체를 생성하고,
         * 생성자를 통해서 프래그먼트 관리를 도와주는 FragmentManager와
         * 페이지의 개수를 탭의 개수와 맞춰주기 위해 Page Count를 받아온다.
         * 뷰페이저에 어댑터를 설정한다.
         * 그 후, tabLayout과 viewPager 연결
         */
        item_search_filter_adapter=new ItemSearchFilterPagerAdapter(getChildFragmentManager(),item_tabLayout.getTabCount());
         item_search_filter_viewpager.setAdapter(item_search_filter_adapter);

        /*
         상단탭이 선택되었을 때, 상단탭의 선택된 현재 위치를 얻어 Fragment를 이동시킨다.
         */
        item_tabLayout.addOnTabSelectedListener(new ItemTopItemSelectedListener());

        /* ViewPager의 페이지가 변경될 때 알려주는 리스너*/
        item_search_filter_viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(item_tabLayout));

        Window window = getDialog().getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        return rootView;
    }



    /* 상단탭이 선택되었을 때, 탭의 위치를 받아와 탭의 하이라이트를 이동시켜줌 */
    class ItemTopItemSelectedListener implements TabLayout.OnTabSelectedListener{
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            item_search_filter_viewpager.setCurrentItem(tab.getPosition());
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
