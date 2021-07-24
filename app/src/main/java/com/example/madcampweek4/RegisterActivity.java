package com.example.madcampweek4;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.madcampweek4.databinding.ActivityMainBinding;
import com.example.madcampweek4.databinding.ContentMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class RegisterActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth; // firebase auth
    private DatabaseReference mDatabaseRef; //realtime db
    private FirebaseStorage storage; //storage
    private StorageReference storageReference;

    private EditText mEtEmail, mEtPwd, mEtName;
    private Button mBtnRegister;
    private ImageView img_profile;
    public Uri imageUri;

    String strEmail, strPwd, strName;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();


        mFirebaseAuth=FirebaseAuth.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("MadCampWeek4");

        mEtEmail=findViewById(R.id.et_email);
        mEtPwd=findViewById(R.id.et_pwd);
        mEtName=findViewById(R.id.et_name);
        mBtnRegister=findViewById(R.id.btn_register);
        img_profile=findViewById(R.id.img_profile);

        img_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseImage();
            }
        });


        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strEmail=mEtEmail.getText().toString();
                strPwd=mEtPwd.getText().toString();
                strName=mEtName.getText().toString();

                mFirebaseAuth.createUserWithEmailAndPassword(strEmail, strPwd).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser=mFirebaseAuth.getCurrentUser(); // 현재 유저
                            
                            // 프로필 사진 저장
                            StorageReference mStorageRef=storageReference.child("UserProfile/"+firebaseUser.getUid());
                            //UploadTask uploadTask = mStorageRef.putFile(imageUri);
                            mStorageRef.putFile(imageUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            Toast.makeText(RegisterActivity.this, "이미지 업로드 성공", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                            
                            // 프로필 저장
                            UserAccount account=new UserAccount();
                            account.setIdToken(firebaseUser.getUid());
                            account.setEmailId(firebaseUser.getEmail());
                            account.setName(strName);
                            account.setPassword(strPwd);// ㄱㅊ

                            mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).setValue(account);


                            Toast.makeText(RegisterActivity.this, "회원가입 성공!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                        } else{
                            Toast.makeText(RegisterActivity.this, "회원가입 실패!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void choseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==1 && resultCode==RESULT_OK && data.getData()!=null){
            imageUri=data.getData();
            img_profile.setImageURI(imageUri);

        }
    }

    // 확장자 찾아줌
    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}