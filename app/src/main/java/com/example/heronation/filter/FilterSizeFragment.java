package com.example.heronation.filter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.heronation.R;
import com.innovattic.rangeseekbar.RangeSeekBar;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FilterSizeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FilterSizeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FilterSizeFragment extends Fragment {
    private TextView text_view1;
    private TextView text_view2;
    private TextView text_view3;
    private TextView text_view4;
    private TextView text_view5;
    private Button reset_button;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FilterSizeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FilterSizeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FilterSizeFragment newInstance(String param1, String param2) {
        FilterSizeFragment fragment = new FilterSizeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView=inflater.inflate(R.layout.fragment_filter_size, container, false);

        /* seekbar의 기본값 설정 작업 */
        final RangeSeekBar seekBar = (RangeSeekBar) rootView.findViewById(R.id.rangeSeekBar);
        seekBar.setSidePadding(12);
        seekBar.setMinThumbValue(0);
        seekBar.setMaxThumbValue(100);
        seekBar.setTrackSelectedColor(Color.parseColor("#000000"));
        RangeSeekBar seekBar2 = (RangeSeekBar) rootView.findViewById(R.id.rangeSeekBar2);
        seekBar2.setSidePadding(12);
        seekBar2.setMinThumbValue(0);
        seekBar2.setMaxThumbValue(100);
        seekBar2.setTrackSelectedColor(Color.parseColor("#000000"));
        RangeSeekBar seekBar3 = (RangeSeekBar) rootView.findViewById(R.id.rangeSeekBar3);
        seekBar3.setSidePadding(12);
        seekBar3.setMinThumbValue(0);
        seekBar3.setMaxThumbValue(100);
        seekBar3.setTrackSelectedColor(Color.parseColor("#000000"));
        RangeSeekBar seekBar4 = (RangeSeekBar) rootView.findViewById(R.id.rangeSeekBar4);
        seekBar4.setSidePadding(12);
        seekBar4.setMinThumbValue(0);
        seekBar4.setMaxThumbValue(100);
        seekBar4.setTrackSelectedColor(Color.parseColor("#000000"));
        RangeSeekBar seekBar5 = (RangeSeekBar) rootView.findViewById(R.id.rangeSeekBar5);
        seekBar5.setSidePadding(12);
        seekBar5.setMinThumbValue(0);
        seekBar5.setMaxThumbValue(100);
        seekBar5.setTrackSelectedColor(Color.parseColor("#000000"));

        /* seekbar의 범위가 변화할때 텍스트뷰에 값 설정 */
        text_view1=rootView.findViewById(R.id.shoulder_length);
        seekBar.setSeekBarChangeListener(new RangeSeekBar.SeekBarChangeListener() {
            @Override
            public void onStartedSeeking() {

            }

            @Override
            public void onStoppedSeeking() {

            }

            @Override
            public void onValueChanged(int i, int i1) {
                text_view1.setText(i+"~"+i1+"cm");
            }
        });

        text_view2=rootView.findViewById(R.id.chest_length);
        seekBar2.setSeekBarChangeListener(new RangeSeekBar.SeekBarChangeListener() {
            @Override
            public void onStartedSeeking() {

            }

            @Override
            public void onStoppedSeeking() {

            }

            @Override
            public void onValueChanged(int i, int i1) {
                text_view2.setText(i+"~"+i1+"cm");
            }
        });

        text_view3=rootView.findViewById(R.id.waist_length);
        seekBar3.setSeekBarChangeListener(new RangeSeekBar.SeekBarChangeListener() {
            @Override
            public void onStartedSeeking() {

            }

            @Override
            public void onStoppedSeeking() {

            }

            @Override
            public void onValueChanged(int i, int i1) {
                text_view3.setText(i+"~"+i1+"cm");
            }
        });

        text_view4=rootView.findViewById(R.id.hip_length);
        seekBar4.setSeekBarChangeListener(new RangeSeekBar.SeekBarChangeListener() {
            @Override
            public void onStartedSeeking() {

            }

            @Override
            public void onStoppedSeeking() {

            }

            @Override
            public void onValueChanged(int i, int i1) {
                text_view4.setText(i+"~"+i1+"cm");
            }
        });

        text_view5=rootView.findViewById(R.id.thigh_length);
        seekBar5.setSeekBarChangeListener(new RangeSeekBar.SeekBarChangeListener() {
            @Override
            public void onStartedSeeking() {

            }

            @Override
            public void onStoppedSeeking() {

            }

            @Override
            public void onValueChanged(int i, int i1) {
                text_view5.setText(i+"~"+i1+"cm");
            }
        });

        /* 초기화 버튼을 눌렀을 때*/
        reset_button=rootView.findViewById(R.id.item_filter_return);
        reset_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*원래대로 되돌리기 작업*/
            }
        });
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
