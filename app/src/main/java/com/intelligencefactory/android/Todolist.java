package com.intelligencefactory.android;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by hp on 2018/5/25.
 */

public class Todolist implements Serializable
{
    private String title;
    private String content;
    private Date starttime;
    private Date endtime;
    private String state;

    public Todolist(String tile, Date starttime, Date endtime)
    {
        this.title = tile;
        this.starttime = starttime;
        this.endtime = endtime;
        state = "Waiting";
        content = null;
    }

    public String getTitle()
    {
        return title;
    }

    public Date getStarttime()
    {
        return  starttime;
    }

    public Date getEndtime()
    {
        return endtime;
    }

    public String getState()
    {
        return state;
    }
    public String getContent()
    {
        return content;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public void setStarttime(Date starttime)
    {
        this.starttime = starttime;
    }

    public void setEndtime(Date endtime)
    {
        this.endtime = endtime;
    }

    public void setState(String state)
    {
        this.state = state;
    }
}
