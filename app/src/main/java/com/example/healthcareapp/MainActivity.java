package com.example.healthcareapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    Patient patient;
    private DrawerLayout drawer;
    TextView fullName,email;
    ImageView profileImage;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();

        drawer =findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        DocumentReference documentReference = fStore.collection("Patients").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                if (error != null) {
                    Log.w(TAG, "Listen failed.", error);
                    return;
                }

                if (value != null && value.exists()) {
                    String nameStr= value.getData().get("Name").toString();
                    String emailStr = value.getData().get("Email").toString();
                    String ageStr = value.getData().get("Age").toString();
                    String genderStr = value.getData().get("Gender").toString();
                    String addressStr = value.getData().get("Address").toString();
                    String aboutStr = value.getData().get("MedHistory").toString();
                    String phoneNoStr = value.getData().get("PhoneNo").toString();


                    patient = new Patient(userID,nameStr,Integer.parseInt(ageStr),genderStr,addressStr,aboutStr,phoneNoStr,emailStr);

                    fullName.setText(patient.getName());
                    email.setText(emailStr);


                } else {
                    Toast.makeText(MainActivity.this, "Current data: null", Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(savedInstanceState == null){
            MainFragment mainFragment =  new MainFragment(patient);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    mainFragment).commit();
            navigationView.setCheckedItem(R.id.nav_main);
        }

        View headerView = navigationView.getHeaderView(0);
        fullName = headerView.findViewById(R.id.person_name);
        email = headerView.findViewById(R.id.person_email);
        profileImage = headerView.findViewById(R.id.profileImage);

        showProfile(profileImage);
//
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_main:
               MainFragment mainFragment =  new MainFragment(patient);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mainFragment)
                        .commit();
                break;
            case R.id.nav_chat:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ChatFragment()).commit();
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new TestFragment()).commit();
                break;
            case R.id.nav_profile:
                ProfileFragment profileFragment = new ProfileFragment(patient);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, profileFragment)
                        .commit();
                break;

            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                Toast.makeText(MainActivity.this, "signOut Successfully", Toast.LENGTH_SHORT).show();
                finish();
                break;

            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void showProfile(ImageView profileImage1){
        storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference profileRef = storageReference.child("Patients/"+fAuth.getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage1);
            }
        });
    }
}