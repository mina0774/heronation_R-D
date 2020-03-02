package com.example.heronation.home.topbarFragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.heronation.adapter.bannerAdapter.bannerAdapter;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ItemBestCategory;
import com.example.heronation.home.itemRecyclerViewAdapter.ItemBestCategoryAdapter;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ItemContent;
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

public class ItemBestFragment extends Fragment {
    @BindView(R.id.nested_item_best) NestedScrollView nested_item_best;
    @BindView(R.id.item_best_item_category) RecyclerView category_recyclerView;
    @BindView(R.id.item_best_items) RecyclerView item_recyclerView;
    private ItemBestCategoryAdapter itemBestCategoryAdapter;
    private ItemVerticalAdapter verticalAdapter;
    private ArrayList<ItemBestCategory> list;
    private ArrayList<ShopItemPackage> item_list;

    /* 배너 슬라이딩을 위한 변수 */
    private bannerAdapter bannerAdapter;
    @BindView(R.id.image_view_best) ViewPager viewPager;
    /* 상품 리스트 묶음 번호 */
    private Integer package_num;
    /* 상품 리스트 묶음 이름의 리스트 */
    private ArrayList<String> package_name_list=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_item_best,container,false);
        ButterKnife.bind(this,rootView);

        //아이템 목록, 아이템 리스트 목록 초기화
        list=new ArrayList<>();
        item_list=new ArrayList<>();

        /* 카테고리 리스트에 아이템 추가
         *  여기서 카테고리 이름이나, 이미지 변경하면 됨*/
        this.make_category();

        /* 리사이클러뷰 객체 생성 */
        itemBestCategoryAdapter=new ItemBestCategoryAdapter(getActivity(),list);
        /* 레이아웃 매니저 수평으로 지정 */
        category_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        /* 리사이클러뷰에 어댑터 지정 */
        category_recyclerView.setAdapter(itemBestCategoryAdapter);

        /*
         * (ex)  수평 리사이클러뷰
         *       수평 리사이클러뷰
         *       수평 리사이클러뷰
         *  3개의 수평 리사이클러뷰가 보여서 수직 리사이클러뷰가 됨
         * */
        /* 아이템 수직 리사이클러뷰 객체 생성 */
        verticalAdapter=new ItemVerticalAdapter(item_list,getActivity());
        /* 레이아웃 매니저 수직으로 지정 */
        item_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
        item_recyclerView.setAdapter(verticalAdapter);

        /* 상품 목록 리스트의 이름 리스트 생성*/
        package_name_list.add("All best");
        package_name_list.add("상의 best");
        package_name_list.add("하의 best");
        loadItems(nested_item_best,getActivity());

        /* 이미지 슬라이딩을 위해 뷰페이저를 이용했고, 이를 설정해주는 이미지 어댑터를 설정하여 슬라이딩 구현 */
        bannerAdapter =new bannerAdapter(getActivity());
        viewPager.setAdapter(bannerAdapter);

        return rootView;
    }

    /*Item의 정보를 얻는 함수*/
    public void GetItemInfo(Integer page_num,String package_name) {
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
        package_num=4;
        GetItemInfo(package_num,package_name_list.get(package_num-4));
        item_recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if(!item_recyclerView.canScrollVertically(1)){
                    if(package_num<6) {
                        package_num+=1;
                        GetItemInfo(package_num, package_name_list.get(package_num-4));
                    }
                }
            }
        });

    }


    public void make_category(){
        /* 카테고리 리스트에 아이템 추가
         *  여기서 카테고리 이름이나, 이미지 변경하면 됨
         */
        addItem(getResources().getDrawable(R.drawable.ic_item_all),"전체");
        addItem(getResources().getDrawable(R.drawable.ic_all_tshirts),"상의");
        addItem(getResources().getDrawable(R.drawable.ic_all_pants),"하의");
        addItem(getResources().getDrawable(R.drawable.ic_all_outer),"아우터");
        addItem(getResources().getDrawable(R.drawable.ic_all_onepiece),"원피스");
        addItem(getResources().getDrawable(R.drawable.ic_all_skirt),"스커트");
        addItem(getResources().getDrawable(R.drawable.ic_all_shoes),"슈즈");
        addItem(getResources().getDrawable(R.drawable.ic_all_bag),"가방");
        addItem(getResources().getDrawable(R.drawable.ic_all_acc),"액세서리");
        addItem(getResources().getDrawable(R.drawable.ic_all_socks),"패션소품");
    }

    public void addItem(Drawable icon, String name){
        ItemBestCategory item=new ItemBestCategory(icon,name);
        list.add(item);
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