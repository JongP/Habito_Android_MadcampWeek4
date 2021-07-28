package com.example.madcampweek4.ui.profile;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.example.madcampweek4.LoginActivity;
import com.example.madcampweek4.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;


public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    String name, email, profileUrl;
    Dialog dialog;
    TextView userName, userEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("My Profile");

        Intent intent=getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");
        profileUrl=intent.getStringExtra("profileUrl");

        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("MadCampWeek4");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("UserProfile");

        FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();

        userName=findViewById(R.id.userName);
        userEmail=findViewById(R.id.userEmail);

        userName.setText(name);
        userEmail.setText(email);

        ImageView iv_profileUrl=findViewById(R.id.iv_profileUrl);

        dialog = new Dialog(this);

        if (profileUrl!=null&&!profileUrl.equals("")){
            Glide.with(this).load(profileUrl).into(iv_profileUrl);
        } else{
            // 파베에 있는 거 가져오기
            String uid=firebaseUser.getUid();
            StorageReference profileRef = storageReference.child(uid);
            Log.d("profileRef 는?1", profileRef.toString());

            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri).into(iv_profileUrl);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.d("실패했음", " ");
                }
            });

        }


        final FloatingActionButton btn_update_pwd = findViewById(R.id.btn_update_pwd);
        final FloatingActionButton btn_update_name=findViewById(R.id.btn_create_group);
        final FloatingActionButton btn_logout = findViewById(R.id.btn_logout);
        final FloatingActionButton btn_signout = findViewById(R.id.btn_signout);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃
                mFirebaseAuth.signOut();
                Toast.makeText(ProfileActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃
                mFirebaseAuth.getCurrentUser().delete();
                Toast.makeText(ProfileActivity.this, "회원 탈퇴" , Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ProfileActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateName();
            }
        });
        btn_update_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePwd();
            }
        });
    }

    private void updateName() {
//        View view = getLayoutInflater().inflate(R.layout.dialog_name_update, null);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(view).show();
//

        dialog.setContentView(R.layout.dialog_name_update);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_update_name = dialog.findViewById(R.id.btn_create_group);
        final EditText et_name = dialog.findViewById(R.id.et_name);
        ImageView iv_closeDialog = dialog.findViewById(R.id.iv_closeDialog);

        btn_update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  현재 유저 이름을 저걸로 setValue
                String EtName = et_name.getText().toString();
                FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser(); // 현재 유저

                userName.setText(EtName);

                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("name").setValue(EtName);
                Toast.makeText(ProfileActivity.this, "이름 변경!", Toast.LENGTH_SHORT).show();

                Log.d("new Name", "name : " + EtName);
                //ProfileActivity.this.recreate();

                dialog.dismiss();
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

    private void updatePwd() {
        View view = getLayoutInflater().inflate(R.layout.dialog_pwd_update, null);

//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(view).show();

        dialog.setContentView(R.layout.dialog_pwd_update);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        final EditText et_current_pwd=dialog.findViewById(R.id.et_current_pwd);
        final EditText et_new_pwd = dialog.findViewById(R.id.et_new_pwd);
        final EditText et_new_pwd_val = dialog.findViewById(R.id.et_new_pwd_val);
        Button btn_update_pwd = dialog.findViewById(R.id.btn_update_pwd);
        ImageView iv_closeDialog2 = dialog.findViewById(R.id.iv_closeDialog2);

        btn_update_pwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser(); // 현재 유저

                //  현재 유저 이름을 저걸로 setValue
                String EtCurrentPwd=et_current_pwd.getText().toString();
                String EtNewPwd=et_new_pwd.getText().toString();
                String EtNewPwdVal=et_new_pwd_val.getText().toString();
                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("password").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        }
                        else {
                            if (String.valueOf(task.getResult().getValue()).equals(EtCurrentPwd)){
                                if(EtNewPwd.equals(EtNewPwdVal)){
                                    mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("password").setValue(EtNewPwd);
                                    Toast.makeText(ProfileActivity.this, "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                    Log.d("비번 변경 됨", EtNewPwd);

                                    //LoginActivity로 가는 Intent 추가해도 되고, 안 해도 되고

                                } else {
                                    Toast.makeText(ProfileActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                                    Log.d("비번 일치 안 함", EtNewPwd+", "+EtNewPwdVal);
                                }

                            } else {
                                Toast.makeText(ProfileActivity.this, "현재 비밀번호 틀림!", Toast.LENGTH_SHORT).show();
                                Log.d("현재 비번 틀림", String.valueOf(task.getResult().getValue()) +", "+EtCurrentPwd);
                            }

                        }
                    }
                });;
            }
        });
        iv_closeDialog2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}
