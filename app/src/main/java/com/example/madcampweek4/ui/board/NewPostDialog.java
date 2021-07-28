package com.example.madcampweek4.ui.board;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.madcampweek4.Login;
import com.example.madcampweek4.R;
import com.example.madcampweek4.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class NewPostDialog extends Dialog {
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference, userDatabase;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String groupId,groupName;

    private  String TAG = "NewPostActivityTAG";

    ImageView iv_newPostImage;
    EditText et_newPostDescription;
    Bitmap bitmap;
    Uri uri_newPostImage;
    String userName, postId;
    Board boardItem;
    Context context;


    public NewPostDialog(@NonNull Context context, String groupId, String groupName) {
        super(context);
        this.context = context;
        this.groupId = groupId;
        this.groupName = groupName;
        setContentView(R.layout.dialog_new_post);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        database= FirebaseDatabase.getInstance(); //파이어베이스 데이터베이스 연동
        databaseReference = database.getReference("MadCampWeek4/Post/"+groupId); //realtime database

        storage = FirebaseStorage.getInstance();  //for image
        storageReference = storage.getReference();

        iv_newPostImage = findViewById(R.id.iv_newPostImage);
        et_newPostDescription = findViewById(R.id.et_newPostDescription);

        findViewById(R.id.btn_addPostImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity((Activity) context)
                        .withPermissions(Manifest.permission.CAMERA)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                showCamera(v);
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                Toast.makeText(context, "촬영이 거부되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        }).check();
            }
        });

        findViewById(R.id.btn_uploadNewPost).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertData();
                Intent intent = new Intent((Activity) context,BoardActivity.class);
                intent.putExtra("groupId",groupId);
                intent.putExtra("groupName",groupName);

                context.startActivity(intent);
                ((Activity) context).finish();
            }
        });
    }

    public void showCamera(View v){
        Log.d("카메라 성공", "제발");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        ((Activity) context).startActivityForResult(intent, 1);
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode == RESULT_OK){
            Log.d("결과 받기 성공", "제발");
            bitmap = data.getParcelableExtra("data");
            uri_newPostImage = getImageUri(context, bitmap);
            Log.d("uri로 받기 성공", "제발");
            iv_newPostImage.setImageURI(uri_newPostImage);
        }
    }

    private Uri getImageUri(Context context, Bitmap inImage) {
        Log.d("이미지 받기 성공", "제발");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    private void insertData(){
        storageReference= storage.getReference().child("Post");
        postId = databaseReference.push().getKey().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String uid = user != null ? user.getUid() : null;

        firebaseAuth = FirebaseAuth.getInstance();
        userDatabase = database.getReference("MadCampWeek4/UserAccount/"+uid);

        userDatabase.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DataSnapshot> task) {
                if(!task.isSuccessful()){
                    Toast.makeText(context, "아이디 가져오기 실패", Toast.LENGTH_SHORT).show();
                    userName="";
                }else{
                    StorageReference contentImageRef = storageReference.child("Post/post_uri/"+ postId);
                    UploadTask uploadTask = contentImageRef.putFile(uri_newPostImage);

                    @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

                    UserAccount userAccount = task.getResult().getValue(UserAccount.class);
                    userName = userAccount.getName();

                    boardItem = new Board(postId, uid, userName,userAccount.getProfileURL(),groupId, et_newPostDescription.getText().toString(), "", timeStamp, 0);

                    databaseReference.child(postId).setValue(boardItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(context.getApplicationContext(), "Post upload Success", Toast.LENGTH_SHORT).show();
                            Login.setPostNum(Login.getPostNum()+1);
                            HashMap<String,Object> todayMap = new HashMap<>();
                            todayMap.put("postNum",Login.getPostNum());
                            userDatabase.child("posts/today").updateChildren(todayMap);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(context.getApplicationContext(), "Data upload Fail", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
