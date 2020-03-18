package com.example.heronation.home.topbarFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.heronation.adapter.bannerAdapter.bannerAdapter;
import com.example.heronation.home.itemRecyclerViewAdapter.ItemStyleVerticalAdapter;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ItemContent;
import com.example.heronation.home.ItemSearchActivity;
import com.example.heronation.home.itemRecyclerViewAdapter.ItemVerticalAdapter;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.StyleRecommendation;
import com.example.heronation.login_register.loginPageActivity;
import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ShopItemInfo;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ShopItemPackage;

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


public class ItemHomeFragment extends Fragment {
    @BindView(R.id.item_home_recyclerViewVertical1) RecyclerView item_recyclerView;
    @BindView(R.id.nested_item_home) NestedScrollView nested_item_home;

    /* 필터 버튼 */
    @BindView(R.id.item_home_filter) ImageButton filter_button;

    /* 배너 슬라이딩을 위한 변수 */
    private bannerAdapter bannerAdapter;
    @BindView(R.id.image_view_home) ViewPager viewPager;

    /* 검색창 */
    @BindView(R.id.item_home_search_edittext) EditText search_item;

    //아이템들의 묶음
    private ArrayList<ShopItemPackage> item_list;
    /* 아이템 수평 리스트 담는 수직 어댑터*/
    private ItemStyleVerticalAdapter verticalAdapter;
    private Integer package_num;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_item_home,container,false);
        ButterKnife.bind(this,rootView);

        // 리사이클러뷰에 들어있는 아이템을 초기화
        item_list=new ArrayList<>();
        /* 수직 리사이클러뷰의 하나의 아이템에 수평 리사이클러뷰의 아이템을 수평 방향으로 배치 설정, 어댑터 지정
         * (ex)  수평 리사이클러뷰
         *       수평 리사이클러뷰
         *       수평 리사이클러뷰
         * */
        verticalAdapter=new ItemStyleVerticalAdapter(item_list,getActivity());
        item_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        item_recyclerView.setAdapter(verticalAdapter);
        loadItems(nested_item_home,getActivity());

        /*  검색창 클릭했을 때, 아이템 검색 액티비티로 이동 */
        search_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), ItemSearchActivity.class);
                startActivity(intent);
            }
        });

        /* 이미지 슬라이딩을 위해 뷰페이저를 이용했고, 이를 설정해주는 이미지 어댑터를 설정하여 슬라이딩 구현 */
        bannerAdapter =new bannerAdapter(getActivity());
        viewPager.setAdapter(bannerAdapter);

        /* 필터 버튼
         *  필터 버튼을 눌렀을 때, 팝업창을 띄어줌
         */
        filter_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).open_panel();
            }
        });

        return rootView;
    }

    /* 스타일 추천 Item의 정보를 얻는 함수 */
    public void GetItemInfoUser(String package_name) {
        String authorization = "zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        String accept = "application/json";

        APIInterface.StyleRecommendationBasedUserService itemInfoService = ServiceGenerator.createService(APIInterface.StyleRecommendationBasedUserService.class);

        retrofit2.Call<ArrayList<StyleRecommendation>> request = itemInfoService.ShopItemInfo(loginPageActivity.style_tag_id, authorization, accept); //사용자 정보 받아오기
        request.enqueue(new Callback<ArrayList<StyleRecommendation>>() {
            @Override
            public void onResponse(Call<ArrayList<StyleRecommendation>> call, Response<ArrayList<StyleRecommendation>> response) {
                System.out.println("Response" + response.code());
                if(response.code()==200) {
                    //아이템의 데이터를 받는 리스트
                    ArrayList<StyleRecommendation> shopItemInfo = response.body();
                    item_list.add(new ShopItemPackage(package_name,shopItemInfo));
                    verticalAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StyleRecommendation>> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    /*타사용자 기반 스타일 추천 Item의 정보를 얻는 함수*/
    public void GetItemInfoOther(String package_name) {
        String authorization = "bearer "+loginPageActivity.access_token;
        String accept = "application/json";

        APIInterface.StyleRecommendationBasedOtherService itemInfoService = ServiceGenerator.createService(APIInterface.StyleRecommendationBasedOtherService.class);
        //사용자 정보 받아오기
        retrofit2.Call<ArrayList<StyleRecommendation>> request = itemInfoService.ShopItemInfo(authorization, accept); //사용자 정보 받아오기
        request.enqueue(new Callback<ArrayList<StyleRecommendation>>() {
            @Override
            public void onResponse(Call<ArrayList<StyleRecommendation>> call, Response<ArrayList<StyleRecommendation>> response) {
                System.out.println("Response" + response.code());
                if(response.code()==200) {
                    //아이템의 데이터를 받는 리스트
                    ArrayList<StyleRecommendation> shopItemInfo = response.body();
                    item_list.add(new ShopItemPackage(package_name,shopItemInfo));
                    verticalAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StyleRecommendation>> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    /*체형 기반 추천 Item의 정보를 얻는 함수*/
    public void GetItemInfoBody(String package_name) {
        String authorization = "bearer "+loginPageActivity.access_token;
        String accept = "application/json";

        APIInterface.BodyRecommendationService itemInfoService = ServiceGenerator.createService(APIInterface.BodyRecommendationService.class);
        //사용자 정보 받아오기
        retrofit2.Call<ArrayList<StyleRecommendation>> request = itemInfoService.ShopItemInfo(authorization, accept); //사용자 정보 받아오기
        request.enqueue(new Callback<ArrayList<StyleRecommendation>>() {
            @Override
            public void onResponse(Call<ArrayList<StyleRecommendation>> call, Response<ArrayList<StyleRecommendation>> response) {
                System.out.println("Response" + response.code());
                if(response.code()==200) {
                    //아이템의 데이터를 받는 리스트
                    ArrayList<StyleRecommendation> shopItemInfo = response.body();
                    item_list.add(new ShopItemPackage(package_name,shopItemInfo));
                    verticalAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StyleRecommendation>> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    //package 넘버가 page 넘버 (임의로 이렇게 구현해둠 변경 필요)
    /** 동적 로딩을 위한 NestedScrollView의 아래 부분을 인식 **/
    public void loadItems(NestedScrollView nestedScrollView, final Context context) {
        package_num=0;
        GetItemInfoBody("내 사이즈와 같은 회원의 인기상품");
        GetItemInfoOther("다른 사용자와 유사한 스타일 추천");
        item_recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!item_recyclerView.canScrollVertically(1)){
                    package_num=package_num+1;
                    if(package_num==1) {
                        GetItemInfoUser("스타일 추천");
                    }
                }
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
