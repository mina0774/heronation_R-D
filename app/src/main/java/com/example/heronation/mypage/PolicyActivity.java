package com.example.heronation.mypage;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.heronation.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PolicyActivity extends AppCompatActivity {
    @BindView(R.id.back_button) Button back_button;
    @BindView(R.id.textview_privacy) TextView textView;
    @BindView(R.id.button_privacy) Button buttonPrivacy;
    @BindView(R.id.button_policy) Button buttonPolicy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        ButterKnife.bind(this);

        back_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                finish();
            }
        });

        buttonPolicy.setPaintFlags(buttonPrivacy.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);

        TextView textViewSubTitle = (TextView)findViewById(R.id.textview_subtitle);

        buttonPrivacy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                textView.setText(R.string.privacy);
                //buttonPrivacy.setPaintFlags(buttonPrivacy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                buttonPrivacy.setTextColor(Color.parseColor("#1048ff"));
                buttonPrivacy.setPaintFlags(buttonPrivacy.getPaintFlags()|Paint.FAKE_BOLD_TEXT_FLAG);
                buttonPolicy.setTextColor(Color.parseColor("#999999"));
                buttonPolicy.setPaintFlags(0);
                textViewSubTitle.setText("개인정보 취급 방침");
            }
        });

        buttonPolicy.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                textView.setText(R.string.usingpolicy);
                //buttonPolicy.setPaintFlags(buttonPrivacy.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                buttonPrivacy.setTextColor(Color.parseColor("#999999"));
                buttonPolicy.setTextColor(Color.parseColor("#1048ff"));
                buttonPolicy.setPaintFlags(buttonPolicy.getPaintFlags()|Paint.FAKE_BOLD_TEXT_FLAG);

                buttonPrivacy.setPaintFlags(0);
                textViewSubTitle.setText("서비스 이용약관");
            }
        });

    }
}
