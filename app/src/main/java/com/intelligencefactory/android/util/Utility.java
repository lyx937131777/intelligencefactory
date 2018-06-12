package com.intelligencefactory.android.util;

import android.text.TextUtils;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.intelligencefactory.android.db.Friends;
import com.intelligencefactory.android.db.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

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

    public static User storeLoginUser(String response, String userID)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                DataSupport.deleteAll(User.class, "userID = ?", userID);
                JSONArray allUsers = new JSONArray(response);
                for (int i = 0; i < allUsers.length(); i++)
                {
                    JSONObject userObject = allUsers.getJSONObject(i);
                    if (userObject.getString("userID").equals(userID))
                    {
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
                        return user;
                    }
                }
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        User user = new User();
        return user;
    }

    public static boolean searchUser(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
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

    public static ArrayList<User> storeUserResponse(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                ArrayList<User> users = new ArrayList<>();
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
                    DataSupport.deleteAll(User.class,"userID = ?",user.getUserID());
                    user.save();
                    users.add(user);
                }
                return users;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        ArrayList<User> users2 = new ArrayList<>();
        return  users2;
    }

    public static ArrayList<User> findbynumber(String response, String number)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                ArrayList<User> users = new ArrayList<>();
                JSONArray allUsers = new JSONArray(response);
                for (int i = 0; i < allUsers.length(); i++)
                {
                    JSONObject userObject = allUsers.getJSONObject(i);
                    if (userObject.getString("phone_number").equals(number))
                    {
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
                        DataSupport.deleteAll(User.class,"userID = ?",user.getUserID());
                        user.save();
                        users.add(user);
                    }

                }
                return users;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        ArrayList<User> users2 = new ArrayList<>();
        return  users2;
    }

    public static ArrayList<String> findPair(String response)
    {
        if (!TextUtils.isEmpty(response))
        {
            try
            {
                ArrayList<String> userIDs = new ArrayList<>();
                JSONArray allPairs = new JSONArray(response);
                for (int i = 0; i < allPairs.length(); i++)
                {
                    JSONObject pairObject = allPairs.getJSONObject(i);
                    String string = pairObject.getString("userID2");
                    Log.e("Utility",string);
                    Friends friends = new Friends();
                    friends.setUserID2(string);
                    friends.setUserID1(pairObject.getString("userID1"));
                    Log.e("Utility",friends.getUserID1());
                    friends.save();
                    userIDs.add(string);
                }
                return userIDs;
            } catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        ArrayList<String> users2 = new ArrayList<>();
        return  users2;
    }
}
