package com.example.madcampweek4.ui.Board;

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
import com.example.madcampweek4.ui.group.Group;
import com.example.madcampweek4.ui.group.GroupRecyclerViewAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BoardRecyclerViewAdapter extends RecyclerView.Adapter<BoardRecyclerViewAdapter.ViewHolder>{
    private LayoutInflater layoutInflater;
    private ArrayList<Board> boardData;


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
//        Glide.with(holder.itemView)
//                .load(boardData.get(position).getPost_content())
//                .into(holder.iv_postImage);
//        Glide.with(holder.itemView)
//                .load(boardData.get(position).getUser_profilePic())
//                .into(holder.civ_userProfile);
//        holder.tv_userId.setText(boardData.get(position).getUser_id());
//        holder.tv_postContent.setText(boardData.get(position).getPost_content());
//        holder.tv_postDate.setText(boardData.get(position).getPost_date());
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
        TextView tv_userId, tv_postContent, tv_postDate;
        ImageView iv_postImage;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);

            tv_userId = itemView.findViewById(R.id.tv_userId);
            tv_postContent = itemView.findViewById(R.id.tv_postContent);
            tv_postDate = itemView.findViewById(R.id.tv_postDate);
            civ_userProfile = itemView.findViewById(R.id.civ_userProfile);
            iv_postImage = itemView.findViewById(R.id.iv_postImage);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
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
