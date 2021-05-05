package com.example.EmpowerHer.ui.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.EmpowerHer.ui.Complains.ComplainActivity;
import com.example.EmpowerHer.ui.Donation.DonationActivity;
import com.example.EmpowerHer.ui.Education.EducationActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.EmpowerHer.R;
import com.example.EmpowerHer.model.UserModel;

import com.example.EmpowerHer.ui.auth.LoginActivity;
import com.example.EmpowerHer.utils.PreferenceHelperDemo;
import com.example.EmpowerHer.utils.Tools;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    int backpress = 0;
    PreferenceHelperDemo preferenceHelperDemo;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "HomeActivity";
    private CardView card1,card2,card3,card4,card5,card6,card7,card8;
    private TextView user_name;
    private String usr_name,imageURL;
    private ImageView user_dp;
    private Button logOutBtn;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private  Boolean isAdmin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferenceHelperDemo = new PreferenceHelperDemo(this);

        Log.e(TAG, "onCreate: "+System.currentTimeMillis() );

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Home");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this,R.color.statusbarC1);



        //getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        //Tools.setSystemBarTransparent(this);


        AlphaAnimation alphaAnimation = new AlphaAnimation(0.5f, 1.0f);
        alphaAnimation.setDuration(500);


        AlphaAnimation dp_alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        dp_alphaAnimation.setDuration(2500);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String userid = firebaseUser.getUid();
        try{
            DatabaseReference get_company_name;
            get_company_name = FirebaseDatabase.getInstance().getReference("users").child(userid);
            get_company_name.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //System.out.println(snapshot.getValue());  //prints "Do you have data? You'll love Firebase."

                    //Toast.makeText(CompanyTasksShowActivity.this, "complete name: "+snapshot, Toast.LENGTH_SHORT).show();
                    if (snapshot.exists()) {
                        //String u_name = (String) snapshot.getValue();
                        Log.e(TAG, "onDataChange: SuperAdmin :" +snapshot.child("isAdmin").exists());

                        if(snapshot.child("isAdmin").exists()){

                            if((Boolean) snapshot.child("isAdmin").getValue()){
                                isAdmin = true;
                            }

                        }

                    }

                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
        catch (Exception e){
            Toast.makeText(HomeActivity.this, "try catch error: Admin activity", Toast.LENGTH_SHORT).show();
        }




        //profile();
    }
    @Override
    public void onBackPressed() {
        backpress = (backpress + 1);
        Toast.makeText(getApplicationContext(), " Press Back again to Exit ", Toast.LENGTH_SHORT).show();
        if (backpress > 1) {
            this.finish();
        }
    }
    private void intent() {
        //startActivity(new Intent(this,StatementActivity.class));
    }
    private void profile() {
        DocumentReference docRef = db.collection("users").document(FirebaseAuth.getInstance().getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(documentSnapshot -> {
            if (!documentSnapshot.exists()) {
                Log.d(TAG, "onSuccess: LIST EMPTY");
                return;
            }
            UserModel types = documentSnapshot.toObject(UserModel.class);
            String gender = types.getGender();
            usr_name= types.getUsername();
            user_name.setText(usr_name);
            imageURL = types.getImageURL();
            Glide.with(getApplicationContext())
                    .load(imageURL)
                    .placeholder(R.drawable.picture_placeholder)
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .error(R.drawable.app_icon_ic_doctor_with_nobg)
                    .into(user_dp);
            preferenceHelperDemo.setKey("gender", gender);
        }).addOnFailureListener(e -> {
            Log.e(TAG, "onFailure: " + e.getLocalizedMessage());
            Toast.makeText(HomeActivity.this, "error", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_donation) {
            Intent profile = new Intent(HomeActivity.this, DonationActivity.class);
            startActivity(profile);
            //startActivity(new Intent(this, PaymentActivity.class));
            // Handle the camera action

        }if (id == R.id.nav_education) {
            Intent profile = new Intent(HomeActivity.this, EducationActivity.class);
            startActivity(profile);
            //startActivity(new Intent(this, PaymentActivity.class));
            // Handle the camera action

        }if (id == R.id.nav_complains) {
            Intent profile = new Intent(HomeActivity.this, ComplainActivity.class);
            startActivity(profile);
            //startActivity(new Intent(this, PaymentActivity.class));
            // Handle the camera action

        }

        if (id == R.id.profile) {
            Intent profile = new Intent(HomeActivity.this,ProfileActivity.class);
            startActivity(profile);
            //startActivity(new Intent(this, PaymentActivity.class));
            // Handle the camera action

        }
        else if (id == R.id.sharelink) {

            startActivity(Intent.createChooser(new Intent(Intent.ACTION_SEND,
                    Uri.parse("http://www.google.com")),"Invite Friend .."));// share url is on your own

            //Intent profile = new Intent(HomeActivity.this,ProfileActivity.class);
            //startActivity(profile);
            //startActivity(new Intent(this, PaymentActivity.class));
            // Handle the camera action

        }

        else if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            //FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();

            ///Intent about = new Intent(Home.this,About.class);
            //startActivity(about);

        } else if (id == R.id.nav_about) {
            //FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(this, AboutAppSimple.class));
            ///Intent about = new Intent(Home.this,About.class);
            //startActivity(about);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
