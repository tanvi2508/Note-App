package com.example.notesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    EditText email,password;
    Button signin_btn;
    TextView forgotPass,sign_upText;
    FirebaseAuth firebaseAuth;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        signin_btn=findViewById(R.id.login_btn);
        forgotPass=findViewById(R.id.forgot_password);
        sign_upText=findViewById(R.id.newAccount);
        firebaseAuth=FirebaseAuth.getInstance();
        FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();
        progressBar=findViewById(R.id.progressbar_main);

        if(firebaseUser!=null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,NotesActivity.class));
        }

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        sign_upText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent1);
            }
        });

        signin_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mail=email.getText().toString().trim();
                String pass=password.getText().toString().trim();


                if(mail.isEmpty() || pass.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"All Fields are required",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(mail,pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull  Task<AuthResult> task) {
                                        if(task.isSuccessful())
                                        {
                                            checkEmailVerification();
                                        }
                                        else
                                        {
                                            Toast.makeText(MainActivity.this,"Account doesn't exit",Toast.LENGTH_SHORT).show();
                                            progressBar.setVisibility(View.INVISIBLE);
                                        }
                                }
                            });
                }
            }
        });
    }

    private void checkEmailVerification() {

  FirebaseUser firebaseUser=firebaseAuth.getCurrentUser();

        if(firebaseUser!=null)
        {
            Toast.makeText(MainActivity.this,"Logged In",Toast.LENGTH_SHORT).show();
            finish();
            startActivity(new Intent(MainActivity.this,NotesActivity.class));
        }
        else
        {
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this,"Verify your Email first",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }
    }
}