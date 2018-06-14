package com.intelligencefactory.android;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.List;

public class WhiteListAdapter extends RecyclerView.Adapter<WhiteListAdapter.ViewHolder> {

    private List<ApplicationInfo> AppinfoList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View appView;
        ImageView appIcon;
        TextView appName;
        ToggleButton appToggle;

        public ViewHolder(View view){
            super(view);
            appView = view;
            appIcon = view.findViewById(R.id.app_icon);
            appName = view.findViewById(R.id.app_name);
            appToggle = view.findViewById(R.id.app_toggle);
        }
    }

    public WhiteListAdapter (List<ApplicationInfo> list){
        AppinfoList = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appinfo_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.appToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    int position = holder.getAdapterPosition();
                    ApplicationInfo info = AppinfoList.get(position);
                    MyService.whitelist.add(info.packageName);
                }else {
                    int position = holder.getAdapterPosition();
                    ApplicationInfo info = AppinfoList.get(position);
                    MyService.whitelist.remove(info.packageName);
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PackageManager pm = WhiteListActivity.pm;
        ApplicationInfo info = AppinfoList.get(position);
        holder.appIcon.setImageDrawable(info.loadIcon(pm));
        holder.appName.setText(info.loadLabel(pm).toString());
        if(MyService.whitelist.contains(info.packageName)){
            holder.appToggle.setChecked(true);
        }
    }

    @Override
    public int getItemCount() {
        return AppinfoList.size();
    }
}
