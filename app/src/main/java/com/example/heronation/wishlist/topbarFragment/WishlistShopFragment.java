package com.example.heronation.wishlist.topbarFragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.heronation.R;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WishlistShopFragment extends Fragment {
    /* 찜한 마켓이 없을 시 */
    @BindView(R.id.have_not_shop) RelativeLayout have_not_shop;
    @BindView(R.id.wishlist_shop_togo_shop_ranking) Button wishlist_best_market; //ShopRankingFragment로 이동하기 위해 필요한 버튼
    @BindView(R.id.shop_image) ImageView shop_image;

    /* 찜한 마켓이 있을 시 */
    @BindView(R.id.have_shop) LinearLayout have_shop;
    @BindView(R.id.wishlist_shop_num) TextView wishlist_shop_num;
    @BindView(R.id.favorite_folder) ImageButton favorite_folder;
    @BindView(R.id.recycler_view_wishilist_shop) RecyclerView recycler_view_wishilist_shop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_wishlist_shop, container,false);
        ButterKnife.bind(this,rootView);

        /* 레이아웃 매니저 수평으로 지정 */
        recycler_view_wishilist_shop.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        return rootView;
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

    /* Acitivity와 Fragment가 통신할 때, OnFragmentInteractionListener를 사용함.
     * 프래그먼트에서 액티비티로 통신(데이터 주고 받는 것)이 있을 수도 있기 때문에
     * MainActivity 에서 이를 implement한 후 오버라이딩 시켜줄 것이다. (임시로)
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
