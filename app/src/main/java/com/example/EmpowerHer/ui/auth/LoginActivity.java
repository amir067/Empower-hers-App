package com.example.EmpowerHer.ui.auth;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;
import com.example.EmpowerHer.R;
import com.example.EmpowerHer.ui.main.HomeActivity;
import com.example.EmpowerHer.utils.MyUtils;
import com.example.EmpowerHer.utils.Tools;

public class LoginActivity extends AppCompatActivity {
    int backpress = 0;
    FirebaseUser user;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    EditText  email, password;
    Button btn_login,btnforgetpsd;
    //ImageView ani_loading;
    public   static  String TAG="MainActivity";
    private AlertDialog loading_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_new);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        email = findViewById(R.id.et_email);
        password = findViewById(R.id.et_password);
        //ani_loading= findViewById(R.id@color/ThemeColorOne.anim_loading_gif);
        Window window = LoginActivity.this.getWindow();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Tools.setSystemBarTransparent(this);
        Tools.setSystemBarLight(this);
        loading_dialog = MyUtils.getLoadingDialog(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        updateUI(user);
    }


    @Override
    public void onResume(){
        super.onResume();
        user = mAuth.getCurrentUser();
        updateUI(user);
    }

    public void reset_psd(View v){
        startActivity(new Intent(LoginActivity.this, com.example.EmpowerHer.ui.auth.ResetPsd.class));
    }
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        } else
        {
            SharedPreferences user_type = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE);
            String user_typ = user_type.getString("user_type", "");
            if(user_typ.equals("old")){
                Toasty.info(this, "login to continue", Toast.LENGTH_SHORT).show();
             }else{
               // startActivity(new Intent(LoginActivity.this, WalkThrowActivity.class));
            }
        }
    }

    @Override
    public void onBackPressed() {
        backpress = (backpress + 1);
        Toasty.info(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        if (backpress > 1) {
            this.finish();
        }
    }

    public void onLogin(View view) {
        loading_dialog.show();
       // startActivity(new Intent(MainActivity.this, LoginActivity.class));
        String txt_email = email.getText().toString();
        String txt_password = password.getText().toString();

        if (TextUtils.isEmpty(txt_email) || !MyUtils.isValidEmail(txt_email)) {
            loading_dialog.dismiss();
            email.setError("Enter Email!");
            email.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(txt_password) ) {
            loading_dialog.dismiss();
            password.setError("Enter password!");
            password.requestFocus();
            return;
        }

        MyUtils.hideKeyboard(this);
             //ani_loading.setVisibility(View.VISIBLE);
            mAuth.signInWithEmailAndPassword(txt_email, txt_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        loading_dialog.dismiss();
                        Toasty.success(LoginActivity.this, "Authentication Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        LoginActivity.this.finish();

                    } else {
                        loading_dialog.dismiss();
                        Toasty.error(LoginActivity.this, "Authentication Failed "+task.getException(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "Error:" + task.getException().getMessage());
                        //ani_loading.setVisibility(View.INVISIBLE);
                    }

                }
            });
    }

    public void onRegister(View view) {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
       finish();
    }
}