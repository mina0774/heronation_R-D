package com.example.heronation.home.itemRecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ItemBestCategory;
import com.example.heronation.R;

import java.util.ArrayList;
import java.util.List;

/* ItemBestFragment에서 상품 카테고리 리사이클러뷰를 위한 어댑터 */
public class ItemBestCategoryAdapter extends RecyclerView.Adapter<ItemBestCategoryAdapter.Holder> {

    private Context context;
    private List<ItemBestCategory> category_list=new ArrayList<>();

    public ItemBestCategoryAdapter(Context context, List<ItemBestCategory> list) {
        this.context=context;
        this.category_list=list;
    }

    /* viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성*/
    @NonNull
    @Override
    public ItemBestCategoryAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_best_category_list_item,parent,false);
        Holder holder=new Holder(view);
        return holder;
    }

    /* position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 */
    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        int item_position=position;
        holder.image_icon.setImageDrawable(category_list.get(item_position).getIcon_drawable());
        holder.category_name.setText(category_list.get(item_position).getItem_name());
    }

    /* 전체 아이템 개수를 return */
    @Override
    public int getItemCount() {
        return category_list.size();
    }

    /* 뷰홀더 데이터가 놓일 공간을 마련해준다. */
    public class Holder extends RecyclerView.ViewHolder{
        public TextView category_name;
        public ImageView image_icon;

        public Holder(View view){
            super(view);
            image_icon=(ImageView)view.findViewById(R.id.item_best_image_icon);
            category_name=(TextView)view.findViewById(R.id.item_best_item_name);
        }
    }
}
