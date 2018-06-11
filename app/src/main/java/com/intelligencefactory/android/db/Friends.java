package com.intelligencefactory.android.db;

import org.litepal.crud.DataSupport;

public class Friends extends DataSupport
{
    private String userID1;
    private String userID2;
    public String getUserID1()
    {
        return userID1;
    }

    public void setUserID1(String userID1)
    {
        this.userID1 = userID1;
    }

    public String getUserID2()
    {
        return userID2;
    }

    public void setUserID2(String userID2)
    {
        this.userID2 = userID2;
    }


}
