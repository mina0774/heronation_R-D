package com.example.heronation.home.topbarFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ItemContent;
import com.example.heronation.home.ItemSearchActivity;
import com.example.heronation.home.itemRecyclerViewAdapter.ItemVerticalAdapter;
import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
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

    //아이템들의 묶음
    private ArrayList<ShopItemPackage> item_list;

    private ItemVerticalAdapter verticalAdapter;

    /* 필터 버튼 */
    @BindView(R.id.item_home_filter) ImageButton filter_button;

    /* 배너 슬라이딩을 위한 변수 */
    private bannerAdapter bannerAdapter;
    @BindView(R.id.image_view_home) ViewPager viewPager;

    /* 검색창 */
    @BindView(R.id.item_home_search_edittext) EditText search_item;

    /* 상품 리스트 묶음 번호 */
    private Integer package_num;
    /* 상품 리스트 묶음 이름의 리스트 */
    private ArrayList<String> package_name_list=new ArrayList<>();

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
        verticalAdapter=new ItemVerticalAdapter(item_list,getActivity());
        item_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        item_recyclerView.setAdapter(verticalAdapter);

        /* 상품 목록 리스트의 이름 리스트 생성*/
        package_name_list.add("신상품");
        package_name_list.add("내 사이즈 추천");
        package_name_list.add("내 사이즈와 같은 회원의 인기상품");
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


    /*Item의 정보를 얻는 함수*/
    public void GetItemInfo(Integer page_num,String package_name) {
        String authorization = "zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        String accept = "application/json";

        ItemInfoService itemInfoService = ServiceGenerator.createService(ItemInfoService.class);
        retrofit2.Call<ShopItemInfo> request = itemInfoService.ItemInfo(page_num,5,"id,asc","heronation","cafe24", authorization, accept);
        request.enqueue(new Callback<ShopItemInfo>() {
            @Override
            public void onResponse(Call<ShopItemInfo> call, Response<ShopItemInfo> response) {
                System.out.println("Response" + response.code());
                if(response.code()==200) {
                    //아이템의 데이터를 받는 리스트
                    ArrayList<ItemContent> item_info=new ArrayList<>();
                    ShopItemInfo shopItemInfo = response.body();
                    /* Shop 목록을 생성함 */
                    for(int i = 0; i<shopItemInfo.getContent().size(); i++){
                        item_info.add(shopItemInfo.getContent().get(i));
                    }
                    item_list.add(new ShopItemPackage(package_name,item_info));
                    verticalAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ShopItemInfo> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    //package 넘버가 page 넘버 (임의로 이렇게 구현해둠 변경 필요)
    /** 동적 로딩을 위한 NestedScrollView의 아래 부분을 인식 **/
    public void loadItems(NestedScrollView nestedScrollView, final Context context) {
        package_num=1;
        GetItemInfo(package_num,package_name_list.get(package_num-1));
        item_recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!item_recyclerView.canScrollVertically(1)){
                    if(package_num<3) {
                        package_num+=1;
                        GetItemInfo(package_num, package_name_list.get(package_num-1));
                    }
                }
            }
        });
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //인터페이스 - 추상 메소드(구현부가 없는 메시드)의 모임
    /* retrofit은 인터페이스에 기술된 명세를 Http API(호출 가능한 객체)로 전환해줌
    => 우리가 요청할 API들에 대한 명세만을 Interface에 기술해두면 됨.
     */
    /* 사용자 정보를 서버에서 받아오는 인터페이스*/
    public interface ItemInfoService {
        @GET("api/items/test")
        retrofit2.Call<ShopItemInfo> ItemInfo(@Query("page") Integer page,
                                              @Query("size") Integer size,
                                              @Query("sort") String sort,
                                              @Query("storeId") String storeId,
                                              @Query("storeType") String storeType,
                                              @Header("authorization") String authorization,
                                              @Header("Accept") String accept);
    }

    /* 아이템 찜 추가 */
    public interface ItemRegisterService {
        @POST("api/consumers/items/{item_id}/interest")
        retrofit2.Call<String> ItemRegister(@Path("item_id") Integer item_id,
                                            @Header("authorization") String authorization,
                                            @Header("Accept") String accept,
                                            @Header("Content-Type") String content_type);
    }

    /* 아이템 찜 삭제*/
    public interface ItemDeleteService {
        @DELETE("api/consumers/items/{item_id}/interest")
        retrofit2.Call<String> ItemDelete(@Path("item_id") Integer item_id,
                                          @Header("authorization") String authorization,
                                          @Header("Accept") String accept,
                                          @Header("Content-Type") String content_type);
    }
}
