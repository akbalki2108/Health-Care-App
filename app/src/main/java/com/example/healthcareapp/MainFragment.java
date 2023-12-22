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




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_main,container, false);
        getActivity().setTitle("Home");

        mAuth = FirebaseAuth.getInstance();

        TextView userText = v.findViewById(R.id.user);
        if(patient != null){
            String nameStr = patient.getName();
            String[] nameParts = nameStr.split(" ");
            nameStr = "Welcome " + nameParts[0];
            userText.setText(nameStr);
        }

        return  v;
    }
}
