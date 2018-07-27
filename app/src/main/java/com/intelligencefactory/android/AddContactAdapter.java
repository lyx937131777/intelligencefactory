package com.intelligencefactory.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.intelligencefactory.android.db.User;
import com.intelligencefactory.android.util.HttpUtil;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AddContactAdapter extends RecyclerView.Adapter<AddContactAdapter.ViewHolder>
{
    private ArrayList<Contact> contactArrayList;
    private Context mContext;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View contactView;
        ImageView contactIcon;
        TextView contactRealName;
        TextView contactNickname;
        Button bt_add;

        public ViewHolder(View view)
        {
            super(view);
            contactView = view;
            contactIcon = (ImageView) view.findViewById(R.id.ic_contact);
            contactRealName = (TextView) view.findViewById(R.id.realname_contact);
            contactNickname = (TextView) view.findViewById(R.id.nickname_contact);
            bt_add = (Button) view.findViewById(R.id.bt_add);
        }
    }

    public AddContactAdapter(ArrayList<Contact> contactArrayList)
    {
        this.contactArrayList = contactArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        if (mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.addcontact_item,
                parent, false);
        final ViewHolder holder = new ViewHolder(view);
        //监听添加好友事件
        holder.bt_add.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int position = holder.getAdapterPosition();
                Contact newOne = contactArrayList.get(position);
                String userID2 = newOne.getUserID();
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
                String userID1 = pref.getString("userID",null);
                String address = HttpUtil.LocalAddress + "/AddFriends";
                Log.e("AddContactAdapter", userID1+"   "+userID2);
                HttpUtil.friendshipRequest(address, userID1, userID2, new Callback()
                {
                    @Override
                    public void onFailure(Call call, IOException e)
                    {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException
                    {

                        final String responseData = response.body().string();
                        Log.e("test",responseData);
                        if(responseData.equals("true"))
                        {
                            AddContactActivity addContactActivity = (AddContactActivity)mContext;
                            addContactActivity.runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    holder.bt_add.setEnabled(false);
                                }
                            });
                        }
                    }
                });
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position)
    {
        Contact contact = contactArrayList.get(position);
        if (!contact.getIcon().equals("null"))
        {
            Glide.with(mContext).load(HttpUtil.LocalAddress + "/" + contact.getIcon()).into
                    (holder.contactIcon);
        } else
        {
            Glide.with(mContext).load(R.drawable.nav_icon).into(holder.contactIcon);
        }
        holder.contactNickname.setText(contact.getNickname());
        holder.contactRealName.setText(contact.getName());
    }

    @Override
    public int getItemCount()
    {
        return contactArrayList.size();
    }
}
