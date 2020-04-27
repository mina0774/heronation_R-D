package com.example.heronation.wishlist.topbarFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.measurement.AR.dataClass.SubCategoryResponse;
import com.example.heronation.wishlist.dataClass.ClosetResponse;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.WishlistClosetAdapter;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass.ClosetItem;
import com.example.heronation.wishlist.WishlistClosetEditBodyActivity;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class WishlistClosetFragment extends Fragment {
    /* 리사이클러뷰*/
    @BindView(R.id.recycler_view_wishilist_closet) RecyclerView closet_recyclerView;
    /* 스피너 */
    @BindView(R.id.wishlist_closet_spinner_category) Spinner spinner_category;
    /* 체형 수정 버튼 */
    @BindView(R.id.wishlist_closet_edit_button) Button edit_button;
    /* 상품 목록 등록 */
    public static ArrayList<ClosetItem> item_list;
    @BindView(R.id.closet_body_gender) TextView closet_body_gender;
    @BindView(R.id.closet_body_age) TextView closet_body_age;
    @BindView(R.id.closet_body_height) TextView closet_body_height;
    @BindView(R.id.closet_body_weight) TextView closet_body_weight;
    @BindView(R.id.snackbar_view) CoordinatorLayout snackbar_view;

    @BindView(R.id.have_no_closet_item) TextView have_no_closet_item;

    public static WishlistClosetAdapter wishlistClosetAdapter;
    Integer page_num; // 동적 로딩을 위한 page number
    public static Context context;
    private List<String> cloth_category_list; //옷 카테고리를 담는 변수
    private ArrayAdapter<String> spinner_adapter; // 스피너 어댑터

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_wishlist_closet, container,false);
        ButterKnife.bind(this,rootView);

        context=getActivity();
        item_list=new ArrayList<>();
        cloth_category_list=new ArrayList<>();

        getClothCategory();
        getBodyInfo();

        /* 리사이클러뷰 객체 생성 */
        wishlistClosetAdapter=new WishlistClosetAdapter(getActivity(),item_list);
        /* 레이아웃 매니저 수평으로 지정 */
        closet_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        /* 리사이클러뷰에 어댑터 지정 */
        closet_recyclerView.setAdapter(wishlistClosetAdapter);

        /* 체형 수정 버튼 */
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), WishlistClosetEditBodyActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }

    /* 옷 종류 카테고리 받아오는 함수 - 수정 */
    public void getClothCategory(){
        String authorization = "zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        APIInterface.GetClothCategoryService getClothCategoryService= ServiceGenerator.createService(APIInterface.GetClothCategoryService.class);
        retrofit2.Call<List<SubCategoryResponse>> request=getClothCategoryService.GetCategory(authorization);
        request.enqueue(new Callback<List<SubCategoryResponse>>() {
            @Override
            public void onResponse(Call<List<SubCategoryResponse>> call, Response<List<SubCategoryResponse>> response) {
                if(response.isSuccessful()) {
                    List<SubCategoryResponse> subCategoryResponses = response.body();
                    cloth_category_list.add("전체");
                    for (int i = 0; i < subCategoryResponses.size(); i++) {
                        cloth_category_list.add(subCategoryResponses.get(i).getName()); //해당 리스트는 옷 카테고리 리스트를 볼 수 있는 spinner의 아이템이 된다.
                    }
                    /* 옷 카테고리 리스트를 선택할 수 있는 스피너 어댑터 설정 */
                    if (getActivity() != null) {
                        spinner_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cloth_category_list);
                        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner_category.setAdapter(spinner_adapter);
                        /* 스피너의 옷 카테고리에 따라 측정 리스트를 다르게 뿌려줌 */
                        spinner_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    item_list.clear();
                                    loadItems(cloth_category_list.get(position));
                            }
                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<List<SubCategoryResponse>> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }


    public void GetClosetList(Integer page_num, String cloth_category){
        String authorization="bearer "+MainActivity.access_token;
        String accept="application/json";

        APIInterface.GetClosetListService getClosetListService=ServiceGenerator.createService(APIInterface.GetClosetListService.class);
        retrofit2.Call<ClosetResponse> request= getClosetListService.GetClosetList(page_num,10,"id,desc",authorization,accept);
        request.enqueue(new Callback<ClosetResponse>() {
            @Override
            public void onResponse(Call<ClosetResponse> call, Response<ClosetResponse> response) {
                if(response.code()==200){
                    ClosetResponse closetResponse=response.body();
                    // 옷장에 아이템이 없을 때
                    if(page_num==1&&closetResponse.getSize()==0){
                        have_no_closet_item.setVisibility(View.VISIBLE);
                    }else {
                        // 옷장에 아이템이 있을 때
                        have_no_closet_item.setVisibility(View.GONE);
                        for (int i = 0; i < closetResponse.getSize(); i++) {
                            ClosetResponse.WardrobeResponse wardrobeResponse = closetResponse.getWardrobeResponses().get(i);
                            if (cloth_category.equals("전체")) {
                                item_list.add(new ClosetItem(wardrobeResponse.getImage(), wardrobeResponse.getSubCategoryName(), wardrobeResponse.getName(),
                                        wardrobeResponse.getCreateDt(), wardrobeResponse.getShopmallName(), "AR", wardrobeResponse.getId().toString()));
                            } else if (cloth_category.equals(wardrobeResponse.getSubCategoryName())) {
                                item_list.add(new ClosetItem(wardrobeResponse.getImage(), wardrobeResponse.getSubCategoryName(), wardrobeResponse.getName(),
                                        wardrobeResponse.getCreateDt(), wardrobeResponse.getShopmallName(), "AR", wardrobeResponse.getId().toString()));
                            }
                        }
                        wishlistClosetAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<ClosetResponse> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    /** 동적 로딩을 위한 NestedScrollView의 아래 부분을 인식,
     * cloth category는 현재 스피너가 선택한 옷 카테고리가 어떤 것인지 알려주는 매개변수 **/
    public void loadItems(String cloth_category) {
    //    item_list=new ArrayList<>();
        page_num=1;
        GetClosetList(page_num,cloth_category);
        closet_recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if(!closet_recyclerView.canScrollVertically(1)){
                    page_num=page_num+1;
                    GetClosetList(page_num,cloth_category);
                }
            }
        });
    }


    public void getBodyInfo(){
        String authorization="";
        String accept="application/json";
     /* access_token이 null이면 비회원 사용자이고, access_token의 값이 존재하면 회원 사용자임
        (token이 유효한지 판단한 후에, 이를 통해 로그인 여부를 판단할 수 있음)
        */
        if(!MainActivity.access_token.matches("null")) { //회원 사용자일 때
            authorization="bearer " +MainActivity.access_token;
            APIInterface.UserInfoService userInfoService= ServiceGenerator.createService(APIInterface.UserInfoService.class);
            retrofit2.Call<UserMyInfo> request=userInfoService.UserInfo(authorization,accept);
            request.enqueue(new Callback<UserMyInfo>() {
                @Override
                public void onResponse(Call<UserMyInfo> call, Response<UserMyInfo> response) {
                    if(response.code()==200) { //정상적으로 로그인이 되었을 때
                        UserMyInfo userMyInfo=response.body();
                        closet_body_gender.setText("성별: "+userMyInfo.getGender());
                        SimpleDateFormat format=new SimpleDateFormat("yyyy");
                        Integer age=(Integer.parseInt(format.format(Calendar.getInstance().getTime()))-userMyInfo.getBirthYear()+1); //현재 년도에서 사용자의 태어난 년도를 뺀 후 1을 더한 값.
                        closet_body_age.setText("나이: "+ age.toString()+"세");
                        if(userMyInfo.getHeight()!=null) {
                            closet_body_height.setText("키: " + userMyInfo.getHeight().toString() + "cm");
                        }
                        if(userMyInfo.getWeight()!=null) {
                            closet_body_weight.setText("몸무게: " + userMyInfo.getWeight().toString() + "kg");
                        }
                    }
                    else{ //토큰 만료기한이 끝나, 재로그인이 필요할 때
                        backgroundThreadShortToast(getActivity(), "세션이 만료되어 재로그인이 필요합니다.");
                    }
                }
                @Override
                public void onFailure(Call<UserMyInfo> call, Throwable t) {
                }
            });
        }else { //비회원 사용자일 때
        }
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
        void onFragmentInteraction(Uri uri);
    }
}
