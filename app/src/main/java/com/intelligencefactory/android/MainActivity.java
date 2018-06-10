package com.intelligencefactory.android;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private DrawerLayout mDrawerLayout;

    private static final int MY_PERMISSIONS_REQUEST_PACKAGE_USAGE_STATS = 1101;
    private EditText editText;
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

        editText = findViewById(R.id.edit_text);
        Button start = findViewById(R.id.start);
        Button stop = findViewById(R.id.stop);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);

        Intent login_intent = getIntent();
        String username = login_intent.getStringExtra("username");
        View view = navView.getHeaderView(0);
        TextView tv_username = (TextView) view.findViewById(R.id.nav_username);
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
                time = Integer.valueOf(editText.getText().toString());
                MyService.isRun = true;
                Intent startIntant = new Intent(this, MyService.class);
                startService(startIntant);
                break;
            case R.id.stop:
                MyService.isRun = false;
                break;
            default:
                break;
        }
    }


}
