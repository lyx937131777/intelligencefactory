package com.intelligencefactory.android;

import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.feeeei.circleseekbar.CircleSeekBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,CircleSeekBar.OnSeekBarChangeListener
{
    private DrawerLayout mDrawerLayout;
    private io.feeeei.circleseekbar.CircleSeekBar seekBar;
    private TextView text2;
    private TextView text;
    private Button start;
    private Button stop;

    private static final int SERVICE_STOP = 23333;
    private static final int TIME_JUMP = 2333;
    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;
    public static long curtime = 0;
    public static long time = 0;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (requestCode == MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS)
        {
            if (!hasPermission())
            {
                //若用户未开启权限，则引导用户开启“Apps with usage access”权限
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }
        }
    }

    //检测用户是否对本app开启了“Apps with usage access”权限
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private boolean hasPermission()
    {
        AppOpsManager appOps = (AppOpsManager)
                getSystemService(Context.APP_OPS_SERVICE);
        int mode = 0;
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        {
            mode = appOps.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                    android.os.Process.myUid(), getPackageName());
        }
        return mode == AppOpsManager.MODE_ALLOWED;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navView = findViewById(R.id.nav_view);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        text = findViewById(R.id.time);
        text2 = findViewById(R.id.text2);
        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(this);
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_reorder_black_24dp);
        }
        navView.setNavigationItemSelectedListener(new NavigationView
                .OnNavigationItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                switch (item.getItemId())
                {
                    case R.id.nav_calendar:
                        Intent intent_calendar = new Intent(MainActivity.this, CalendarActivity
                                .class);
                        startActivity(intent_calendar);
                        break;

                    case R.id.nav_todolist:
                        Intent intent_todolist = new Intent(MainActivity.this, TodolistActivity
                                .class);
                        startActivity(intent_todolist);
                        break;
                    case R.id.nav_memo:
                        Intent intent_memo = new Intent(MainActivity.this, MemoActivity.class);
                        startActivity(intent_memo);
                        break;
                    case R.id.nav_friends:
                        Intent intent_friends = new Intent(MainActivity.this, FriendsActivity.class);
                        startActivity(intent_friends);
                        break;
                    case R.id.nav_setting:
                        Intent intent_setting = new Intent(MainActivity.this, SettingsActivity
                                .class);
                        startActivity(intent_setting);
                        break;
                }
                return true;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            if (!hasPermission())
            {
                startActivityForResult(
                        new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS),
                        MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS);
            }
        }

        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);

        Intent login_intent = getIntent();
        String username = login_intent.getStringExtra("username");
        View view = navView.getHeaderView(0);
        TextView tv_username = view.findViewById(R.id.nav_username);
        if (username != null)
        {
            tv_username.setText("123");
            tv_username.setText(username);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return true;
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.start:
                time = curtime;
                if(time==0){
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("请选择持续时间！")
                            .setPositiveButton("确定", null)
                            .show();
                    break;
                }
                MyService.isRun = true;
                Intent startIntant = new Intent(this, MyService.class);
                startService(startIntant);
                seekBar.setVisibility(View.INVISIBLE);
                text2.setVisibility(View.VISIBLE);
                start.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.VISIBLE);
                if (this.time != 0)
                {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                TimeUnit.MINUTES.sleep(MainActivity.time);
                                MyService.isRun = false;
                            }catch (InterruptedException e){
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    //mainactivity倒计时
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            for(long i = MainActivity.time;i>0;i--){
                                try {
                                    TimeUnit.MINUTES.sleep(1);
                                    Message msg = new Message();
                                    msg.what = TIME_JUMP;
                                    mHandler.sendMessage(msg);

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            Message msg = new Message();
                            msg.what = SERVICE_STOP;
                            mHandler.sendMessage(msg);
                        }
                    }).start();
                }
                break;

            case R.id.stop:
                MyService.isRun = false;
                Message msg = new Message();
                msg.what = SERVICE_STOP;
                mHandler.sendMessage(msg);
                break;
            default:
                break;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case SERVICE_STOP:
                    seekBar.setVisibility(View.VISIBLE);
                    text2.setVisibility(View.INVISIBLE);
                    start.setVisibility(View.VISIBLE);
                    stop.setVisibility(View.INVISIBLE);
                    seekBar.setCurProcess(0);
                    break;
                case TIME_JUMP:
                    if(MyService.isRun){
                        curtime--;
                        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
                        Date date = new Date(curtime*60*1000);
                        text.setText(format.format(date));
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onChanged(CircleSeekBar circleSeekBar, int i) {
        curtime = seekBar.getCurProcess();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        Date date = new Date(curtime*60*1000);
        text.setText(format.format(date));

    }

    private void queryFilterAppInfo() {
        PackageManager pm = this.getPackageManager();
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> appInfos= pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);// GET_UNINSTALLED_PACKAGES代表已删除，但还有安装目录的
        List<ApplicationInfo> applicationInfos=new ArrayList<>();

        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        // 通过getPackageManager()的queryIntentActivities方法遍历,得到所有能打开的app的packageName
        List<ResolveInfo>  resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        Set<String> allowPackages=new HashSet();
        for (ResolveInfo resolveInfo:resolveinfoList){
            allowPackages.add(resolveInfo.activityInfo.packageName);

        }

        for (ApplicationInfo app:appInfos) {
//            if((app.flags & ApplicationInfo.FLAG_SYSTEM) <= 0)//通过flag排除系统应用，会将电话、短信也排除掉
//            {
//                applicationInfos.add(app);
//            }

            if (allowPackages.contains(app.packageName)){
                applicationInfos.add(app);
            }
        }
        for (int i=0;i<applicationInfos.size();i++){
            ApplicationInfo info = applicationInfos.get(i);
            String pkName = info.packageName;
            String name = info.loadLabel(this.getPackageManager()).toString();
            Drawable drawable = info.loadIcon(this.getPackageManager());
        }
    }


}
