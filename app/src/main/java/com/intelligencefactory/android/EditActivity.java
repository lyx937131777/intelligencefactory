package com.intelligencefactory.android;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EditActivity extends AppCompatActivity implements TimePickerDialog.TimePickerDialogInterface
{
    private TimePickerDialog mTimePickerDialog;
    private Todolist todolist;
    private EditText todolistTitle;
    private TextView todolistStarttime;
    private TextView todolistEndtime;
    private EditText todolistContent;
    private EditText todolistState;
    private SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd  HH:mm");
    private int commond;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        final Intent intent = getIntent();
        todolist = (Todolist) intent.getSerializableExtra("todolist");
        getWindow().setTitle(todolist.getTitle());
        mTimePickerDialog = new TimePickerDialog(EditActivity.this);

        todolistTitle = (EditText) findViewById(R.id.todolist_title2);
        todolistStarttime = (TextView) findViewById(R.id.todolist_starttime);
        todolistEndtime = (TextView) findViewById(R.id.todolist_endtime);
        todolistContent = (EditText) findViewById(R.id.todolist_content);
        todolistState = (EditText) findViewById(R.id.todolist_state2);
        Button okButton = (Button) findViewById(R.id.ok_button);

        todolistTitle.setText(todolist.getTitle());
        todolistStarttime.setText(ft.format(todolist.getStarttime()));
        todolistEndtime.setText(ft.format(todolist.getEndtime()));
        todolistContent.setText(todolist.getContent());
        todolistState.setText(todolist.getState());

        todolistStarttime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commond = 0;
                mTimePickerDialog.showDateAndTimePickerDialog();
            }
        });

        todolistEndtime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                commond = 1;
                mTimePickerDialog.showDateAndTimePickerDialog();
            }
        });

        okButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                todolist.setTitle(todolistTitle.getText().toString());
                Date starttime = null;
                try
                {
                    starttime = ft.parse(todolistStarttime.getText().toString());
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
                todolist.setStarttime(starttime);

                Date endtime = null;
                try
                {
                    endtime = ft.parse(todolistEndtime.getText().toString());
                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
                todolist.setEndtime(endtime);

                todolist.setContent(todolistContent.getText().toString());
                todolist.setState(todolistState.getText().toString());
                Intent intent1 = new Intent();
                intent.putExtra("return_todolist",todolist);
                setResult(RESULT_OK,intent);
                finish();
            }
        });
    }

    @Override
    public void positiveListener()
    {
        Date newdate =new Date();
        newdate.setYear(mTimePickerDialog.getYear()-1900);
        newdate.setMonth(mTimePickerDialog.getMonth());
        newdate.setDate(mTimePickerDialog.getDay());
        newdate.setMinutes(mTimePickerDialog.getMinute());
        newdate.setHours(mTimePickerDialog.getHour());
        switch (commond)
        {
            case 0:
                todolistStarttime.setText(ft.format(newdate));
            case 1:
                todolistEndtime.setText(ft.format(newdate));
        }
    }

    @Override
    public void negativeListener()
    {

    }
}
