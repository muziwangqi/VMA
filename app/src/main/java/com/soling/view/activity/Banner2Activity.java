package com.soling.view.activity;

        import java.util.ArrayList;
        import java.util.Arrays;
        import java.util.List;

        import com.soling.utils.PermissionUtil;
        import com.soling.view.fragment.SettingFragment;
        import com.soling.R;

        import android.Manifest;
        import android.app.Activity;
        import android.app.AlertDialog;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.SharedPreferences.Editor;
        import android.content.pm.PackageManager;
        import android.net.Uri;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.provider.Settings;
        import android.support.v4.app.ActivityCompat;
        import android.support.v4.content.ContextCompat;
        import android.support.v4.view.PagerAdapter;
        import android.support.v4.view.ViewPager;
        import android.support.v4.view.ViewPager.OnPageChangeListener;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.ViewGroup;
        import android.view.View.OnClickListener;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.RadioGroup;
        import android.widget.TextView;
        import android.widget.Toast;

public class Banner2Activity extends BaseActivity implements OnClickListener {
    private static final String TAG = "Banner2Activity";


    private Button enterButtn;
    private TextView textView;
    private LinearLayout linearLayout;
    private RadioGroup radioGroup;
    private List<ImageView> pointList;// point
    private List<ImageView> list;
    private int count = 0;
    private int[] pictures = {R.drawable.banner_a, R.drawable.banner_b,
            R.drawable.banner_c, R.drawable.banner_d};// picture
    private ViewPager viewPager;
    private SharedPreferences sharedPreferences;
    private Editor editor;

    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS, Manifest.permission.SEND_SMS, Manifest.permission.WRITE_CALL_LOG};
    private List<String> unPermissions = new ArrayList<>();

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            count++;
            viewPager.setCurrentItem(count);
            sendEmptyMessageDelayed(0, 1000);
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_banner2);
        isFirst();
        initView();
        initData();
        BannerAdapter bannerAdapter = new BannerAdapter();
        viewPager.setAdapter(bannerAdapter);
        // add point
        addPoint();
        enterButtn.setOnClickListener(this);
        handler.sendEmptyMessageDelayed(0, 1000);
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageSelected(int arg0) {
                // TODO Auto-generated method stub
                int index = arg0 % list.size();
                for (int i = 0; i < pictures.length; i++) {
                    if (index == 1) {
                        pointList.get(i).setImageResource(R.drawable.point);
                    } else {
                        pointList.get(i).setImageResource(R.drawable.point);
                    }
                }
                if (index == pictures.length - 1) {
                    enterButtn.setVisibility(View.VISIBLE);// kejian
                } else {
                    enterButtn.setVisibility(View.GONE);
                }
            }

            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub

            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

        });

        initPermission();
        requestPermission();

    }

    private void initPermission() {
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                unPermissions.add(permissions[i]);
            }
        }
    }

    // 请求权限 hyw
    private void requestPermission() {
        if (unPermissions.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions, 0);
        }
    }

    // hyw
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean hasUnpermission = false;
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (!(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    hasUnpermission = true;
                }
            }
        }
        if (hasUnpermission) {
            android.support.v7.app.AlertDialog dialog = new android.support.v7.app.AlertDialog.Builder(this)
                    .setMessage("请授予所需权限")
                    .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Uri packageUri = Uri.parse("package:" + getPackageName());
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageUri);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Banner2Activity.this.finish();
                        }
                    })
                    .setCancelable(false)
                    .create();
            dialog.show();
        }
    }

    private void addPoint() {
        linearLayout = (LinearLayout) findViewById(R.id.id_ll_linearlayout);
        pointList = new ArrayList<ImageView>();
        for (int i = 0; i < pictures.length; i++) {
            ImageView imageView = new ImageView(this);
            if (i == 0) {
                imageView.setImageResource(R.drawable.point);
            } else {
                imageView.setImageResource(R.drawable.point);
            }
            pointList.add(imageView);
            linearLayout.addView(imageView);
        }
    }

    private void isFirst() {

        sharedPreferences = getSharedPreferences("drawable", MODE_PRIVATE);
        if (sharedPreferences.getBoolean("isFirst", false)) {
            Intent intent = new Intent(this, SettingFragment.class);
            startActivity(intent);
            finish();
        }
    }

    private void initView() {
        enterButtn = (Button) findViewById(R.id.id_button_enter);
        viewPager = (ViewPager) findViewById(R.id.id_vp_banner2);
    }

    private void initData() {
        list = new ArrayList<ImageView>();
        for (int i = 0; i < pictures.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(pictures[i]);
            list.add(imageView);
        }
    }

    public void onClick(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onStop() {
        if (handler != null) {
            handler.removeMessages(0);
        }
        super.onStop();
    }

    class BannerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            int index = position % list.size();
            container.addView(list.get(index));
            return list.get(index);
        }
    }

    private static final int WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 1;
    private static final int READ_CONTACTS_REQUEST_CODE = 2;


    // 请求权限 hyw
    private void requestPermission(String[] strings, PermissionListener 已授权) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, READ_CONTACTS_REQUEST_CODE);
        }
    }


    private void permission() {
        String[] deniedPermissions = PermissionUtil.PHONE;
        requestPermission(deniedPermissions, new BaseActivity.PermissionListener() {
            @Override
            public void onGranted() {
                Toast.makeText(Banner2Activity.this, "已授权", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onDenied(List<String> deniedPermission) {
                for (String permission : deniedPermission) {
                    Toast.makeText(Banner2Activity.this, "被拒绝的权限：" + permission, Toast.LENGTH_SHORT).show();
                    openAppDetails();
                }
            }
        });
    }

    /**
     * 打开 APP 的详情设置
     */
    private void openAppDetails() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setMessage("备份通讯录需要访问 “通讯录” 和 “外部存储器”，请到 “应用信息 -> 权限” 中授予！");
        builder.setPositiveButton("去手动授权", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.addCategory(Intent.CATEGORY_DEFAULT);
                intent.setData(Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
