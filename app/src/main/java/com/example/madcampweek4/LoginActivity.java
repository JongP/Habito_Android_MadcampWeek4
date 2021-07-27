package com.example.madcampweek4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.internal.GoogleApiAvailabilityCache;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private FirebaseAuth mFirebaseAuth; // firebase auth
    private DatabaseReference mDatabaseRef; //realtime db
    private EditText mEtEmail, mEtPwd;
    private  String userId;

    private SignInButton btn_google;
    private GoogleApiClient googleApiClient;
    private static final int REQ_SIGN_GOOGLE=100;

    private String TAG ="LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar ab = getSupportActionBar();
        ab.hide();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("MadCampWeek4");

        if(user!=null){
            Log.d(TAG, "user is not null");
            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("email", user.getEmail());
            userId=user.getUid();
            updateUserToday();
            mDatabaseRef.child("UserAccount").child(userId).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {

                        intent.putExtra("name", String.valueOf(task.getResult().getValue()));
                        // 여기는 일반 사용자 사진 가져오기!
                        intent.putExtra("profileUrl", "일반");
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }


        TextView tv_register=findViewById(R.id.tv_register);
        Button btn_login = findViewById(R.id.btn_login);


        mEtEmail=findViewById(R.id.et_email);
        mEtPwd=findViewById(R.id.et_pwd);

        // 로그인 처리
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail=mEtEmail.getText().toString();
                String strPwd=mEtPwd.getText().toString();

                mFirebaseAuth.signInWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                            FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser();
                            userId=firebaseUser.getUid();
                            updateUserToday();
                            intent.putExtra("email", firebaseUser.getEmail());
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("name").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    }
                                    else {
                                        intent.putExtra("name", String.valueOf(task.getResult().getValue()));
                                        // 여기는 일반 사용자 사진 가져오기!
                                        intent.putExtra("profileUrl", "일반");
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(LoginActivity.this, "로그인 실패!", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        // 회원 가입 처리
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });


        // 구글 로그인 관련!
        GoogleSignInOptions googleSignInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient=new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                .build();

        btn_google=findViewById(R.id.btn_google);
        btn_google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                startActivityForResult(intent, REQ_SIGN_GOOGLE);//인증 화면 절차 넘기고 다시 돌아오는
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==REQ_SIGN_GOOGLE){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()){
                GoogleSignInAccount gaccount=result.getSignInAccount();
                resultGoogleLogin(gaccount);// 닉네임, 프로필사진Url, 이메일 주소 등
            }
        }
    }

    private void resultGoogleLogin(GoogleSignInAccount gaccount) {
        AuthCredential credential= GoogleAuthProvider.getCredential(gaccount.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("name", String.valueOf(gaccount.getDisplayName()));
                            intent.putExtra("email", String.valueOf(gaccount.getEmail()));
                            intent.putExtra("profileUrl", String.valueOf(String.valueOf(gaccount.getPhotoUrl())));
                            startActivity(intent);
                            finish();
                        } else{
                            Toast.makeText(LoginActivity.this, "로그인 실패!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void updateUserToday(){
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        mDatabaseRef.child("UserAccount").child(userId).child("groups").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                HashMap<String,Object> groupMap = (HashMap<String, Object>) dataSnapshot.getValue();
                if(groupMap==null || groupMap.isEmpty()){
                    Login.setGroupNum(0);
                }
                else{
                    Login.setGroupNum(groupMap.size());
                }

                mDatabaseRef.child("UserAccount").child(userId).child("posts/today").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.getValue()!=null){
                            HashMap<String,Object> map = (HashMap<String, Object>)dataSnapshot.getValue();
                            //for new day
                            if(!map.get("date").equals(timeStamp)){
                                double ratio=0;

                                long postNum= (long) map.get("postNum");
                                long groupNum=(long)map.get("groupNum");
                                String pastDate = (String) map.get("date");

                                Log.d(TAG, String.valueOf(postNum)+" "+String.valueOf(groupNum));

                                if(groupNum!=0)  {
                                    ratio = (double)postNum/groupNum;
                                    Log.d(TAG, "ratio is "+String.valueOf(ratio));
                                }
                                HashMap<String,Object> ratioMap = new HashMap<>();
                                ratioMap.put(pastDate,ratio);
                                mDatabaseRef.child("UserAccount").child(userId).child("posts/ratios").updateChildren(ratioMap);

                                Login.setPostNum(0);

                                HashMap<String,Object> updateMap = new HashMap<>();
                                updateMap.put("postNum",0);
                                updateMap.put("date",timeStamp);
                                updateMap.put("groupNum",Login.getGroupNum());
                                mDatabaseRef.child("UserAccount").child(userId).child("posts/today").updateChildren(updateMap);

                            }
                        }else{
                            //for new users
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("date",timeStamp);
                            map.put("postNum",0);
                            map.put("groupNum",0);

                            Login.setPostNum(0);
                            mDatabaseRef.child("UserAccount").child(userId).child("posts/today").setValue(map);

                        }
                    }
                });

            }
        });

    }
}