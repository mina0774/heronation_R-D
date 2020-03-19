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
    MeasurementArFragment measurementArFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView=(ViewGroup)inflater.inflate(R.layout.fragment_measurement,container,false);
        ButterKnife.bind(this,rootView);
        measurementArFragment=new MeasurementArFragment();

        /* AR 측정 버튼 클릭시 측정 화면으로 이동*/
        measurement_AR_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().beginTransaction().replace(R.id.fragment_container, measurementArFragment).commit();
            }
        });

        return rootView;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
