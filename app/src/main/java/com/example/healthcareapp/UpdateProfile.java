package com.example.healthcareapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class UpdateProfile extends AppCompatActivity {

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    StorageReference storageReference;

    String userID;

    Patient patient;

    Button updateBtn;

    String nameStr,phoneStr,addressStr,emailStr,ageStr,aboutStr,genderStr;
    String updatedName,updatedPhone,updatedAddress,updatedEmail,updatedAbout,updatedGender;

    EditText nameText, phoneText, addressText, emailText, ageText, aboutText, genderText;
    ImageView imageView;
    Uri pickedImgUri ;
    public UpdateProfile(){}
    UpdateProfile(Patient patient){
        this.patient = patient;
    }


    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result !=null && result.getResultCode() == RESULT_OK){
                        // There are no request codes
                        Intent data = result.getData();
                        pickedImgUri = data.getData();
                        imageView.setImageURI(pickedImgUri);
                    }
                }
            }
    );


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        Intent i = getIntent();
        patient = (Patient) i.getSerializableExtra("Patient");

        Toolbar toolbar = findViewById(R.id.toolbarUpdate);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Enable the Up button
            actionBar.setTitle("Update Profile");
        }

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();

        imageView = findViewById(R.id.person_photo);

        nameText = findViewById(R.id.updateName);
        phoneText = findViewById(R.id.updatePhoneNo);
        addressText = findViewById(R.id.updateAddress);
        emailText = findViewById(R.id.updateMail);
        ageText = findViewById(R.id.updateAge);
        aboutText = findViewById(R.id.updateAbout);
        genderText = findViewById(R.id.updateGender);


        if(patient != null){
            nameStr = patient.getName();
            phoneStr = patient.getContactInfo();
            addressStr = patient.getAddress();
            emailStr = patient.getEmail();
            ageStr = String.valueOf(patient.getAge());
            aboutStr = patient.getMedicalHistory();
            genderStr = patient.getGender();

            nameText.setText(nameStr);
            phoneText.setText(phoneStr);
            addressText.setText(addressStr);
            emailText.setText(emailStr);
            ageText.setText(ageStr);
            aboutText.setText(aboutStr);
            genderText.setText(genderStr);
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        updateBtn = findViewById(R.id.updateBtn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve edited information from EditText fields
                updatedName = nameText.getText().toString().trim();
                updatedPhone = phoneText.getText().toString().trim();
                updatedAddress = addressText.getText().toString().trim();
                updatedEmail = emailText.getText().toString().trim();
                int updatedAge = Integer.parseInt(ageText.getText().toString().trim());
                updatedAbout = aboutText.getText().toString().trim();
                updatedGender = genderText.getText().toString().trim();

                patient.setName(updatedName);
                patient.setContactInfo(updatedPhone);
                patient.setAddress(updatedAddress);
                patient.setEmail(updatedEmail);
                patient.setAge(updatedAge);
                patient.setMedicalHistory(updatedAbout);
                patient.setGender(updatedGender);


                // Create a DocumentReference for the specific user's document
                DocumentReference docRef = fStore.collection("Patients").document(userID);

                // Update the fields in the Firestore document
                docRef.update("Name", updatedName,
                                "ContactInfo", updatedPhone,
                                "Address", updatedAddress,
                                "Email", updatedEmail,
                                "Age", updatedAge,
                                "MedHistory", updatedAbout,
                                "Gender", updatedGender)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                uploadImageAndUpdateProfile();
                                Toast.makeText(UpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(UpdateProfile.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                            }
                        });

                Intent resultIntent = new Intent();
                resultIntent.putExtra("UpdatedPatient", patient);
                setResult(RESULT_OK, resultIntent);
                finish();
            }

        });

    }
    private void uploadImageAndUpdateProfile() {
        if (pickedImgUri != null) {
            // Upload image to Firebase Storage
            StorageReference storageRef = FirebaseStorage.getInstance().getReference()
                    .child("Patients/" + fAuth.getCurrentUser().getUid() + "/profile.jpg");
            storageRef.putFile(pickedImgUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully, get its download URL
                            storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Update user's profile with the new image URL
                                    UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                                            .setPhotoUri(uri)
                                            .build();

                                    fAuth.getCurrentUser().updateProfile(profileUpdate)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(UpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                                        // Finish the activity or perform any other action upon successful update
                                                    } else {
                                                        Toast.makeText(UpdateProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UpdateProfile.this, "Error uploading image", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            // If no image is selected, update the profile without changing the image
            // Update user's profile with the existing details
            UserProfileChangeRequest profileUpdate = new UserProfileChangeRequest.Builder()
                    // Set other fields if needed, except the photo
                    .setDisplayName(updatedName)
                    .build();

            fAuth.getCurrentUser().updateProfile(profileUpdate)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(UpdateProfile.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                // Finish the activity or perform any other action upon successful update
                            } else {
                                Toast.makeText(UpdateProfile.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
    private void openGallery() {

        Intent openGallery = new Intent(Intent.ACTION_GET_CONTENT);
        openGallery.setType("image/jpg");
        openGallery.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
        startForResult.launch(openGallery);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) { // Check if the home/up button is pressed
            onBackPressed(); // Handle the back button press
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}