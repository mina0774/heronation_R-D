package com.example.heronation.measurement;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.heronation.R;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.measurement.dataClass.SubCategoryResponse;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MeasurementArFragment extends Fragment {
    @BindView(R.id.ar_back_btn) ImageButton ar_back_btn; //뒤로 가기 버튼
    @BindView(R.id.ar_add_cloth_btn) ImageButton ar_add_cloth_btn; // 사진 등록 버튼
    @BindView(R.id.ar_cloth_name_et) EditText ar_cloth_name_et; // 등록 아이템의 이름 설정
    @BindView(R.id.ar_start_measure_image_body) ImageView ar_start_measure_image_body;  // 카테고리를 선택했을 때 밑에 나타나는 옷 카테고리 이미지
    @BindView(R.id.ar_spinner_select_category) Spinner ar_spinner_select_category; //카테고리 선택 spinner
    private ArrayAdapter<String> spinner_adapter; // 스피너 어댑터
    List<String> cloth_category_list; //옷 카테고리를 담는 변수
//    public ArrayList<String> Measure_item, Image_item, measureItemId, min_scope, max_scope; //옷 카테고리에 따른 측정 목록을 담는 변수들
    MeasurementFragment measurementFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_measurement_ar,container,false);
        ButterKnife.bind(this,rootView);
        measurementFragment=new MeasurementFragment();
        cloth_category_list=new ArrayList<>();
        /* 측정할 옷 종류 카테고리 받아오는 함수 - 스피너 설정, 이미지뷰 설정 */
        getClothCategory();

        /* 뒤로가기 버튼을 눌렀을 때*/
        ar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, measurementFragment).commit();
            }
        });
        return rootView;
    }

    /* 측정할 옷 종류 카테고리 받아오는 함수 */
    public void getClothCategory(){
        String authorization = "zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        APIInterface.GetClothCategoryService getClothCategoryService= ServiceGenerator.createService(APIInterface.GetClothCategoryService.class);
        retrofit2.Call<List<SubCategoryResponse>> request=getClothCategoryService.GetCategory(authorization);
        request.enqueue(new Callback<List<SubCategoryResponse>>() {
            @Override
            public void onResponse(Call<List<SubCategoryResponse>> call, Response<List<SubCategoryResponse>> response) {
                List<SubCategoryResponse> subCategoryResponses = response.body();
                HashMap<String, String> category = new HashMap<>();
                for (int i = 0; i < subCategoryResponses.size(); i++) {
                    cloth_category_list.add(subCategoryResponses.get(i).getName()); //해당 리스트는 옷 카테고리 리스트를 볼 수 있는 spinner의 아이템이 된다.
                    category.put(subCategoryResponses.get(i).getName(), subCategoryResponses.get(i).getId()); //뽑아낸 이름과 ID를 해쉬맵에 넣는다.
                }
                /* 옷 카테고리 리스트를 선택할 수 있는 스피너 어댑터 설정 */
                spinner_adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, cloth_category_list);
                spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ar_spinner_select_category.setAdapter(spinner_adapter);
                /*스피너에 옷 카테고리 아이템 추가 */
                ar_spinner_select_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        int pos = ar_spinner_select_category.getSelectedItemPosition();
                        String temp_id = subCategoryResponses.get(pos).getId();
                        setClothCategoryImageView(temp_id); //스피너에 선택된 옷 카테고리에 따라 이미지 뷰 설정
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }
            @Override
            public void onFailure(Call<List<SubCategoryResponse>> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    public void setClothCategoryImageView(String temp_id){
        if(temp_id.equals("2"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_tshirt);
        if(temp_id.equals("3"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_cardigan);
        if(temp_id.equals("4"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_coat);
        if(temp_id.equals("5"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_jacket);
        if(temp_id.equals("6"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_shirt);
        if(temp_id.equals("7"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_blouse);
        if(temp_id.equals("8"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_padding);
        if(temp_id.equals("9"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_vest);
        if(temp_id.equals("10"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_hood);
        if(temp_id.equals("11"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_sleeveless);
        if(temp_id.equals("12"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_onepiece);
        if(temp_id.equals("13"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_pants);
        if(temp_id.equals("14"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_short_pants);
        if(temp_id.equals("15"))
            ar_start_measure_image_body.setBackgroundResource(R.drawable.img_skirt);

    }
}
