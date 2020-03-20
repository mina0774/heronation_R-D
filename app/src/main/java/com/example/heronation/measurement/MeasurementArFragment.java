package com.example.heronation.measurement;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
import android.widget.Toast;

import com.example.heronation.R;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.main.MainActivity;
import com.example.heronation.measurement.dataClass.MeasureItemResponse;
import com.example.heronation.measurement.dataClass.SubCategoryResponse;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
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
    @BindView(R.id.ar_start_measure) Button ar_start_measure; // 측정 시작 버튼
    @BindView(R.id.ar_cloth_name_et) EditText ar_cloth_name_et; // 등록 아이템의 이름 설정
    @BindView(R.id.ar_start_measure_image_body) ImageView ar_start_measure_image_body;  // 카테고리를 선택했을 때 밑에 나타나는 옷 카테고리 이미지
    @BindView(R.id.ar_spinner_select_category) Spinner ar_spinner_select_category; //카테고리 선택 spinner

    private ArrayAdapter<String> spinner_adapter; // 스피너 어댑터
    private List<String> cloth_category_list; //옷 카테고리를 담는 변수
    public String category_select_id; //선택된 옷의 특정 카테고리의 ID, 이 아이디를 통해 측정 목록에 접근하여 담을 수 있음.
    public ArrayList<String> Measure_item, Image_item, measureItemId, min_scope, max_scope; //옷 카테고리에 따른 측정 목록을 담는 변수들
    public String clothName; // 옷 이름 저장하는 변수

    //카메라, 갤러리 접근 관련
    private Boolean isPermission = true; // 카메라 접근 권한 허용 여부를 나태내는 변수
    private static final int PICK_FROM_ALBUM = 1;
    private static final int PICK_FROM_CAMERA = 2;
    private File tempFile;
    public File file; // 사진 파일 저장하는 변수
    public String cameraFilePath;
    public String Path;

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

        /* 사진 등록 위한 카메라 접근 권한 */
        cameraPermission();

        /* 뒤로가기 버튼을 눌렀을 때 */
        ar_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, measurementFragment).commit();
            }
        });

        /* 측정 시작 버튼을 눌렀을 때 */
        ar_start_measure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ar_cloth_name_et.getText().toString().length() == 0){ //이름이 비었는지 확인
                    Toast.makeText(getActivity(),"이름을 입력해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                else if(file == null){ //사진이 비었는지 확인
                    Toast.makeText(getActivity(),"사진을 등록해주세요.",Toast.LENGTH_SHORT).show();
                    return;
                }
                clothName=ar_cloth_name_et.getText().toString();
                Intent intent = new Intent(getActivity(), MeasurementARActivity.class);
                startActivity(intent);
            }
        });

        /* 옷 사진 등록 버튼을 눌렀을 때 - 사진을 받아올 경로를 선택하도록 알림창을 띄워줌 */
        ar_add_cloth_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title_text="선택";
                final CharSequence[] image_path_pick = {"앨범에서 선택", "카메라 촬영"};
                AlertDialog.Builder dialogBuilder= new AlertDialog.Builder(getActivity());
                ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#353535"));
                SpannableStringBuilder spannableStringBuilder=new SpannableStringBuilder(title_text);
                spannableStringBuilder.setSpan(foregroundColorSpan,0,title_text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                dialogBuilder.setTitle(spannableStringBuilder);
                /* 다이얼로그에 앨범 혹은 카메라에서 사진을 받아올 지, 선택되었을 때 일어나는 이벤트 */
                dialogBuilder.setItems(image_path_pick, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){ // 앨범에서 선택을 누른 경우
                            if(isPermission) gotoAlbum();
                            else Toast.makeText(getActivity(), "사진 및 파일을 저장하기 위해선 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                        }
                        else if(i==1){ // 카메라 촬영을 누른 경우
                            if(isPermission) takePhoto();
                            else Toast.makeText(getActivity(), "사진 및 파일을 저장하기 위해선 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                AlertDialog alertDialog=dialogBuilder.create();
                alertDialog.show();
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
                        category_select_id=category.get(cloth_category_list.get(pos)); //선택된 카테고리 id 설정
                        /* 선택된 옷 카테고리에 따른 측정 목록을 받아오는 함수 */
                        getMeasurementIndex(category_select_id);
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

    /* 스피너에 선택된 옷 카테고리에 따라 이미지 뷰 설정 */
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

    /* 선택된 옷 카테고리에 따른 측정 목록을 받아오는 함수 */
    public void getMeasurementIndex(String category_select_id){
        String authorization = "zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        APIInterface.GetMeasurementIndexService getMeasurementIndexService= ServiceGenerator.createService(APIInterface.GetMeasurementIndexService.class);
        retrofit2.Call<List<MeasureItemResponse>> request=getMeasurementIndexService.GetMeasurementIndex(category_select_id, authorization);
        request.enqueue(new Callback<List<MeasureItemResponse>>() {
            @Override
            public void onResponse(Call<List<MeasureItemResponse>> call, Response<List<MeasureItemResponse>> response) {
                List<MeasureItemResponse> measureItemResponses = response.body();
                Measure_item = new ArrayList<>();                                                         //측정 항목들의 이름이 들어가는 배열
                Image_item = new ArrayList<>();                                                           //측정 항목들의 이미지들이 들어가는 배열
                measureItemId = new ArrayList<>();                                                        //측정 항목들의 ID가 들어가는 배열
                min_scope = new ArrayList<>();                                                             //측정 항목들의 최소 측정 값이 들어가는 배열
                max_scope = new ArrayList<>();                                                             //측정 항목들의 최대 측정 값이 들어가는 배열
                //위의 배열들은 MeasureActivity에서 count라는 인덱스로 접근해 측정 항목별로 관리한다.
                for(int i=0;i<measureItemResponses.size();i++){
                    Measure_item.add(measureItemResponses.get(i).getItemName());
                    Image_item.add(measureItemResponses.get(i).getItemImage());
                    measureItemId.add(measureItemResponses.get(i).getId());
                    min_scope.add(measureItemResponses.get(i).getItemMinScope());
                    max_scope.add(measureItemResponses.get(i).getItemMaxScope());
                }
            }
            @Override
            public void onFailure(Call<List<MeasureItemResponse>> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    /* 사진 등록 위한 카메라 접근 권한*/
    private void cameraPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                isPermission = true;
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                isPermission = false;
            }
        };
        TedPermission.with(getActivity())
                .setPermissionListener(permissionListener)
                .setRationaleMessage("사진 및 파일을 저장하기 위하여 접근 권한이 필요합니다.")
                .setDeniedMessage("[설정] > [권한] 에서 권한을 허용할 수 있습니다.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();
    }

    /* 앨범에 접근하여 사진을 받아오는 함수 */
    private void gotoAlbum(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent,PICK_FROM_ALBUM);
    }

    /* 카메라를 이용하여 사진을 받아오는 함수 */
    private void takePhoto(){
        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    }

    /* 앨범, 카메라에서 받아온 사진 파일 처리*/
    @Override
    public void startActivityForResult(Intent intent, int requestCode, @Nullable Bundle options) {
        super.startActivityForResult(intent, requestCode, options);
    }
}
