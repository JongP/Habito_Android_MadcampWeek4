package com.example.madcampweek4.ui.group;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek4.R;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private LayoutInflater layoutInflater;
    private List<String> groupData;


    RecyclerViewAdapter(Context context, List<String> groupData){
        this.layoutInflater = LayoutInflater.from(context);
        this.groupData = groupData;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.group_cardview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //bind the textview with data received

        String groupName = groupData.get(position);
        holder.tv_groupName.setText(groupName);

        String groupInfo = groupData.get(position);
        holder.tv_groupInfo.setText(groupInfo);

//        String groupImage = data.get(position);
//        holder.img_groupImage.setImageResource(groupImage);
    }

    @Override
    public int getItemCount() {
        return groupData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_groupName, tv_groupInfo;
        ImageView img_groupImage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_groupName = itemView.findViewById(R.id.tv_groupName);
            tv_groupInfo = itemView.findViewById(R.id.tv_groupInfo);
            img_groupImage = itemView.findViewById(R.id.img_groupImage);
        }
    }
}
