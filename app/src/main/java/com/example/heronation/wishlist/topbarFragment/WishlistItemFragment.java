package com.example.heronation.wishlist.topbarFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heronation.login_register.IntroActivity;
import com.example.heronation.login_register.loginPageActivity;
import com.example.heronation.main.MainActivity;
import com.example.heronation.R;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.FavoriteItemAdapter;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass.FavoriteItem;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Header;


public class WishlistItemFragment extends Fragment {
    private ArrayList<FavoriteItem> item_list;
    private FavoriteItemAdapter favoriteItemAdapter;

    /* 찜한 아이템이 없을 시*/
    @BindView(R.id.have_not_item) RelativeLayout have_not_item;
    @BindView(R.id.wishlist_item_togo_measurement) Button wishlist_style_recommendation; //MeasurementFragment로 이동하기 위해 필요한 버튼
    @BindView(R.id.recommend_image) ImageView recommend_image;

    /* 찜한 아이템이 있을 시*/
    @BindView(R.id.have_item) LinearLayout have_item;
    @BindView(R.id.wishlist_item_num) TextView wishlist_item_num;
    @BindView(R.id.recycler_view_item_favorites) RecyclerView recycler_view_item_favorites;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_wishlist_item, container,false);
        ButterKnife.bind(this,rootView);
        item_list=new ArrayList<>();

        recycler_view_item_favorites.setLayoutManager(new GridLayoutManager(getActivity(),2, GridLayoutManager.VERTICAL,false));

        GetItemInfo();
        favoriteItemAdapter=new FavoriteItemAdapter(item_list,getActivity()); //Adapter 안에 horizontal adapter를 선언하여 이에 대한 레이아웃을 Grid로 지정
        /* 레이아웃 매니저 수직으로 지정 */
        recycler_view_item_favorites.setAdapter(favoriteItemAdapter);

         /* 찜한 아이템이 없을 시에 MeasurementFragment로 이동*/
        wishlist_style_recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity)getActivity()).go_to_measurement();
            }
        });
        return rootView;
    }

    //아이템 찜 목록 받아오는 기능
    public void GetItemInfo() {
        String authorization = "Bearer " + MainActivity.access_token;
        String content_type = "application/json";

        APIInterface.FavoriteItemInfoService favoriteItemInfoService = ServiceGenerator.createService(APIInterface.FavoriteItemInfoService.class);
        retrofit2.Call<List<FavoriteItem>> request = favoriteItemInfoService.favoriteItemInfo(authorization, content_type);
        request.enqueue(new Callback<List<FavoriteItem>>() {
            @Override
            public void onResponse(Call<List<FavoriteItem>> call, Response<List<FavoriteItem>> response) {
                if(response.isSuccessful()) {
                    List<FavoriteItem> itemFavoritesListInfo = response.body();
                    if (itemFavoritesListInfo.size() != 0) {
                        /* 찜 아이템 없을 시 화면 삭제 */
                        have_not_item.setVisibility(View.GONE);
                        /* 찜 화면 있을시 화면 보이게 하기 */
                        have_item.setVisibility(View.VISIBLE);
                        /*shop 목록을 생성*/
                        make_item_list(itemFavoritesListInfo);
                        wishlist_item_num.setText("찜한 상품 " + itemFavoritesListInfo.size() + "개");
                        favoriteItemAdapter.notifyDataSetChanged();
                    }
                }else if(response.code()==401){
                    backgroundThreadShortToast(getActivity(), "세션이 만료되어 재로그인이 필요합니다."); // 토스트 메시지 ( 메인 쓰레드에서 실행되어야하므로 사용 )
                    Intent intent=new Intent(getActivity(), IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<List<FavoriteItem>> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }


    public void make_item_list(List<FavoriteItem> shopListInfo){
        /* 아이템 목록을 생성함 */
        for(int i = 0; i<shopListInfo.size(); i++){
            item_list.add(shopListInfo.get(i));
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
    /* Acitivity와 Fragment가 통신할 때, OnFragmentInteractionListener를 사용함.
     * 프래그먼트에서 액티비티로 통신(데이터 주고 받는 것)이 있을 수도 있기 때문에
     * MainActivity 에서 이를 implement한 후 오버라이딩 시켜줄 것이다. (임시로)
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
