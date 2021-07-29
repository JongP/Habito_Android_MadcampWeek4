package com.example.madcampweek4.ui.board;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.madcampweek4.Login;
import com.example.madcampweek4.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class BoardRecyclerViewAdapter extends RecyclerView.Adapter<BoardRecyclerViewAdapter.ViewHolder>{
    private LayoutInflater layoutInflater;
    private ArrayList<Board> boardData;
    private DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference("MadCampWeek4/Post");

    private String TAG ="myBoard";


    BoardRecyclerViewAdapter(Context context, ArrayList<Board> boardData){
        this.layoutInflater = LayoutInflater.from(context);
        this.boardData = boardData;
    }


    @NonNull
    public BoardRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.board_cardview, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(@NonNull @NotNull BoardRecyclerViewAdapter.ViewHolder holder, int position) {
        //bind the textview with data received

        Board board = boardData.get(position);

          Glide.with(holder.itemView)
                .load(board.getPost_uri())
                .into(holder.iv_postImage);
          String userProfileUrl= board.getUser_profilePic();
          //Log.d("Hello", userProfileUrl);
          if(userProfileUrl!=null&& !userProfileUrl.equals("")){
              Glide.with(holder.itemView)
                      .load(userProfileUrl)
                      .into(holder.civ_userProfile);
          }
          holder.tv_userId.setText(board.getUser_name());
          holder.tv_postContent.setText(board.getPost_content());
        holder.tv_postDate.setText(board.getPost_date());

        if(board.getLikes()==null || board.getLikes().get(Login.getUid())==null){
            holder.iv_like.setImageResource(R.drawable.heart);
        }else{
            holder.iv_like.setImageResource(R.drawable.heart_filled);
        }
        if(board.getLikes()!=null){
            holder.tv_likeNum.setText(String.valueOf(board.getLikes().size()));
        }
        holder.iv_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(board.getLikes()==null || board.getLikes().get(Login.getUid())==null){

                    if(board.getLikes()==null){
                        HashMap<String,Object> map = new HashMap<>();
                        board.setLikes(map);
                    }
                    board.getLikes().put(Login.getUid(),Login.getUid());
                    //Log.d(TAG, "board added: "+board.getLikes().toString());
                    databaseReference.child(board.getGroup_id()).child(board.getPost_id())
                            .child("likes").setValue(board.getLikes());
                    holder.iv_like.setImageResource(R.drawable.heart_filled);
                    holder.tv_likeNum.setText(String.valueOf(Integer.parseInt(String.valueOf(holder.tv_likeNum.getText()))+1));

                }else{
                    board.getLikes().remove(Login.getUid());
                    //Log.d(TAG, "board removed: "+board.getLikes().toString());
                    databaseReference.child(board.getGroup_id()).child(board.getPost_id())
                            .child("likes").setValue(board.getLikes());
                    holder.iv_like.setImageResource(R.drawable.heart);

                    holder.tv_likeNum.setText(String.valueOf(Integer.parseInt(String.valueOf(holder.tv_likeNum.getText()))-1));
                }
            }
        });



    }

    public int getItemCount() {
        return (boardData != null ? boardData.size() : 0);
    }

    public interface OnItemClickListener{
        void onItemClick(View v, int pos);
    }

    private OnItemClickListener mListener = null;

    public void setOnItemClickListener(OnItemClickListener listener){
        this.mListener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        CircleImageView civ_userProfile;
        TextView tv_userId, tv_postContent, tv_postDate, tv_likeNum;
        ImageView iv_postImage, iv_like;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_userId = itemView.findViewById(R.id.tv_userId);
            tv_postContent = itemView.findViewById(R.id.tv_postContent);
            tv_postDate = itemView.findViewById(R.id.tv_postDate);
            civ_userProfile = itemView.findViewById(R.id.civ_userProfile);
            iv_postImage = itemView.findViewById(R.id.iv_postImage);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_likeNum =itemView.findViewById(R.id.tv_likeNum);

        }
    }
}
