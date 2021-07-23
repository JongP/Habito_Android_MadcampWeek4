package com.example.madcampweek4.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek4.R;
import com.example.madcampweek4.item.SearchGroupItem;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SearchGroupAdapter extends RecyclerView.Adapter<SearchGroupAdapter.ViewHolder> {

    private List<SearchGroupItem> searchGroupItemList = new ArrayList<>();
    private Context context;


    public SearchGroupAdapter(List<SearchGroupItem> searchGroupItemList, Context context) {
        this.searchGroupItemList = searchGroupItemList;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public SearchGroupAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view =LayoutInflater.from(context).inflate(R.layout.searchgroup_item,parent,false);
        return new SearchGroupAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SearchGroupAdapter.ViewHolder holder, int position) {
        SearchGroupItem searchGroupItem = searchGroupItemList.get(position);
        holder.groupName.setText(searchGroupItem.getGroupName());
        holder.groupContent.setText(searchGroupItem.getGroupContent());
    }

    @Override
    public int getItemCount() {
        return searchGroupItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView groupName, groupContent;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            groupName=itemView.findViewById(R.id.tv_groupName);
            groupContent=itemView.findViewById(R.id.tv_groupContent);
        }
    }
}
