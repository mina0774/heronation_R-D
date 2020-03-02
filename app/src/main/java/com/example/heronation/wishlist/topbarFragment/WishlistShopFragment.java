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

import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.shop.ShoplistRecyclerViewAdapter.ShopFavoritesRecyclerViewAdapter;
import com.example.heronation.shop.ShoplistRecyclerViewAdapter.dataClass.ShopFavoritesContent;
import com.example.heronation.shop.topbarFragment.ShopFavoritesFragment;
import com.example.heronation.zeyoAPI.ServiceGenerator;

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

    private ArrayList<ShopFavoritesContent> shop_list;
    private ShopFavoritesRecyclerViewAdapter shopRecyclerViewAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_wishlist_shop, container,false);
        ButterKnife.bind(this,rootView);
        shop_list=new ArrayList<>();

        /* 레이아웃 매니저 수평으로 지정 */
        recycler_view_wishilist_shop.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        GetShopInfo();
        /* 리사이클러뷰 객체 생성 */
        shopRecyclerViewAdapter=new ShopFavoritesRecyclerViewAdapter(getActivity(),shop_list);
        /* 리사이클러뷰에 어댑터 지정 */
        recycler_view_wishilist_shop.setAdapter(shopRecyclerViewAdapter);

          /* 찜한 마켓이 없을 시에 ShopRankingFragment로 이동*/
        wishlist_best_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).go_to_shop_fragment();
            }
        });

        return rootView;
    }

    //쇼핑몰 찜 목록 받아오는 기능
    public void GetShopInfo() {
        String authorization = "Bearer " + MainActivity.access_token;
        String accept = "application/json";

        ShopFavoritesFragment.ShopFavoritesInfoService shopFavoritesInfoService = ServiceGenerator.createService(ShopFavoritesFragment.ShopFavoritesInfoService.class);
        retrofit2.Call<List<ShopFavoritesContent>> request = shopFavoritesInfoService.ShopInfo(authorization, accept);
        request.enqueue(new Callback<List<ShopFavoritesContent>>() {
            @Override
            public void onResponse(Call<List<ShopFavoritesContent>> call, Response<List<ShopFavoritesContent>> response) {
                System.out.println("Response" + response.code());
                List<ShopFavoritesContent> shopFavoritesListInfo=response.body();
                if(shopFavoritesListInfo.size()!=0) {
                    /* 찜 마켓 없을 시 화면 삭제 */
                    have_not_shop.setVisibility(View.GONE);
                    /* 찜 화면 있을시 화면 보이게 하기 */
                    have_shop.setVisibility(View.VISIBLE);
                    /*shop 목록을 생성*/
                    make_shop_list(shopFavoritesListInfo);
                    wishlist_shop_num.setText("즐겨찾기 " + shopFavoritesListInfo.size() + "개");
                    shopRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<ShopFavoritesContent>> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    public void make_shop_list(List<ShopFavoritesContent> shopListInfo){
        /* Shop 목록을 생성함 */
        for(int i = 0; i<shopListInfo.size(); i++){
            shop_list.add(shopListInfo.get(i));
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

    /* Acitivity와 Fragment가 통신할 때, OnFragmentInteractionListener를 사용함.
     * 프래그먼트에서 액티비티로 통신(데이터 주고 받는 것)이 있을 수도 있기 때문에
     * MainActivity 에서 이를 implement한 후 오버라이딩 시켜줄 것이다. (임시로)
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
