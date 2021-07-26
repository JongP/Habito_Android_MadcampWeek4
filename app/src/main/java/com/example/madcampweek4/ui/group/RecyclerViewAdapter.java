package com.example.madcampweek4.ui.group;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.madcampweek4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
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
        View view = layoutInflater.inflate(R.layout.group_search_cardview, parent, false);
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

        HashMap<String,String> map = groupData.get(position).getUsers();
        if( map!=null) {
            String userId= FirebaseAuth.getInstance().getCurrentUser().getUid();
            if(map.get(userId)!=null){
                Log.d("recycler", userId);
                holder.fb_joinGroup.setText("unjoin");
            }else holder.fb_joinGroup.setText("Join");
        }else holder.fb_joinGroup.setText("Join");

        holder.fb_joinGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database=FirebaseDatabase.getInstance();
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                String groupId = groupData.get(position).getId();
                Log.d("AdapterOnItemLIstenr", userId);

                DatabaseReference groupRef=database.getReference("MadCampWeek4/Group/"+groupId+"/users");
                DatabaseReference userRef = database.getReference("MadCampWeek4/UserAccount/"+userId+"/groups");

                userRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        //Log.d("Adapter", dataSnapshot.getValue().toString());
                        HashMap<String,Object> hashMap=null;
                        if(dataSnapshot!=null ) hashMap = (HashMap<String, Object>) dataSnapshot.getValue();
                        //Log.d("Adapter", hashMap.toString());
                        if(dataSnapshot==null||hashMap==null ||hashMap.get(groupId)==null) {
                            HashMap<String,Object> map = new HashMap<>();
                            map.put(groupData.get(position).getId(),groupData.get(position).getId());
                            userRef.updateChildren(map);

                            HashMap<String,Object> map1 =new HashMap<>();
                            map1.put(userId,userId);
                            groupRef.updateChildren(map1);
                            holder.fb_joinGroup.setText("Unjoin");
                            Toast.makeText(v.getContext(),"Joined",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            userRef.child(groupId).removeValue();
                            groupRef.child(userId).removeValue();

                            holder.fb_joinGroup.setText("Join");
                            Toast.makeText(v.getContext(),"Unjoined",Toast.LENGTH_SHORT).show();
                        }

                    }
                });


            }
        });

    }

    @Override
    public int getItemCount() {
        return (groupData != null ? groupData.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv_groupName, tv_groupInfo;
        ImageView img_groupImage;
        //FloatingActionButton fb_joinGroup;
        ExtendedFloatingActionButton fb_joinGroup;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tv_groupName = itemView.findViewById(R.id.tv_groupSearchName);
            tv_groupInfo = itemView.findViewById(R.id.tv_groupSearchInfo);
            img_groupImage = itemView.findViewById(R.id.img_groupSearchImage);
            fb_joinGroup = itemView.findViewById(R.id.fb_joinGroup);


        }
    }
}