package com.example.heronation.wishlist.wishlistRecyclerViewAdapter;

import android.content.Context;
import android.content.SharedPreferences;
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
import com.example.heronation.R;
import com.example.heronation.home.dataClass.RecentlyViewedItem;
import com.example.heronation.wishlist.topbarFragment.WishlistClosetFragment;
import com.example.heronation.wishlist.topbarFragment.WishlistRecentlyViewedItemFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class RecentlyViewedItemAdapter extends RecyclerView.Adapter<RecentlyViewedItemAdapter.ViewHolder> {
    private List<RecentlyViewedItem> itemList=new ArrayList<>();
    /* glide를 통해 URL을 통해서 이미지를 받아올 때,
     * 현재 어떤 액태비티의 Context인지 알아야하므로, 이를 받아오기 위함
     * 생성자에서 받아줌*/
    private Context context;

    public RecentlyViewedItemAdapter(List<RecentlyViewedItem> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
    }

    /* viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성*/
    @NonNull
    @Override
    public RecentlyViewedItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* 아이템 하나를 나타내는 xml파일을 뷰에 바인딩 */
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recently_viewed_item, parent, false);
        /* 뷰홀더 객체 생성 */
        RecentlyViewedItemAdapter.ViewHolder holder = new RecentlyViewedItemAdapter.ViewHolder(view);
        return holder;
    }

    /* position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 */
    @Override
    public void onBindViewHolder(@NonNull final RecentlyViewedItemAdapter.ViewHolder holder, int position) {
        int item_position = position;
        Glide.with(context).load(itemList.get(item_position).getImage_url()).error(R.drawable.shop_item_example_img_2).crossFade().into(holder.item_image);
        holder.item_name.setText(itemList.get(item_position).getItem_name());
        holder.item_price.setText(itemList.get(item_position).getItem_price());
        holder.item_id.setText(itemList.get(item_position).getItem_id());


        //휴지통 버튼을 눌렀을 때, shared preference에서 해당 아이템을 삭제
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SharedPreferences 생성
                SharedPreferences sharedPreferences=context.getSharedPreferences("RecentlyViewedItem",MODE_PRIVATE);
                Gson gson=new GsonBuilder().create();
                LinkedHashMap linkedHashMap=gson.fromJson(sharedPreferences.getString("items",""),LinkedHashMap.class);
                linkedHashMap.remove(itemList.get(item_position).getItem_id());

                String items_info=gson.toJson(linkedHashMap,LinkedHashMap.class);
                SharedPreferences.Editor editor=sharedPreferences.edit();

                editor.putString("items",items_info);

                // 실시간으로 잘 지워졌음을 확인시켜주기 위해 임의로 리사이클러뷰에서 삭제
                WishlistRecentlyViewedItemFragment.item_list.remove(position);
                WishlistRecentlyViewedItemFragment.recentlyViewedItemAdapter.notifyItemRemoved(position);
                WishlistRecentlyViewedItemFragment.recentlyViewedItemAdapter.notifyItemRangeChanged(position,WishlistClosetFragment.item_list.size());

                editor.commit();
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
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView item_image;
        private TextView item_name;
        private TextView item_price;
        private TextView item_id;
        private ImageButton delete_button;

        public ViewHolder(View view) {
            super(view);
            item_image = view.findViewById(R.id.recycler_view_recently_viewed_item_image);
            item_name = view.findViewById(R.id.recycler_view_recently_viewed_item_name);
            item_price=view.findViewById(R.id.recycler_view_recently_viewed_price);
            item_id=view.findViewById(R.id.recycler_view_recently_viewed_item_id);
            delete_button = view.findViewById(R.id.recently_viewed_delete_button);
        }
    }

}
