package com.example.heronation.wishlist.wishlistRecyclerViewAdapter;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
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
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.RecentlyViewedItem;
import java.util.ArrayList;
import java.util.List;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopitem_list_item, parent, false);
        /* 뷰홀더 객체 생성 */
        RecentlyViewedItemAdapter.ViewHolder holder = new RecentlyViewedItemAdapter.ViewHolder(view);
        return holder;
    }

    /* position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 */
    @Override
    public void onBindViewHolder(@NonNull final RecentlyViewedItemAdapter.ViewHolder holder, int position) {
        int item_position = position;
        /* Glide를 통해 URL로 받아온 이미지를 로드해서 뷰홀더에 있는 이미지뷰에 뿌려줌 */
        Glide.with(context).load(itemList.get(item_position).getImage_url()).error(R.drawable.shop_item_example_img_2).crossFade().into(holder.item_image);
        holder.item_name.setText(itemList.get(item_position).getItem_name());
        holder.item_price.setText(itemList.get(item_position).getItem_price());
        holder.item_id.setText(itemList.get(item_position).getItem_id());
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
        // 좋아요 클릭 여부
        private boolean isSongLikedClicked = false;

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
