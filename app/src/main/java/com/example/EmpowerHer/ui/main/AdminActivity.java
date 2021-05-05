package com.example.EmpowerHer.ui.main;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.EmpowerHer.R;
import com.example.EmpowerHer.utils.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import es.dmoral.toasty.Toasty;

public class AdminActivity extends AppCompatActivity {
    @BindView(R.id.user_name)
    TextView user_name;

    @BindView(R.id.total_users)
    TextView totallUsers;

    @BindView(R.id.approved_users)
    TextView approveUser;

    @BindView(R.id.maincard)
    CardView main_card;

    private AlertDialog loading_dialog;
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference user_payment;

    private long time,amount,transion_id;
    private FirebaseAuth mAuth;
    private FirebaseUser user;

    private RecyclerView recycle_transactions;
    private RecyclerView.LayoutManager layoutManager;

   // private FirebaseRecyclerAdapter<UserRequestModel, UserRequestViewHolder> adapter;

    private long update_date;
    //private UserModel usermodel;
    String user_phone_number;
    long current_balance;
    private static final String TAG = "AdminActivity";

   private DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
         ButterKnife.bind(this);

        user_name = findViewById(R.id.user_name);
        totallUsers = findViewById(R.id.total_users);
        approveUser = findViewById(R.id.approved_users);

        recycle_transactions = findViewById(R.id.recycle_requests);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Admin Panel");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // Tools.setSystemBarColor(this);
        Tools.setSystemBarColor(this,R.color.statusbarC1);
       // String userid = firebaseUser.getUid();

        mAuth = FirebaseAuth.getInstance();

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        alphaAnimation.setDuration(3000);
        user_name.startAnimation(alphaAnimation);



       // String userid = firebaseUser.getUid();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        //Toast.makeText(PaymentActivity.this,"Current User Id: "+userid,Toast.LENGTH_SHORT).show();

        user_payment = database.getReference("User");
        //loading_dialog = MyUtils.getLoadingDialog(this);


        recycle_transactions.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycle_transactions.setLayoutManager(layoutManager);

        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("User").child(userid);
            get_company_name.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                    String u_name = (String) snapshot.getValue();
                    //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+snapshot, Toast.LENGTH_SHORT).show();
                    if (u_name!=null) {
                        //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+companyname, Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(CompanyTasksShowActivity.this, firsttimeprofile.class));
                        //company_name_show.setText(value);
                        user_name.setText(u_name);
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Toast.makeText(AdminActivity.this, "try catch error: Admin activity", Toast.LENGTH_SHORT).show();
        }



        LoadAnalitysic();
       // Loaddata();
    }

    private void LoadAnalitysic() {
        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("User");
            get_company_name.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                    //String u_name = (String) snapshot.getValue();
                    //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+snapshot, Toast.LENGTH_SHORT).show();
                    if (snapshot.exists()) {
                        //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+companyname, Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(CompanyTasksShowActivity.this, firsttimeprofile.class));
                        //company_name_show.setText(value);
                        totallUsers.setText(""+snapshot.getChildrenCount());
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Toast.makeText(AdminActivity.this, "try catch error: Admin activity", Toast.LENGTH_SHORT).show();
        }
        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("User");
            get_company_name.orderByChild("reqStatus").equalTo("approved").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."
                    //String u_name = (String) snapshot.getValue();
                    //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+snapshot, Toast.LENGTH_SHORT).show();
                    if (snapshot.exists()) {
                        //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+companyname, Toast.LENGTH_SHORT).show();
                        // startActivity(new Intent(CompanyTasksShowActivity.this, firsttimeprofile.class));
                        //company_name_show.setText(value);
                        approveUser.setText(""+snapshot.getChildrenCount());
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Toast.makeText(AdminActivity.this, "try catch error: Admin activity", Toast.LENGTH_SHORT).show();
        }





    }


    private void dissApproveItem(String key) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("User").child(key);
            get_company_name.child("reqStatus").addListenerForSingleValueEvent(new ValueEventListener() {
                String reqStatus;
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    reqStatus =(String) snapshot.getValue();

                    if(snapshot.exists()){
                        snapshot.getRef().setValue("Disapproved");
                        Toasty.success(AdminActivity.this,"User Disapproved",Toasty.LENGTH_SHORT).show();

                    }else{
                        Toasty.error(AdminActivity.this,"Error While Disapproved User",Toasty.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Log.e(TAG, "approveItem: "+e.getLocalizedMessage());

        }
    }

    private void approveItem(String userid ) {

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("User").child(userid);
            get_company_name.child("reqStatus").addListenerForSingleValueEvent(new ValueEventListener() {
                String reqStatus;
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    reqStatus =(String) snapshot.getValue();

                    if(snapshot.exists()){
                        snapshot.getRef().setValue("approved");
                       // Toasty.success(com.mcs.HelloMechanic.ui.main.AdminActivity.this,"User Approved",Toasty.LENGTH_SHORT).show();

                    }else{
                       // Toasty.error(com.mcs.HelloMechanic.ui.main.AdminActivity.this,"Error While Approving User",Toasty.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Log.e(TAG, "approveItem: "+e.getLocalizedMessage());

        }

    }


    public void  balanct_setzero( ){
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("User").child(userid);
            get_company_name.child("user_balance").addListenerForSingleValueEvent(new ValueEventListener() {
                long value;
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //value =(long) snapshot.getValue();
                    value = 0;
                    snapshot.getRef().setValue(value);
                    //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+snapshot, Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
          //  Toast.makeText(PaymentActivity.this, "try catch error: balance  set zero", Toast.LENGTH_SHORT).show();
        }


    }


    public void showAlertDialog(View view) {
        ButterKnife.bind(this);


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Payment Request ?");
        builder.setPositiveButton("Confirm", (dialogInterface, i) -> {
            Snackbar.make(view, "Confirm clicked", Snackbar.LENGTH_SHORT).show();


        });
        builder.setNegativeButton("Cancel", null);
        builder.show();



    }

    public void showCustomDialog(View view) {
        ButterKnife.bind(this);
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // before
       // dialog.setContentView(R.layout.dialog_info);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;



        dialog.show();
        dialog.getWindow().setAttributes(lp);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //go to next activity
                dialog.dismiss();
            }
        }, 5000); // for 3 second
    }
}