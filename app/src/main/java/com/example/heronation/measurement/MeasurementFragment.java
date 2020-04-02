package com.example.heronation.measurement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.heronation.R;
import com.example.heronation.measurement.AR.MeasurementArInfoActivity;
import com.example.heronation.measurement.Body.MeasurementBodyActivity;
import com.example.heronation.measurement.Style.MeasurementStyleActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeasurementFragment extends Fragment {
    @BindView(R.id.measurement_AR_button) Button measurement_AR_button;
    @BindView(R.id.measurement_style_button) Button measurement_style_button;
    @BindView(R.id.measurement_body_button) Button measurement_body_button;

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
                Intent intent = new Intent(getActivity(), MeasurementArInfoActivity.class);
                startActivity(intent);
            }
        });

        /* 스타일 버튼 클릭시 스타일 선택 화면으로 이동 */
        measurement_style_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeasurementStyleActivity.class);
                startActivity(intent);
            }
        });

        /* 신체 비교 버튼 클릭시 신체 정보 입력 화면으로 이동 */
        measurement_body_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MeasurementBodyActivity.class);
                startActivity(intent);
            }
        });

        return rootView;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
