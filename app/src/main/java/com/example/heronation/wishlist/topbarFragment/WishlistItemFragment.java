package com.example.heronation.wishlist.topbarFragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

import com.example.heronation.home.itemRecyclerViewAdapter.ItemNewAdapter;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ShopItemInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.shop.ShoplistRecyclerViewAdapter.ShopFavoritesRecyclerViewAdapter;
import com.example.heronation.shop.ShoplistRecyclerViewAdapter.dataClass.ShopFavoritesContent;
import com.example.heronation.shop.topbarFragment.ShopFavoritesFragment;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.FavoriteItemAdapter;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass.FavoriteItem;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;


public class WishlistItemFragment extends Fragment {
    private ArrayList<FavoriteItem> item_list;
    private FavoriteItemAdapter favoriteItemAdapter;

    /* 찜한 아이템이 없을 시*/
    @BindView(R.id.have_not_item) RelativeLayout have_not_item;
    @BindView(R.id.wishlist_item_togo_measurement) Button wishlist_style_recommendation; //MeasurementFragment로 이동하기 위해 필요한 버튼
    @BindView(R.id.recommend_image) ImageView recommend_image;

    /* 찜한 아이템이 있을 시*/
    @BindView(R.id.have_item) LinearLayout have_item;
    @BindView(R.id.wishlist_item_num) TextView wishlist_item_num;
    @BindView(R.id.favorite_folder) ImageButton favorite_folder;
    @BindView(R.id.recycler_view_item_favorites) RecyclerView recycler_view_item_favorites;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_wishlist_item, container,false);
        ButterKnife.bind(this,rootView);
        item_list=new ArrayList<>();

        recycler_view_item_favorites.setLayoutManager(new GridLayoutManager(getActivity(),2, GridLayoutManager.VERTICAL,false));

        GetItemInfo();
        favoriteItemAdapter=new FavoriteItemAdapter(item_list,getActivity()); //Adapter 안에 horizontal adapter를 선언하여 이에 대한 레이아웃을 Grid로 지정
        /* 레이아웃 매니저 수직으로 지정 */
        recycler_view_item_favorites.setAdapter(favoriteItemAdapter);

         /* 찜한 아이템이 없을 시에 MeasurementFragment로 이동*/
        wishlist_style_recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).go_to_measurement();
            }
        });
        return rootView;
    }

    //아이템 찜 목록 받아오는 기능
    public void GetItemInfo() {
        String authorization = "Bearer " + MainActivity.access_token;
        String content_type = "application/json";

        FavoriteItemInfoService favoriteItemInfoService = ServiceGenerator.createService(FavoriteItemInfoService.class);
        retrofit2.Call<List<FavoriteItem>> request = favoriteItemInfoService.favoriteItemInfo(authorization, content_type);
        request.enqueue(new Callback<List<FavoriteItem>>() {
            @Override
            public void onResponse(Call<List<FavoriteItem>> call, Response<List<FavoriteItem>> response) {
                System.out.println("Response" + response.code());
                List<FavoriteItem> itemFavoritesListInfo=response.body();
                if(itemFavoritesListInfo.size()!=0) {
                    /* 찜 아이템 없을 시 화면 삭제 */
                    have_not_item.setVisibility(View.GONE);
                    /* 찜 화면 있을시 화면 보이게 하기 */
                    have_item.setVisibility(View.VISIBLE);
                    /*shop 목록을 생성*/
                    make_item_list(itemFavoritesListInfo);
                    wishlist_item_num.setText("찜한 상품 " + itemFavoritesListInfo.size() + "개");
                    favoriteItemAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<FavoriteItem>> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }


    public void make_item_list(List<FavoriteItem> shopListInfo){
        /* 아이템 목록을 생성함 */
        for(int i = 0; i<shopListInfo.size(); i++){
            item_list.add(shopListInfo.get(i));
        }
    }


    /* 사용자 정보를 서버에서 받아오는 인터페이스*/
    public interface FavoriteItemInfoService {
        @GET("api/consumers/items/interest")
        retrofit2.Call<List<FavoriteItem>> favoriteItemInfo(@Header("authorization") String authorization,
                                                            @Header("Content-Type") String cotent_type);
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
