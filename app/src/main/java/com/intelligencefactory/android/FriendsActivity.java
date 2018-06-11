package com.intelligencefactory.android;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import com.intelligencefactory.android.db.User;
import com.intelligencefactory.android.util.HttpUtil;
import com.intelligencefactory.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FriendsActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //测试连接服务器
        Log.e("test","Start!!!!!");
        String address = HttpUtil.LocalAddress + "/Query";
        HttpUtil.sendOkHttpRequest(address, new Callback()
        {
            @Override
            public void onFailure(Call call, IOException e)
            {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException
            {
                final String responseData = response.body().string();
                Log.e("test",responseData);
                Utility.handleUserResponse(responseData);
            }
        });

        Log.e("test","End!!!!!!!!");
        List<User> userlist = DataSupport.findAll(User.class);
        for(User user : userlist)
        {
            Log.e("test", "userID : "+user.getUserID());
            Log.e("test", "password : "+user.getPassword());
            Log.e("test", "nickname : "+user.getNickname());
            Log.e("test", "phone_number : "+user.getPhone_number());
            Log.e("test", "profile_photo : "+user.getProfile_photo());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
