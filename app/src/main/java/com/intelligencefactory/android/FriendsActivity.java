package com.intelligencefactory.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.intelligencefactory.android.db.User;
import com.intelligencefactory.android.util.HttpUtil;
import com.intelligencefactory.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class FriendsActivity extends AppCompatActivity
{
    private ArrayList<User> friendList = new ArrayList<>();
    private ListView listView;

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
        /*
        for(int i = 0; i< 10 ;i++)
        {
            User user = new User();
            user.setUserID(i+"@qq.com");
            user.setPassword("111111");
            user.setPhone_number("123456789");
            user.setNickname(i+""+i+""+i);
            user.setProfile_photo(HttpUtil.LocalAddress+"/images/rushhour.jpg");
            friendList.add(user);
        }
        */

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
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        friendList =(ArrayList<User>) DataSupport.findAll(User.class);
                        FriendsAdapter friendsAdapter = new FriendsAdapter(FriendsActivity.this, R.layout.friend_item, friendList);
                        listView.setAdapter(friendsAdapter);
                    }
                });

            }
        });

        Log.e("test","End!!!!!!!!");
        friendList =(ArrayList<User>) DataSupport.findAll(User.class);

        FriendsAdapter friendsAdapter = new FriendsAdapter(this, R.layout.friend_item, friendList);
        listView = (ListView) findViewById(R.id.friend_list);
        listView.setAdapter(friendsAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                finish();
                break;
            case R.id.menu_add:
                Toast.makeText(this,"add!!!!",Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(this, NewFriendActivity.class);
                //startActivity(intent);
                break;
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return true;
    }

}
