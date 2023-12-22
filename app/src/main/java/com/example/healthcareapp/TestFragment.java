package com.example.healthcareapp;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class TestFragment extends Fragment {


    TextView fullName,phoneNo,email,address,age,about;
    EditText editName,editPhone;
    Button submitBtn,readBtn;

    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_chat,container, false);
        getActivity().setTitle("Test");

        MainActivity activity = (MainActivity) getActivity();


        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();



        fullName = v.findViewById(R.id.test_name);
        phoneNo = v.findViewById(R.id.test_phone);

        editName = v.findViewById(R.id.editName);
        editPhone = v.findViewById(R.id.editPhone);

        submitBtn = v.findViewById(R.id.submit);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "submit", Toast.LENGTH_SHORT).show();

                String nameStr = editName.getText().toString();
                String phoneStr = editPhone.getText().toString();



                DocumentReference documentReference = fStore.collection("Patients").document(userID);

                Map<String,Object> user = new HashMap<>();
                user.put("Name",nameStr);
                user.put("phone",phoneStr);

                documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("TAG","onSucess : user profile is created for "+user);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });;
            }
        });


        readBtn = v.findViewById(R.id.read);
        readBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MainActivity activity1 = (MainActivity) getActivity();

                String nameStr = activity1.patient.getName();
                String phoneStr = activity1.patient.getContactInfo();

                fullName.setText(nameStr);
                phoneNo.setText(phoneStr);

            }
        });


        return  v;
    }
}
