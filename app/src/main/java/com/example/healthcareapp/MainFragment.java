package com.example.healthcareapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class MainFragment extends Fragment {

    FirebaseAuth mAuth;
    Patient patient;

    MainFragment(Patient patient){
        this.patient = patient;
    }

    Button docBtn, labBtn, medFolBtn, medBtn, exerciseBtn, appointementBtn;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main,container, false);
        getActivity().setTitle("Home");

        mAuth = FirebaseAuth.getInstance();
        MainActivity activity = (MainActivity)getActivity();
        TextView userText = v.findViewById(R.id.user);
        if(patient != null){
            String nameStr = patient.getName();
            String[] nameParts = nameStr.split(" ");
            nameStr = "Welcome " + nameParts[0];
            userText.setText(nameStr);
        }

        docBtn = v.findViewById(R.id.docBtn);
        labBtn = v.findViewById(R.id.labBtn);
        medFolBtn = v.findViewById(R.id.medFolBtn);
        medBtn = v.findViewById(R.id.medBtn);
        exerciseBtn = v.findViewById(R.id.ExerciseBtn);
        appointementBtn = v.findViewById(R.id.appointementBtn);

        docBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Search Doctor", Toast.LENGTH_SHORT).show();
            }
        });

        labBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "My lab Test", Toast.LENGTH_SHORT).show();
            }
        });

        medFolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Medical Folder", Toast.LENGTH_SHORT).show();
            }
        });

        medBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Medicine", Toast.LENGTH_SHORT).show();
            }
        });

        exerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Excercise", Toast.LENGTH_SHORT).show();
            }
        });

        appointementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Appointment", Toast.LENGTH_SHORT).show();
            }
        });


        return  v;
    }
}
