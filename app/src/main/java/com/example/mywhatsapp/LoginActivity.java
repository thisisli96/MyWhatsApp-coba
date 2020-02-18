package com.example.mywhatsapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private EditText mPhoneNumber, mcode ;
    private Button mSend;

    // private String mVerificationId;
    String mVerificationId;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this); // 1. untuk menggunakan firebase di dalam apikasi

        userIsLoggedIn();
        mPhoneNumber = findViewById(R.id.phoneNumber);
        mcode = findViewById(R.id.code);
        mSend = findViewById(R.id.send);

        mSend.setOnClickListener(new View.OnClickListener() { // NOTED step 5 started
            @Override
            public void onClick(View v) {
                if (mVerificationId != null)
                    verifyPhoneNumberWithCode(); // cari tahu
                else
                    startPhoneNumberVerification();
            }
        });


        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                signInWithPhoneAuthCredential(phoneAuthCredential);  // 7 Sign in fucntion // NOTED
            }

            @Override
            public void onVerificationFailed(FirebaseException e) { } // and the message not same and the user input something wrong , this method will be call


            // step 9
            @Override
            public void onCodeSent(String mverificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(mverificationId, forceResendingToken);

                mVerificationId = mverificationId;
                mSend.setText("verity code");
                Log.i("aadasdada", mVerificationId);
            }
        };

    }






    // next step 9
    private void verifyPhoneNumberWithCode() {
        PhoneAuthCredential credential  = PhoneAuthProvider.getCredential(mVerificationId, mcode.getText().toString());
        signInWithPhoneAuthCredential(credential); // cari tahu
    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){ //3.Database Structure (checking if contact is also a user)
                    // 2 saving to database
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null){}
                        final DatabaseReference mUserDB = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
                    mUserDB.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if(!dataSnapshot.exists()){
                                Map<String, Object> userMap = new HashMap<>();
                                userMap.put("phone", user.getPhoneNumber());
                                userMap.put("name", user.getPhoneNumber());
                                mUserDB.updateChildren(userMap);
                            }
                            userIsLoggedIn();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } // end task

               // userIsLoggedIn();

            }
        });
    }

    private void userIsLoggedIn() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            startActivity(new Intent(getApplicationContext(), MainPageActivity.class));
            finish();
            return;
        }
    }


    private void startPhoneNumberVerification() { //  firebase documentation
        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                mPhoneNumber.getText().toString(),
                60,                                 // the time user input the code
                TimeUnit.SECONDS,
                this,
                mCallbacks                              // what happen next
        );
    }
}
