package com.example.heronation.home.ItemDetailPage.Wardrobe;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.heronation.R;
import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass.ClosetItem;
import com.google.android.material.card.MaterialCardView;


import java.util.ArrayList;
import java.util.List;

import butterknife.internal.ListenerClass;


public class ItemWardrobeClosetAdapter extends RecyclerView.Adapter<ItemWardrobeClosetAdapter.Holder>{
    private Context context;
    private List<ClosetItem> item_list=new ArrayList<>();
    private static MaterialCardView lastCardView=null;
    private static int lastCardViewPos=0;
    public static String selectItemId;
    public static String imageURL;
    public static String itemName;

    public ItemWardrobeClosetAdapter(Context context, List<ClosetItem> item_list) {
        this.context = context;
        this.item_list = item_list;
    }

    /* viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성*/
    @NonNull
    @Override
    public ItemWardrobeClosetAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_closet_list_item,parent,false);
        ItemWardrobeClosetAdapter.Holder holder=new ItemWardrobeClosetAdapter.Holder(view);
        return holder;
    }

    /* position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 */
    @Override
    public void onBindViewHolder(@NonNull final ItemWardrobeClosetAdapter.Holder holder, int position) {
        Glide.with(context).load(item_list.get(position).getImage_url()).error(R.drawable.shop_item_example_img_2).crossFade().into(holder.image);
        holder.category.setText(item_list.get(position).getCategory());
        holder.item_name.setText(item_list.get(position).getItem_name());
        holder.date.setText(item_list.get(position).getDate());
        holder.shop_name.setText(item_list.get(position).getShop_name());
        holder.measurement_type.setText(item_list.get(position).getMeasurement_type());
        holder.id.setText(item_list.get(position).getId());

        /* 즐겨찾기 버튼 삭제*/
        holder.favorite_button.setVisibility(View.INVISIBLE);

        /* 휴지통 버튼 삭제 */
        holder.delete_button.setVisibility(View.INVISIBLE);

    }


    /* 전체 아이템 개수를 return */
    @Override
    public int getItemCount() {
        return item_list.size();
    }

    /* 뷰홀더 데이터가 놓일 공간을 마련해준다. */
    public class Holder extends RecyclerView.ViewHolder{
        public MaterialCardView closet_list_cardview;
        public ImageView image;
        public TextView category;
        public TextView item_name;
        public TextView date;
        public TextView shop_name;
        public TextView measurement_type;
        public ImageButton favorite_button;
        public ImageButton delete_button;
        public TextView id;

        public Holder(View view){
            super(view);
            closet_list_cardview=(MaterialCardView)view.findViewById(R.id.closet_list_cardview);
            image=(ImageView)view.findViewById(R.id.wishlist_closet_item);
            category=(TextView)view.findViewById(R.id.wishlist_closet_item_category);
            item_name=(TextView)view.findViewById(R.id.wishlist_closet_item_name);
            date=(TextView)view.findViewById(R.id.wishlist_closet_item_date);
            shop_name=(TextView)view.findViewById(R.id.wishlist_closet_item_shop_name);
            measurement_type=(TextView)view.findViewById(R.id.wishlist_closet_item_measurement_type);
            favorite_button=(ImageButton)view.findViewById(R.id.favorite_button);
            delete_button=(ImageButton)view.findViewById(R.id.delete_button);
            id=(TextView)view.findViewById(R.id.wishlist_closet_item_measurement_id);

            //특정 아이템이 클릭되었을 때 그 아이템을 비교 상품으로 선택
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MaterialCardView materialCardView=(MaterialCardView)v;
                    int clickedPos=getAdapterPosition();

                    if(lastCardView!=null){
                        lastCardView.setStrokeWidth(0);
                    }

                    closet_list_cardview.setStrokeColor(Color.parseColor("#656ead"));
                    closet_list_cardview.setStrokeWidth(5);

                    lastCardView=materialCardView;

                    selectItemId=id.getText().toString();
                    imageURL=item_list.get(clickedPos).getImage_url();
                    itemName=item_list.get(clickedPos).getItem_name();
                }
            });
        }

    }


}
