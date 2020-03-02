package com.example.heronation.shop.topbarFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.example.heronation.shop.ShopRankingSearchActivity;
import com.example.heronation.shop.ShoplistRecyclerViewAdapter.ShopRecyclerViewAdapter;
import com.example.heronation.shop.ShoplistRecyclerViewAdapter.dataClass.ShopContent;
import com.example.heronation.shop.ShoplistRecyclerViewAdapter.dataClass.ShopListInfo;
import com.example.heronation.adapter.bannerAdapter.bannerAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public class ShopRankingFragment extends Fragment {
    @BindView(R.id.recycler_view_shop_ranking) RecyclerView shop_recyclerView;
    private ShopRecyclerViewAdapter shopRecyclerViewAdapter;
    private ArrayList<ShopContent> shop_list=new ArrayList<>();
    @BindView(R.id.shop_ranking_filter) ImageButton filter_button;
    @BindView(R.id.shop_ranking_search) Button search_button;

    /* 배너 슬라이딩을 위한 변수 */
    private bannerAdapter bannerAdapter;
    @BindView(R.id.image_view_shop_ranking) ViewPager viewPager;

    /* 데이터의 총 페이지수와 현재 가리키고 있는 페이지 번호*/
    private Integer current_page;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_shop_ranking, container,false);
        ButterKnife.bind(this,rootView);
        /* 레이아웃 매니저 수평으로 지정 */
        shop_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        /* 처음에 리사이클러뷰 값을 설정해주는 작업 */
        SetRecyclerViewFirst();
        /* 페이징 처리한 데이터를 로딩하는 함수 */
        LoadingData();

        /* 필터 버튼
         *  필터 버튼을 눌렀을 때, 팝업창을 띄어줌
         */
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).open_panel();
            }
        });

        /* 검색 버튼
         *  검색 버튼을 눌렀을 때 Shop 검색 Activity로 이동*/
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ShopRankingSearchActivity.class);
                startActivity(intent);
            }
        });

        /* 이미지 슬라이딩을 위해 뷰페이저를 이용했고, 이를 설정해주는 이미지 어댑터를 설정하여 슬라이딩 구현 */
        bannerAdapter =new bannerAdapter(getActivity());
        viewPager.setAdapter(bannerAdapter);

        return rootView;
    }

    public void make_shop_list(ShopListInfo shopListInfo){
        /* Shop 목록을 생성함 */
        for(int i = shopListInfo.getShopContent().size()-1; i>=0; i--){
            shop_list.add(shopListInfo.getShopContent().get(i));
        }
      /*  shop_list.add(new Shop("1","크림치즈마켓","#20대 #심플베이직 #러블리",
                "https://creamcheese.co.kr/web/product/extra/big/20200103/a6f044e55e57a52499d86d8d52fbbe97.jpg",
                "https://creamcheese.co.kr/web/product/extra/big/201910/771a37dd6951ee991d401d58000999d6.jpeg",
                "https://creamcheese.co.kr/web/product/extra/big/201908/4bb1ddaaaacbbc33aef005355888877a.jpeg"));
*/
    }

    /* 처음에 리사이클러뷰 값을 설정해주는 작업 */
    public void SetRecyclerViewFirst(){
        //전체 페이지의 개수는 4개이므로 4페이지부터 시작
        GetShopInfo(4);
        //현재 가리키고 있는 페이지 번호
        current_page = 4;
        //데이터를 서버에서 8개씩 로드해서 리사이클러뷰에 뿌려줌
        /* 리사이클러뷰 객체 생성 */
        shopRecyclerViewAdapter = new ShopRecyclerViewAdapter(getActivity(), shop_list);
        /* 리사이클러뷰에 어댑터 지정 */
        shop_recyclerView.setAdapter(shopRecyclerViewAdapter);
    }

    /*Shop의 정보를 얻는 함수*/
    public void GetShopInfo(Integer page_num) {
        String authorization = "zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        String accept = "application/json";

        ShopInfoService shopInfoService = ServiceGenerator.createService(ShopInfoService.class);
        retrofit2.Call<ShopListInfo> request = shopInfoService.ShopInfo(page_num,8,"id,asc", authorization, accept);
        request.enqueue(new Callback<ShopListInfo>() {
            @Override
            public void onResponse(Call<ShopListInfo> call, Response<ShopListInfo> response) {
                System.out.println("Response" + response.code());
                if(response.code()==200) {
                    ShopListInfo shopListInfo = response.body();
                    /* Shop 목록을 생성함 */
                    make_shop_list(shopListInfo);
                    shopRecyclerViewAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ShopListInfo> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    /* 페이징 처리한 데이터를 로딩하는 함수 */
    public void LoadingData() {
        shop_recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();
                int itemTotalCount = recyclerView.getAdapter().getItemCount() - 1;
                if (lastVisibleItemPosition == itemTotalCount) {
                    current_page-=1;
                    GetShopInfo(current_page);
                }
            }

        });
    }

    /* Acitivity와 Fragment가 통신할 때, OnFragmentInteractionListener를 사용함.
     * 프래그먼트에서 액티비티로 통신(데이터 주고 받는 것)이 있을 수도 있기 때문에
     * MainActivity 에서 이를 implement한 후 오버라이딩 시켜줄 것이다. (임시로)
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //인터페이스 - 추상 메소드(구현부가 없는 메서드)의 모임
    /* retrofit은 인터페이스에 기술된 명세를 Http API(호출 가능한 객체)로 전환해줌
    => 우리가 요청할 API들에 대한 명세만을 Interface에 기술해두면 됨.
     */
    /* 사용자 정보를 서버에서 받아오는 인터페이스*/
    public interface ShopInfoService {
        @GET("api/shopmalls")
            //여기서 size는 몇개의 쇼핑몰의 정보를 불러올 것인지, 오름차순으로 sort 해야하는데 안됨.
        retrofit2.Call<ShopListInfo> ShopInfo(@Query("page") Integer page_num,
                                              @Query("size") Integer size,
                                              @Query("sort") String sort,
                                              @Header("authorization") String authorization,
                                              @Header("Accept") String accept);
    }

    /* 쇼핑몰 찜 추가 */
    public interface ShopRegisterService {
        @POST("api/consumers/shopmalls/{shop_id}/interest")
        retrofit2.Call<String> ShopRegister(@Path("shop_id") Integer shop_id,
                                    @Header("authorization") String authorization,
                                    @Header("Accept") String accept,
                                    @Header("Content-Type") String content_type);
    }

    /* 쇼핑몰 찜 삭제*/
    public interface ShopDeleteService {
        @DELETE("api/consumers/shopmalls/{shop_id}/interest")
        retrofit2.Call<String> ShopDelete(@Path("shop_id") Integer shop_id,
                                          @Header("authorization") String authorization,
                                          @Header("Accept") String accept,
                                          @Header("Content-Type") String content_type);
    }


}