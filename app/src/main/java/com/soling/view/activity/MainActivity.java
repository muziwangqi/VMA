package com.soling.view.activity;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentPagerAdapter;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

import android.widget.RelativeLayout;

import android.widget.TextView;
import android.widget.Toast;

import com.soling.App;
import com.soling.R;
import com.soling.utils.SharedPreferenceUtil;
import com.soling.view.adapter.ScollAdapter;
import com.soling.view.fragment.PhoneFragment;
import com.soling.view.fragment.PlayerFragment;
import com.soling.view.fragment.SettingFragment;
import com.soling.view.fragment.SettingModuleFragment;

import java.util.ArrayList;
import java.util.List;

//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements OnClickListener {
    private static final String TAG = "MainActivity";
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private TextView tvPhone, tvMusic, tvSetting;
    private ImageButton addMenu;
    private PhoneFragment phoneFragment;
    private PlayerFragment playerFragment;
    private SettingFragment settingFragment;
    private SettingModuleFragment settingModuleFragment;
    private android.app.FragmentManager fragmentManager;
    private ImageButton ibSearch;
<<<<<<< HEAD
    private SharedPreferences sharedPreferences;
=======
    private RelativeLayout rlMain;
>>>>>>> d218d0aabc28059c56374fcee69dfecee2d8b719

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //changeTheme();
        Log.d(TAG, "onCreate: " + App.getInstance().isTHEMEC());
        if (App.getInstance().isTHEMEC()){
            Log.d(TAG, "onCreate: ");
            setTheme(R.style.dayTheme);
        }else{
            setTheme(R.style.nightTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        //mainPresenter.showPhoneList();
		/*LayoutInflater layoutInflater = getLayoutInflater();
		View layoutSetting = layoutInflater.inflate(R.layout.fragent_setting,
				null);
		View layoutPhone = layoutInflater
				.inflate(R.layout.fragment_phone, null);
		View layoutMusic = layoutInflater
				.inflate(R.layout.fragment_music, null);*/

<<<<<<< HEAD
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
        //System.out.println("setAdapter");
=======
>>>>>>> d218d0aabc28059c56374fcee69dfecee2d8b719


//		viewPager = (ViewPager) findViewById(R.id.id_vp_scoll);
//		tvPhone = (TextView) findViewById(R.id.id_tv_phone);
//		tvMusic = (TextView) findViewById(R.id.id_tv_music);
//		tvSetting = (TextView) findViewById(R.id.id_tv_setting);
//		tvPhone.setOnClickListener(this);
//		tvMusic.setOnClickListener(this);
//		tvSetting.setOnClickListener(this);
//		fragments.add(new PhoneFragment());
//		fragments.add(new PlayerFragment());
//		fragments.add(new SettingFragment());
//
//		viewPager.setAdapter(new ScollAdapter(getSupportFragmentManager(),fragments));
//		//System.out.println("setAdapter");


		View decorView = getWindow().getDecorView();
		decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        rlMain = findViewById(R.id.rl_main);
        viewPager = (ViewPager) findViewById(R.id.id_vp_scoll);
        tvPhone = (TextView) findViewById(R.id.id_tv_phone);
        tvMusic = (TextView) findViewById(R.id.id_tv_music);
        tvSetting = (TextView) findViewById(R.id.id_tv_setting);
        ibSearch = findViewById(R.id.id_ib_search);
        tvPhone.setOnClickListener(this);
        tvMusic.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
        fragments.add(new PhoneFragment());
        fragments.add(new PlayerFragment());
        fragments.add(new SettingModuleFragment());

        ibSearch.setOnClickListener(this);
        viewPager.setAdapter(new ScollAdapter(getSupportFragmentManager(), fragments));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                if (1 == i) {
                    rlMain.setBackground(getResources().getDrawable(R.drawable.main_music_bg));
                }
                else {
                    rlMain.setBackground(null);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        //System.out.println("setAdapter");

        // addMenu
        addMenu = (ImageButton) findViewById(R.id.id_ib_add);
        addMenu.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);
                popupMenu.getMenuInflater().inflate(R.menu.menu_add,
                        popupMenu.getMenu());
                popupMenu
                        .setOnMenuItemClickListener(new OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem menuItem) {
                                Toast.makeText(MainActivity.this, "item", Toast.LENGTH_SHORT).show();
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
            case R.id.id_ib_search:
                Intent intent = new Intent(this, SearchMusicActivity.class);
                startActivity(intent);
                break;
        }
    }

}
