package com.example.healthcareapp;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    TextView header;
    private EditText userEmail, userPassword, userPassword2, userName,phoneNo,address,age,about,gender;
    private ProgressBar loadingProgress;
    private Button regBtn,nextBtn;
    private FirebaseAuth mAuth;

    FirebaseFirestore fStore;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();


        header = findViewById(R.id.home_text);

        //ini views
        userName = findViewById(R.id.regName);
        userEmail = findViewById(R.id.regMail);
        userPassword = findViewById(R.id.regPassword);
        userPassword2 = findViewById(R.id.regPassword2);

        phoneNo = findViewById(R.id.phoneNo);
        phoneNo.setVisibility(View.INVISIBLE);

        address = findViewById(R.id.address);
        address.setVisibility(View.INVISIBLE);

        age = findViewById(R.id.age);
        age.setVisibility(View.INVISIBLE);

        about = findViewById(R.id.about);
        about.setVisibility(View.INVISIBLE);

        gender = findViewById(R.id.gender);
        gender.findViewById(View.INVISIBLE);


        loadingProgress = findViewById(R.id.regProgressBar);
        loadingProgress.setVisibility(View.INVISIBLE);

        regBtn = findViewById(R.id.regBtn);
        regBtn.setVisibility(View.INVISIBLE);

        nextBtn = findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String str1 = userEmail.getText().toString();
                final String str2 = userPassword.getText().toString();
                final String str3 = userPassword2.getText().toString();
                final String str4 = userName.getText().toString();

                if (str1.isEmpty() || str2.isEmpty() || str3.isEmpty() || str4.isEmpty()){
                    showMessage("Please Verify all fields");
                    nextBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);
                }else{
                    userName.setVisibility(View.INVISIBLE);
                    userEmail.setVisibility(View.INVISIBLE);
                    userPassword.setVisibility(View.INVISIBLE);
                    userPassword2.setVisibility(View.INVISIBLE);
                    nextBtn.setVisibility(View.INVISIBLE);

                    phoneNo.setVisibility(View.VISIBLE);
                    address.setVisibility(View.VISIBLE);
                    age.setVisibility(View.VISIBLE);
                    about.setVisibility(View.VISIBLE);
                    gender.setVisibility(View.VISIBLE);
                    regBtn.setVisibility(View.VISIBLE);

                }

            }
        });


        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                regBtn.setVisibility(View.INVISIBLE);
                loadingProgress.setVisibility(View.VISIBLE);

                final String emailStr = userEmail.getText().toString();
                final String passwordStr = userPassword.getText().toString();
                final String password2Str = userPassword2.getText().toString();
                final String nameStr = userName.getText().toString();

                final String phoneNoStr = phoneNo.getText().toString();
                final String addressStr = address.getText().toString();
                final String ageStr = age.getText().toString();
                final String genderStr = gender.getText().toString();
                final String aboutStr = about.getText().toString();



                // Inside the else block of onClick method in your RegisterActivity

                if (emailStr.isEmpty() || nameStr.isEmpty() || passwordStr.isEmpty() || !passwordStr.equals(password2Str)
                || phoneNoStr.isEmpty() || addressStr.isEmpty() || ageStr.isEmpty() || aboutStr.isEmpty()) {

                    showMessage("Please Verify all fields");
                    regBtn.setVisibility(View.VISIBLE);
                    loadingProgress.setVisibility(View.INVISIBLE);

                } else {
                    // Create a new user with email and password
                    mAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            // Update user's profile information (display name)
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(nameStr)
                                                    .build();

                                            user.updateProfile(profileUpdates)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> profileTask) {
                                                            if (profileTask.isSuccessful()) {
                                                                Patient patient = new Patient(user.getUid(),nameStr,Integer.parseInt(ageStr),"",addressStr,"",phoneNoStr,emailStr);
                                                                showMessage("Registration successful!");
                                                                updateUI();
                                                            }
                                                        }
                                                    });

                                            DocumentReference documentReference = fStore.collection("Patients").document(user.getUid());

                                            Map<String,Object> u = new HashMap<>();
                                            u.put("Name",nameStr);
                                            u.put("Email",emailStr);
                                            u.put("Age",ageStr);
                                            u.put("Gender",genderStr);
                                            u.put("Address",addressStr);
                                            u.put("MedHistory",aboutStr);
                                            u.put("PhoneNo",phoneNoStr);

                                            documentReference.set(u).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Log.d("TAG","onSucess : user profile is created for "+nameStr);
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w(TAG, "Error adding document", e);
                                                }
                                            });;



                                        }
                                    } else {
                                        // If registration fails, display a message to the user.
                                        showMessage("Registration failed: " + Objects.requireNonNull(task.getException()).getMessage());
                                        regBtn.setVisibility(View.VISIBLE);
                                        loadingProgress.setVisibility(View.INVISIBLE);
                                    }
                                }
                            });
                }

            }
        });
    }


    private void updateUI() {

        Intent homeActivity = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(homeActivity);
        finish();
    }

    private void showMessage(String message) {
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
    }


}