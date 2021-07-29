package com.example.madcampweek4.ui.collection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.madcampweek4.Display_Fish;
import com.example.madcampweek4.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class CollectionRecyclerViewAdapter extends RecyclerView.Adapter<CollectionRecyclerViewAdapter.ViewHolder> {

    private FirebaseAuth mFirebaseAuth; // firebase auth
    private DatabaseReference mDatabaseRef; //realtime db
    private FirebaseUser user;

    private LayoutInflater layoutInflater;
    private ArrayList<Boolean> fish, display_fish;


    CollectionRecyclerViewAdapter(Context context, ArrayList<Boolean> fish, ArrayList<Boolean> display_fish){
        this.layoutInflater = LayoutInflater.from(context);
        this.fish = fish;
        this.display_fish = display_fish;

        user= FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("MadCampWeek4");

    }

    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.collection_cardview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        //display_fish.get(position)
        holder.tv_groupInfo.setText("Click button to add/remove from the aquarium");
        String[] names=Display_Fish.getNames();

        switch (position){
            case 1:
                holder.img_fish.setAnimation(R.raw.fish_shark);
                break;
            case 2:
                holder.img_fish.setAnimation(R.raw.fish_spin_jelly);
                break;
            case 3:
                holder.img_fish.setAnimation(R.raw.fish_turtle);
                break;
            case 4:
                holder.img_fish.setAnimation(R.raw.fish_whale);
                break;
            case 5:
                holder.img_fish.setAnimation(R.raw.fish_balloon);
                break;
            case 6:
                holder.img_fish.setAnimation(R.raw.fish_blue);
                break;
            case 0:
                holder.img_fish.setAnimation(R.raw.cutefish);
                break;
        }

        if (fish.get(position)){
            holder.tv_groupName.setText(names[position]);
        }else{
            holder.img_fish.setImageResource(R.drawable.question);
            holder.tv_groupName.setText("?");
            holder.fb_addfish.setVisibility(View.GONE);
            holder.tv_groupInfo.setText("Don't have it yet");
        }


        if (display_fish.get(position)){
            holder.fb_addfish.setText("REMOVE");
        }else{
            holder.fb_addfish.setText("ADD");
        }

        holder.fb_addfish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.fb_addfish.getText().equals("ADD")){
                    Log.d("리사이클러", "추가"+position);
                    holder.fb_addfish.setText("REMOVE");
                    // position이랑 display_fish 순번, 위치 같음
                    ArrayList<Boolean> displayFish=Display_Fish.getOwn();
                    displayFish.set(position, true);
                    Display_Fish.setOwn(displayFish);
                    Log.d("리사이클러a",Display_Fish.getOwn().toString());
                    mDatabaseRef.child("UserAccount").child(user.getUid()).child("display_fish").setValue(displayFish);

                } else{
                    Log.d("리사이클러", "삭제"+position);
                    holder.fb_addfish.setText("ADD");
                    ArrayList<Boolean> displayFish=Display_Fish.getOwn();
                    displayFish.set(position, false);
                    Display_Fish.setOwn(displayFish);
                    Log.d("리사이클러r",Display_Fish.getOwn().toString());
                    mDatabaseRef.child("UserAccount").child(user.getUid()).child("display_fish").setValue(displayFish);

                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return (fish != null ? fish.size() : 0);
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tv_groupName, tv_groupInfo;
        ExtendedFloatingActionButton fb_addfish;
        LottieAnimationView img_fish;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            int pos = getAdapterPosition();
            tv_groupName = (TextView)itemView.findViewById(R.id.tv_groupSearchName);
            tv_groupInfo = (TextView)itemView.findViewById(R.id.tv_groupSearchInfo);
            fb_addfish = (ExtendedFloatingActionButton)itemView.findViewById(R.id.fb_addfish);
            img_fish = itemView.findViewById(R.id.img_fish);

            itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Log.d("리사이클러", "아이템 누름");
                    if(pos!=RecyclerView.NO_POSITION){
                        if(mListener!=null){
                            mListener.onItemClick(v, pos);
                        }
                    }
                }
            });

        }
    }
}
