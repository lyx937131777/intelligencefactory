package com.intelligencefactory.android.util;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.intelligencefactory.android.db.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

public class Utility
{
    public static boolean handleUserResponse(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                DataSupport.deleteAll(User.class);
                JSONArray allUsers = new JSONArray(response);
                for (int i = 0; i < allUsers.length(); i++)
                {
                    JSONObject userObject = allUsers.getJSONObject(i);
                    User user = new User();
                    user.setNickname(userObject.getString("nickname"));
                    user.setUserID(userObject.getString("userID"));
                    user.setPassword(userObject.getString("password"));
                    user.setPhone_number(userObject.getString("phone_number"));
                    user.setProfile_photo(userObject.getString("profile_photo"));
                    Log.e("test", user.getUserID());
                    Log.e("test", user.getPassword());
                    Log.e("test", user.getNickname());
                    Log.e("test", user.getPhone_number());
                    Log.e("test", user.getProfile_photo());
                    user.save();
                }
                return true;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        return false;
    }
}
