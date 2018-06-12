package com.intelligencefactory.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class NewFriendActivity extends AppCompatActivity implements OnClickListener
{
    private EditText edittext_search;
    private Button bt_turn_contact;
    private ImageView iv_search;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newfriend);
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        edittext_search = (EditText) findViewById(R.id.edittext_search);
        bt_turn_contact = (Button) findViewById(R.id.bt_turn_contact);
        iv_search = (ImageView)findViewById(R.id.search_iv);

        bt_turn_contact.setOnClickListener(this);
        iv_search.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.bt_turn_contact:
                Intent intent = new Intent(this, AddContactActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                break;
            case R.id.search_iv:
                String key = edittext_search.getText().toString();
                if(key.length()<1)
                {
                    Toast.makeText(this,"请输入要搜索的内容",Toast.LENGTH_LONG).show();
                    break;
                }
                Intent intent_search = new Intent(this, AddContactActivity.class);
                intent_search.putExtra("type",2);
                intent_search.putExtra("key",key);
                startActivity(intent_search);
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
