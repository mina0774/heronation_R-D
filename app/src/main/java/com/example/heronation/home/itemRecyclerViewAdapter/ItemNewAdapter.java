package com.example.heronation.home.itemRecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heronation.R;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ShopItemPackage;

import java.util.ArrayList;

public class ItemNewAdapter extends RecyclerView.Adapter<ItemNewAdapter.ViewHolder> {
    private ArrayList<ShopItemPackage> itemList_List;
    private Context context;

    public ItemNewAdapter(ArrayList<ShopItemPackage> itemList_List, Context context) {
        this.itemList_List = itemList_List;
        this.context = context;
    }

    /* viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성*/
    @NonNull
    @Override
    public ItemNewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* 아이템 하나를 나타내는 xml파일을 뷰에 바인딩 */
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_new_list_item,parent,false);
        /* 뷰홀더 객체 생성 */
        ItemNewAdapter.ViewHolder holder=new ItemNewAdapter.ViewHolder(view);
        return holder;
    }

    /* position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 */
    @Override
    public void onBindViewHolder(@NonNull ItemNewAdapter.ViewHolder holder, int position) {

        ItemHorizontalAdapter adapter = new ItemHorizontalAdapter(itemList_List.get(position).getShopItems(), context);
        holder.recyclerView.setLayoutManager(new GridLayoutManager(context,2, GridLayoutManager.VERTICAL,false));
        holder.recyclerView.setAdapter(adapter);
        holder.packageName.setText(itemList_List.get(position).getPackageName());

    }

    @Override
    public int getItemCount() {
        return itemList_List.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected RecyclerView recyclerView;
        protected TextView packageName;

        public ViewHolder(View view)
        {
            super(view);
            this.recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewVertical);
            this.packageName=(TextView)view.findViewById(R.id.packed_item_name);
        }
    }
}
