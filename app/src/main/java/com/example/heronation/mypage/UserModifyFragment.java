package com.example.heronation.mypage;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.heronation.login_register.DatePickerFragment;
import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.example.heronation.mypage.dataClass.UserModifyInfo;
import com.example.heronation.login_register.dataClass.UserMyInfo;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.PATCH;

public class UserModifyFragment extends Fragment{
    @BindView(R.id.textView_modify_datepicker) TextView modify_datepicker;
    @BindView(R.id.userModify_edit_btn) TextView userModify_edit_btn;
    @BindView(R.id.userModify_id_text) TextView userModify_id_text;
    @BindView(R.id.userModify_present_pw_et) TextView userModify_present_pw_et;
    @BindView(R.id.userModify_email_text) TextView userModify_email_text;
    @BindView(R.id.userModify_name_text) EditText userModify_name_text;
    @BindView(R.id.userModify_female) TextView userModify_female;
    @BindView(R.id.userModify_male) TextView userModify_male;
    @BindView(R.id.userModify_ad_check) CheckBox userModify_ad_check;
    @BindView(R.id.userModify_hi_nickname_text) TextView userModify_hi_nickname_text;

    Integer modify_year;
    Integer modify_month;
    Integer modify_date;
    String gender;
    String termsAdvertisement;
    String user_name;

    /*생년월일 TextView클릭시 showDatePicker가 실행됨*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_modify, container, false);
        ButterKnife.bind(this,rootView);

        // 현재 유저 정보를 받아와 화면에 표시
        getCurrentUserInfo();

        //날짜 변경
        modify_datepicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        //성별 변경 - 여자
        userModify_female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModify_female.setTextColor(Color.parseColor("#000000"));
                userModify_male.setTextColor(Color.parseColor("#d3d3d3"));
                gender="F";
            }
        });
        //성별 변경 - 남자
        userModify_male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userModify_female.setTextColor(Color.parseColor("#d3d3d3"));
                userModify_male.setTextColor(Color.parseColor("#000000"));
                gender="M";
            }
        });

        // 수정 버튼을 눌렀을 때ㅜ
        userModify_edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyUserInfo(); //회원 정보 수정
            }
        });
        return rootView;
    }

    //회원 정보를 수정
    private void modifyUserInfo(){
        //변경된 정보를 담은 객체 생성
        user_name=userModify_name_text.getText().toString();
        if(userModify_ad_check.isChecked()){
            termsAdvertisement="Y";
        }else if(!userModify_ad_check.isChecked()){
            termsAdvertisement="N";
        }
        UserModifyInfo userModifyInfo=new UserModifyInfo(user_name,modify_year,modify_month,modify_date,gender,termsAdvertisement);

        String authorization="bearer "+ MainActivity.access_token;
        String accept="application/json";
        String content_type="application/json";
        ModifyUserInfoService modifyUserInfoService= ServiceGenerator.createService(ModifyUserInfoService.class);
        retrofit2.Call<String> request=modifyUserInfoService.ModifyUserInfo(authorization,accept,content_type,userModifyInfo);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String>  response) {
                if(response.code()==204) { //정상적으로 로그인이 되었을 때
                    Bundle bundle1=new Bundle();
                    bundle1.putString("access_token", MainActivity.access_token);
                    MainActivity.backgroundThreadShortToast(getActivity(), "수정되었습니다.");
                    MypageConnectingFragment mypageConnectingFragment=new MypageConnectingFragment();
                    mypageConnectingFragment.setArguments(bundle1);
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, mypageConnectingFragment).commit();
                }
                else{ //토큰 만료기한이 끝나, 재로그인이 필요할 때
                   MainActivity.backgroundThreadShortToast(getActivity(), "세션이 만료되어 재로그인이 필요합니다.");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    // 현재 유저 정보를 받아와 화면에 표시
    private void getCurrentUserInfo(){
        //회원정보를 받아옴
        String authorization="bearer " + MainActivity.access_token;
        String accept="application/json";
        MainActivity.UserInfoService userInfoService=ServiceGenerator.createService(MainActivity.UserInfoService.class);
        retrofit2.Call<UserMyInfo> request=userInfoService.UserInfo(authorization,accept);
        request.enqueue(new Callback<UserMyInfo>() {
            @Override
            public void onResponse(Call<UserMyInfo> call, Response<UserMyInfo> response) {
                /* 정상적으로 로그인이 되었을 때 */
                if (response.code() == 200) {
                    UserMyInfo userMyInfo = response.body();
                    /* 현재 유저 정보값 설정 */
                    userModify_hi_nickname_text.setText("안녕하세요!\n"+userMyInfo.getName()+" 님");
                    userModify_id_text.setText(userMyInfo.getConsumerId());
                    userModify_email_text.setText(userMyInfo.getEmail());
                    user_name=userMyInfo.getName();
                    userModify_name_text.setText(user_name);
                    modify_year=userMyInfo.getBirthYear();
                    modify_month=userMyInfo.getBirthMonth();
                    modify_date=userMyInfo.getBirthDay();
                    gender=userMyInfo.getGender();
                    modify_datepicker.setText(modify_year+"-"+modify_month+"-"+modify_date);
                    if(gender.matches("F")){
                        userModify_female.setTextColor(Color.parseColor("#000000"));
                    }else if(gender.matches("M")){
                        userModify_male.setTextColor(Color.parseColor("#000000"));
                    }
                    termsAdvertisement=userMyInfo.getTermsAdvertisement();
                    if(termsAdvertisement.matches("Y")){
                        userModify_ad_check.setChecked(true);
                    }else if(termsAdvertisement.matches("N")){
                        userModify_ad_check.setChecked(false);
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

    /*각 생년 월일 입력받음*/
    private void showDatePicker() {
        DatePickerFragment date = new DatePickerFragment();
        /**
         * Set Up Current Date Into dialog
         */
        Calendar calender = Calendar.getInstance();
        Bundle args = new Bundle();
        args.putInt("year", calender.get(Calendar.YEAR));
        args.putInt("month", calender.get(Calendar.MONTH));
        args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
        date.setArguments(args);
        /**
         * Set Call back to capture selected date
         */
        date.setCallBack(ondate);
        date.show(getFragmentManager(), "Date Picker");
    }

    /*입력 받은것 출력*/
    DatePickerDialog.OnDateSetListener ondate = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            TextView textView_modify_datepicker = (TextView) getView().findViewById(R.id.textView_modify_datepicker);
            modify_year=year;
            modify_month=monthOfYear+1;
            modify_date=dayOfMonth;
            textView_modify_datepicker.setText(modify_year + "-" + modify_month + "-" + modify_date);
        }
    };


    /* Acitivity와 Fragment가 통신할 때, OnFragmentInteractionListener를 사용함.
     * 프래그먼트에서 액티비티로 통신(데이터 주고 받는 것)이 있을 수도 있기 때문에
     * MainActivity 에서 이를 implement한 후 오버라이딩 시켜줄 것이다. (임시로)
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //인터페이스 - 추상 메소드(구현부가 없는 메시드)의 모임
    /* retrofit은 인터페이스에 기술된 명세를 Http API(호출 가능한 객체)로 전환해줌
    => 우리가 요청할 API들에 대한 명세만을 Interface에 기술해두면 됨.
     */
    /* 사용자 정보를 변경하는 인터페이스*/
    public interface ModifyUserInfoService {
        @PATCH("api/consumers")
        retrofit2.Call<String> ModifyUserInfo(@Header("Authorization") String authorization,
                                      @Header("Accept") String accept,
                                      @Header("ShopContent-Type") String content_type,
                                      @Body UserModifyInfo userModifyInfo);
    }
}
