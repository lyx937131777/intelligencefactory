package com.intelligencefactory.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.intelligencefactory.android.db.Todolist;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by hp on 2018/5/25.
 */

public class TodolistAdapter extends ArrayAdapter<Todolist>
{
    private int resourceID;
    public TodolistAdapter(Context context, int textViewResourceID, List<Todolist> objects )
    {
        super(context, textViewResourceID, objects);
        resourceID = textViewResourceID;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Todolist todolist = getItem(position);
        View view;
        ViewHolder viewHolder = null;
        if(convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceID,parent,false);
            viewHolder = new ViewHolder();
            viewHolder.todolistTime = (TextView) view.findViewById(R.id.todolist_time);
            viewHolder.todolistTitle = (TextView) view.findViewById(R.id.todolist_title);
            viewHolder.todolistState = (TextView) view.findViewById(R.id.todolist_state);
            view.setTag(viewHolder);
        }
        else
        {
            view = convertView;
            viewHolder = (ViewHolder)view.getTag();
        }

        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd  HH:mm");

        viewHolder.todolistTime.setText(ft.format(todolist.getStarttime())+" / "+ft.format(todolist.getEndtime()));
        viewHolder.todolistTitle.setText(todolist.getTitle());
        Date now = new Date();
        if(todolist.getStarttime().after(now))
        {
            viewHolder.todolistState.setText("Waiting");
        }else if(todolist.getEndtime().after(now))
        {
            viewHolder.todolistState.setText("Running");
        }else
        {
            viewHolder.todolistState.setText("Ended");
        }

        return view ;
    }

    class ViewHolder
    {
        TextView todolistTime;
        TextView todolistState;
        TextView todolistTitle;

    }
}
