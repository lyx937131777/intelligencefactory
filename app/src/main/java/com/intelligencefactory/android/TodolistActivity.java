package com.intelligencefactory.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Date;
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

        TodolistAdapter adapter = new TodolistAdapter(TodolistActivity.this,R.layout.todolist_item, todolistList);
        listview = (ListView) findViewById(R.id.list_view);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                todolist = todolistList.get(position);
                Intent intent = new Intent(TodolistActivity.this, EditActivity.class);
                intent.putExtra("todolist",todolist);
                startActivityForResult(intent,1);
            }
        });
        Button newButton = (Button) findViewById(R.id.new_button);
        newButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Date startdate = new Date();
                Date enddate = new Date();
                Todolist task = new Todolist("NewTask",startdate,enddate);
                Intent intent = new Intent(TodolistActivity.this, EditActivity.class);
                intent.putExtra("todolist",task);
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
                    int index =todolistList.indexOf(todolist);
                    Todolist return_todolist = (Todolist) data.getSerializableExtra("return_todolist");
                    todolistList.set(index,return_todolist);
                    TodolistAdapter adapter = new TodolistAdapter(TodolistActivity.this,R.layout.todolist_item, todolistList);
                    listview.setAdapter(adapter);
                }
                break;
            case 2:
                if(resultCode == RESULT_OK)
                {
                    Todolist return_todolist = (Todolist) data.getSerializableExtra("return_todolist");
                    todolistList.add(return_todolist);
                    TodolistAdapter adapter = new TodolistAdapter(TodolistActivity.this,R.layout.todolist_item,todolistList);
                    listview.setAdapter(adapter);
                }
                break;
        }
    }

    private void initTodolist()
    {
        for(int i = 1; i < 20; i++)
        {
            Date startdate = new Date();
            Date enddate = new Date();
            enddate.setMonth(7);
            Todolist task = new Todolist("Task"+i,startdate, enddate);
            todolistList.add(task);
        }
    }
}
