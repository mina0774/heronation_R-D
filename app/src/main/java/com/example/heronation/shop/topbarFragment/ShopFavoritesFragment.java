package com.example.heronation.shop.topbarFragment;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.heronation.R;
import com.example.heronation.main.MainActivity;
import com.example.heronation.shop.ShoplistRecyclerViewAdapter.ShopFavoritesRecyclerViewAdapter;
import com.example.heronation.shop.ShoplistRecyclerViewAdapter.dataClass.ShopFavoritesContent;
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


public class ShopFavoritesFragment extends Fragment {
    @BindView(R.id.recycler_view_shop_favorites) RecyclerView shop_recyclerView;
    @BindView(R.id.favorites_shop_num) TextView favorites_shop_num;
    private ArrayList<ShopFavoritesContent> shop_list=new ArrayList<>();
    private ShopFavoritesRecyclerViewAdapter shopRecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_shop_favorites, container,false);
        ButterKnife.bind(this,rootView);
        /* 레이아웃 매니저 수평으로 지정 */
        shop_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));

        GetShopInfo();
        /* 리사이클러뷰 객체 생성 */
        shopRecyclerViewAdapter=new ShopFavoritesRecyclerViewAdapter(getActivity(),shop_list);
        /* 리사이클러뷰에 어댑터 지정 */
        shop_recyclerView.setAdapter(shopRecyclerViewAdapter);

        return rootView;
    }

    //쇼핑몰 찜 목록 받아오는 기능
    public void GetShopInfo() {
        String authorization = "Bearer " + MainActivity.access_token;
        String accept = "application/json";

        ShopFavoritesInfoService shopFavoritesInfoService = ServiceGenerator.createService(ShopFavoritesInfoService.class);
        retrofit2.Call<List<ShopFavoritesContent>> request = shopFavoritesInfoService.ShopInfo(authorization, accept);
        request.enqueue(new Callback<List<ShopFavoritesContent>>() {
            @Override
            public void onResponse(Call<List<ShopFavoritesContent>> call, Response<List<ShopFavoritesContent>> response) {
                System.out.println("Response" + response.code());
                List<ShopFavoritesContent> shopFavoritesListInfo=response.body();
                /*shop 목록을 생성*/
                make_shop_list(shopFavoritesListInfo);
                favorites_shop_num.setText("즐겨찾기 "+shopFavoritesListInfo.size()+"개");
                shopRecyclerViewAdapter.notifyDataSetChanged();
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


    public interface ShopFavoritesInfoService {
        @GET("api/consumers/shopmalls/interest")
        retrofit2.Call<List<ShopFavoritesContent>> ShopInfo(@Header("authorization") String authorization,
                                                   @Header("Accept") String accept);
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
