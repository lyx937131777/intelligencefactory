package com.intelligencefactory.android;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import java.io.File;
import java.util.List;

public class UserInfoActivity extends AppCompatActivity
{
    private String userID;
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // get userId from main

        pref = PreferenceManager.getDefaultSharedPreferences(this);
        userID = pref.getString("userID", null);
        User user = DataSupport.where("userID = ?", userID).findFirst(User.class);
        String nickname = user.getNickname();
        String phone_number = user.getPhone_number();
        String profile_photo = user.getProfile_photo();

        TextView tv_nickname = (TextView) findViewById(R.id.tv_nickname);
        TextView tv_userID = (TextView) findViewById(R.id.tv_userid);
        TextView tv_phone_number = (TextView) findViewById(R.id.tv_phone_number);
        de.hdodenhof.circleimageview.CircleImageView photo = (de.hdodenhof.circleimageview
                .CircleImageView) findViewById(R.id.icon_image);


        // get request from server by post userID


        tv_nickname.setText(nickname);
        tv_userID.setText(userID);
        tv_phone_number.setText(phone_number);
        if (!profile_photo.equals("null"))
        {
            Glide.with(UserInfoActivity.this).load(HttpUtil.LocalAddress +"/"+ profile_photo).signature
                    (new StringSignature(pref.getString("latest",""))).into(photo);
        } else
        {
            Glide.with(UserInfoActivity.this).load(R.drawable.nav_icon).into(photo);
        }

        final Button bt_modify_password = (Button) findViewById(R.id.bt_modify_password);
        bt_modify_password.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent_toModifyPW = new Intent(UserInfoActivity.this,
                        ModifyPasswordActivity.class);
                startActivity(intent_toModifyPW);
            }
        });


        Button bt_edit_info = (Button) findViewById(R.id.bt_edit_info);
        bt_edit_info.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent_toEditInfo = new Intent(UserInfoActivity.this, EditUserInfoActivity
                        .class);
                startActivityForResult(intent_toEditInfo, 1);
            }
        });
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.e("UserInfo", "refresh");
        refresh();
    }

    private void refresh()
    {
        finish();
        Intent intent = new Intent(UserInfoActivity.this, UserInfoActivity.class);
        startActivity(intent);
    }
}
