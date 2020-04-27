package com.example.heronation.home.ItemDetailPage.Wardrobe;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heronation.R;
import com.example.heronation.login_register.IntroActivity;
import com.example.heronation.main.MainActivity;
import com.example.heronation.wishlist.dataClass.ClosetDetailResponse;
import com.example.heronation.wishlist.dataClass.ClosetResponse;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.WishlistClosetAdapter;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass.ClosetItem;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemSelectItemForComparisonAcitivity extends AppCompatActivity {
    /* 리사이클러뷰*/
    @BindView(R.id.closet_recyclerview) RecyclerView closet_recyclerView;
    @BindView(R.id.total_item_number_textview) TextView total_item_number_textview;
    @BindView(R.id.compare_button) Button compare_button;

    String item_id;
    String item_subcategory_id;
    int total_item_number=0;

    public static ArrayList<ClosetItem> item_list;
    public static ItemWardrobeClosetAdapter itemWardrobeClosetAdapter;

    public static ItemSelectItemForComparisonAcitivity itemSelectItemForComparisonAcitivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_select_item_for_comparison_acitivity);
        ButterKnife.bind(this);
        itemSelectItemForComparisonAcitivity=this;

        item_id=getIntent().getStringExtra("item_id");
        item_subcategory_id=getIntent().getStringExtra("item_subcategory");

        GetClosetList(1,item_subcategory_id);

        item_list=new ArrayList<>();
        /* 리사이클러뷰 객체 생성 */
        itemWardrobeClosetAdapter=new ItemWardrobeClosetAdapter(getApplicationContext(),item_list);
        /* 레이아웃 매니저 수평으로 지정 */
        closet_recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL,false));
        /* 리사이클러뷰에 어댑터 지정 */
        closet_recyclerView.setAdapter(itemWardrobeClosetAdapter);

        /* 비교하기 버튼을 눌렀을 때 */
        compare_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ItemWardrobeClosetAdapter.selectItemId!=null) {
                    Intent intent = new Intent(ItemSelectItemForComparisonAcitivity.this, ItemCompareItemSizeActivity.class);
                    intent.putExtra("item_id",item_id);
                    intent.putExtra("select_wardrobe_id", ItemWardrobeClosetAdapter.selectItemId);
                    intent.putExtra("image_url",ItemWardrobeClosetAdapter.imageURL);
                    intent.putExtra("item_name",ItemWardrobeClosetAdapter.itemName);
                    startActivity(intent);
                }else{
                    Toast.makeText(getApplicationContext(),"아이템을 선택해주세요.",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void click_back_button(View view){
        finish();
    }


    public void GetClosetList(Integer page_num, String cloth_category){
        String authorization="bearer "+MainActivity.access_token;
        String accept="application/json";

        APIInterface.GetClosetListService getClosetListService=ServiceGenerator.createService(APIInterface.GetClosetListService.class);
        retrofit2.Call<ClosetResponse> request= getClosetListService.GetClosetList(page_num,100,"id,desc",authorization,accept);
        request.enqueue(new Callback<ClosetResponse>() {
            @Override
            public void onResponse(Call<ClosetResponse> call, Response<ClosetResponse> response) {
                if(response.code()==200){
                    ClosetResponse closetResponse=response.body();
                    for(int i=0; i<closetResponse.getSize();i++){
                        ClosetResponse.WardrobeResponse wardrobeResponse = closetResponse.getWardrobeResponses().get(i);
                       if(cloth_category.equals(wardrobeResponse.getSubCategoryId().toString())){
                            item_list.add(new ClosetItem(wardrobeResponse.getImage(), wardrobeResponse.getSubCategoryName(), wardrobeResponse.getName(),
                                    wardrobeResponse.getCreateDt(), wardrobeResponse.getShopmallName(), "AR", wardrobeResponse.getId().toString()));
                            total_item_number+=1;
                        }
                    }
                    itemWardrobeClosetAdapter.notifyDataSetChanged();
                    total_item_number_textview.setText("옷장에 "+total_item_number+"개 상품이 등록되어있습니다");
                }
                else if(response.code()==401){
                    backgroundThreadShortToast(getApplicationContext(), "세션이 만료되어 재로그인이 필요합니다."); // 토스트 메시지 ( 메인 쓰레드에서 실행되어야하므로 사용 )
                    Intent intent=new Intent(ItemSelectItemForComparisonAcitivity.this, IntroActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<ClosetResponse> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
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


}
