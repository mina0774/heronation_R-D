package com.example.heronation.mypage;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.heronation.R;
import com.example.heronation.login_register.loginPageActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MypageFragment extends Fragment {
    @BindView(R.id.mypage_signInUp_btn) Button mypage_signInUp_btn;
    @BindView(R.id.mypage_notice_btn) Button mypage_notice_btn;
    @BindView(R.id.mypage_service_btn) Button mypage_service_btn;
    @BindView(R.id.mypage_personalinfo_btn) Button mypage_personalinfo_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_mypage,container,false);
        ButterKnife.bind(this,rootView);
        /* 로그인/회원가입 버튼 클릭시 이동 */
        mypage_signInUp_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), loginPageActivity.class);
                startActivity(intent);
                getActivity().finish();
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

    /* Acitivity와 Fragment가 통신할 때, OnFragmentInteractionListener를 사용함.
     * 프래그먼트에서 액티비티로 통신(데이터 주고 받는 것)이 있을 수도 있기 때문에
     * MainActivity 에서 이를 implement한 후 오버라이딩 시켜줄 것이다. (임시로)
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
