package com.example.madcampweek4.ui.group;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek4.MainActivity;
import com.example.madcampweek4.R;
import com.example.madcampweek4.ui.board.BoardActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupFragment extends Fragment {
    private View view;
    private RecyclerView recyclerView;
    private GroupRecyclerViewAdapter groupRecyclerViewAdapter;
    private ArrayList<Group> groupData;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference,userRef,groupRef;
    private String TAG = "GroupFragmentTAG";

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group, container, false);

        groupData = new ArrayList<>();  //객체 담을 arraylist

        recyclerView = view.findViewById(R.id.groupRecyclerView);
        recyclerView.setHasFixedSize(true);  //기존성능 강화
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        groupRecyclerViewAdapter = new GroupRecyclerViewAdapter(getContext(), groupData);
        recyclerView.setAdapter(groupRecyclerViewAdapter);

        database=FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("MadCampWeek4/Group"); //db Table 연동 : 오빠

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        groupRecyclerViewAdapter.setOnItemClickListener(new GroupRecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                Intent intent = new Intent(getActivity(), BoardActivity.class);
                startActivity(intent);
            }
        });


        userRef = database.getReference("MadCampWeek4/UserAccount/"+userId+"/groups");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if(snapshot!=null &&snapshot.getValue()!=null) {
                    Log.d(TAG, snapshot.getValue().toString());
                    HashMap<String,Object> groupsOfUsers = (HashMap<String,Object>) snapshot.getValue();

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        groupsOfUsers.forEach((key,value)->{
                            groupRef=database.getReference("MadCampWeek4/Group/"+value);
                            groupRef.get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    Group group = dataSnapshot.getValue(Group.class);
                                    Log.d(TAG, "onSuccess: ");
                                    Log.d(TAG, group.toString());
                                    groupData.add(group);
                                    groupRecyclerViewAdapter.notifyDataSetChanged();
                                }
                            });
                            return;
                        });
                    }else{
                        Toast.makeText(getContext(),"This should not happen",Toast.LENGTH_SHORT).show();
                    }
                }
                else Log.d(TAG, "snapdata null");
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });




        return view;
    }

}