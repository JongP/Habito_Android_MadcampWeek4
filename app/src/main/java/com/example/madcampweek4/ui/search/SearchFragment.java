package com.example.madcampweek4.ui.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek4.R;
import com.example.madcampweek4.ui.group.Group;
import com.example.madcampweek4.ui.group.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class SearchFragment extends Fragment {

    private TextView tv_search;
    private RecyclerView rv_searchGroup;
    private RecyclerViewAdapter recyclerViewAdapter;
    private FloatingActionButton fb_search;
    private ArrayList<Group> groupItemList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private LinearLayout dialogView;

    private  String TAG = "SearchFragment";

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        rv_searchGroup= view.findViewById(R.id.rv_groupSearch);
        fb_search = view.findViewById(R.id.fb_search);
        rv_searchGroup.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        groupItemList= new ArrayList<Group>();
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(),groupItemList);

        rv_searchGroup.setAdapter(recyclerViewAdapter);

        database= FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("MadCampWeek4/Group"); //db Table 연동 :

        fb_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: start");
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                LayoutInflater dialog_inflater = requireActivity().getLayoutInflater();
                dialogView = (LinearLayout) View.inflate(getContext(),R.layout.dialog_creategroup,null);
//dialog_inflater.inflate(R.layout.dialog_creategroup,null
                builder.setView(dialogView)
                        .setPositiveButton("create", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText et_groupName = dialogView.findViewById(R.id.et_groupName);
                                EditText et_groupInfo = dialogView.findViewById(R.id.et_groupInfo);

                                String groupId = databaseReference.push().getKey().toString();

                                Group group = new Group(groupId,"",et_groupName.getText().toString(),et_groupInfo.getText().toString());

                                databaseReference.child(groupId).setValue(group);
                                return;
                            }
                        })
                        .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                return;
                            }
                        }).create().show();
            }
        });



        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는곳
                Log.d(TAG, "listenr staart");
                Log.d(TAG,snapshot.toString());
                groupItemList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    Log.d(TAG, "onDataChange: loop");
                    Group group = snapshot1.getValue(Group.class);
                    groupItemList.add(group);
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(),"DB fetch Error",Toast.LENGTH_SHORT);
            }
        });



        return view;
    }

}