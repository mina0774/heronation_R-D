package com.example.heronation.measurement.AR.InnerGuide;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.heronation.R;
import com.example.heronation.measurement.AR.MeasurementArFragment;


public class MeasurementThirdGuideFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;

    // newInstance constructor for creating fragment with arguments
    public static MeasurementThirdGuideFragment newInstance(int page, String title) {
        MeasurementThirdGuideFragment fragment = new MeasurementThirdGuideFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragment.setArguments(args);
        return fragment;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_measurement_third_guide, container, false);
        ImageView imageView = view.findViewById(R.id.measurement_guide_result_image);
        Glide.with(getActivity()).load(MeasurementArFragment.file.getAbsolutePath()).into(imageView);
        return view;
    }
}