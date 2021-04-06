package com.example.app1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText lEmail,lPassword;
    Button lLogin;
    TextView lRegister;
    FirebaseAuth lauth;


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        // hiding action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //------------------------------------------------------------------------------------------

        lEmail = findViewById(R.id.login_email);
        lPassword = findViewById(R.id.login_password);

        lLogin = findViewById(R.id.login_login);
        lRegister = findViewById(R.id.register_textView);

        lauth = FirebaseAuth.getInstance();

        if (lauth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Main.class));
            finish();
        }

        lLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = lEmail.getText().toString().trim();
                String password = lPassword.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    lEmail.setError("Email required");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    lPassword.setError("Password required");
                    return;
                }
                if (password.length() < 6) {
                    lPassword.setError("Password must be longer than 6 characters");
                    return;
                }

                // check for validity of user
                lauth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), Main.class));
                        } else{
                            Toast.makeText(Login.this,"Login Failed",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });

        lRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),Register.class));
            }
        });
    }

}