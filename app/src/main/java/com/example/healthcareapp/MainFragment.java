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
                Intent searchDoctor = new Intent(activity,SearchDoctor.class);
                searchDoctor.putExtra("Patient", patient);
                startActivity(searchDoctor);
            }
        });

        labBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "My lab Test", Toast.LENGTH_SHORT).show();
                Intent labTest = new Intent(activity,LabTest.class);
                labTest.putExtra("Patient", patient);
                startActivity(labTest);
            }
        });

        medFolBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Medical Folder", Toast.LENGTH_SHORT).show();
                Intent medicalFolder = new Intent(activity,MedicalFolder.class);
                medicalFolder.putExtra("Patient", patient);
                startActivity(medicalFolder);
            }
        });

        medBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Medicine", Toast.LENGTH_SHORT).show();
                Intent medicine = new Intent(activity,Medicine.class);
                medicine.putExtra("Patient", patient);
                startActivity(medicine);
            }
        });

        exerciseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Excercise", Toast.LENGTH_SHORT).show();
                Intent exercise = new Intent(activity,Exercise.class);
                exercise.putExtra("Patient", patient);
                startActivity(exercise);
            }
        });

        appointementBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(activity, "Appointment", Toast.LENGTH_SHORT).show();
                Intent appointment = new Intent(activity,Appointment.class);
                appointment.putExtra("Patient", patient);
                startActivity(appointment);
            }
        });

        return  v;
    }
}
