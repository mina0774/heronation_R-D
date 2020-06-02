package com.example.heronation.home.itemRecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.heronation.R;
import com.example.heronation.home.dataClass.Content;
import com.example.heronation.home.itemRecyclerViewAdapter.dataClass.ShopItemPackage;
import com.example.heronation.home.topbarFragment.ItemHomeFragment;
import com.example.heronation.wishlist.topbarFragment.WishlistClosetFragment;

import java.util.ArrayList;

public class ItemStyleVerticalAdapter extends RecyclerView.Adapter<ItemStyleVerticalAdapter.VerticalViewHolder> {
    private ArrayList<ShopItemPackage> itemList_List;
    private Context context;
    String log="\n";

    public ItemStyleVerticalAdapter(ArrayList<ShopItemPackage> itemList_List, Context context) {
        this.itemList_List = itemList_List;
        this.context = context;
    }

    /* viewType 형태의 아이템 뷰를 위한 뷰홀더 객체 생성*/
    @NonNull
    @Override
    public ItemStyleVerticalAdapter.VerticalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        /* 아이템 하나를 나타내는 xml파일을 뷰에 바인딩 */
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recyclerview_vertical_list_item,parent,false);
        /* 뷰홀더 객체 생성 */
        ItemStyleVerticalAdapter.VerticalViewHolder holder=new ItemStyleVerticalAdapter.VerticalViewHolder(view);
        return holder;
    }

    /* position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시 */
    @Override
    public void onBindViewHolder(@NonNull ItemStyleVerticalAdapter.VerticalViewHolder holder, int position) {
            ItemStyleHorizontalAdapter adapter = new ItemStyleHorizontalAdapter(itemList_List.get(position).getShopItems(), context);
            holder.recyclerView.setHasFixedSize(true);
            holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            holder.recyclerView.setAdapter(adapter);
            holder.packageName.setText(itemList_List.get(position).getPackageName());

        // 시간 측정 로그값 나타내기
        long endTime=System.nanoTime();
        log="elapsed time: "+(double)(endTime- ItemHomeFragment.startTime)/1000000000.0;
        ItemHomeFragment.log_textview.setText(log);
    }

    @Override
    public int getItemCount() {
        return itemList_List.size();
    }

    public class VerticalViewHolder extends RecyclerView.ViewHolder{
        protected RecyclerView recyclerView;
        protected TextView packageName;

        public VerticalViewHolder(View view)
        {
            super(view);
            this.recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewVertical);
            this.packageName=(TextView)view.findViewById(R.id.packed_item_name);
        }
    }
}
