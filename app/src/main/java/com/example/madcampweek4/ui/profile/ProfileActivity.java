package com.example.madcampweek4.ui.profile;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;
import com.example.madcampweek4.R;
import com.example.madcampweek4.RegisterActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    String name, email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent=getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");

        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("MadCampWeek4");

        TextView userName=findViewById(R.id.userName);
        TextView userEmail=findViewById(R.id.userEmail);

        userName.setText(name);
        userEmail.setText(email);


        ImageView iv_profileUrl=findViewById(R.id.iv_profileUrl);
        iv_profileUrl.setImageResource(R.drawable.ic_menu_profile);
        String profileUrl=intent.getStringExtra("profileUrl");
        if (profileUrl!="일반"){
            Glide.with(this).load(profileUrl).into(iv_profileUrl);
        }


        final Button btn_update_pwd = findViewById(R.id.btn_update_pwd);
        final Button btn_update_name=findViewById(R.id.btn_update_name);
        final Button btn_logout = findViewById(R.id.btn_logout);
        final Button btn_signout = findViewById(R.id.btn_signout);


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃
                mFirebaseAuth.signOut();
                Toast.makeText(ProfileActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ProfileActivity.this, com.example.madcampweek4.LoginActivity.class);
                startActivity(intent);
            }
        });
        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 로그아웃
                mFirebaseAuth.getCurrentUser().delete();
                Toast.makeText(ProfileActivity.this, "회원 탈퇴" , Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(ProfileActivity.this, com.example.madcampweek4.LoginActivity.class);
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
        View view = getLayoutInflater().inflate(R.layout.dialog_name_update, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();

        final EditText et_name = view.findViewById(R.id.et_name);
        Button btn_update_name = view.findViewById(R.id.btn_update_name);

        btn_update_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  현재 유저 이름을 저걸로 setValue
                String EtName=et_name.getText().toString();
                FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser(); // 현재 유저

                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("name").setValue(EtName);
                Toast.makeText(ProfileActivity.this, "이름 변경!", Toast.LENGTH_SHORT).show();


            }
        });


    }

    private void updatePwd() {
        View view = getLayoutInflater().inflate(R.layout.dialog_pwd_update, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();

        final EditText et_current_pwd=view.findViewById(R.id.et_current_pwd);
        final EditText et_new_pwd = view.findViewById(R.id.et_new_pwd);
        final EditText et_new_pwd_val = view.findViewById(R.id.et_new_pwd_val);
        Button btn_update_pwd = view.findViewById(R.id.btn_update_pwd);

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


    }
}
