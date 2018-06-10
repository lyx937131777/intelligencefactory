package com.intelligencefactory.android.db;

import org.litepal.crud.DataSupport;

public class User extends DataSupport
{
    private String userID;
    private String password;
    private String nickname;
    private String phofile_photo;
    private String phone_number;

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public String getPhofile_photo()
    {
        return phofile_photo;
    }

    public void setPhofile_photo(String phofile_photo)
    {
        this.phofile_photo = phofile_photo;
    }

    public String getPhone_number()
    {
        return phone_number;
    }

    public void setPhone_number(String phone_number)
    {
        this.phone_number = phone_number;
    }
}
