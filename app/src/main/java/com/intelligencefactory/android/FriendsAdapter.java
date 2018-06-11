package com.intelligencefactory.android;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intelligencefactory.android.db.User;

import java.util.ArrayList;

public class FriendsAdapter extends ArrayAdapter<User>
{
    private int resourceID;
    private Context myContext;

    public FriendsAdapter(Context mContext, int mResourceID, ArrayList<User> objects)
    {
        super(mContext, mResourceID, objects);
        myContext = mContext;
        resourceID = mResourceID;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        User user = getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView == null)
        {
            view = LayoutInflater.from(getContext()).inflate(resourceID, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.userIcon = (de.hdodenhof.circleimageview.CircleImageView) view.findViewById(R.id.ic_friend);
            viewHolder.userName = (TextView) view.findViewById(R.id.nickname_friend);
            view.setTag(viewHolder);
        } else
        {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.userName.setText(user.getNickname());
        String photo = user.getProfile_photo();
//        这里用Glide
        Glide.with(myContext).load(photo).into(viewHolder.userIcon);
        return view;
    }

    class ViewHolder
    {
        de.hdodenhof.circleimageview.CircleImageView userIcon;
        TextView userName;
    }
}
