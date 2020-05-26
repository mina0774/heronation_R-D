package com.example.heronation.home.topbarFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.heronation.adapter.bannerAdapter.bannerAdapter;
import com.example.heronation.home.dataClass.Content;
import com.example.heronation.home.dataClass.SearchItemInfo;
import com.example.heronation.home.itemRecyclerViewAdapter.ItemSearchAdapter;
import com.example.heronation.home.itemRecyclerViewAdapter.ItemStyleVerticalAdapter;
import com.example.heronation.home.ItemSearchActivity;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.StyleRecommendation;
import com.example.heronation.login_register.IntroActivity;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ShopItemPackage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ItemHomeFragment extends Fragment {
    @BindView(R.id.item_home_recyclerViewVertical1) RecyclerView item_recyclerView;
    @BindView(R.id.item_home_best_recyclerView) RecyclerView item_home_best_recyclerView;
    @BindView(R.id.nested_item_home) NestedScrollView nested_item_home;

    /* 필터 버튼 */
    @BindView(R.id.item_home_filter) ImageButton filter_button;

    /* 배너 슬라이딩을 위한 변수 */
    private bannerAdapter bannerAdapter;
    @BindView(R.id.image_view_home) ViewPager viewPager;

    /* 검색창 */
    @BindView(R.id.item_home_search_edittext) TextView search_item;

    @BindView(R.id.have_no_user_info) TextView have_no_user_info;
    //아이템들의 묶음
    private ArrayList<ShopItemPackage> item_list;
    public static List<Content> item_best_list;
    /* 아이템 수평 리스트 담는 수직 어댑터*/
    private ItemStyleVerticalAdapter verticalAdapter;
    private static ItemSearchAdapter itemSearchAdapter;

    public static long startTime;
    private Button log;
    public static TextView log_textview;
    public static String style_tag_id;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        startTime=System.nanoTime(); // 시간 측정

        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_item_home,container,false);
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

        /* 인기 상품 아이템 리스트 */
        item_best_list=new ArrayList<>();
        item_home_best_recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2,GridLayoutManager.VERTICAL,false));
        item_home_best_recyclerView.setPadding(50,0,0,0);
        itemSearchAdapter=new ItemSearchAdapter(item_best_list,getActivity());
        item_home_best_recyclerView.setAdapter(itemSearchAdapter);

        loadItems();

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

        retrofit2.Call<ArrayList<StyleRecommendation>> request = itemInfoService.ShopItemInfo(style_tag_id, authorization, accept); //사용자 정보 받아오기
        request.enqueue(new Callback<ArrayList<StyleRecommendation>>() {
            @Override
            public void onResponse(Call<ArrayList<StyleRecommendation>> call, Response<ArrayList<StyleRecommendation>> response) {
                if(response.code()==200) {
                    //아이템의 데이터를 받는 리스트
                    ArrayList<StyleRecommendation> shopItemInfo = response.body();
                    item_list.add(new ShopItemPackage(package_name,shopItemInfo));
                    verticalAdapter.notifyItemChanged(1);
                    if(item_list.size()==0){
                        have_no_user_info.setVisibility(View.VISIBLE);
                    }else if(item_list.size()!=0){
                        have_no_user_info.setVisibility(View.GONE);
                    }
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
        String authorization = "bearer "+MainActivity.access_token;
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
                    verticalAdapter.notifyItemChanged(2);
                    if(item_list.size()==0){
                        have_no_user_info.setVisibility(View.VISIBLE);
                    }else if(item_list.size()!=0){
                        have_no_user_info.setVisibility(View.GONE);
                    }
                }else if(response.code()==401){
                    backgroundThreadShortToast(getActivity(), "세션이 만료되어 재로그인이 필요합니다."); // 토스트 메시지 ( 메인 쓰레드에서 실행되어야하므로 사용 )
                    Intent intent=new Intent(getActivity(), IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
        String authorization = "bearer "+MainActivity.access_token;
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
                    verticalAdapter.notifyItemChanged(0);
                    if(item_list.size()==0){
                        have_no_user_info.setVisibility(View.VISIBLE);
                    }else if(item_list.size()!=0){
                        have_no_user_info.setVisibility(View.GONE);
                    }
                }else if(response.code()==401){
                    backgroundThreadShortToast(getActivity(), "세션이 만료되어 재로그인이 필요합니다."); // 토스트 메시지 ( 메인 쓰레드에서 실행되어야하므로 사용 )
                    Intent intent=new Intent(getActivity(), IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<StyleRecommendation>> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    /*Item의 정보를 얻는 함수*/
    public void GetItemInfo(Integer subCategoryId) {
        String authorization = "zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        String accept = "application/json";
        APIInterface.ItemSortByCategoryService itemInfoService = ServiceGenerator.createService(APIInterface.ItemSortByCategoryService.class);
        Call<SearchItemInfo> request = itemInfoService.ItemInfo(1,20,"hit,desc",subCategoryId, authorization, accept);
        request.enqueue(new Callback<SearchItemInfo>() {
            @Override
            public void onResponse(Call<SearchItemInfo> call, Response<SearchItemInfo> response) {
                if(response.code()==200) {
                    SearchItemInfo shopItemInfo = response.body();
                    /* Shop 목록을 생성함 */
                    for(int i = 0; i<shopItemInfo.getContent().size(); i++){
                        item_best_list.add(shopItemInfo.getContent().get(i));
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

    // 스타일 태그 받아와서 스타일 추천 리스트를 뿌려줌
    public void GetUserInfo() {
        String authorization = "bearer " + MainActivity.access_token;
        String accept = "application/json";
        APIInterface.UserInfoService userInfoService = ServiceGenerator.createService(APIInterface.UserInfoService.class);
        retrofit2.Call<UserMyInfo> request = userInfoService.UserInfo(authorization, accept);
        request.enqueue(new Callback<UserMyInfo>() {
            @Override
            public void onResponse(Call<UserMyInfo> call, Response<UserMyInfo> response) {
                if (response.code() == 200) { //정상적으로 로그인이 되었을 때
                    UserMyInfo userMyInfo = response.body(); // 사용자 정보를 받아온 후에
                    if(userMyInfo.getStyleTagResponses()!=null) {
                        style_tag_id="";
                        for (int i = 0; i < userMyInfo.getStyleTagResponses().size(); i++) {
                            style_tag_id += userMyInfo.getStyleTagResponses().get(i).getId() + ",";
                            if (i == userMyInfo.getStyleTagResponses().size() - 1) {
                                style_tag_id += userMyInfo.getStyleTagResponses().get(i).getId();
                            }
                        }
                    }
                    GetItemInfoUser("스타일 추천");
                } else { //토큰 만료기한이 끝나, 재로그인이 필요할 때
                    backgroundThreadShortToast(getActivity(), "세션이 만료되어 재로그인이 필요합니다."); // 토스트 메시지 ( 메인 쓰레드에서 실행되어야하므로 사용 )
                    Intent intent = new Intent(getActivity(), IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<UserMyInfo> call, Throwable t) {
            }
        });
    }


    //Toast는 비동기 태스크 내에서 처리할 수 없으므로, 메인 쓰레드 핸들러를 생성하여 toast가 메인쓰레드에서 생성될 수 있도록 처리해준다.
    public static void backgroundThreadShortToast(final Context context, final String msg) {
        if (context != null && msg != null) {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //package 넘버가 page 넘버 (임의로 이렇게 구현해둠 변경 필요)
    /** 동적 로딩을 위한 NestedScrollView의 아래 부분을 인식 **/
    public void loadItems() {
        GetItemInfoBody("사이즈 추천");
        GetItemInfoOther("비슷한 스타일 유저의 추천");
        GetUserInfo();

        GetItemInfo(null);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
