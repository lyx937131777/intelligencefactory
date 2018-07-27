package com.intelligencefactory.android;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.intelligencefactory.android.db.Todolist;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class TodolistActivity extends AppCompatActivity
{
    private List<Todolist> todolistList = new ArrayList<>();
    private ListView listview;
    private Todolist todolist;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todolist);
        initTodolist();
        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        final TodolistAdapter adapter = new TodolistAdapter(TodolistActivity.this,R.layout.todolist_item, todolistList);
        listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                todolist = todolistList.get(position);
                Intent intent = new Intent(TodolistActivity.this, EditActivity.class);
                int tID = todolist.getId();
                intent.putExtra("tID",tID);
                startActivityForResult(intent,1);
            }
        });
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id)
            {
                AlertDialog.Builder builder=new AlertDialog.Builder(TodolistActivity.this);
                builder.setMessage("确定删除?");
                builder.setTitle("提示");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        todolist = todolistList.get(position);
                        DataSupport.delete(Todolist.class,todolist.getId());
                        initTodolist();
                        TodolistAdapter adapter = new TodolistAdapter(TodolistActivity.this,R.layout.todolist_item, todolistList);
                        listview.setAdapter(adapter);
                        Toast.makeText(getBaseContext(), "删除列表项", Toast.LENGTH_SHORT).show();
                    }
                });

                //添加AlertDialog.Builder对象的setNegativeButton()方法
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
                return true;
            }
        });
        Button newButton = (Button) findViewById(R.id.new_button);
        newButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(TodolistActivity.this, EditActivity.class);
                startActivityForResult(intent,2);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 1:
                if(resultCode == RESULT_OK)
                {
                    initTodolist();
                    TodolistAdapter adapter = new TodolistAdapter(TodolistActivity.this,R.layout.todolist_item, todolistList);
                    listview.setAdapter(adapter);
                }
                break;
            case 2:
                if(resultCode == RESULT_OK)
                {
                    initTodolist();
                    TodolistAdapter adapter = new TodolistAdapter(TodolistActivity.this,R.layout.todolist_item, todolistList);
                    listview.setAdapter(adapter);
                }
                break;
        }
    }

    private void initTodolist()
    {
        todolistList = DataSupport.order("endtime").order("starttime").find(Todolist.class);
    }
}
