package com.example.heronation.home;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.example.heronation.filter.DialogWindowFilterFragment;
import com.example.heronation.filter.FilterCategoryFragment;
import com.example.heronation.filter.FilterPriceFragment;
import com.example.heronation.filter.FilterSizeFragment;
import com.example.heronation.R;
import com.example.heronation.filter.FilterColorFragment;

public class ItemSearchActivity extends AppCompatActivity
        implements DialogWindowFilterFragment.OnFragmentInteractionListener,
        FilterCategoryFragment.OnFragmentInteractionListener,
        FilterColorFragment.OnFragmentInteractionListener,
        FilterPriceFragment.OnFragmentInteractionListener,
        FilterSizeFragment.OnFragmentInteractionListener {
    private DialogWindowFilterFragment dialogWindowFilterFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_search);

    }

    public void click_back_button(View view){
        finish();
    }

    public void item_search_filter_button(View view){
        /* 필터 PopUp창 띄우기 */
        dialogWindowFilterFragment=new DialogWindowFilterFragment();
        dialogWindowFilterFragment.show(getSupportFragmentManager(), "");
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
