package com.example.madcampweek4;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.madcampweek4.ui.profile.ProfileActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madcampweek4.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    String email, name, profileUrl;
    int point;

    private FirebaseAuth mFirebaseAuth;
    private DatabaseReference mDatabaseRef;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent=getIntent();
        email = intent.getStringExtra("email");
        name=intent.getStringExtra("name");
        profileUrl=intent.getStringExtra("profileUrl");
        point=intent.getIntExtra("points", 0);



        mFirebaseAuth= FirebaseAuth.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("MadCampWeek4");

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference().child("UserProfile");

        FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();


        // nav header 관련
        View nav_header_view=binding.navView.getHeaderView(0);
        nav_header_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                intent.putExtra("profileUrl", profileUrl);


                Toast.makeText(MainActivity.this, "헤더 누름!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        // 기본 사진
        ImageView imageView=nav_header_view.findViewById(R.id.imageView);

        if (profileUrl!=null && !profileUrl.equals("")){
            Glide.with(this).load(profileUrl).into(imageView);
        } else{
            // 파베에 있는 거 가져오기
            String uid=firebaseUser.getUid();
            StorageReference profileRef = storageReference.child(uid);

            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(getApplicationContext()).load(uri).into(imageView);
                    HashMap<String,Object> userMap = new HashMap<>();
                    userMap.put("profileURL",uri.toString());
                    mDatabaseRef.child("UserAccount/"+uid).updateChildren(userMap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    Log.d("실패했음", " ");
                }
            });

        }


        // 이메일
        TextView userEmail=(TextView)nav_header_view.findViewById(R.id.userEmail);
        TextView userName=(TextView)nav_header_view.findViewById(R.id.userName);
        userEmail.setText(email);
        userName.setText(name);

        setSupportActionBar(binding.appBarMain.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_groups, R.id.nav_search, R.id.nav_calendar, R.id.nav_aquarium)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("recent_points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                        if (!task.isSuccessful()) {
                            Log.e("firebase", "Error getting data", task.getException());
                        } else {
                            Long pp = (Long) task.getResult().getValue();
                            // Dialog
                            View dialogView = getLayoutInflater().inflate(R.layout.dialog_point, null);
                            final TextView tv_point = dialogView.findViewById(R.id.tv_point);
                            tv_point.setText("Today's Point : " + pp);
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setView(dialogView);

                            AlertDialog alertDialog = builder.create();
                            alertDialog.show();

                        }
                    }
                });
            }}, 2000);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer=(DrawerLayout)findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
            super.onBackPressed();
        }
    }

}