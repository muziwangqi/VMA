package com.soling.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.soling.R;
import com.soling.presenter.SetLight;

public class LightActivity extends BaseActivity{
    private Button btnLess,btnMore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_light);
        btnLess=(Button)findViewById(R.id.btn_less);
        btnMore=(Button)findViewById(R.id.btn_more);
        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetLight setLight=new SetLight(LightActivity.this);
                setLight.settingMore();
            }
        });

        btnLess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SetLight setLight=new SetLight(LightActivity.this);
                setLight.settingLess();
            }
        });
    }
}
