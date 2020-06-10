package com.example.heronation.wishlist.wishlistRecyclerViewAdapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.heronation.login_register.IntroActivity;
import com.example.heronation.main.MainActivity;
import com.example.heronation.wishlist.topbarFragment.WishlistClosetFragment;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass.ClosetItem;
import com.example.heronation.R;
import com.example.heronation.wishlist.WishlistClosetItemDetailActivity;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.heronation.measurement.AR.MeasurementArInfoActivity.temp_id;

public class WishlistClosetAdapter extends RecyclerView.Adapter<WishlistClosetAdapter.Holder>{

    private Context context;
    private List<ClosetItem> item_list=new ArrayList<>();
    String log="\n";

    public WishlistClosetAdapter(Context context, List<ClosetItem> item_list) {
        this.context = context;
        this.item_list = item_list;
    }

    /* viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성*/
    @NonNull
    @Override
    public WishlistClosetAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_closet_list_item,parent,false);
        WishlistClosetAdapter.Holder holder=new WishlistClosetAdapter.Holder(view);
        return holder;
    }

    /* position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 */
    @Override
    public void onBindViewHolder(@NonNull final WishlistClosetAdapter.Holder holder, int position) {
        // 측정 시작 시에 촬영한 사진 or 갤러리에서 받아온 사진을 띄워줌
        if(item_list.get(position).getCategory_id().toString().equals("2"))
            Glide.with(context).load(R.drawable.img_tshirt).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("3"))
            Glide.with(context).load(R.drawable.img_cardigan).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("4"))
            Glide.with(context).load(R.drawable.img_coat).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("5"))
            Glide.with(context).load(R.drawable.img_jacket).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("6"))
            Glide.with(context).load(R.drawable.img_shirt).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("7"))
            Glide.with(context).load(R.drawable.img_blouse).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("8"))
            Glide.with(context).load(R.drawable.img_padding).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("9"))
            Glide.with(context).load(R.drawable.img_vest).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("10"))
            Glide.with(context).load(R.drawable.img_hood).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("11"))
            Glide.with(context).load(R.drawable.img_sleeveless).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("12"))
            Glide.with(context).load(R.drawable.img_onepiece).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("13"))
            Glide.with(context).load(R.drawable.img_pants).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("14"))
            Glide.with(context).load(R.drawable.img_short_pants).into(holder.image);
        if(item_list.get(position).getCategory_id().toString().equals("15"))
            Glide.with(context).load(R.drawable.img_skirt).into(holder.image);

        holder.category.setText(item_list.get(position).getCategory());
        holder.item_name.setText(item_list.get(position).getItem_name());
        holder.date.setText(item_list.get(position).getDate());
        holder.shop_name.setText(item_list.get(position).getShop_name());
        holder.measurement_type.setText(item_list.get(position).getMeasurement_type());
        holder.id.setText(item_list.get(position).getId());

        // 시간 측정 로그값 나타내기
       long endTime=System.nanoTime();
       log="elapsed time: "+(double)(endTime-WishlistClosetFragment.startTime)/1000000000.0;
        WishlistClosetFragment.log_textview.setText(log);

        /* 휴지통 버튼을 클릭했을 때, 아이템 삭제 */
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 아이템의 휴지통 버튼을 클릭하면, 다이얼로그가 뜸
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("해당 옷을 삭제하시겠습니까?");
                builder.setMessage("삭제한 옷은 다시 복구할 수 없습니다.");
                builder.setPositiveButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("삭제하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String authorization="bearer "+ MainActivity.access_token;
                        String accept="application/json";
                        String content_type="application/json";
                        APIInterface.DeleteClosetItemService deleteClosetItemService= ServiceGenerator.createService(APIInterface.DeleteClosetItemService.class);
                        retrofit2.Call<String> request=deleteClosetItemService.DeleteClosetItem(Integer.parseInt(item_list.get(position).getId()),authorization,accept,content_type);
                        request.enqueue(new Callback<String>() {
                            @Override
                            public void onResponse(Call<String> call, Response<String> response) {
                                System.out.println(response.code());
                                if(response.code()==204){
                                    // 실시간으로 잘 지워졌음을 확인시켜주기 위해 임의로 리사이클러뷰에서 삭제
                                    WishlistClosetFragment.item_list.remove(position);
                                    WishlistClosetFragment.wishlistClosetAdapter.notifyItemRemoved(position);
                                    WishlistClosetFragment.wishlistClosetAdapter.notifyItemRangeChanged(position,WishlistClosetFragment.item_list.size());

                                    backgroundThreadShortToast(context,"옷 삭제가 완료되었습니다.");
                                }else if(response.code()==401){
                                    backgroundThreadShortToast(context, "세션이 만료되어 재로그인이 필요합니다.");
                                    Intent intent=new Intent(context, IntroActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    context.startActivity(intent);
                                }
                            }
                            @Override
                            public void onFailure(Call<String> call, Throwable t) {
                            }
                        });
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
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
        return item_list.size();
    }

    /* 뷰홀더 데이터가 놓일 공간을 마련해준다. */
    public class Holder extends RecyclerView.ViewHolder{
        public ImageView image;
        public TextView category;
        public TextView item_name;
        public TextView date;
        public TextView shop_name;
        public TextView measurement_type;
        public ImageButton delete_button;
        public TextView id;

        public Holder(View view){
            super(view);
            image=(ImageView)view.findViewById(R.id.wishlist_closet_item);
            category=(TextView)view.findViewById(R.id.wishlist_closet_item_category);
            item_name=(TextView)view.findViewById(R.id.wishlist_closet_item_name);
            date=(TextView)view.findViewById(R.id.wishlist_closet_item_date);
            shop_name=(TextView)view.findViewById(R.id.wishlist_closet_item_shop_name);
            measurement_type=(TextView)view.findViewById(R.id.wishlist_closet_item_measurement_type);
            delete_button=(ImageButton)view.findViewById(R.id.delete_button);
            id=(TextView)view.findViewById(R.id.wishlist_closet_item_measurement_id);

            //특정 아이템이 클릭되었을 때 아이템에 대한 데이터 정보를 아이템 수정 페이지로 이동시켜줌
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION){
                        Intent intent=new Intent(context, WishlistClosetItemDetailActivity.class);
                        intent.putExtra("image",item_list.get(position).getImage_url());
                        intent.putExtra("category",item_list.get(position).getCategory());
                        intent.putExtra("item_name",item_list.get(position).getItem_name());
                        intent.putExtra("date",item_list.get(position).getDate());
                        intent.putExtra("shop_name",item_list.get(position).getShop_name());
                        intent.putExtra("measurement_type",item_list.get(position).getMeasurement_type());
                        intent.putExtra("id",item_list.get(position).getId());
                        intent.putExtra("category_id",item_list.get(position).getCategory_id().toString());
                        context.startActivity(intent);
                    }
                }
            });

        }

    }


}
