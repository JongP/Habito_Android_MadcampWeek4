package com.example.madcampweek4;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.facebook.FacebookSdk;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private FirebaseAuth mFirebaseAuth; // firebase auth
    private DatabaseReference mDatabaseRef; //realtime db
    private EditText mEtEmail, mEtPwd;
    private  String userId;

    private SignInButton btn_google;
    private LoginButton btn_facebook;
    private GoogleApiClient googleApiClient;
    private static final int REQ_SIGN_GOOGLE=100;

    private CallbackManager mCallbackManager;
    int point;

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
            mDatabaseRef.child("UserAccount").child(userId).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (!task.isSuccessful()) {
                        Log.e("firebase", "Error getting data", task.getException());
                    }
                    else {
                        UserAccount userAccount = task.getResult().getValue(UserAccount.class);
                        intent.putExtra("name", userAccount.getName());
                        // 여기는 일반 사용자 사진 가져오기!
                        intent.putExtra("profileUrl", userAccount.getProfileURL());
                        intent.putExtra("points", userAccount.getPoints());
                        Login.setPoints(userAccount.getPoints());
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
                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DataSnapshot> task) {
                                    if (!task.isSuccessful()) {
                                        Log.e("firebase", "Error getting data", task.getException());
                                    }
                                    else {
                                        UserAccount userAccount = task.getResult().getValue(UserAccount.class);
                                        intent.putExtra("name", userAccount.getName());
                                        // 여기는 일반 사용자 사진 가져오기!
                                        intent.putExtra("profileUrl", userAccount.getProfileURL());
                                        intent.putExtra("points", point);
                                        Login.setPoints(userAccount.getPoints());
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


        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager=CallbackManager.Factory.create();

        btn_facebook=(LoginButton)findViewById(R.id.btn_facebook);
        btn_facebook.setReadPermissions("email", "public_profile");

        // Callback registration
        btn_facebook.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "facebook:onError", exception);
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
        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void resultGoogleLogin(GoogleSignInAccount gaccount) {
        AuthCredential credential= GoogleAuthProvider.getCredential(gaccount.getIdToken(), null);


        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();

                            FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser(); // 현재 유저

                            Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("name", String.valueOf(gaccount.getDisplayName()));
                            intent.putExtra("email", String.valueOf(gaccount.getEmail()));
                            intent.putExtra("profileUrl", String.valueOf(String.valueOf(gaccount.getPhotoUrl())));

                            //google id로 이것저것 할 수 있긴 한데, 로그인할 때마다 생기는 단점이 있음
//                            UserAccount account=new UserAccount();
//                            account.setIdToken(firebaseUser.getUid());
//                            account.setEmailId(gaccount.getEmail());
//                            account.setName(gaccount.getDisplayName());
//                            account.setProfileURL("");
//
//                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);


                            startActivity(intent);
                            finish();
                        } else{
                            Toast.makeText(LoginActivity.this, "로그인 실패!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            Log.d("페이스북 로그인 됐니? ", user.getEmail());
                            getProfile();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void getProfile() {
        try {
            AccessToken accessToken = AccessToken.getCurrentAccessToken();
            GraphRequest request = GraphRequest.newMeRequest(
                    accessToken,
                    new GraphRequest.GraphJSONObjectCallback() {
                        private String FBName, FBEmail, FBUUID;
                        FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();

                        @Override
                        public void onCompleted(
                                JSONObject object,
                                GraphResponse response) {
                            try {
                                String[] splitStr = object.getString("name").split("\\s+");
                                FBName = splitStr[0];
                                FBEmail = object.getString("email");
                                FBUUID = object.getString("id");

                                Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("name", FBName);//FBUUID);
                                intent.putExtra("email", FBEmail);
                                intent.putExtra("profileUrl", "페이스북");
                                Log.d("페이스북 정보 : ", FBName +" "+FBEmail);

                                //facebook id로 이것저것 할 수 있긴 한데, 로그인할 때마다 생기는 단점이 있음
//                                UserAccount account=new UserAccount();
//                                account.setIdToken(firebaseUser.getUid());
//                                account.setEmailId(FBEmail);
//                                account.setName(FBName);
//                                account.setProfileURL("");
//
//                                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);

                                startActivity(intent);
                                finish();

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
            Bundle parameters = new Bundle();
            parameters.putString("fields", "id,name,link,birthday,gender,email");
            request.setParameters(parameters);
            request.executeAsync();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void updateUserToday(){
        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        //update user fish
        mDatabaseRef.child("UserAccount").child(userId).child("fish").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    Fish.setOwn( (ArrayList<Boolean>) dataSnapshot.getValue());
                    //Log.d(TAG, dataSnapshot.getValue().toString());
                    //Log.d(TAG, Fish.getOwn().toString());
                } else//if user is first time to log in
                {
                    Log.d(TAG, "fish data null");
                    ArrayList<Boolean> arrayList=new ArrayList<>();
                    for(int i=0;i<Fish.getMaxFish();i++){
                        arrayList.add(false);
                    }
                    mDatabaseRef.child("UserAccount").child(userId).child("fish").setValue(arrayList);
                }
            }

        });
        //update user display_fish
        mDatabaseRef.child("UserAccount").child(userId).child("display_fish").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    Display_Fish.setOwn((ArrayList<Boolean>) dataSnapshot.getValue());
                    //Log.d(TAG, dataSnapshot.getValue().toString());
                    //Log.d(TAG, Fish.getOwn().toString());
                } else//if user is first time to log in
                {
                    Log.d(TAG, "fish data null");
                    ArrayList<Boolean> arrayList=new ArrayList<>();
                    for(int i=0;i<Display_Fish.getMaxFish();i++){
                        arrayList.add(true);
                    }
                    mDatabaseRef.child("UserAccount").child(userId).child("display_fish").setValue(arrayList);
                }
            }

        });

        //update user today
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
                            //for new day  when date is different
                            if(map!=null&&map.get("date")!=null&&!map.get("date").equals(timeStamp)){
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
                                updateMap.put("toasted",0);
                                mDatabaseRef.child("UserAccount").child(userId).child("posts/today").updateChildren(updateMap);

                                point= (int) (Math.floor(Math.sqrt(groupNum*postNum)*10));
                                final int[] pastpoints = {0};
                                Log.d("오늘의 포인트",""+point+"점");

                                FirebaseUser firebaseUser = mFirebaseAuth.getCurrentUser();
                                mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).child("points").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                                    @RequiresApi(api = Build.VERSION_CODES.N)
                                    @Override
                                    public void onComplete(@NonNull Task<DataSnapshot> task) {
                                        if (!task.isSuccessful()) {
                                            Log.e("firebase", "Error getting data", task.getException());
                                        }
                                        else {
                                            Long pp=(Long) task.getResult().getValue();
                                            pastpoints[0] =Math.toIntExact(pp);

                                            int total=point+pastpoints[0];
                                            mDatabaseRef.child("UserAccount").child(userId).child("points").setValue(total);

                                            Login.setPoints(total);
                                            Log.d(TAG, "setPoints toal: "+String.valueOf(total));
                                            mDatabaseRef.child("UserAccount").child(userId).child("recent_points").setValue(point);

                                        }
                                    }
                                });

                            }
                        }else{
                            //for new users
                            HashMap<String,Object> map = new HashMap<>();
                            map.put("date",timeStamp);
                            map.put("postNum",0);
                            map.put("groupNum",0);
                            map.put("toasted",0);
                            Login.setPostNum(0);
                            mDatabaseRef.child("UserAccount").child(userId).child("posts/today").setValue(map);
                        }
                    }
                });

            }
        });

    }
}