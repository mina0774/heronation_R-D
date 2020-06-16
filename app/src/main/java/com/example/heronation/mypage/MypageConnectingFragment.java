package com.example.heronation.mypage;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.heronation.login_register.IntroActivity;
import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.measurement.Body.MeasurementBodyActivity;
import com.example.heronation.measurement.Body.MeasurementBodySizeDetailInfoActivity;
import com.example.heronation.measurement.Style.MeasurementStyleActivity;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;

public class MypageConnectingFragment extends Fragment {
    @BindView(R.id.mypage_userModify_btn) ImageButton mypage_userModify_btn;
    @BindView(R.id.mypage_ninkname_text) TextView mypage_ninkname_text;
    @BindView(R.id.mypage_logout_btn) Button mypage_logout_btn;
    @BindView(R.id.mypage_mysize_btn) Button mypage_mysize_btn;
    @BindView(R.id.mypage_mystyle_btn) Button mypage_mystyle_btn;
    @BindView(R.id.mypage_notice_btn) Button mypage_notice_btn;
    @BindView(R.id.mypage_service_btn) Button mypage_service_btn;
    @BindView(R.id.mypage_personalinfo_btn) Button mypage_personalinfo_btn;

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
                Intent intent=new Intent(getActivity(),UserModifyActivity.class);
                startActivity(intent);
            }
        });

        /*로그아웃 버튼을 눌렀을 때, intro 홈페이지로 이동 */
        mypage_logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refresh_token 값 비우기
                SharedPreferences pref = getActivity().getSharedPreferences("token_management", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.clear();
                editor.commit(); // commit changes

                // 기존 신체 정보 바탕 구독 정보 삭제
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            FirebaseInstanceId.getInstance().deleteInstanceId();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

                Intent intent = new Intent(getActivity(), IntroActivity.class);
                startActivity(intent);
                MainActivity.mainActivity.finish();
            }
        });

        /* mysize 버튼을 눌렀을 경우 */
        mypage_mysize_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeasurementBodyActivity.class);
                startActivity(intent);
            }
        });

        /* mystyle 버튼을 눌렀을 경우 */
        mypage_mystyle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeasurementStyleActivity.class);
                startActivity(intent);
            }
        });

        mypage_notice_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NoticeActivity.class);
                startActivity(intent);
            }
        });

        mypage_personalinfo_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PolicyActivity.class);
                startActivity(intent);
            }
        });

        mypage_service_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PolicyActivity.class);
                startActivity(intent);
            }
        });
        return rootView;
    }

    private void getUserInfo(String access_token){
        //회원정보를 받아옴

        String authorization="bearer " + access_token;
        String accept="application/json";
        APIInterface.UserInfoService userInfoService= ServiceGenerator.createService(APIInterface.UserInfoService.class);
        retrofit2.Call<UserMyInfo> request=userInfoService.UserInfo(authorization,accept);
        request.enqueue(new Callback<UserMyInfo>() {
            @Override
            public void onResponse(Call<UserMyInfo> call, Response<UserMyInfo> response) {
                /* 정상적으로 로그인이 되었을 때 */
                if (response.code() == 200) {
                    UserMyInfo userMyInfo = response.body();
                    //회원정보 설정
                    /* 상단 텍스트뷰 설정 */
                    mypage_ninkname_text.setText(userMyInfo.getName()+"님, "+"안녕하세요!\n"+userMyInfo.getEmail());
                }
                /*토큰 만료기한이 끝나, 재로그인이 필요할 때*/
                else if(response.code()==401) {
                    MainActivity.backgroundThreadShortToast(getActivity(), "세션이 만료되어 재로그인이 필요합니다.");
                    Intent intent=new Intent(getActivity(),IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
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
