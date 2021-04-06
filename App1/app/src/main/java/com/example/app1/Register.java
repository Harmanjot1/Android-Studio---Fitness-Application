package com.example.app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    EditText rEmail,rPassword,rReEnterPassword;
    Button rRegister;
    TextView rLogin;
    FirebaseAuth rauth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        // hiding action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //------------------------------------------------------------------------------------------

        rEmail = findViewById(R.id.register_email);
        rPassword = findViewById(R.id.register_password);
        rReEnterPassword = findViewById(R.id.register_reEnterPassword);
        rRegister = findViewById(R.id.login_register);
        rLogin = findViewById(R.id.login_textview);

        rauth = FirebaseAuth.getInstance();

        if (rauth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Main.class));
            finish();
        }


        rRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = rEmail.getText().toString().trim();
                String password = rPassword.getText().toString().trim();
                String reEnterPassword = rReEnterPassword.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if (TextUtils.isEmpty(email)) {
                    rEmail.setError("Email required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    rPassword.setError("Password required");
                    return;
                }
                if (TextUtils.isEmpty(reEnterPassword)){
                    rReEnterPassword.setError("Re-Enter Password");
                    return;
                }
                if (!email.matches(emailPattern)){
                    Toast.makeText(Register.this,"Email Pattern invalid",Toast.LENGTH_SHORT).show();
                }
                if (password.length() < 6) {
                    rPassword.setError("Password must be longer than 6 characters");
                    return;
                }
                if (password != reEnterPassword){
                    rReEnterPassword.setError("Passwords not matching!");
                    return;
                }


                // register user
                rauth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"User registered",Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(),Login.class));
                        }else{
                            Toast.makeText(Register.this,"Unable to register" + task.getException().getMessage(),Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });
        rLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}