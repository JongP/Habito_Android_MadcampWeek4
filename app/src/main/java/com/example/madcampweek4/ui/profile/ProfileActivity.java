package com.example.madcampweek4.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.example.madcampweek4.R;
import com.google.firebase.auth.FirebaseAuth;


public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mFirebaseAuth;
    String name, email;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent=getIntent();
        name = intent.getStringExtra("name");
        email = intent.getStringExtra("email");

        mFirebaseAuth=FirebaseAuth.getInstance();

        TextView userName=findViewById(R.id.userName);
        TextView userEmail=findViewById(R.id.userEmail);

        userName.setText(name);
        userEmail.setText(email);

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

    }
}
