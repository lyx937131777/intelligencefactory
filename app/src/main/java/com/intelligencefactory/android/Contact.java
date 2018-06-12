package com.intelligencefactory.android;

public class Contact {
    private String userID;
    private String phone_number;
    private String real_name;
    private String nickname;
    private String profile_photo;

    public Contact(String userID, String phone_number, String real_name, String nickname, String
            profile_photo)
    {
        this.userID = userID;
        this.phone_number = phone_number;
        this.real_name = real_name;
        this.nickname = nickname;
        this.profile_photo = profile_photo;
    }

    public String getUserID()
    {
        return userID;
    }

    public void setUserID(String userID)
    {
        this.userID = userID;
    }

    public String getPhone_number()
    {
        return phone_number;
    }

    public void setPhone_number(String phone_number)
    {
        this.phone_number = phone_number;
    }



    public String getName() {
        return real_name;
    }

    public String getIcon() {
        return profile_photo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setIcon(String profile_photo) {
        this.profile_photo = profile_photo;
    }

    public void setName(String name) {
        this.real_name = name;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

}
