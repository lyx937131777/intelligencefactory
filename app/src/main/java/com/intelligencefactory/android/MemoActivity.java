package com.intelligencefactory.android;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
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
import android.widget.SimpleAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoActivity extends AppCompatActivity
{
    private Button btn_create;
    private ListView listView;
    private SimpleAdapter simp_adapter;
    public List<Map<String, Object>> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Log.e("0", "" + this.getFilesDir());
        Log.e("0", "" + this.getApplicationContext().toString());
        listView = (ListView) findViewById(R.id.listView);
        //得到文件名------------to do：需要对文件数组 files 按照文件名排序
        final String filepath = this.getFilesDir().toString();
        File dir = new File(filepath);
        File[] files = dir.listFiles();
        if (files != null)
        {
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].isFile())
                {
                    Log.e("1", files[i].getName());
                    //files[i].delete();
                }
            }
        }

        //适配器
        dataList = new ArrayList<Map<String, Object>>();
        Log.e("2", "=======map======");
        simp_adapter = new SimpleAdapter(this, getData(files), R.layout.memo_item,
                new String[]{"time", "content"}, new int[]{R.id.tv_time, R.id.tv_content});

        Log.e("3", "=======map======");
        listView.setAdapter(simp_adapter);
        //listView点击事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition
                        (position);
                Log.e("4", map.toString());
                Log.e("4", map.get("fileName"));
                Intent myIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("type", "edit");
                bundle.putString("fileName", map.get("fileName"));
                bundle.putString("time", map.get("time"));
                myIntent.putExtras(bundle);
                myIntent.setClass(MemoActivity.this, editMemoActivity.class);
                startActivityForResult(myIntent, 1);
            }
        });
        //listView长按事件
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener()
        {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {

                HashMap<String, String> map = (HashMap<String, String>) parent.getItemAtPosition
                        (position);
                Log.e("4", map.toString());
                Log.e("4", map.get("fileName"));
                final String toDel_fileName = map.get("fileName");
                Log.e("8", "===1===");
                Builder builder = new AlertDialog.Builder(MemoActivity.this);
                //builder.setTitle("删除该日志");
                builder.setMessage("是否删除所选备忘录？");

                Log.e("8", "===2===");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int i)
                    {

                        Log.e("8", "===2.1===");
                        File file = new File(filepath + "/" + toDel_fileName);
                        file.delete();
                        refresh();

                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int i)
                    {
                        Log.e("8", "===2.2===");
                    }
                });
                Log.e("8", "===3===");
                builder.create();

                Log.e("8", "===4===");
                builder.show();

                Log.e("8", "===5===");
                return true;
            }
        });

        btn_create = (Button) findViewById(R.id.btn_create);
        btn_create.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putString("type", "create");
                Intent intent = new Intent(MemoActivity.this, editMemoActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, 1);
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

    private List<Map<String, Object>> getData(File[] files)
    {
        if (files != null)
        {
            Log.e("2", "=======file is no empty======");
            for (int i = 0; i < files.length; i++)
            {
                if (files[i].isFile())
                {
                    Log.e("2", "=======files has file======");
                    String fileName = files[i].getName();
                    Log.e("2", fileName.substring(0, 5));
                    if (fileName.length() > 4 && fileName.substring(0, 5).equals("memo_"))
                    {

                        String content = loadDataFromFile(getApplicationContext(), fileName);
                        String time = fileName.substring(5);
                        Map<String, Object> map = new HashMap<String, Object>();
                        map.put("content", content);
                        map.put("time", time);
                        map.put("fileName", fileName);
                        dataList.add(map);
                    }
                }
            }
        }
        return dataList;
    }


    /**
     * 从文件中读取数据
     *
     * @param context  context
     * @param fileName 文件名
     * @return 从文件中读取的数据
     */
    private String loadDataFromFile(Context context, String fileName)
    {
        FileInputStream fileInputStream = null;
        BufferedReader bufferedReader = null;
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            fileInputStream = context.openFileInput(fileName);
            bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String result = "";
            while ((result = bufferedReader.readLine()) != null)
            {
                stringBuilder.append(result);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            if (bufferedReader != null)
            {
                try
                {
                    bufferedReader.close();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }
        return stringBuilder.toString();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        switch (requestCode)
        {
            case 1:
                Log.e("7", "refresh");
                refresh();
                break;
            default:
                break;
        }
    }

    private void refresh()
    {
        finish();
        Intent intent = new Intent(MemoActivity.this, MemoActivity.class);
        startActivity(intent);
    }
}
