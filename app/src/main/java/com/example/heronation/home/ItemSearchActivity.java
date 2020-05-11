package com.example.heronation.home;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.heronation.filter.DialogWindowFilterFragment;
import com.example.heronation.filter.FilterCategoryFragment;
import com.example.heronation.filter.FilterPriceFragment;
import com.example.heronation.filter.FilterSizeFragment;
import com.example.heronation.R;
import com.example.heronation.filter.FilterColorFragment;
import com.example.heronation.home.dataClass.Content;
import com.example.heronation.home.dataClass.SearchItemInfo;
import com.example.heronation.home.itemRecyclerViewAdapter.ItemSearchAdapter;
import com.example.heronation.zeyoAPI.APIInterface;
import com.example.heronation.zeyoAPI.ServiceGenerator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemSearchActivity extends AppCompatActivity
        implements DialogWindowFilterFragment.OnFragmentInteractionListener,
        FilterCategoryFragment.OnFragmentInteractionListener,
        FilterColorFragment.OnFragmentInteractionListener,
        FilterPriceFragment.OnFragmentInteractionListener,
        FilterSizeFragment.OnFragmentInteractionListener {
    private DialogWindowFilterFragment dialogWindowFilterFragment;
    private ItemSearchAdapter itemSearchAdapter;
    private List<Content> item_list;
    @BindView(R.id.item_home_search) SearchView item_home_search;
    @BindView(R.id.item_search_recycler_view) RecyclerView item_search_recycler_view;
    @BindView(R.id.no_search_result) TextView no_search_result;
    @BindView(R.id.recently_search_linear_layout) LinearLayout recently_search_linear_layout;
    @BindView(R.id.delete_recently_search) TextView delete_recently_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_search);
        ButterKnife.bind(this);

        //최근 검색어가 있을 때 화면에 뿌려주기
        SharedPreferences pref=getSharedPreferences("searching_keyword",MODE_PRIVATE);
        Collection<?> collection=pref.getAll().values();
        Iterator<?> iterator=collection.iterator();

        while(iterator.hasNext()){
            String keyword=(String)iterator.next();

            LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            TextView tv=new TextView(this);
            tv.setText(keyword);
            tv.setPadding(8,8,8,8);
            tv.setLayoutParams(layoutParams);
            layoutParams.leftMargin=8;
            tv.setBackground(getDrawable(R.drawable.button_background));
            recently_search_linear_layout.addView(tv);
        }

        item_list=new ArrayList<>();
        item_search_recycler_view.setLayoutManager(new GridLayoutManager(getApplicationContext(),2,GridLayoutManager.VERTICAL,false));
        itemSearchAdapter=new ItemSearchAdapter(item_list,getApplicationContext());
        item_search_recycler_view.setAdapter(itemSearchAdapter);

        item_home_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 검색 버튼이 눌러졌을 때 이벤트 처리
                item_list.clear();
                search_item(query);

                // 최근 검색어 모두 저장
                SharedPreferences.Editor editor=pref.edit();
                editor.putString(query,query);
                editor.commit();

                //최근 검색어가 있을 때 화면에 뿌려주기
                recently_search_linear_layout.removeAllViews();
                SharedPreferences pref=getSharedPreferences("searching_keyword",MODE_PRIVATE);
                Collection<?> collection=pref.getAll().values();
                Iterator<?> iterator=collection.iterator();

                while(iterator.hasNext()){
                    String keyword=(String)iterator.next();

                    LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView tv=new TextView(getApplicationContext());
                    tv.setText(keyword);
                    tv.setPadding(8,8,8,8);
                    tv.setLayoutParams(layoutParams);
                    layoutParams.leftMargin=8;
                    tv.setBackground(getDrawable(R.drawable.button_background));
                    recently_search_linear_layout.addView(tv);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 검색어가 변경되었을 때
                return false;
            }
        });

        // 지우기를 클릭했을 때
        delete_recently_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 최근 검색어 모두 삭제
                SharedPreferences.Editor editor=pref.edit();
                editor.clear();
                editor.commit();
                recently_search_linear_layout.removeAllViews();
            }
        });

    }

    public void click_back_button(View view){
        finish();
    }

    public void item_search_filter_button(View view) {
        /* 필터 PopUp창 띄우기 */
        dialogWindowFilterFragment = new DialogWindowFilterFragment();
        dialogWindowFilterFragment.show(getSupportFragmentManager(), "");
    }

    public void search_item(String search_item_name) {
        String authorization = "zeyo-api-key QVntgqTsu6jqt7hQSVpF7ZS8Tw==";
        String accept = "application/json";

        APIInterface.SearchItemService searchItemService = ServiceGenerator.createService(APIInterface.SearchItemService.class);
        retrofit2.Call<SearchItemInfo> request = searchItemService.SearchItem(1,20,"hit,desc",search_item_name, authorization, accept);

        request.enqueue(new Callback<SearchItemInfo>() {
            @Override
            public void onResponse(Call<SearchItemInfo> call, Response<SearchItemInfo> response) {
                SearchItemInfo searchItemInfo=response.body();
                for(int i=0; i<searchItemInfo.getContent().size(); i++){
                    item_list.add(searchItemInfo.getContent().get(i));
                }
                itemSearchAdapter.notifyDataSetChanged();
                if(item_list.size()==0){
                    no_search_result.setVisibility(View.VISIBLE);
                }else{
                    no_search_result.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<SearchItemInfo> call, Throwable t) {

            }
        });
    }

    /*
     * onFragmenInteraciton~ 를 한 이유
     * Acitivity와 Fragment가 통신할 때, OnFragmentInteractionListener를 사용함.
     * 프래그먼트에서 액티비티로 통신(데이터 주고 받는 것)이 있을 수도 있기 때문에
     * MainActivity 에서 이를 implement한 후 오버라이딩 (임시로)
     */
    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
