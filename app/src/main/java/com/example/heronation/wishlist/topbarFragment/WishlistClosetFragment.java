package com.example.heronation.wishlist.topbarFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.WishlistClosetAdapter;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass.ClosetItem;
import com.example.heronation.wishlist.WishlistClosetDeleteActivity;
import com.example.heronation.wishlist.WishlistClosetEditBodyActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

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
    /* 옷장 아이템 삭제 버튼 */
    @BindView(R.id.closet_delete) ImageButton delete_button;
    /* 상품 목록 등록 */
    private ArrayList<ClosetItem> item_list;
    @BindView(R.id.closet_body_gender) TextView closet_body_gender;
    @BindView(R.id.closet_body_age) TextView closet_body_age;
    @BindView(R.id.closet_body_height) TextView closet_body_height;
    @BindView(R.id.closet_body_weight) TextView closet_body_weight;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_wishlist_closet, container,false);
        ButterKnife.bind(this,rootView);

        item_list=new ArrayList<>();
        this.make_item_list();

        getBodyInfo();
        /* 리사이클러뷰 객체 생성 */
        WishlistClosetAdapter wishlistClosetAdapter=new WishlistClosetAdapter(getActivity(),item_list);
        /* 레이아웃 매니저 수평으로 지정 */
        closet_recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false));
        /* 리사이클러뷰에 어댑터 지정 */
        closet_recyclerView.setAdapter(wishlistClosetAdapter);

        //spinnerArray.xml에서 생성한 item을 String 배열로 가져오기
        String[] str_category=getResources().getStringArray(R.array.spinnerArray_category);

        //item_new_spinner_item과 str_category, str_order를 인자로 어댑터를 생성하고, 어댑터를 설정
        ArrayAdapter<String> adapter_category=new ArrayAdapter<String>(getContext(), R.layout.item_new_spinner_item,str_category);
        adapter_category.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner_category.setAdapter(adapter_category);

        /* 체형 수정 버튼 */
        edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), WishlistClosetEditBodyActivity.class);
                startActivity(intent);
            }
        });

        /* 옷장 아이템 삭제 버튼 */
        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getContext(), WishlistClosetDeleteActivity.class);
                intent.putExtra("closet item",item_list);
                startActivity(intent);
            }
        });

        return rootView;
    }

    // 등록된 item 정보를 만드는 함수
    public void make_item_list(){
        item_list.add(new ClosetItem("티셔츠","니트","2019/10/20","육육걸즈","AR"));
        item_list.add(new ClosetItem("하의","슬랙스","2019/10/21","고고싱","직접 입력"));
        item_list.add(new ClosetItem("하의","청바지","2019/10/22","유니클로","신체 비교"));
    }

    public void getBodyInfo(){
        String authorization="";
        String accept="application/json";
     /* access_token이 null이면 비회원 사용자이고, access_token의 값이 존재하면 회원 사용자임
        (token이 유효한지 판단한 후에, 이를 통해 로그인 여부를 판단할 수 있음)
        */
        if(!MainActivity.access_token.matches("null")) { //회원 사용자일 때
            authorization="bearer " +MainActivity.access_token;
            MainActivity.UserInfoService userInfoService= ServiceGenerator.createService(MainActivity.UserInfoService.class);
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
                        closet_body_height.setText("키: "+userMyInfo.getHeight().toString()+"cm");
                        closet_body_weight.setText("몸무게: "+userMyInfo.getWeight().toString()+"kg");
                    }
                    else{ //토큰 만료기한이 끝나, 재로그인이 필요할 때
                        backgroundThreadShortToast(getActivity(), "세션이 만료되어 재로그인이 필요합니다.");
                    }
                }
                @Override
                public void onFailure(Call<UserMyInfo> call, Throwable t) {
                }
            });
        }else {//비회원 사용자일 때
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
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
