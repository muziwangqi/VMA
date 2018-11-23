package com.soling.view.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.graphics.Color;
import android.os.Build;

import android.support.v4.view.ViewPager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import android.widget.RelativeLayout;

import android.widget.TextView;

import com.soling.App;
import com.soling.R;
import com.soling.model.Music;
import com.soling.view.adapter.ScollAdapter;
import com.soling.view.fragment.PhoneFragment;
import com.soling.view.fragment.PlayerFragment;
import com.soling.view.fragment.SettingFragment;
import com.soling.view.fragment.SettingModuleFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "MainActivity";
    private ViewPager viewPager;
    private List<android.support.v4.app.Fragment> fragments = new ArrayList<android.support.v4.app.Fragment>();
    private TextView tvPhone, tvMusic, tvSetting;
    private ImageButton addMenu;
    private PhoneFragment phoneFragment;
    private PlayerFragment playerFragment;
    private SettingFragment settingFragment;
    private SettingModuleFragment settingModuleFragment;
    private android.app.FragmentManager fragmentManager;

    private SharedPreferences sharedPreferences;
    private RelativeLayout rlMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //changeTheme();
        //Log.d(TAG, "onCreate: " + App.getInstance().isTHEMEC());
//        if (App.getInstance().isTHEMEC()) {
//            //Log.d(TAG, "onCreate: ");
//            App.getInstance().setTHEMEC(true);
//            setTheme(R.style.dayTheme);
//        } else {
//            App.getInstance().setTHEMEC(false);
//            setTheme(R.style.nightTheme);
//        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);

        viewPager = (ViewPager) findViewById(R.id.id_vp_scoll);
        tvPhone = (TextView) findViewById(R.id.id_tv_phone);
        tvMusic = (TextView) findViewById(R.id.id_tv_music);
        tvSetting = (TextView) findViewById(R.id.id_tv_setting);
        tvPhone.setOnClickListener(this);
        tvMusic.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
        fragments.add(new PhoneFragment());
        fragments.add(new PlayerFragment());
        fragments.add(new SettingModuleFragment());

        viewPager.setAdapter(new ScollAdapter(getSupportFragmentManager(), fragments));
        viewPager.setCurrentItem(1);
        //System.out.println("setAdapter");


        // 隐藏系统状态栏和actionbar
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        rlMain = findViewById(R.id.rl_main);

        tvPhone.setOnClickListener(this);
        tvMusic.setOnClickListener(this);
        tvSetting.setOnClickListener(this);

        final PhoneFragment phoneFragment = new PhoneFragment();
        PlayerFragment playerFragment = new PlayerFragment();
        // 分享音乐的回调 hyw
        playerFragment.setShareMusicListener(new PlayerFragment.OnShareMusicListener() {
            @Override
            public void shareMusic(Music music) {
                Log.d(TAG, "shareMusic: ");
                viewPager.setCurrentItem(0);
                phoneFragment.shareMusic(music);
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (1 == i) {
                    rlMain.setBackground(getResources().getDrawable(R.drawable.main_music_bg));
                } else {
                    rlMain.setBackground(null);
                    switch (i) {
                        case 0:
                            rlMain.setBackground(null);
                            break;
                        case 1:
                            rlMain.setBackground(getResources().getDrawable(R.drawable.main_music_bg));
                            break;
                        case 2:
                            rlMain.setBackground(null);
                            break;
                    }
                }

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        addMenu = (ImageButton) findViewById(R.id.id_ib_add);
        addMenu.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                android.support.v7.widget.PopupMenu popupMenu = new android.support.v7.widget.PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_add,
                        popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new android.support.v7.widget.PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.id_item_scan:
//                                        Intent openCameraIntent = new Intent(MainActivity.this,CaptureActivity.class);
//                                        startActivityForResult(openCameraIntent, 0);
                                break;
                            case R.id.id_item_addcontacts:
                                intentJump(getBaseContext(), UpdateInformation.class);
                                break;
                        }
                        return true;
                    }
                });
                popupMenu.show();
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_tv_phone:
                viewPager.setCurrentItem(0);
                break;
            case R.id.id_tv_music:
                viewPager.setCurrentItem(1);
                break;
            case R.id.id_tv_setting:
                viewPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.HOME");
            startActivity(intent);
        }
        return super.onKeyDown(event.getKeyCode(), event);
    }

}
