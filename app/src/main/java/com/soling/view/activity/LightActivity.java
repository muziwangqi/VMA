package com.soling.view.activity;

import android.os.Bundle;
import android.os.health.SystemHealthManager;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SeekBar;

import com.soling.R;
import com.soling.presenter.SetLight;

public class LightActivity extends BaseActivity{
    private Button btnSystemBright;
    private SeekBar sbBright;
    private boolean flag=false;
    private int defaultSeekBarBright=75;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_light);
        btnSystemBright=(Button) findViewById(R.id.btn_systembright);
        sbBright=(SeekBar)findViewById(R.id.sb_bright);
        sbBright.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setBright(defaultSeekBarBright);
                System.out.println("screen bright:"+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    public void getSystemBright (View view){
        if (flag){
            flag=false;
            setBright(defaultSeekBarBright);
        }else{
            flag=true;
            boolean isAutoBright=SetLight.isAutoBright(this);
            if (isAutoBright){
                SetLight.setBright(this,-1);
            }else{
                int brightValue=SetLight.getBright(this);
                SetLight.setBright(this,brightValue);
                System.out.println("System bright:"+brightValue);
            }
        }
    }

    public void setBright(int progress){
        if (progress<1){
            progress=1;
        }else if (progress>255){
            progress=255;
        }
        final WindowManager.LayoutParams layoutParams=getWindow().getAttributes();
        layoutParams.screenBrightness=progress/255f;
        getWindow().setAttributes(layoutParams);
        defaultSeekBarBright=progress;
    }
}
