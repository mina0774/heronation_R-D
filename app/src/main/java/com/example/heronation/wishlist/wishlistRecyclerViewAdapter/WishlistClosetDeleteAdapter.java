package com.example.heronation.wishlist.wishlistRecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heronation.wishlist.wishlistRecyclerViewAdapter.dataClass.ClosetItem;
import com.example.heronation.R;

import java.util.ArrayList;
import java.util.List;

public class WishlistClosetDeleteAdapter extends RecyclerView.Adapter<WishlistClosetDeleteAdapter.Holder>{

    private Context context;
    private List<ClosetItem> item_list=new ArrayList<>();


    public WishlistClosetDeleteAdapter(Context context, List<ClosetItem> item_list) {
        this.context = context;
        this.item_list = item_list;
    }

    /* viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성*/
    @NonNull
    @Override
    public WishlistClosetDeleteAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.wishlist_closet_delete_item,parent,false);
        WishlistClosetDeleteAdapter.Holder holder=new WishlistClosetDeleteAdapter.Holder(view);
        return holder;
    }

    /* position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 */
    @Override
    public void onBindViewHolder(@NonNull final WishlistClosetDeleteAdapter.Holder holder, int position) {
        holder.category.setText(item_list.get(position).getCategory());
        holder.item_name.setText(item_list.get(position).getItem_name());
        holder.date.setText(item_list.get(position).getDate());
        holder.shop_name.setText(item_list.get(position).getShop_name());
        holder.measurement_type.setText(item_list.get(position).getMeasurement_type());

        /* 즐겨찾기 버튼 별 모양을 클릭했을 때,
        선택될 시에 사진을 노란색 별모양으로 설정
        선택되지 않을 시에 사진을 검은색 별모양으로 설정
        노란색 별모양일때 클릭하면 검은색 별모양
        검은색 별모양일때 클릭하면 노란색 별모양
        */


    }

    /* 전체 아이템 개수를 return */
    @Override
    public int getItemCount() {
        return item_list.size();
    }

    /* 뷰홀더 데이터가 놓일 공간을 마련해준다. */
    public class Holder extends RecyclerView.ViewHolder{
        public TextView category;
        public TextView item_name;
        public TextView date;
        public TextView shop_name;
        public TextView measurement_type;
        public ImageButton favorite_button;
        public CheckBox checkBox;


        public Holder(View view){
            super(view);
            category=(TextView)view.findViewById(R.id.wishlist_closet_item_category);
            item_name=(TextView)view.findViewById(R.id.wishlist_closet_item_name);
            date=(TextView)view.findViewById(R.id.wishlist_closet_item_date);
            shop_name=(TextView)view.findViewById(R.id.wishlist_closet_item_shop_name);
            measurement_type=(TextView)view.findViewById(R.id.wishlist_closet_item_measurement_type);
            favorite_button=(ImageButton)view.findViewById(R.id.favorite_button);
            checkBox=(CheckBox)view.findViewById(R.id.checkBox);

        }

    }


}
