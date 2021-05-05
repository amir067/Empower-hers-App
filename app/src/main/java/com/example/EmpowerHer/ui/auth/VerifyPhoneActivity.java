package com.example.EmpowerHer.ui.auth;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.example.EmpowerHer.R;
import com.example.EmpowerHer.ui.main.HomeActivity;


import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import es.dmoral.toasty.Toasty;

public class VerifyPhoneActivity extends AppCompatActivity {

    private static final String TAG = "VerifyPhoneActivity";
    //three objects needed
    //this is the verification id that will be sent to the user
    private String mVerificationId;
    private ProgressBar progressBar;

    //ditText to input the code
   // private EditText editTextCodeP1,editTextCodeP2,editTextCodeP3,editTextCodeP4,editTextCodeP5,editTextCodeP6;

    //fireBase authentication object
    private FirebaseAuth mAuth;
    //Button btnSignIn;

    private  DatabaseReference reference;

    Context context;
    private NavController navController;
    private boolean allSet=false;
    private EditText editText1, editText2, editText3, editText4,editText5,editText6;
    private EditText[] editTexts;

    @BindView(R.id.btn_submit)
    Button submitBtn;

    @BindView(R.id.ly_code_screen)
    LinearLayout codeScreenLayout;

    @BindView(R.id.ly_save_info_screen)
    LinearLayout saveInfoLayout;

    String mobile="";

    String s_userName,s_userEmail,s_userPhone,s_userAddress,s_userPassword,s_confirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_phone);
        ButterKnife.bind(this);

        //initializing objects
        mAuth = FirebaseAuth.getInstance();

        /*editTextCodeP1 = findViewById(R.id.et_code_p1);
        editTextCodeP2 = findViewById(R.id.et_code_p2);
        editTextCodeP3 = findViewById(R.id.et_code_p3);
        editTextCodeP4 = findViewById(R.id.et_code_p4);
        editTextCodeP5 = findViewById(R.id.et_code_5);
        editTextCodeP6 = findViewById(R.id.et_code_p6);*/

        editText1 = (EditText) findViewById(R.id.et_code_p1);
        editText2 = (EditText) findViewById(R.id.et_code_p2);
        editText3 = (EditText) findViewById(R.id.et_code_p3);
        editText4 = (EditText) findViewById(R.id.et_code_p4);
        editText5 = (EditText) findViewById(R.id.et_code_p5);
        editText6 = (EditText) findViewById(R.id.et_code_p6);

        // btnSignIn = findViewById(R.id.btn_submit);
        progressBar = findViewById(R.id.progressBar_otp);

        editTexts = new EditText[]{editText1, editText2, editText3, editText4,editText5,editText6};

        editText1.addTextChangedListener(new PinTextWatcher(0));
        editText2.addTextChangedListener(new PinTextWatcher(1));
        editText3.addTextChangedListener(new PinTextWatcher(2));
        editText4.addTextChangedListener(new PinTextWatcher(3));
        editText5.addTextChangedListener(new PinTextWatcher(4));
        editText6.addTextChangedListener(new PinTextWatcher(5));

        editText1.setOnKeyListener(new PinOnKeyListener(0));
        editText2.setOnKeyListener(new PinOnKeyListener(1));
        editText3.setOnKeyListener(new PinOnKeyListener(2));
        editText4.setOnKeyListener(new PinOnKeyListener(3));
        editText5.setOnKeyListener(new PinOnKeyListener(4));
        editText6.setOnKeyListener(new PinOnKeyListener(5));



        //getting the datbundel from other activity incoming
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            //s_userName =extras.getString("name");
           // s_userEmail =extras.getString("email");
            //s_userAddress =extras.getString("address");
            mobile =extras.getString("phone");
           // s_userPassword =extras.getString("psd");
            Log.e(TAG, "onCreate: mobile received :"+mobile );

            sendVerificationCode(mobile);

        }else{
            Log.e(TAG, "onCreate: bundle :"+extras.toString() );
            Log.e(TAG, "onCreate: null bundle received!" );
        }

//        Intent intent = getIntent();
//        mobile = intent.getStringExtra("mobile");
//        Log.e(TAG, "onCreate: intent received :"+mobile );
//        //sendVerificationCode(mobile);
//        String value = mobile;
        //value = value.substring(1);


        //getting mobile number from the previous activity
        //and sending the verification code to the number

        /*if( getIntent() ==null){
            Log.e(TAG, "onCreate: intent received :"+mobile );
            //sendVerificationCode(mobile);
            //String value = mobile;
            //value = value.substring(1);
        }else{
            Intent intent = getIntent();
            mobile = intent.getStringExtra("mobile");
            Log.e(TAG, "onCreate: intent received :"+mobile );
            sendVerificationCode(mobile);
            String value = mobile;
            //value = value.substring(1);
        }*/


       // Toast.makeText(this,"Sending OTP code to: "+value,Toast.LENGTH_SHORT).show();

        //Log.e(TAG, "onCreate: sending code on:  "+mobile );


        //if the automatic sms detection did not work, user can also enter the code manually
        //so adding a click listener to the button
       /* btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String codeP1 = editText1.getText().toString().trim();
                String codeP2 = editText2.getText().toString().trim();
                String codeP3 = editText3.getText().toString().trim();
                String codeP4 = editText4.getText().toString().trim();
                String codeP5 = editText5.getText().toString().trim();
                String codeP6 = editText6.getText().toString().trim();

                String allParts=codeP1.concat(codeP2).concat(codeP3).concat(codeP4).concat(codeP5).concat(codeP6);

                if (allParts.isEmpty() || allParts.length() < 6) {
                    Toasty.error(VerifyPhoneActivity.this,"Invalid code input!",Toasty.LENGTH_SHORT).show();
                    return;
                }

                Log.e(TAG, "onClick: veriy by manul code is:"+allParts );
                //verifying the code entered manually
                verifyVerificationCode(allParts);
            }
        });*/



        }
        //the method is sending verification code
        //the country id is concatenated
        //you can take the country id as user input as well
    //dependencies --isses   implementation platform('com.google.firebase:firebase-bom:26.0.0')
        private void sendVerificationCode(String mobile) {
            String value = mobile.substring(1);

            String numberOk = "+92"+value;

            Toast.makeText(this,"Sending OTP code to: "+numberOk,Toast.LENGTH_SHORT).show();

            Log.e(TAG, "sendVerificationCode: sending on number :"+numberOk );


//            PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                    numberOk ,      //  "+92"+value         //phoneNo that is given by user
//                    60,                             //Timeout Duration
//                    TimeUnit.SECONDS,                   //Unit of Timeout
//                    this,          //Work done on main Thread
//                    mCallbacks);                       // OnVerificationStateChangedCallbacks

            PhoneAuthOptions options =
                    PhoneAuthOptions.newBuilder(mAuth)
                            .setPhoneNumber(numberOk)       // Phone number to verify
                            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                            .setActivity(this)                 // Activity (for callback binding)
                            .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                            .build();
            PhoneAuthProvider.verifyPhoneNumber(options);


        }
        //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();
            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                Log.e(TAG, "onVerificationCompleted: otp receved code: "+code );

                String[] codeArray =new String[6] ;

                for(int i=0; i < code.length(); i++) {
                    //num.charAt(i)
                    codeArray[i] = String.valueOf(code.charAt(i));
                    //codeArray[i] = String.valueOf(code.codePointAt(i));
                    //codeArray[i] = code.substring(i,6);
                }

                String codeP1 =code.substring(0,6);
                editText1.setText(codeP1);

                String codeP2 =code.substring(1,6);
                editText2.setText(codeP2);

                String codeP3 =code.substring(2,6);
                editText3.setText(codeP3);

                String codeP4 =code.substring(3,6);
                editText4.setText(codeP4);

                String codeP5 =code.substring(4,6);
                editText5.setText(codeP5);

                String codeP6 =code.substring(5,6);
                editText6.setText(codeP6);


                //verifying the code
                verifyVerificationCode(code);
                Toasty.success(VerifyPhoneActivity.this, "Success", Toast.LENGTH_LONG).show();
                update_status();
            }
        }
        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(VerifyPhoneActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            Log.e("TAG",e.getMessage() );
        }
        //when the code is generated then this method will receive the code.
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
//                super.onCodeSent(s, forceResendingToken);
            Log.e(TAG, "onCodeSent: code is sent by server to user number" );

            progressBar.setVisibility(View.VISIBLE);
            //storing the verification id that is sent to the user
            mVerificationId = s;
            Log.e(TAG, "onCodeSent: verification id: "+s );
        }
    };

    private void update_status() {

        /*try{
            mAuth = FirebaseAuth.getInstance();
            FirebaseUser firebaseUser = mAuth.getCurrentUser();
            String userid = firebaseUser.getUid();
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("User").child(userid);
            get_company_name.child("number_verified").setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(VerifyPhoneActivity.this, " Update success.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
        catch (Exception e){
            Toast.makeText(VerifyPhoneActivity.this, "try catch error: verify activity get balance", Toast.LENGTH_SHORT).show();
        }*/

    }

    @OnClick(R.id.btn_submit)
    public void SubmitClicked(){
        Log.e(TAG, "SubmitClicked: " );

        String codeP1 = editText1.getText().toString().trim();
        String codeP2 = editText2.getText().toString().trim();
        String codeP3 = editText3.getText().toString().trim();
        String codeP4 = editText4.getText().toString().trim();
        String codeP5 = editText5.getText().toString().trim();
        String codeP6 = editText6.getText().toString().trim();

        String allParts=codeP1.concat(codeP2).concat(codeP3).concat(codeP4).concat(codeP5).concat(codeP6);

        if (allParts.isEmpty() || allParts.length() < 6) {
            Toasty.error(VerifyPhoneActivity.this,"Invalid code input!",Toasty.LENGTH_SHORT).show();
            return;
        }

        Log.e(TAG, "onClick: veriy by manul code is:"+allParts );

        //verifying the code entered manually
        verifyVerificationCode(allParts);
        Log.e(TAG, "SubmitClicked: going back :"+allParts );




    }

    public void btnSubmitClicked(View v){
        Log.e(TAG, "btnSubmitClicked: ok" );

    }

    private void verifyVerificationCode(String code) {
            //creating the credential

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        //Log.e(TAG, "verifyVerificationCode: "+credential.getSmsCode() );

        //signInWithPhoneAuthCredential(credential);

        Log.e(TAG, "user code: "+code);
        Log.e(TAG, "credential.getSmsCode(): "+credential.getSmsCode());


        doSentCallBack();

        //credential.getSignInMethod()

        }

    private void doSentCallBack() {

        Intent returnIntent = new Intent();
        returnIntent.putExtra("result","ok");
        setResult(Activity.RESULT_OK,returnIntent);
        finish();


    }

    //used for signing the user
        private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(VerifyPhoneActivity.this,
                            new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //verification successful we will start the profile activity

                                Intent intent = new Intent(VerifyPhoneActivity.this, RegisterActivity.class);
                                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                //startActivity(intent);


                                doSaveUserInfo();

                                Log.e(TAG, "onComplete: line 380" );

                                VerifyPhoneActivity.this.finish();
                            } else {
                                //verification unsuccessful.. display an error message
                                String message = "Somthing is wrong, we will fix it soon...";
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    message = "Invalid code entered...";
                                }
                                Toast.makeText(VerifyPhoneActivity.this,message,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    public class PinTextWatcher implements TextWatcher {

        private int currentIndex;
        private boolean isFirst = false, isLast = false;
        private String newTypedString = "";

        PinTextWatcher(int currentIndex) {
            this.currentIndex = currentIndex;

            if (currentIndex == 0)
                this.isFirst = true;
            else if (currentIndex == editTexts.length - 1)
                this.isLast = true;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            newTypedString = s.subSequence(start, start + count).toString().trim();
        }

        @Override
        public void afterTextChanged(Editable s) {

            String text = newTypedString;

            /* Detect paste event and set first char */
            if (text.length() > 1)
                text = String.valueOf(text.charAt(0)); // TODO: We can fill out other EditTexts

            editTexts[currentIndex].removeTextChangedListener(this);
            editTexts[currentIndex].setText(text);
            editTexts[currentIndex].setSelection(text.length());
            editTexts[currentIndex].addTextChangedListener(this);

            if (text.length() == 1)
                moveToNext();
            else if (text.length() == 0)
                moveToPrevious();
        }

        private void moveToNext() {
            if (!isLast)
                editTexts[currentIndex + 1].requestFocus();

            if (isAllEditTextsFilled() && isLast) { // isLast is optional
                editTexts[currentIndex].clearFocus();
                hideKeyboard();
            }
        }

        private void moveToPrevious() {
            if (!isFirst)
                editTexts[currentIndex - 1].requestFocus();
        }

        private boolean isAllEditTextsFilled() {
            for (EditText editText : editTexts)
                if (editText.getText().toString().trim().length() == 0)
                    return false;
            return true;
        }

        private void hideKeyboard() {
            if (getCurrentFocus() != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
        }

    }

    public class PinOnKeyListener implements View.OnKeyListener {

        private int currentIndex;

        PinOnKeyListener(int currentIndex) {
            this.currentIndex = currentIndex;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_DEL && event.getAction() == KeyEvent.ACTION_DOWN) {
                if (editTexts[currentIndex].getText().toString().isEmpty() && currentIndex != 0)
                    editTexts[currentIndex - 1].requestFocus();
            }
            return false;
        }

    }



    public void doSaveUserInfo(){

        Toasty.success(VerifyPhoneActivity.this,"Sign up Successfully ",Toast.LENGTH_SHORT).show();

        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        int random = new Random().nextInt(400000) + 1000; // [0, 60] + 20 => [20, 80]

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(userid);

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id",userid);
        hashMap.put("name", s_userName);
        hashMap.put("email", s_userEmail);
        hashMap.put("address", s_userAddress);
        hashMap.put("phone", s_userPhone);
        hashMap.put("psd", s_userPassword);
        hashMap.put("imageURL", "default");

        reference.setValue(hashMap).addOnCompleteListener((Task<Void> task1) -> {
            if (task1.isSuccessful()) {
                progressBar.setVisibility(View.GONE);
                Log.d(TAG, "user created and Real time database updated");
                Toasty.success(this,"Register Success",Toasty.LENGTH_SHORT).show();

                startActivity(new Intent(VerifyPhoneActivity.this, HomeActivity.class));
                VerifyPhoneActivity.this.finish();

            }
            else {
                Log.e(TAG, "doSaveUserInfo: error:"+task1.getException().getCause() );
                Log.e(TAG, "doSaveUserInfo: error message:"+task1.getException().getCause().getMessage() );
                Log.e(TAG, "doSaveUserInfo: result:"+task1.getResult() );

            }
        });


    }









}
