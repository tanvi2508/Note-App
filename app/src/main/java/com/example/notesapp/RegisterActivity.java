package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
        EditText signup_email,signup_pass;
        Button signup_button;
        TextView already_have_an_Acc;
        FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        signup_email=findViewById(R.id.reg_email);
        signup_pass=findViewById(R.id.reg_password);
        signup_button=findViewById(R.id.signup_btn);
        already_have_an_Acc=findViewById(R.id.alreadyhaveanaccount);
        firebaseAuth=FirebaseAuth.getInstance();

        already_have_an_Acc.setOnClickListener(view -> {
            Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(intent);
        });

        signup_button.setOnClickListener(view -> {
            String userEmail=signup_email.getText().toString().trim();
            String userPass=signup_pass.getText().toString().trim();

            if(userEmail.isEmpty() || userPass.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"All fields are Required",Toast.LENGTH_SHORT).
                        show();
            }

            else  if(userPass.length()<7)
            {
                Toast.makeText(getApplicationContext(),"Password should greater than 7 digits",Toast.LENGTH_SHORT).
                        show();
            }
            else
            {
                 firebaseAuth.createUserWithEmailAndPassword(userEmail,userPass)
                         .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                             @Override
                             public void onComplete(@NonNull  Task<AuthResult> task) {
                                     if(task.isSuccessful())
                                     {
                                         Toast.makeText(getApplicationContext(),"Registration is Successfully",Toast.LENGTH_SHORT).show();
                                         sendEmailVerification();
                                     }

                                     else
                                     {
                                         Toast.makeText(getApplicationContext(),"Failed to Register",Toast.LENGTH_SHORT).show();
                                     }
                             }
                         });
            }
        });
    }
    // send email verification

    private void sendEmailVerification() {
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull  Task<Void> task) {
                    Toast.makeText(getApplicationContext(),"Verification Email is Sent,Verify and Log In agin",Toast.LENGTH_SHORT).show();
                    firebaseAuth.signOut();
                    finish();
                    startActivity(new Intent(RegisterActivity.this,MainActivity.class));
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Failed to send Verification Email",Toast.LENGTH_SHORT).show();
        }
    }


}