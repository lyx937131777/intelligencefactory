package com.intelligencefactory.android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

public class editMemoActivity extends AppCompatActivity
{
    private TextView tv_time;
    private EditText et_body;
    private String type;
    private String fileName;
    private String filepath;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_memo);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
        {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Bundle myBundle = this.getIntent().getExtras();
        type = myBundle.getString("type");
        fileName = myBundle.getString("fileName");
        filepath = this.getFilesDir().toString();

        et_body = (EditText) findViewById(R.id.et_body);
        tv_time = (TextView) findViewById(R.id.tv_time);
        switch (type)
        {
            case "create":
                //et_body.setText("123");
                //et_body.setText(loadDataFromFile(this.getApplicationContext(),"memoSaveFile"));
                tv_time.setText(getCurrentTime());
                break;
            case "edit":
                String time = myBundle.getString("time");
                if (time != null)
                {
                    tv_time.setText(time);
                }
                if (fileName != null)
                {
                    et_body.setText(loadDataFromFile(this.getApplicationContext(), fileName));
                }
                break;
            default:
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
        }

        //载入最后修改时间---------undone
        //tv_time.setText(getCurrentTime());

    }

    @Override
    protected void onDestroy()
    {
        Log.e("editMemoActivity","des!!!!!!!");
        //更新最后修改时间
        tv_time.setText(getCurrentTime());
        Log.e("4", "==to delete file===");
        if (fileName != null)
        {
            File file = new File(filepath + "/" + fileName);
            Log.e("4", "====file is no null====");
            Log.e("4", "file name is:" + fileName + "");
            if (file.exists() && file.isFile())
            {
                Log.e("4", "file is ok");
                file.delete();
            }
        }
        //如果文件内容非空，将memo存入本地文件
        fileName = "memo_" + getCurrentTimeSS();
        String content = et_body.getText().toString();
        if (content.length() > 0)
        {
            saveDataToFile(getApplicationContext(), content, fileName);
        }
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
        super.onDestroy();
    }

    @Override
    public void onBackPressed()
    {
        //更新最后修改时间
        tv_time.setText(getCurrentTime());
        Log.e("4", "==to delete file===");
        if (fileName != null)
        {
            File file = new File(filepath + "/" + fileName);
            Log.e("4", "====file is no null====");
            Log.e("4", "file name is:" + fileName + "");
            if (file.exists() && file.isFile())
            {
                Log.e("4", "file is ok");
                file.delete();
            }
        }
        //如果文件内容非空，将memo存入本地文件
        fileName = "memo_" + getCurrentTimeSS();
        String content = et_body.getText().toString();
        if (content.length() > 0)
        {
            saveDataToFile(getApplicationContext(), content, fileName);
        }
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                //更新最后修改时间
                tv_time.setText(getCurrentTime());
                Log.e("4", "==to delete file===");
                if (fileName != null)
                {
                    File file = new File(filepath + "/" + fileName);
                    Log.e("4", "====file is no null====");
                    Log.e("4", "file name is:" + fileName + "");
                    if (file.exists() && file.isFile())
                    {
                        Log.e("4", "file is ok");
                        file.delete();
                    }
                }
                //如果文件内容非空，将memo存入本地文件
                fileName = "memo_" + getCurrentTimeSS();
                String content = et_body.getText().toString();
                if (content.length() > 0)
                {
                    saveDataToFile(getApplicationContext(), content, fileName);
                }
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return true;
    }

    private String getCurrentTime()
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 ahh:mm");
        return sdf.format(date);
    }

    private String getCurrentTimeSS()
    {
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 ahh:mm:ss");
        return sdf.format(date);
    }

    /**
     * 将数据存到文件中
     *
     * @param context  context
     * @param data     需要保存的数据
     * @param fileName 文件名
     */
    private void saveDataToFile(Context context, String data, String fileName)
    {
        FileOutputStream fileOutputStream = null;
        BufferedWriter bufferedWriter = null;
        try
        {

            fileOutputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            bufferedWriter.write(data);

        } catch (IOException e)
        {
            e.printStackTrace();
        } finally
        {
            try
            {
                if (bufferedWriter != null)
                {
                    bufferedWriter.close();
                }
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
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
}
