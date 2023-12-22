package com.example.healthcareapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

public class ProfileFragment extends Fragment {

    private FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    private StorageReference storageReference;

    private FloatingActionButton fab;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    ImageView profileImage;
    TextView fullName,phoneNo,email,address,age,about;

    private String mParam1;
    private String mParam2;

    Patient patient;
    MainActivity activity;

    ProfileFragment(Patient patient){
        this.patient = patient;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

//        View v = inflater.inflate(R.layout.fragment_profile,null);
        View v = inflater.inflate(R.layout.fragment_profile,container, false);
        getActivity().setTitle("My Profile");

        activity = (MainActivity)getActivity();

        profileImage = v.findViewById(R.id.profileImage);

        fullName = v.findViewById(R.id.person_name);
        phoneNo = v.findViewById(R.id.person_phoneNo);
        email = v.findViewById(R.id.person_email);
        address = v.findViewById(R.id.person_address);
        age = v.findViewById(R.id.person_age);
        about = v.findViewById(R.id.person_about);

        fab = v.findViewById(R.id.fab);

        if(patient != null){
            String nameStr = patient.getName();
            String emailStr = patient.getEmail();
            String ageStr = String.valueOf(patient.getAge());
            String genderStr = patient.getGender();
            String addressStr = patient.getAddress();
            String aboutStr = patient.getMedicalHistory();
            String phoneNoStr = patient.getContactInfo();

            fullName.setText(nameStr);
            phoneNo.setText(phoneNoStr);
            email.setText(emailStr);
            address.setText(addressStr);
            age.setText("Age : " + ageStr);
            about.setText(aboutStr);

            activity.showProfile(profileImage);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Edit Profile", Toast.LENGTH_SHORT).show();
                Intent updateProfile = new Intent(activity,UpdateProfile.class);
                updateProfile.putExtra("Patient", patient);
                startActivity(updateProfile);
            }
        });


        return  v;

    }
}
