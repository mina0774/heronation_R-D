package com.example.heronation.home.itemRecyclerViewAdapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ItemContent;
import com.example.heronation.R;
import com.example.heronation.home.topbarFragment.ItemHomeFragment;
import com.example.heronation.main.MainActivity;
import com.example.heronation.shop.topbarFragment.ShopRankingFragment;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* 수평 리사이클러뷰를 위한 어댑터 */
public class ItemHorizontalAdapter extends RecyclerView.Adapter<ItemHorizontalAdapter.HorizontalViewHolder> {
    private ArrayList<ItemContent> itemList;
    /* glide를 통해 URL을 통해서 이미지를 받아올 때,
     * 현재 어떤 액태비티의 Context인지 알아야하므로, 이를 받아오기 위함
     * 생성자에서 받아줌*/
    private Context context;

    public ItemHorizontalAdapter(ArrayList<ItemContent> itemList, Context context){
        this.itemList=itemList;
        this.context=context;
    }

    /* viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성*/
    @NonNull
    @Override
    public ItemHorizontalAdapter.HorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* 아이템 하나를 나타내는 xml파일을 뷰에 바인딩 */
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.shopitem_list_item,parent,false);
        /* 뷰홀더 객체 생성 */
        HorizontalViewHolder holder=new HorizontalViewHolder(view);
        return holder;
    }

    /* position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 */
    @Override
    public void onBindViewHolder(@NonNull final HorizontalViewHolder holder, int position) {
        int item_position=position;
        /* Glide를 통해 URL로 받아온 이미지를 로드해서 뷰홀더에 있는 이미지뷰에 뿌려줌 */
        Glide.with(context).load(itemList.get(item_position).getShopImage()).error(R.drawable.shop_item_example_img_2).crossFade().into(holder.item_image);
        holder.item_name.setText(itemList.get(item_position).getName());
      //  holder.shop_name.setText(itemList.get(item_position).getShopName());
      //    holder.originalPrice.setText(itemList.get(item_position).getOriginalPrice().toString());
      //  holder.salePrice.setText(itemList.get(item_position).getSalePrice().toString());


        holder.heart_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!holder.isSongLikedClicked){
                    // 애니메이션을 한번 실행시킨다.
                    // Custom animation speed or duration.
                    // ofFloat(시작 시간, 종료 시간).setDuration(지속시간)
                    ValueAnimator animator = ValueAnimator.ofFloat(0f, 0.5f).setDuration(1000);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            holder.heart_button.setProgress((Float) animation.getAnimatedValue());
                        }
                    });
                    animator.start();
                    holder.isSongLikedClicked = true;
                    RegisterItem(itemList.get(holder.getAdapterPosition()).getId());
                }
                else {
                    // 애니메이션을 한번 실행시킨다.
                    // Custom animation speed or duration.
                    // ofFloat(시작 시간, 종료 시간).setDuration(지속시간)
                    ValueAnimator animator = ValueAnimator.ofFloat(0.5f, 1f).setDuration(1000);
                    animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            holder.heart_button.setProgress((Float) animation.getAnimatedValue());
                        }
                    });
                    animator.start();
                    holder.isSongLikedClicked = false;
                    DeleteItem(itemList.get(holder.getAdapterPosition()).getId());
                }

            }
        });
    }

    //아이템 찜 등록하는 기능, Zeyo API 연동
    public void RegisterItem(Integer item_id) {
        String authorization = "Bearer "+MainActivity.access_token;
        String accept = "application/json";
        String content_type = "application/json";

        ItemHomeFragment.ItemRegisterService itemRegisterService = ServiceGenerator.createService(ItemHomeFragment.ItemRegisterService.class);
        retrofit2.Call<String> request = itemRegisterService.ItemRegister(item_id,authorization,accept,content_type);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String>  call, Response<String>  response) {
                System.out.println("Response" + response.code());
                if(response.code()==204){
                    //등록 완료
                    backgroundThreadShortToast(context,"아이템 찜 등록이 완료되었습니다.");
                }else if(response.code()==401){
                    //로그인이 필요한 서비스입니다.
                    backgroundThreadShortToast(context,"로그인이 필요한 서비스입니다.");
                }else if(response.code()==500){
                    //이미 찜목록에 등록되어있습니다.
                    backgroundThreadShortToast(context,"이미 찜 목록에 등록된 아이템입니다.");
                }

            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                System.out.println("error + Connect Server Error is " + t.toString());
            }
        });
    }

    //아이템 찜 목록에서 삭제하는 기능
    public void DeleteItem(Integer item_id){
        String authorization = "Bearer "+ MainActivity.access_token;
        String accept = "application/json";
        String content_type = "application/json";

        ItemHomeFragment.ItemDeleteService shopRegisterService = ServiceGenerator.createService(ItemHomeFragment.ItemDeleteService.class);
        retrofit2.Call<String>  request = shopRegisterService.ItemDelete(item_id,authorization,accept,content_type);
        request.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String>  call, Response<String> response) {
                System.out.println("Response" + response.code());
                if(response.code()==204){
                    //등록 완료
                    backgroundThreadShortToast(context,"아이템 찜 목록에서 삭제되었습니다.");
                }else if(response.code()==401){
                    //로그인이 필요한 서비스입니다.
                    backgroundThreadShortToast(context,"로그인이 필요한 서비스입니다.");
                }
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
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

    /* 전체 아이템 개수를 return */
    @Override
    public int getItemCount() {
        return itemList.size();
    }

    /* 뷰홀더 데이터가 놓일 공간을 마련해준다. */
    public class HorizontalViewHolder extends RecyclerView.ViewHolder{
        private ImageView item_image;
        private TextView item_name;
//        private TextView shop_name;
 //       private TextView originalPrice;
 //       private TextView salePrice;
        private LottieAnimationView heart_button;
        // 좋아요 클릭 여부
        private boolean isSongLikedClicked = false;

        public HorizontalViewHolder(View view){
            super(view);
            item_image=view.findViewById(R.id.recycler_view_item_best_item_image);
            item_name=view.findViewById(R.id.recycler_view_item_best_item_name);
    //        shop_name=view.findViewById(R.id.recycler_view_item_best_shop_name);
    //        originalPrice=view.findViewById(R.id.recycler_view_item_best_original_price);
    //        salePrice=view.findViewById(R.id.recycler_view_item_best_sale_price);
            heart_button=view.findViewById(R.id.heart_button);
        }

    }
}
