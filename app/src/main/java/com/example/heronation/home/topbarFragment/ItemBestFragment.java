package com.example.heronation.home.topbarFragment;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.heronation.adapter.bannerAdapter.bannerAdapter;
import com.example.heronation.home.dataClass.Content;
import com.example.heronation.home.dataClass.SearchItemInfo;
import com.example.heronation.home.itemRecyclerViewAdapter.ItemSearchAdapter;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ItemBestCategory;
import com.example.heronation.home.itemRecyclerViewAdapter.ItemBestCategoryAdapter;
import com.example.heronation.R;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemBestFragment extends Fragment {
    @BindView(R.id.item_best_item_category) RecyclerView category_recyclerView;
    @BindView(R.id.item_best_items) RecyclerView item_recyclerView;
    private ItemBestCategoryAdapter itemBestCategoryAdapter;
    private ArrayList<ItemBestCategory> list;

    private static ItemSearchAdapter itemSearchAdapter;
    public static List<Content> item_list;

    /* 배너 슬라이딩을 위한 변수 */
    private bannerAdapter bannerAdapter;
    @BindView(R.id.image_view_best) ViewPager viewPager;


    public static long startTime;
    private Button log;
    public static TextView log_textview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 시간 측정
        startTime=System.nanoTime();

        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_item_best,container,false);
        ButterKnife.bind(this,rootView);


        // 시간 측정 관련 로그
        log=rootView.findViewById(R.id.log);
        log_textview=rootView.findViewById(R.id.log_textview);
        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log_textview.setVisibility(View.VISIBLE);
            }
        });


        /* 카테고리 리스트에 아이템 추가
         *  여기서 카테고리 이름이나, 이미지 변경하면 됨*/
        list=new ArrayList<>();
        this.make_category();
        /* 리사이클러뷰 객체 생성 */
        itemBestCategoryAdapter=new ItemBestCategoryAdapter(getActivity(),list);
        /* 레이아웃 매니저 수평으로 지정 */
        category_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
        /* 리사이클러뷰에 어댑터 지정 */
        category_recyclerView.setAdapter(itemBestCategoryAdapter);

        /* 상품 아이템 리스트 */
        item_list=new ArrayList<>();
        item_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false));
        item_recyclerView.setPadding(50,0,0,0);
        itemSearchAdapter=new ItemSearchAdapter(item_list,getActivity());
        item_recyclerView.setAdapter(itemSearchAdapter);
        // 첫 화면 - 전체
        GetItemInfo(null);

        /* 이미지 슬라이딩을 위해 뷰페이저를 이용했고, 이를 설정해주는 이미지 어댑터를 설정하여 슬라이딩 구현 */
        bannerAdapter =new bannerAdapter(getActivity());
        viewPager.setAdapter(bannerAdapter);

        return rootView;
    }

    /*Item의 정보를 얻는 함수*/
    public static void GetItemInfo(Integer subCategoryId) {
        String authorization = "zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        String accept = "application/json";
        item_list.clear();
        APIInterface.ItemSortByCategoryService itemInfoService = ServiceGenerator.createService(APIInterface.ItemSortByCategoryService.class);
        Call<SearchItemInfo> request = itemInfoService.ItemInfo(1,20,"hit,desc",subCategoryId, authorization, accept);
        request.enqueue(new Callback<SearchItemInfo>() {
            @Override
            public void onResponse(Call<SearchItemInfo> call, Response<SearchItemInfo> response) {
                if(response.code()==200) {
                    SearchItemInfo shopItemInfo = response.body();
                    /* Shop 목록을 생성함 */
                    for(int i = 0; i<shopItemInfo.getContent().size(); i++){
                        item_list.add(shopItemInfo.getContent().get(i));
                    }
                }
                itemSearchAdapter.notifyDataSetChanged();
                // 시간 측정
                long endTime=System.nanoTime();
                String log="elapsed time: "+(double)(endTime- ItemBestFragment.startTime)/1000000000.0;
                ItemBestFragment.log_textview.setText(log);
            }

            @Override
            public void onFailure(Call<SearchItemInfo> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }


    public void make_category(){
        /* 카테고리 리스트에 아이템 추가
         *  여기서 카테고리 이름이나, 이미지 변경하면 됨
         */
        addItem(getResources().getDrawable(R.drawable.ic_item_all),"전체");
        addItem(getResources().getDrawable(R.drawable.img_tshirt),"티셔츠/맨투맨");
        addItem(getResources().getDrawable(R.drawable.img_cardigan),"가디건");
        addItem(getResources().getDrawable(R.drawable.img_coat),"코트");
        addItem(getResources().getDrawable(R.drawable.img_jacket),"자켓");
        addItem(getResources().getDrawable(R.drawable.img_shirt),"셔츠/남방");
        addItem(getResources().getDrawable(R.drawable.img_blouse),"블라우스");
        addItem(getResources().getDrawable(R.drawable.img_padding),"패딩/점퍼");
        addItem(getResources().getDrawable(R.drawable.img_vest),"조끼/베스트");
        addItem(getResources().getDrawable(R.drawable.img_hood),"후드");
        addItem(getResources().getDrawable(R.drawable.img_sleeveless),"슬리브리스");
        addItem(getResources().getDrawable(R.drawable.img_onepiece),"원피스");
        addItem(getResources().getDrawable(R.drawable.img_pants),"팬츠/데님팬츠");
        addItem(getResources().getDrawable(R.drawable.img_short_pants),"숏팬츠");
        addItem(getResources().getDrawable(R.drawable.img_skirt),"스커트");
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