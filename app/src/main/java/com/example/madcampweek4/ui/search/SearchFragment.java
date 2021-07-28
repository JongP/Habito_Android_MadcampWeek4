package com.example.madcampweek4.ui.search;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.madcampweek4.R;
import com.example.madcampweek4.ui.group.Group;
import com.example.madcampweek4.ui.group.RecyclerViewAdapter;
import com.github.ybq.android.spinkit.style.CubeGrid;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;


public class SearchFragment extends Fragment {

    private EditText et_searchGroup;
    private RecyclerView rv_searchGroup;
    private RecyclerViewAdapter recyclerViewAdapter;
    private FloatingActionButton fb_search;
    private ProgressBar progressBar;
    private ArrayList<Group> groupItemList;
    private Uri selectImageUri;

    private ImageView iv_groupProfile;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference,todayGroupRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    private  String TAG = "SearchFragment";
    private final int GALLERY_CODE = 10;

    private Dialog dialog;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        rv_searchGroup= view.findViewById(R.id.rv_groupSearch);
        fb_search = view.findViewById(R.id.fb_search);
        et_searchGroup=view.findViewById(R.id.et_searchGroup);
        progressBar = view.findViewById(R.id.spin_kit_searchGroup);
        progressBar.setIndeterminateDrawable(new CubeGrid());

        rv_searchGroup.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.VERTICAL,false));

        groupItemList= new ArrayList<Group>();
        recyclerViewAdapter = new RecyclerViewAdapter(getContext(),groupItemList);

        rv_searchGroup.setAdapter(recyclerViewAdapter);

        database= FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("MadCampWeek4/Group"); //db Table 연동
        todayGroupRef =database.getReference("MadCampWeek4/UserAccount");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("Group/profile");

        fb_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(getContext());

                dialog.setContentView(R.layout.dialog_creategroup);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                iv_groupProfile = dialog.findViewById(R.id.iv_newPostImage);
                Button btn_create_group = dialog.findViewById(R.id.btn_create_group);
                EditText et_groupName = dialog.findViewById(R.id.et_groupName);
                EditText et_groupInfo = dialog.findViewById(R.id.et_groupInfo);
                ImageView iv_closeDialog = dialog.findViewById(R.id.iv_closeDialog);

                iv_groupProfile.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Intent.ACTION_PICK);
                        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                        startActivityForResult(intent, GALLERY_CODE);
                    }
                });
                btn_create_group.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String groupId = databaseReference.push().getKey().toString();

                        StorageReference profileRef = storageReference.child(groupId);
                        UploadTask uploadTask = profileRef.putFile(selectImageUri);

                        Log.d(TAG, "profileRef: "+profileRef.toString());


                        Group group = new Group(groupId,""
                                ,et_groupName.getText().toString(),et_groupInfo.getText().toString());

                        databaseReference.child(groupId).setValue(group);

                    }
                });
                iv_closeDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                //파이어베이스 데이터베이스의 데이터를 받아오는곳

                groupItemList.clear();
                for(DataSnapshot snapshot1 : snapshot.getChildren()){
                    //Log.d(TAG, "onDataChange: loop");
                    Group group = snapshot1.getValue(Group.class);


                    String groupId = group.getId();
                    StorageReference profileRef = storageReference.child(groupId);


                    if(group.getProfile().equals("")) {
                        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Log.d(TAG, "Uri of" + groupId + ": " + uri.toString());

                                groupItemList.add(new Group(group.getId(), uri.toString(), group.getGroupName(), group.getGroupInfo()));
                                recyclerViewAdapter.notifyDataSetChanged();

                                database= FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
                                databaseReference = database.getReference("MadCampWeek4/Group/"+group.getId()); //db Table 연동 :
                                HashMap <String,Object> hashMap= new HashMap<>();
                                hashMap.put("profile",uri.toString());
                                databaseReference.updateChildren(hashMap);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull @NotNull Exception e) {
                                Log.d(TAG, "wtf of " + groupId);
                                Log.d(TAG, e.toString());
                            }
                        });
                    }else{
                        groupItemList.add(group);
                    }
                }
                recyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getContext(),"DB fetch Error",Toast.LENGTH_SHORT);
            }
        });

        view.findViewById(R.id.iv_closeSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_searchGroup.setText("");
                final InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_searchGroup.getWindowToken(), 0);
            }
        });

        et_searchGroup.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filterGroups(s.toString());
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==GALLERY_CODE && resultCode==RESULT_OK && data!=null &&data.getData()!=null){
            selectImageUri=data.getData();
            iv_groupProfile.setImageURI(selectImageUri);
        }
    }

    private  void filterGroups(String name){
        ArrayList<Group> filterList = new ArrayList<>();
        for(Group item : groupItemList){
            if(item.getGroupName().toLowerCase().contains(name.toLowerCase())){
                filterList.add(item);
            }
        }
        if (filterList.isEmpty()){
            Toast.makeText(getContext(),"No Group found for searched query",Toast.LENGTH_SHORT).show();
        }
        else {
            recyclerViewAdapter.filterList(filterList);
        }
    }
}