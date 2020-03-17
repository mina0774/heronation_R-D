package com.example.heronation.zeyoAPI;

import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ShopItemInfo;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.StyleRecommendation;
import com.example.heronation.login_register.dataClass.UserLoginInfo;
import com.example.heronation.login_register.dataClass.UserMyInfo;
import com.example.heronation.mypage.dataClass.UserModifyInfo;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface APIInterface {

    //인터페이스 - 추상 메소드(구현부가 없는 메시드)의 모임
    /* retrofit은 인터페이스에 기술된 명세를 Http API(호출 가능한 객체)로 전환해줌
    => 우리가 요청할 API들에 대한 명세만을 Interface에 기술해두면 됨.
     */
    /* 아이템 정보를 서버에서 받아오는 인터페이스*/
    public interface ItemInfoService {
        @GET("api/items/test")
        retrofit2.Call<ShopItemInfo> ItemInfo(@Query("page") Integer page,
                                              @Query("size") Integer size,
                                              @Query("sort") String sort,
                                              @Query("storeId") String storeId,
                                              @Query("storeType") String storeType,
                                              @Header("authorization") String authorization,
                                              @Header("Accept") String accept);
    }

    /* 아이템 찜 추가 */
    public interface ItemRegisterService {
        @POST("api/consumers/items/{item_id}/interest")
        retrofit2.Call<String> ItemRegister(@Path("item_id") Integer item_id,
                                            @Header("authorization") String authorization,
                                            @Header("Accept") String accept,
                                            @Header("Content-Type") String content_type);
    }

    /* 아이템 찜 삭제*/
    public interface ItemDeleteService {
        @DELETE("api/consumers/items/{item_id}/interest")
        retrofit2.Call<String> ItemDelete(@Path("item_id") Integer item_id,
                                          @Header("authorization") String authorization,
                                          @Header("Accept") String accept,
                                          @Header("Content-Type") String content_type);
    }

    /* 로그인 서비스 */
    public interface LoginService{
        @FormUrlEncoded
        @POST("oauth/token")
        retrofit2.Call<UserLoginInfo> LoginInfo(@Header("Accept") String accept,
                                                @Header("ShopContent-Type") String content_type,
                                                @Header("heronation-api-login-key") String heronation_api_login_key,
                                                @Header("heronation-api-uniqId-key") String heronation_api_uniqId_key,
                                                @Header("Authorization") String authorization,
                                                @Field("username") String username,
                                                @Field("password") String password,
                                                @Field("grant_type") String grant_type);
    }

    /* 회원가입 서비스 */
    public interface RegisterService{
        @FormUrlEncoded
        @POST("api/consumers/registry")
        Call<String> postInfo(@Header("Authorization") String authorization,
                              @Header("Accept") String accept,
                              @Header("ShopContent-Type") String content_type,
                              @Field("consumerId") String consumerID, @Field("email") String email,
                              @Field("name") String name, @Field("password") String password,
                              @Field("gender") String gender, @Field("termsAdvertisement") String termsAdvertisement,
                              @Field("birthYear") String birthYear, @Field("birthMonth") String birthMonth, @Field("birthDay") String birthDay);
    }

    /* 사용자 정보를 서버에서 받아오는 인터페이스*/
    public interface UserInfoService {
        @GET("api/consumers/me")
        retrofit2.Call<UserMyInfo> UserInfo(@Header("authorization") String authorization,
                                            @Header("Accept") String accept);
    }

    /* 사용자 정보를 변경하는 인터페이스*/
    public interface ModifyUserInfoService {
        @PATCH("api/consumers")
        retrofit2.Call<String> ModifyUserInfo(@Header("Authorization") String authorization,
                                              @Header("Accept")  String accept,
                                              @Header("ShopContent-Type") String content_type,
                                              @Body UserModifyInfo userModifyInfo);
    }

    /* 사용자 기반 스타일 추천 상품 리스트를 뿌려주는 인터페이스
     * style_tag_id는 사용자 정보를 받아올 때, 사용자 정보에서 Style_Tag 정보를 볼 수 있는 login_register 폴더,
     * 하위 폴더 dataClass에 있는 StyleTagResponse의 id를 받아주면 됨.
     */
    public interface StyleRecommendationBasedUserService {
        @GET("api/items/style-tags/{style_tag_id}/user-base")
    retrofit2.Call<ArrayList<StyleRecommendation>> ShopItemInfo(@Path("style_tag_id") String style_tag_id,
                                                               @Header("authorization") String authorization,
                                                               @Header("Accept") String accept);
}

    /* 타사용자 기반 스타일 추천 상품 리스트를 뿌려주는 인터페이스*/
    public interface StyleRecommendationBasedOtherService {
        @GET("api/items/others-user-base")
        retrofit2.Call<ArrayList<StyleRecommendation>> ShopItemInfo(@Header("authorization") String authorization,
                                                         @Header("Accept") String accept);
    }

    /* 체형 기반 스타일 추천 상품 리스트를 뿌려주는 인터페이스*/
    public interface BodyRecommendationService {
        @GET("api/items/body-type")
        retrofit2.Call<ArrayList<StyleRecommendation>> ShopItemInfo(@Header("authorization") String authorization,
                                                        @Header("Accept") String accept);
    }
}
