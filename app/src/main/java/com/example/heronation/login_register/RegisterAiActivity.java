package com.example.heronation.login_register;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.heronation.R;

public class RegisterAiActivity extends AppCompatActivity {
    /*스타일 버튼 클릭 상태를 체크하기 위한 변수*/
    boolean casual_state = false;
    boolean street_state = false;
    boolean vintage_state = false;
    boolean sexy_state = false;
    boolean modern_state = false;
    boolean feminine_state = false;
    boolean dandy_state = false;
    boolean lovely_state = false;
    boolean unique_state = false;
    boolean classic_state = false;
    boolean sporty_state = false;
    boolean layered_state = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_ai);

        /*스타일 버튼들*/
        final Button casual_btn = (Button) findViewById(R.id.btn_casual);
        final Button street_btn = (Button) findViewById(R.id.btn_street);
        final Button vintage_btn = (Button) findViewById(R.id.btn_vintage);
        final Button sexy_btn = (Button) findViewById(R.id.btn_sexy);
        final Button modern_btn = (Button) findViewById(R.id.btn_modern);
        final Button feminine_btn = (Button) findViewById(R.id.btn_feminine);
        final Button dandy_btn = (Button) findViewById(R.id.btn_dandy);
        final Button lovely_btn = (Button) findViewById(R.id.btn_lovely);
        final Button unique_btn = (Button) findViewById(R.id.btn_unique);
        final Button classic_btn = (Button) findViewById(R.id.btn_classic);
        final Button sporty_btn = (Button) findViewById(R.id.btn_sporty);
        final Button layered_btn = (Button) findViewById(R.id.btn_layered);

        /*아래부턴 각 스타일들의 클릭시 디자인변화 이벤트*/

        casual_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (casual_state == false) {
                    casual_state = true;
                    casual_btn.setBackgroundResource(R.drawable.btn_background_black);
                } else {
                    casual_btn.setBackgroundResource(R.drawable.btn_background);
                    casual_state = false;
                }
            }
        });
        street_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (street_state == false) {
                    street_state = true;
                    street_btn.setBackgroundResource(R.drawable.btn_background_black);
                } else {
                    street_btn.setBackgroundResource(R.drawable.btn_background);
                    street_state = false;
                }
            }
        });
        vintage_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vintage_state == false) {
                    vintage_state = true;
                    vintage_btn.setBackgroundResource(R.drawable.btn_background_black);
                } else {
                    vintage_btn.setBackgroundResource(R.drawable.btn_background);
                    vintage_state = false;
                }
            }
        });

        sexy_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sexy_state == false) {
                    sexy_state = true;
                    sexy_btn.setBackgroundResource(R.drawable.btn_background_black);
                } else {
                    sexy_btn.setBackgroundResource(R.drawable.btn_background);
                    sexy_state = false;
                }
            }
        });
        modern_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (modern_state == false) {
                    modern_state = true;
                    modern_btn.setBackgroundResource(R.drawable.btn_background_black);
                } else {
                    modern_btn.setBackgroundResource(R.drawable.btn_background);
                    modern_state = false;
                }
            }
        });
        feminine_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feminine_state == false) {
                    feminine_state = true;
                    feminine_btn.setBackgroundResource(R.drawable.btn_background_black);
                } else {
                    feminine_btn.setBackgroundResource(R.drawable.btn_background);
                    feminine_state = false;
                }
            }
        });
        dandy_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dandy_state == false) {
                    dandy_state = true;
                    dandy_btn.setBackgroundResource(R.drawable.btn_background_black);
                } else {
                    dandy_btn.setBackgroundResource(R.drawable.btn_background);
                    dandy_state = false;
                }
            }
        });
        lovely_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (lovely_state == false) {
                    lovely_state = true;
                    lovely_btn.setBackgroundResource(R.drawable.btn_background_black);
                } else {
                    lovely_btn.setBackgroundResource(R.drawable.btn_background);
                    lovely_state = false;
                }
            }
        });
        unique_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (unique_state == false) {
                    unique_state = true;
                    unique_btn.setBackgroundResource(R.drawable.btn_background_black);
                } else {
                    unique_btn.setBackgroundResource(R.drawable.btn_background);
                    unique_state = false;
                }
            }
        });
        classic_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (classic_state == false) {
                    classic_state = true;
                    classic_btn.setBackgroundResource(R.drawable.btn_background_black);
                } else {
                    classic_btn.setBackgroundResource(R.drawable.btn_background);
                    classic_state = false;
                }
            }
        });
        sporty_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sporty_state == false) {
                    sporty_state = true;
                    sporty_btn.setBackgroundResource(R.drawable.btn_background_black);
                } else {
                    sporty_btn.setBackgroundResource(R.drawable.btn_background);
                    sporty_state = false;
                }
            }
        });
        layered_btn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (layered_state == false) {
                    layered_state = true;
                    layered_btn.setBackgroundResource(R.drawable.btn_background_black);
                } else {
                    layered_btn.setBackgroundResource(R.drawable.btn_background);
                    layered_state = false;
                }
            }
        });

    }

    /*이전버튼과 완료버튼 이동*/
    public void click_registerai_previous(View view) {
        Intent intent = new Intent(this, RegisterBodyActivity.class);
        startActivity(intent);
    }

    public void click_registerai_next(View view) {
        Intent intent = new Intent(this, IntroActivity.class);
        startActivity(intent);
    }
}
