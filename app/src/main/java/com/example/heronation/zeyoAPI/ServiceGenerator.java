package com.example.heronation.zeyoAPI;


import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static final String URL="http://rest.dev.zeyo.co.kr/"; // 기본 BASE URL
    private static OkHttpClient.Builder httpClient=new OkHttpClient.Builder();

    //baseUrl은 어떤 서버로 네트워크 통신을 요청할 것인지에 대한 설정
    //addConverterFactory는 통신이 완료된 후, 어떤 Converter(Gson-Converter)를 이용하여 데이터를 파싱할 것인지에 대한 설정
    private static Retrofit.Builder builder=
            new Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build());

    //Retrofit 객체를 저장할 Retrofit Type의 변수
    private static Retrofit retrofit=builder.build();


    /* API 명세에 따라 용도에 맞게 Interface를 생성할 수 있기 때문에, Generic 타입으로 Interface Class를 매개변수로 받을 수 있도록 함
       받아온 Interface 타입으로 클라이언트 객체를 리턴해주기 위해 Generic 타입을 그대로 다시 리턴해주는 역할*/
    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
