package com.example.madcampweek4.ui.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.madcampweek4.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private ArrayList<Group> groupData;


    public RecyclerViewAdapter(Context context, ArrayList<Group> groupData){
        this.layoutInflater = LayoutInflater.from(context);
        this.groupData = groupData;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.group_cardview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //bind the textview with data received
        Glide.with(holder.itemView)
                .load(groupData.get(position).getProfile())
                .into(holder.img_groupImage);
        holder.tv_groupName.setText(groupData.get(position).getGroupName());
        holder.tv_groupInfo.setText(groupData.get(position).getGroupInfo());

    }

    @Override
    public int getItemCount() {
        return (groupData != null ? groupData.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_groupName, tv_groupInfo;
        ImageView img_groupImage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_groupName = itemView.findViewById(R.id.tv_groupName);
            tv_groupInfo = itemView.findViewById(R.id.tv_groupInfo);
            img_groupImage = itemView.findViewById(R.id.img_groupImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
    }
}