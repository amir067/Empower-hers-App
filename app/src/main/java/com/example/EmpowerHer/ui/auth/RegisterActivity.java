package com.example.EmpowerHer.ui.auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;
import com.example.EmpowerHer.R;
import com.example.EmpowerHer.ui.main.HomeActivity;
import com.example.EmpowerHer.utils.MyUtils;
import com.example.EmpowerHer.utils.Tools;

import java.util.HashMap;
import java.util.Random;

import static com.example.EmpowerHer.utils.MyUtils.isValidEmail;


//import static my.personal.psychiatrist.ui.auth.LoginActivity.isValidEmail;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private static final int REQUEST_SIGNUP = 0;

    private FirebaseAuth auth;


    @BindView(R.id.iv_back)
    ImageView backIcon;

    @BindView(R.id.r_username)
    EditText rUsername;

    @BindView(R.id.r_email)
    EditText rEmail;

    @BindView(R.id.r_address)
    EditText rAddress;

    @BindView(R.id.r_phone_number)
    EditText rPhone;

    @BindView(R.id.r_password)
    EditText rPassword;

    @BindView(R.id.r_confirm_password)
    EditText rConfirmPassword;

    //Database
    String s_userName,s_userEmail,s_userPhone,s_userAddress,s_userPassword,s_confirmPassword;

    //Firebase
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference blogsRef = db.collection("users");

    int LAUNCH_REGISTER_ACTIVITY = 1;


    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("message");



    private AlertDialog loading_dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new);
        ButterKnife.bind(this);
        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(this);
        Tools.setSystemBarLight(this);
        loading_dialog = MyUtils.getLoadingDialog(this);


        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.setValue("Hello, World!");
                //RegisterActivity.this.finish();
            }
        });

    }

    public void onRegisterClick(View view) {
       // loading_dialog.show();
         s_userName = rUsername.getText().toString();
         s_userEmail = rEmail.getText().toString();
         s_userAddress = rAddress.getText().toString();
         s_userPhone = rPhone.getText().toString();
         s_userPassword = rPassword.getText().toString();
         s_confirmPassword = rConfirmPassword.getText().toString();

       // loading.setVisibility(View.INVISIBLE);

        if (TextUtils.isEmpty(s_userName)) {
            rUsername.setError("Enter you Name");
            rUsername.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(s_userEmail) || !isValidEmail(s_userEmail)) {
            rEmail.setError("Please Enter a Valid Email");
            rEmail.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(s_userAddress)) {
            rAddress.setError("Enter you Address");
            rAddress.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(s_userPassword) || s_userPassword.length() < 6) {
            Log.e(TAG, "onRegisterClick:userPassword "+s_userPassword );
            rPassword.setError("Enter you Password");
            rPassword.requestFocus();
            return;
        }

        if (!s_userPassword.equals(s_confirmPassword)) {
            rConfirmPassword.setError("Password not match!");
            rConfirmPassword.requestFocus();
            return;
        }

        if (TextUtils.isEmpty(s_userPhone)) {
            rPhone.setError("Enter your Number");
            rPhone.requestFocus();
            return;
        }


        //doVerifyPhoneNumber(s_userPhone);
        //loading_dialog.show();

        //Intent newIntent =new Intent(RegisterActivity.this,PhoneAuthActivity.class);
        Intent intent=new Intent(RegisterActivity.this,VerifyPhoneActivity.class);

        Bundle bundle = new Bundle();
        bundle.putString("name",s_userName);
        bundle.putString("email",s_userEmail);
        bundle.putString("address",s_userAddress);
        bundle.putString("phone",s_userPhone);
        bundle.putString("psd",s_userPassword);

        intent.putExtras(bundle);

       // startActivity(new Intent(RegisterActivity.this,VerifyPhoneActivity.class).putExtras(bundle));

        //Intent intent = new Intent(getApplicationContext(), VerifyPhoneActivity.class).putExtra("mobile", s_userPhone);
       // startActivity(intent);



        Intent i = new Intent(this, VerifyPhoneActivity.class);
        i.putExtra("phone",s_userPhone);
        startActivityForResult(i, LAUNCH_REGISTER_ACTIVITY);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == LAUNCH_REGISTER_ACTIVITY) {
            if(resultCode == Activity.RESULT_OK){
                Log.e(TAG, "onActivityResult: RESULT_OK" );

                String result=data.getStringExtra("result");

                if(result.equals("ok")){

                    doSignUp();
                }else{
                    Log.e(TAG, "onActivityResult: result was not ok" );
                }

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
                Log.e(TAG, "onActivityResult: RESULT_CANCELED" );
            }
        }
    }//onActivityResult



    public void doSignUp() {
        loading_dialog.show();

        auth.createUserWithEmailAndPassword(s_userEmail, s_userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    loading_dialog.dismiss();
                    Log.e(TAG, "doSignUp: sign in with email psd success");

                    Toasty.success(RegisterActivity.this, "Register Successfully", Toasty.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, HomeActivity.class));

                    doSaveRegistration();
                } else {

                    loading_dialog.dismiss();
                    Log.d(TAG, "onComplete: " + task.getException().getMessage());
                    Toasty.error(RegisterActivity.this, "You Can't register with this email /E-mail already register", Toast.LENGTH_SHORT).show();
                    // loading.setVisibility(View.INVISIBLE);
                }
            }

        });

    }

        private void doVerifyPhoneNumber (String phone){

            Intent intent = new Intent(RegisterActivity.this, VerifyPhoneActivity.class);
            intent.putExtra("mobile", phone);
            startActivity(intent);
            finish();

        }

        public void onBackPressed (View view){
            //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }

        @Override
        public void onBackPressed () {
            //startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            finish();
        }


        public void doSaveRegistration () {

            FirebaseUser firebaseUser = auth.getCurrentUser();
            String userid = firebaseUser.getUid();

            int random = new Random().nextInt(400000) + 1000; // [0, 60] + 20 => [20, 80]

            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("users").child(userid);

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("id", userid);
            hashMap.put("name", s_userName);
            hashMap.put("email", s_userEmail);
            hashMap.put("address", s_userAddress);
            hashMap.put("phone", s_userPhone);
            hashMap.put("psd", s_userPassword);
            hashMap.put("imageURL", "default");

            reference1.setValue(hashMap).addOnCompleteListener(task1 -> {
                if (task1.isSuccessful()) {
                    loading_dialog.dismiss();
                    Log.d(TAG, "user created and Real time database updated");

                    finish();

                } else {
                    Log.e(TAG, "doSignUp: task failed: " + task1.getResult());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    loading_dialog.dismiss();
                    Log.e(TAG, "onFailure: " + e.getMessage());
                }
            });




    }



    }