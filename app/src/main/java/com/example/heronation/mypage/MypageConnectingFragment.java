package com.example.heronation.mypage;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.example.heronation.login_register.dataClass.UserMyInfo;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MypageConnectingFragment extends Fragment {
    @BindView(R.id.mypage_userModify_btn) ImageButton mypage_userModify_btn;

    @BindView(R.id.mypage_ninkname_text) TextView mypage_ninkname_text;

    @BindView(R.id.mypage_gender_f) TextView mypage_gender_f;

    @BindView(R.id.mypage_gender_m) TextView mypage_gender_m;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_mypage_connecting,container,false);
        ButterKnife.bind(this,rootView);
        getUserInfo( MainActivity.access_token);

        /* 회원 정보 수정 버튼을 눌렀을 때 */
        mypage_userModify_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserModifyFragment userModifyFragment=new UserModifyFragment();
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, userModifyFragment).commit();
            }
        });

        return rootView;
    }

    private void getUserInfo(String access_token){
        //회원정보를 받아옴

        String authorization="bearer " + access_token;
        String accept="application/json";
        MainActivity.UserInfoService userInfoService= ServiceGenerator.createService(MainActivity.UserInfoService.class);
        retrofit2.Call<UserMyInfo> request=userInfoService.UserInfo(authorization,accept);
        request.enqueue(new Callback<UserMyInfo>() {
            @Override
            public void onResponse(Call<UserMyInfo> call, Response<UserMyInfo> response) {
                /* 정상적으로 로그인이 되었을 때 */
                if (response.code() == 200) {
                    UserMyInfo userMyInfo = response.body();
                    //회원정보 설정
                    /* 상단 텍스트뷰 설정 */
                    mypage_ninkname_text.setText("안녕하세요!\n"+userMyInfo.getName()+"님\n"+userMyInfo.getEmail());
                    /* 성별 설정 */
                    if(userMyInfo.getGender().matches("M")){
                        mypage_gender_m.setTextColor(Color.parseColor("#000000"));
                    }else if(userMyInfo.getGender().matches("F")){
                        mypage_gender_f.setTextColor(Color.parseColor("#000000"));
                    }
                }
                /*토큰 만료기한이 끝나, 재로그인이 필요할 때*/
                else {
                    MainActivity.backgroundThreadShortToast(getActivity(), "세션이 만료되어 재로그인이 필요합니다.");
                }
            }
            @Override
            public void onFailure(Call<UserMyInfo> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
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


}
