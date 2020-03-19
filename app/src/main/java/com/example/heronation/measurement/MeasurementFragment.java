package com.example.heronation.measurement;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.heronation.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MeasurementFragment extends Fragment {
    @BindView(R.id.measurement_AR_button) Button measurement_AR_button;
    @BindView(R.id.measurement_style_button) Button measurement_style_button;
    @BindView(R.id.measurement_body_button) Button measurement_body_button;
    MeasurementArFragment measurementArFragment=new MeasurementArFragment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_measurement,container,false);
        ButterKnife.bind(this,rootView);

        /* AR 측정 버튼 클릭시 측정 화면으로 이동*/
        measurement_AR_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, measurementArFragment).commit();
            }
        });

        return rootView;
    }



    /* Acitivity와 Fragment가 통신할 때, OnFragmentInteractionListener를 사용함.
     * 프래그먼트에서 액티비티로 통신(데이터 주고 받는 것)이 있을 수도 있기 때문에
     * MainActivity 에서 이를 implement한 후 오버라이딩 시켜줄 것이다. (임시로)
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
