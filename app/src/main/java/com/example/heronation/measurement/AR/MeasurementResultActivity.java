package com.example.heronation.measurement.AR;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.icu.util.Measure;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.heronation.R;
import com.example.heronation.login_register.loginPageActivity;
import com.example.heronation.main.MainActivity;
import com.example.heronation.measurement.AR.MeasurementARActivity;
import com.example.heronation.measurement.AR.MeasurementArFragment;
import com.example.heronation.measurement.AR.dataClass.MeasureItemResponse;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.graphics.Typeface.BOLD;

public class MeasurementResultActivity extends AppCompatActivity {
    @BindView(R.id.measurement_result_imageview) ImageView measurement_result_imageview;
    @BindView(R.id.measurement_again_button) Button measurement_again_button;
    @BindView(R.id.save_measurement_result_button) Button save_measurement_result_button;
    @BindView(R.id.measurement_result_item) LinearLayout measurement_result_item;
    @BindView(R.id.measurement_result_distance) LinearLayout measurement_result_distance;
    @BindView(R.id.measurement_result_cm) LinearLayout measurement_result_cm;

    public ArrayList<String> MeasureItem;
    public static Double[] measurement_items_distance;
    String temp_file; // 이미지 파일 임시 저장 이름
    ProgressDialog progressDialog; // 저장 시에 띄우는 다이얼로그

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_measurement_result);
        ButterKnife.bind(this);

        MeasureItem= MeasurementArFragment.Measure_item; // 측정 항목을 받아옴
        measurement_items_distance=MeasurementARActivity.measurement_items_distance; // 측정 항목의 거리를 받아옴

        TextView result_measure_item[]=new TextView[MeasureItem.size()];
        TextView result_distance[]=new TextView[MeasureItem.size()];
        TextView result_cm[]=new TextView[MeasureItem.size()];


        // 측정 시작 시에 촬영한 사진 or 갤러리에서 받아온 사진을 띄워줌
        Glide.with(this).load(MeasurementArFragment.file.getAbsolutePath()).into(measurement_result_imageview);

        // 측정 결과값을 예쁘게 출력하기 위한 작업
        for(int i=0;i<MeasureItem.size();i++){
            result_measure_item[i]=new TextView(this);
            result_distance[i]=new TextView(this);
            result_cm[i]=new TextView(this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity= Gravity.CENTER;
            result_measure_item[i].setLayoutParams(layoutParams);
            result_measure_item[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_measure_item[i].setTextSize(16);
            result_measure_item[i].setText(MeasureItem.get(i) + "\n");
            result_measure_item[i].setTextColor(Color.parseColor("#1d1d1d"));
            result_distance[i].setLayoutParams(layoutParams);
            result_distance[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_distance[i].setTextSize(16);
            String distanceString=String.format(Locale.getDefault(),"%d",Math.round(measurement_items_distance[i]*100));
            result_distance[i].setText(distanceString + "\n");
            result_distance[i].setTextAppearance(BOLD);
            result_distance[i].setTextColor(Color.parseColor("#3464ff"));
            result_cm[i].setLayoutParams(layoutParams);
            result_cm[i].setBackgroundColor(Color.parseColor("#FFFFFF"));
            result_cm[i].setTextSize(16);
            result_cm[i].setText("cm\n");
            result_cm[i].setTextColor(Color.parseColor("#777777"));
            measurement_result_item.addView(result_measure_item[i]);
            measurement_result_distance.addView(result_distance[i]);
            measurement_result_cm.addView(result_cm[i]);
        }



        // 재촬영 버튼을 눌렀을 경우, AR로 돌아감
        measurement_again_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplicationContext(), MeasurementARActivity.class);
                startActivity(intent);
                finish();
            }
        });

        // 저장 버튼을 눌렀을 경우 - 로그인 되지 않았을때 토큰이 만료되었을 때 어떻게 할지 생각해보기
        save_measurement_result_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(MeasurementResultActivity.this);
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("옷장 저장 중입니다...");
                progressDialog.show();

                saveImageFile();
            }
        });
    }

    // 사진 파일 저장하는 함수
    public void saveImageFile(){
        File compressedImageFile=null;
        try {
            compressedImageFile=new Compressor(getApplicationContext()).compressToFile(MeasurementArFragment.file);
        } catch (IOException e){
            e.printStackTrace();
        }
        RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),compressedImageFile);
        MultipartBody.Part body=MultipartBody.Part.createFormData("file",MeasurementArFragment.file.getName(),requestBody);

        String authorization="bearer "+MainActivity.access_token;
        String accept="application/json";
        String content_type="multipart/form-data";

        APIInterface.UploadImageFileService uploadImageFileService=ServiceGenerator.createService(APIInterface.UploadImageFileService.class);
        retrofit2.Call<String> request=uploadImageFileService.UploadImageFile(authorization,accept,content_type,body);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    Response<String> temp_file_name=request.execute();
                    temp_file=temp_file_name.body();
                    Log.d("템프",temp_file);
                }catch (IOException e){
                }
                return null;
            }
            //temp/upload에 정상적으로 사진이 추가되면, 그 후에 JsonObject를 생성해 옷장 추가에 필요한 body를 생성
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                SaveMeasurementItem_JSONObejct();
            }
        }.execute();
    }

    // JSONObject를 생성하고, 측정 목록을 저장하는 함수
    public void SaveMeasurementItem_JSONObejct(){
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("id", 0); // ???????? id가 무슨 역할 ???????
            jsonObject.put("name",MeasurementArFragment.clothName);
            jsonObject.put("subCategoryId",MeasurementArFragment.category_select_id);
            jsonObject.put("registerType","D");

            JSONObject fileObject=new JSONObject();
            fileObject.put("temp_name",temp_file);
            fileObject.put("real_name",MeasurementArFragment.file.getName());
            JSONArray attachFile=new JSONArray();
            attachFile.put(fileObject);
            jsonObject.put("attachFile",attachFile);

            JSONObject measurementObject;
            JSONArray wardrobe=new JSONArray();
            for (int i=0; i< MeasureItem.size(); i++){
                measurementObject=new JSONObject();
                measurementObject.put("measureItemId",MeasurementArFragment.measureItemId.get(i));
                measurementObject.put("value",Math.round(measurement_items_distance[i]*100));
                wardrobe.put(measurementObject);
            }
            jsonObject.put("wardrobeScmmValueRequests", wardrobe);
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), jsonObject.toString());

            String authorization="bearer "+MainActivity.access_token;
            String accept="application/json";
            String content_type="application/json";
            APIInterface.UploadMeasurementResultService uploadMeasurementResultService=ServiceGenerator.createService(APIInterface.UploadMeasurementResultService.class);
            retrofit2.Call<JSONObject> request=uploadMeasurementResultService.UploadMeasurementResult(authorization,accept,content_type,requestBody);
            request.enqueue(new Callback<JSONObject>() {
                @Override
                public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {
                   Log.d("저장",response.toString());
                    AlertDialog.Builder builder = new AlertDialog.Builder(MeasurementResultActivity.this);
                    progressDialog.dismiss();
                    builder.setCancelable(false);
                    builder.setMessage("내 옷장에 저장되었습니다.");
                    builder.setNegativeButton("메인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // finish하고 main activity로 돌아갔을 때, main activity에서 어떤 작업을 해줘야하는지를 나타내는 변수
                                    MainActivity.control_closet_to_activity=0;
                                    finish();
                                }
                            });
                    builder.setPositiveButton("옷장으로",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // finish하고 main activity로 돌아갔을 때, main activity에서 어떤 작업을 해줘야하는지를 나타내는 변수
                                    MainActivity.control_closet_to_activity=1;
                                    finish();
                                }
                            });
                    builder.show();
                }

                @Override
                public void onFailure(Call<JSONObject> call, Throwable t) {
                    backgroundThreadShortToast(getApplicationContext(),"로그인 정보가 초기화되었습니다. 다시 로그인 해주세요.");
                }
            });


        }catch (JSONException e){
            e.printStackTrace();
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
            }); } }


}
