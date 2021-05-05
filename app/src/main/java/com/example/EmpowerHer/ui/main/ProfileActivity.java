package com.example.EmpowerHer.ui.main;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.example.EmpowerHer.R;
import com.example.EmpowerHer.model.UserObject;
import com.example.EmpowerHer.ui.auth.LoginActivity;
import com.example.EmpowerHer.utils.MyUtils;
import com.example.EmpowerHer.utils.Tools;


import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";


    public static final int IMAGE_SELECT_CODE = 1001;
    @BindView(R.id.profile_pic_imageView)
    ImageView profilePicImageView;

    @BindView(R.id.profile_username)
    TextView profileUsername;
    @BindView(R.id.profile_email)
    TextView profileEmail;
    @BindView(R.id.profile_institute)
    TextView profileInistitue;
    @BindView(R.id.profile_class)
    TextView profileClass;
    @BindView(R.id.profile_stf_reg_no)
    TextView profileStd_reg_no;

    @BindView(R.id.profile_gender)
    TextView profileGender;


    @BindView(R.id.user_class_container)
    CardView userClassCV;
    @BindView(R.id.user_regno_container)
    CardView userRegNoCV;


    private FirebaseAuth auth;

    private Uri image_uri = null;

    private BottomSheetDialog bottomSheetDialog;

    String age,email,gender,id,imageURL,password,phone,username;

    private static final int STORAGE_PERMISSION_CODE = 123;

    private ImageView update_imageView;

    private AlertDialog loading_dialog;

    private StorageReference mStorageRef = FirebaseStorage.getInstance().getReference();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private EditText et_Name;
    private EditText et_Phone;
    private String s_name ;
    private String s_phone ;
    private DocumentReference blogRef;
    private Button verify;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);
        auth = FirebaseAuth.getInstance();
        loading_dialog = MyUtils.getLoadingDialog(this);
        bottomSheetDialog = new BottomSheetDialog(this);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
       // toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("User Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this,R.color.statusbarC1);

        profile();

    }


    public void onLogoutClick(View view) {
        FirebaseAuth.getInstance().signOut();
        //System.exit(1);
        startActivity(new Intent(this, LoginActivity.class));
        //CompanyTasksShowActivity.finish();
        finish();


    }

    public void onEditProfileClick(View view) {

        @SuppressLint("InflateParams") View view1 = this.getLayoutInflater().inflate(R.layout.activity_edit_profile, null);

        et_Name = view1.findViewById(R.id.et_Name);
        et_Name.setText(username);

        et_Phone = view1.findViewById(R.id.et_Phone);
        et_Phone.setText(phone);

        TextView tv_email = view1.findViewById(R.id.tv_email);
        tv_email.setText(email);

        update_imageView = view1.findViewById(R.id.update_imageView);
        update_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               ProfileActivity.this.pickImage();
            }
        });

        if(!imageURL.equals("default")){
            Glide.with(this)
                    .load(imageURL)
                    .placeholder(R.drawable.picture_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .error(R.drawable.ic_edit_dp_color)
                    .into(update_imageView);
        }


        Button button = view1.findViewById(R.id.btnSaveButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                s_name = et_Name.getText().toString();
                s_phone = et_Phone.getText().toString();

                if (TextUtils.isEmpty(s_name)) {
                    et_Name.setError("");
                    et_Name.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(s_phone)) {
                    et_Phone.setError("");
                    et_Phone.requestFocus();
                    return;
                }

                FirebaseUser firebaseUser = auth.getCurrentUser();
                String userid = firebaseUser.getUid();

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users").child(userid);

                loading_dialog.show();
                loading_dialog.setTitle("Please wait");
                loading_dialog.setCancelable(false);

                if (image_uri != null) {

                    mStorageRef.child("users/" + userid).putFile(image_uri)

                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                    Log.d(TAG, "onViewClicked: " + "photo upload");

                                    Objects.requireNonNull(Objects.requireNonNull(taskSnapshot.getMetadata()).getReference()).getDownloadUrl()

                                            .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {

                                                    Map<String, Object> blog_update = new HashMap<>();
                                                    blog_update.put("name", s_name);
                                                    blog_update.put("phone", s_phone);
                                                    blog_update.put("imageURL", uri.toString());
                                                    blog_update.put("updated_at", new Date().getTime());

                                                    rootRef.updateChildren(blog_update).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    loading_dialog.dismiss();
                                                                    bottomSheetDialog.dismiss();
                                                                    Log.d(TAG, "onSuccess: update ");
                                                                    et_Name.setText("");
                                                                    et_Phone.setText("");
                                                                    ProfileActivity.this.profile();
                                                                    Toast.makeText(ProfileActivity.this, "Update Profile Success", Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    loading_dialog.dismiss();
                                                                    if (e instanceof IOException)
                                                                        Toast.makeText(ProfileActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                                                                    else
                                                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                                                }
                                                            });

                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    loading_dialog.dismiss();
                                                    if (e instanceof IOException)
                                                        Toast.makeText(ProfileActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                                                    else
                                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                                }
                                            });
                                }
                            })

                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loading_dialog.dismiss();
                                    if (e instanceof IOException)
                                        Toast.makeText(ProfileActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                                    else
                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                }
                            });

                } else {

                    Map<String, Object> blog_update = new HashMap<>();
                    blog_update.put("name", s_name);
                    blog_update.put("phone", s_phone);
                    blog_update.put("updated_at", new Date().getTime());

                    rootRef.updateChildren(blog_update)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    loading_dialog.dismiss();
                                    bottomSheetDialog.dismiss();
                                    Log.d(TAG, "onSuccess: update ");
                                    ProfileActivity.this.profile();
                                    et_Name.setText("");
                                    et_Phone.setText("");
                                    Toast.makeText(ProfileActivity.this, "Profile Updated Successfully ", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    loading_dialog.dismiss();
                                    if (e instanceof IOException)
                                        Toast.makeText(ProfileActivity.this, "internet connection error", Toast.LENGTH_SHORT).show();
                                    else
                                        Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                                }
                            });

                }

            }
        });
        DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
        int height = displayMetrics.heightPixels;
        int maxHeight = (int) (height*0.80);


        bottomSheetDialog.setContentView(view1);
        bottomSheetDialog.show();
        bottomSheetDialog.getBehavior().setPeekHeight(maxHeight);
        bottomSheetDialog.setDismissWithAnimation(true);
        bottomSheetDialog.setCancelable(true);
    }

    private void pickImage() {
        requestStoragePermission();

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_SELECT_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_SELECT_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    //Display an error
                    Toast.makeText(this, "Unable to handle image.", Toast.LENGTH_SHORT).show();
                    image_uri = null;
                    update_imageView.setImageResource(R.drawable.defavatar);
                    return;
                }
                image_uri = data.getData();
                {
                    Log.i(TAG, "onActivityResult: setting image");
                    update_imageView.setImageURI(image_uri);


                }
            } else {
                image_uri = null;
                update_imageView.setImageResource(R.drawable.defavatar);
            }
        }
    }

    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            return;

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE))
            requestStoragePermission();

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    private void profile() {
        loading_dialog.show();

        FirebaseUser firebaseUser = auth.getCurrentUser();
        String userid = firebaseUser.getUid();
        Log.e(TAG, "userid: "+userid);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("users");

        rootRef.child(userid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    loading_dialog.dismiss();

                    try{
                        Log.e(TAG, "snapshot value : "+snapshot.getValue());

                        UserObject userObject = snapshot.getValue(UserObject.class);

                        if(!userObject.getName().isEmpty()){
                            profileUsername.setText(userObject.getName());
                            username=userObject.getName();
                        }

                        if(!userObject.getEmail().isEmpty()){
                            profileEmail.setText(userObject.getEmail());
                            email=userObject.getEmail();
                        }
                        if(!userObject.getSchool().isEmpty()){
                            profileInistitue.setText(userObject.getSchool());
                        }
                        if(!userObject.getStudentClass().isEmpty()){
                            profileClass.setText(userObject.getStudentClass());
                        }
                        else{
                            userClassCV.setVisibility(View.GONE);
                        }
                        if(!userObject.getGender().isEmpty()){
                            profileGender.setText(userObject.getGender());
                        }
                        if(!userObject.getRegNo().isEmpty()){
                            profileStd_reg_no.setText(userObject.getRegNo());
                        }
                        else{
                            userRegNoCV.setVisibility(View.GONE);
                        }

                        if(!userObject.getImageURL().isEmpty()){

                            imageURL=userObject.getImageURL();

                            if(userObject.getGender().equals("Male")){
                                Glide.with(ProfileActivity.this.getApplicationContext())
                                        .load(imageURL)
                                        .placeholder(R.drawable.picture_placeholder)
                                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                                        .error(R.drawable.ic_std_avatar_male)
                                        .into(profilePicImageView);
                            }
                            else{
                                Glide.with(ProfileActivity.this.getApplicationContext())
                                        .load(imageURL)
                                        .placeholder(R.drawable.picture_placeholder)
                                        .diskCacheStrategy(DiskCacheStrategy.DATA)
                                        .error(R.drawable.ic_std_avatar_female)
                                        .into(profilePicImageView);
                            }

                        }
                        else{

                            if(userObject.getGender().equals("Male")){
                                profilePicImageView.setImageResource(R.drawable.ic_std_avatar_male);
                            }
                            else{
                                profilePicImageView.setImageResource(R.drawable.ic_std_avatar_female);
                            }
                        }

                    }
                    catch (Exception e){
                        Log.e(TAG, "onDataChange: "+e.getLocalizedMessage() );

                    }



                } else {
                    Log.e(TAG, "onDataChange: snaphot not eits" );
                    loading_dialog.dismiss();
                    // Don't exist! Do something.
                //   Toast.makeText(requireContext(), "Error ", Toast.LENGTH_SHORT).show();

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "onCancelled: "+error.getDetails() );
                loading_dialog.dismiss();
            }

        });

    }



    public void number_verification_check() {
       // verify.setText("testing");
        try {
            auth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = auth.getCurrentUser();
            String userid = firebaseUser.getUid();

            DatabaseReference get_use_number_status;
            get_use_number_status = FirebaseDatabase.getInstance().getReference("User").child(userid);
            get_use_number_status.child("number_verified").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()){
                        long value=(long) snapshot.getValue();
                       if(value==1){
                           verify.setClickable(false);
                           verify.setText("Verified");
                           //Toast.makeText(ProfileActivity.this,"Data received: "+value,Toast.LENGTH_SHORT).show();
                       }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }

            });


        } catch (Exception e) {
            Toast.makeText(ProfileActivity.this, "try catch error: verify activity get balance", Toast.LENGTH_SHORT).show();
        }

    }


    public void on_veryfy_click(View view) {

       // startActivity(new Intent(this, VerifyPhoneActivity.class));
        //CompanyTasksShowActivity.finish();
    //    Intent intent = new Intent(ProfileActivity.this, VerifyPhoneActivity.class);
    //    intent.putExtra("mobile", phone);
    //    startActivity(intent);
     //   finish();


    }



    @Override
    public void onBackPressed() {
        //FirebaseAuth.getInstance().signOut();
        //startActivity(new Intent(ProfileActivity.this, Navigation.class));
       // startActivity(new Intent(this, CompanyTasksShowActivity.class));
        finish();
    }

}