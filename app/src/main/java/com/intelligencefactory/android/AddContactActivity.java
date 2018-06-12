package com.intelligencefactory.android;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.intelligencefactory.android.db.User;
import com.intelligencefactory.android.util.HttpUtil;
import com.intelligencefactory.android.util.Utility;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddContactActivity extends AppCompatActivity
{
    private ArrayList<Contact> contactArrayList = new ArrayList<>();
    private AddContactAdapter addContactAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcontact);
        Log.e("AddContactActivity", "Start!!!!!");
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        RecyclerView contactView = (RecyclerView) findViewById(R.id.contact_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        contactView.setLayoutManager(linearLayoutManager);
        addContactAdapter = new AddContactAdapter(contactArrayList);
        contactView.setAdapter(addContactAdapter);

        int type = getIntent().getIntExtra("type", 0);
        if (type == 2)
        {
            String key = getIntent().getStringExtra("key");
            String address = HttpUtil.LocalAddress + "/QueryUserInfo";
            HttpUtil.searchKeyRequest(address, key, new Callback()
            {
                @Override
                public void onFailure(Call call, IOException e)
                {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException
                {
                    final String responsDate = response.body().string();
                    Log.e("Response!!!!", responsDate);
                    ArrayList<User> users = Utility.storeUserResponse(responsDate);
                    for(User user : users)
                    {
                        Contact contact = new Contact(user.getUserID(), user.getPhone_number(),
                                user.getUserID(), user.getNickname(), user.getProfile_photo());
                        contactArrayList.add(contact);
                        runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                addContactAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                }
            });
        } else if (type == 1)
        {
            initRecyclerView();
        } else
        {
            Toast.makeText(this, "出现异常错误", Toast.LENGTH_LONG).show();
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

    private void initRecyclerView()
    {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission
                    .READ_CONTACTS}, 1);
        } else
        {
            readContacts();
        }
    }

    private void readContacts()
    {
        Cursor cursor = null;
        try
        {
            cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null);
            if (cursor != null)
            {
                while (cursor.moveToNext())
                {
                    final String displayName = cursor.getString(cursor.getColumnIndex
                            (ContactsContract
                            .CommonDataKinds.Phone.DISPLAY_NAME));
                    final String number = cursor.getString(cursor.getColumnIndex(ContactsContract
                            .CommonDataKinds.Phone.NUMBER));
                    Log.e("Tel","telephone : "+ number);
                    String address = HttpUtil.LocalAddress + "/QueryUserInfo";
                    HttpUtil.searchKeyRequest(address, number, new Callback()
                    {
                        @Override
                        public void onFailure(Call call, IOException e)
                        {

                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException
                        {
                            final String responsDate = response.body().string();
                            Log.e("Response!!!!", responsDate);
                            ArrayList<User> users = Utility.findbynumber(responsDate, number);
                            for (User user : users)
                            {
                                Contact contact = new Contact(user.getUserID(), number,
                                        displayName, user.getNickname(), user.getProfile_photo());
                                contactArrayList.add(contact);
                                runOnUiThread(new Runnable()
                                {
                                    @Override
                                    public void run()
                                    {
                                        addContactAdapter.notifyDataSetChanged();
                                    }
                                });

                            }
                        }
                    });

                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        } finally
        {
            if (cursor != null)
            {
                cursor.close();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        switch (requestCode)
        {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    readContacts();
                } else
                {
                    Toast.makeText(this, "你拒绝了权限请求！", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }
}
