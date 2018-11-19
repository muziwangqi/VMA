package com.soling.view.activity;

import java.util.ArrayList;
import java.util.List;

import com.soling.view.adapter.ScollAdapter;
import com.soling.view.fragment.PhoneFragment;
import com.soling.view.fragment.PlayerFragment;
import com.soling.view.fragment.SettingFragment;
import com.soling.R;
import com.soling.view.fragment.SettingModuleFragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentActivity;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.view.ViewPager;
//import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class MainActivity extends BaseActivity implements OnClickListener {
    private ViewPager viewPager;
    private List<Fragment> fragments = new ArrayList<Fragment>();
    private TextView tvPhone, tvMusic, tvSetting;
    private ImageButton addMenu;
    private PhoneFragment phoneFragment;
    private PlayerFragment playerFragment;
    private SettingFragment settingFragment;
    private SettingModuleFragment settingModuleFragment;
    private FragmentManager fragmentManager;
    private ImageButton ibSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
<<<<<<< HEAD

=======
<<<<<<< HEAD

=======
<<<<<<< HEAD
//<<<<<<< HEAD
>>>>>>> d31494114299b5c5287d03943db7cec989f666bd
>>>>>>> 6e5d727ef9768036f36fbea0c815e82268cccdf5
=======
>>>>>>> 94231ecf1b43ca2597c25ab9958dda55305a84e4
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
		
		viewPager.setAdapter(new ScollAdapter(getSupportFragmentManager(),fragments));
		//System.out.println("setAdapter");
<<<<<<< HEAD
<<<<<<< HEAD

=======
<<<<<<< HEAD

=======
//=======
=======
>>>>>>> 6e5d727ef9768036f36fbea0c815e82268cccdf5
=======
>>>>>>> 94231ecf1b43ca2597c25ab9958dda55305a84e4
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
<<<<<<< HEAD
<<<<<<< HEAD

=======
>>>>>>> f01468819ae371d307634f267ddf256a2db0e29c
>>>>>>> d31494114299b5c5287d03943db7cec989f666bd
>>>>>>> 6e5d727ef9768036f36fbea0c815e82268cccdf5
=======

>>>>>>> 94231ecf1b43ca2597c25ab9958dda55305a84e4
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
                                Toast.makeText(MainActivity.this, "item",Toast.LENGTH_SHORT).show();
                                return true;
                            }
                        });
                popupMenu.show();
            }
        });
    }
<<<<<<< HEAD
<<<<<<< HEAD

=======
<<<<<<< HEAD
=======
//>>>>>>> 6d0060cba469c5e8dc4bc00558bab81f6e8d0212
>>>>>>> d31494114299b5c5287d03943db7cec989f666bd
>>>>>>> 6e5d727ef9768036f36fbea0c815e82268cccdf5
=======
>>>>>>> 94231ecf1b43ca2597c25ab9958dda55305a84e4

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
