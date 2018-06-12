package com.intelligencefactory.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.preference.PreferenceManager;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.intelligencefactory.android.db.User;
import com.intelligencefactory.android.util.HttpUtil;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import io.feeeei.circleseekbar.CircleSeekBar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener,
        CircleSeekBar.OnSeekBarChangeListener
{
    private CountDownTimer timer;

    public static boolean serviviceRun = false;
    public static MainActivity instance = null;
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
    public static long countdowntime = 0;
    private SharedPreferences pref;
    private String userID;

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
    protected void onRestart()
    {
        super.onRestart();
        NavigationView navView = findViewById(R.id.nav_view);
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userID = pref.getString("userID", null);
        View view = navView.getHeaderView(0);
        TextView tv_username = view.findViewById(R.id.nav_username);
        if (userID != null)
        {
            tv_username.setText(userID);
        }
        User user = DataSupport.where("userID = ?", userID).findFirst(User.class);
        String nickname = user.getNickname();
        String profile_photo = user.getProfile_photo();

        TextView nick_text = (TextView) view.findViewById(R.id.nav_nickname);
        nick_text.setText(nickname);
        de.hdodenhof.circleimageview.CircleImageView photo = (de.hdodenhof.circleimageview
                .CircleImageView) view.findViewById(R.id.icon_image);
        if (!profile_photo.equals("null"))
        {
            Glide.with(MainActivity.this).load(HttpUtil.LocalAddress +"/"+ profile_photo).signature
                    (new StringSignature(pref.getString("latest",""))).into(photo);
            Log.e("test", "Hello");
        } else
        {
            Glide.with(MainActivity.this).load(R.drawable.nav_icon).into(photo);
            //photo.setImageResource(R.drawable.nav_icon);
            Log.e("test", "NULL!!!!!!!");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        instance = this;
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
                        Intent intent_friends = new Intent(MainActivity.this, FriendsActivity
                                .class);
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

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userID = pref.getString("userID", null);
        View view = navView.getHeaderView(0);
        TextView tv_username = view.findViewById(R.id.nav_username);
        if (userID != null)
        {
            tv_username.setText(userID);
        }
        User user = DataSupport.where("userID = ?", userID).findFirst(User.class);
        String nickname = user.getNickname();
        String profile_photo = user.getProfile_photo();

        TextView nick_text = (TextView) view.findViewById(R.id.nav_nickname);
        nick_text.setText(nickname);
        de.hdodenhof.circleimageview.CircleImageView photo = (de.hdodenhof.circleimageview
                .CircleImageView) view.findViewById(R.id.icon_image);

        if (!profile_photo.equals("null"))
        {
            Glide.with(MainActivity.this).load(HttpUtil.LocalAddress +"/"+ profile_photo).signature
                    (new StringSignature(pref.getString("latest",""))).into(photo);
            Log.e("test", "Hello");
        } else
        {
            Glide.with(MainActivity.this).load(R.drawable.nav_icon).into(photo);
            //photo.setImageResource(R.drawable.nav_icon);
            Log.e("test", "NULL!!!!!!!");
        }
        photo.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent_toUserInfo = new Intent(MainActivity.this, UserInfoActivity.class);
                startActivityForResult(intent_toUserInfo, 1);
            }
        });
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
                if (time == 0)
                {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("请选择持续时间！")
                            .setPositiveButton("确定", null)
                            .show();
                    break;
                }

                serviviceRun = true;
                MyService.isRun = true;
                Intent startIntant = new Intent(this, MyService.class);
                startService(startIntant);
                seekBar.setVisibility(View.INVISIBLE);
                text2.setVisibility(View.VISIBLE);
                start.setVisibility(View.INVISIBLE);
                stop.setVisibility(View.VISIBLE);

                timer = new CountDownTimer(curtime * 60 * 1000, 1000)
                {
                    @Override
                    public void onTick(long millisUntilFinished)
                    {
                        long ttime = millisUntilFinished / 1000;

                        if (ttime <= 59)
                        {
                            text.setText(String.format("00:%02d", ttime));
                        } else
                        {
                            text.setText(String.format("%02d:%02d", ttime / 60, ttime % 60));
                        }
                    }

                    @Override
                    public void onFinish()
                    {
                        MyService.isRun = false;
                        serviviceRun = false;
                        seekBar.setVisibility(View.VISIBLE);
                        text2.setVisibility(View.INVISIBLE);
                        start.setVisibility(View.VISIBLE);
                        stop.setVisibility(View.INVISIBLE);
                        seekBar.setCurProcess(0);
                    }
                };
                timer.start();
                break;

            case R.id.stop:
                new AlertDialog.Builder(this)
                        .setTitle("警告")
                        .setMessage("你正在享受寂静，是否停止！")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                timer.cancel();
                                timer.onFinish();
                                timer = null;
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            default:
                break;
        }
    }


    @Override
    public void onChanged(CircleSeekBar circleSeekBar, int i)
    {
        curtime = seekBar.getCurProcess();
        text.setText(curtime + "分钟");
    }

    public void onDestroy()
    {
        if (timer != null)
        {
            timer.cancel();
            timer = null;
        }
        super.onDestroy();
    }
}
