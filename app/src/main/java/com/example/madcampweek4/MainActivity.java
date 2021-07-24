package com.example.madcampweek4;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.madcampweek4.ui.board.BoardFragment;
import com.example.madcampweek4.ui.board.NewPostFragment;
import com.example.madcampweek4.ui.profile.ProfileActivity;
import com.google.android.material.navigation.NavigationView;

import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.madcampweek4.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private FragmentManager fm;
    private FragmentTransaction ft;
    String email, name, profileUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Intent intent=getIntent();
        email = intent.getStringExtra("email");
        name=intent.getStringExtra("name");
        profileUrl=intent.getStringExtra("profileUrl");

        //디버그 전용 주석
        ////////////////////// 여기에 이메일, 아이디 넣으세여 ///////////////////
//        Login login = new Login("id4", "id4@gmail.com");
//        name=login.getName();
//        email=login.getEmail();


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        // nav header 관련
        View nav_header_view=binding.navView.getHeaderView(0);
        nav_header_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this, ProfileActivity.class);
                intent.putExtra("email", email);
                intent.putExtra("name", name);
                if (profileUrl!="일반"){
                    intent.putExtra("profileUrl", profileUrl);
                } else{
                    intent.putExtra("profileUrl", "일반");
                }

                Toast.makeText(MainActivity.this, "헤더 누름!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });

        // 기본 사진
        ImageView imageView=nav_header_view.findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.ic_menu_profile);
        if (profileUrl!="일반"){
            Glide.with(this).load(profileUrl).into(imageView);
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
                R.id.nav_groups, R.id.nav_search, R.id.nav_calendar)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
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

    public void setFragment(int n){
        fm = getSupportFragmentManager();
        ft= fm.beginTransaction();
        switch (n){
            //from Group to Board
            case 0:
                ft.replace(R.id.nav_host_fragment_content_main, new BoardFragment());
                ft.commit();
                break;
            //from Board to NewPost
            case 1:
                ft.replace(R.id.nav_host_fragment_content_main, new NewPostFragment());
                ft.commit();
                break;
        }
    }
}