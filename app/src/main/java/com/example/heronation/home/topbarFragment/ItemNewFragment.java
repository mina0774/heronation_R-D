package com.example.heronation.home.topbarFragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.heronation.adapter.bannerAdapter.bannerAdapter;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ItemContent;
import com.example.heronation.home.itemRecyclerViewAdapter.ItemNewAdapter;
import com.example.heronation.home.itemRecyclerViewAdapter.ItemVerticalAdapter;
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


public class ItemNewFragment extends Fragment {
    @BindView(R.id.item_new_recyclerView1) RecyclerView item_recyclerView1;
    @BindView(R.id.item_new_recyclerView_grid) RecyclerView item_recyclerView_grid;
    @BindView(R.id.nested_item_new)  NestedScrollView nested_item_new;

    private ItemVerticalAdapter newAdapter1;
    private ArrayList<ShopItemPackage> item_list1;

    private ItemNewAdapter newAdapter2;
    private ArrayList<ShopItemPackage> item_list2;


    /* 배너 슬라이딩을 위한 변수 */
    @BindView(R.id.image_view_new) ViewPager viewPager;
    private bannerAdapter bannerAdapter;

    /* 스피너 */
    @BindView(R.id.item_new_spinner1) Spinner spinner_category;
    @BindView(R.id.item_new_spinner2) Spinner spinner_order;
    /* 상품 리스트 묶음 번호 */
    private Integer package_num;
    /* 상품 리스트 묶음 이름의 리스트 */
    private ArrayList<String> package_name_list=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_item_new,container,false);
        ButterKnife.bind(this,rootView);
        nested_item_new=(NestedScrollView)rootView.findViewById(R.id.nested_item_new);

        item_list1=new ArrayList<>();
        item_list2=new ArrayList<>();

        /* 상품 목록 리스트의 이름 리스트 생성*/
        package_name_list.add("급상승");
        package_name_list.add("신상품 best");
        /* 수직 리사이클러뷰의 하나의 아이템에 수평 리사이클러뷰의 아이템을 수평 방향으로 배치 설정, 어댑터 지정
         * (ex)  수평 리사이클러뷰
         *       수평 리사이클러뷰
         *       수평 리사이클러뷰
         * */
        /* 첫번째 리사이클러뷰*/
        /* 아이템 수직 리사이클러뷰 객체 생성 */
        newAdapter1=new ItemVerticalAdapter(item_list1,getActivity()); //New Adapter 안에 horizontal adapter를 선언하여 이에 대한 레이아웃을 horizontal로 지정
        /* 레이아웃 매니저 수직으로 지정 */
        item_recyclerView1.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        item_recyclerView1.setAdapter(newAdapter1);

        /* 두번째 리사이클러뷰*/
        /* 아이템 수직 리사이클러뷰 객체 생성 */
        newAdapter2=new ItemNewAdapter(item_list2,getActivity()); //New Adapter 안에 horizontal adapter를 선언하여 이에 대한 레이아웃을 Grid로 지정
        /* 레이아웃 매니저 수직으로 지정 */
        item_recyclerView_grid.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        item_recyclerView_grid.setAdapter(newAdapter2);

        loadItems(nested_item_new,getActivity());

        //spinnerArray.xml에서 생성한 item을 String 배열로 가져오기
        String[] str_category=getResources().getStringArray(R.array.spinnerArray_category);
        String[] str_order=getResources().getStringArray(R.array.spinnerArray_order);

        //item_new_spinner_item과 str_category, str_order를 인자로 어댑터를 생성하고, 어댑터를 설정
        ArrayAdapter<String> adapter_category=new ArrayAdapter<String>(getContext(), R.layout.item_new_spinner_item,str_category);
        ArrayAdapter<String> adapter_order=new ArrayAdapter<String>(getContext(), R.layout.item_new_spinner_item,str_order);
        adapter_category.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        adapter_order.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter_category);
        spinner_order.setAdapter(adapter_order);

        /* 이미지 슬라이딩을 위해 뷰페이저를 이용했고, 이를 설정해주는 이미지 어댑터를 설정하여 슬라이딩 구현 */
        bannerAdapter =new bannerAdapter(getActivity());
        viewPager.setAdapter(bannerAdapter);

        return rootView;
    }
    /*첫번째 Fragment Item의 정보를 얻는 함수*/
    public void GetItemInfo1(Integer page_num,String package_name) {
        String authorization = "zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        String accept = "application/json";

        ItemHomeFragment.ItemInfoService itemInfoService = ServiceGenerator.createService(ItemHomeFragment.ItemInfoService.class);
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
                    item_list1.add(new ShopItemPackage(package_name,item_info));
                    newAdapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ShopItemInfo> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    /*첫번째 Fragment Item의 정보를 얻는 함수*/
    public void GetItemInfo2(Integer page_num,String package_name) {
        String authorization = "zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        String accept = "application/json";

        ItemHomeFragment.ItemInfoService itemInfoService = ServiceGenerator.createService(ItemHomeFragment.ItemInfoService.class);
        retrofit2.Call<ShopItemInfo> request = itemInfoService.ItemInfo(page_num,6,"id,asc","heronation","cafe24", authorization, accept);
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
                    item_list2.add(new ShopItemPackage(package_name,item_info));
                    newAdapter2.notifyDataSetChanged();
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
        GetItemInfo1(package_num,package_name_list.get(package_num-1));
        package_num+=1;
        GetItemInfo1(package_num, package_name_list.get(package_num-1));
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    if(package_num<8) {
                        package_num += 1;
                        GetItemInfo2(package_num, "");
                    }
                }
            }
        });
    }

        /* 아이템(상품) 추가
        ArrayList<ShopItem> shopItem1=new ArrayList<>();
        shopItem1.add(new ShopItem("https://slowand.com/web/product/medium/20191231/19123abae92c3f10204863e9d4bba5b9.webp",
                "버터 케이블 가디건", "슬로우앤드", 30000, 25000));
        shopItem1.add(new ShopItem("https://slowand.com/web/product/medium/20200106/d43bb6f547046b1924d113cd1eef352c.webp",
                "마리 세미크롭 펀칭니트", "슬로우앤드", 53000, 45000));
        shopItem1.add(new ShopItem("https://slowand.com/web/product/medium/201911/9f06ecd9233c6627262923c3e0d56c14.gif",
                "핸드메이드 코트", "고고싱", 63000, 60000));
        shopItem1.add(new ShopItem("https://slowand.com/web/product/medium/20191209/9b76134f5337b694553eb9fb190961b3.gif",
                "빈티지 노르딕 가디건", "고고싱", 40000, 31000));
        shopItem1.add(new ShopItem("https://slowand.com/web/product/medium/20191231/4ef6dd9fedd4a31ed7d56d427fc90b67.webp",
                "오트밀 세인트 핸드메이드 코트", "고고싱", 53000, 50000));
        // 상품들 묶음 추가
        item_list1.add(new ShopItemPackage("급상승",shopItem1));





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
