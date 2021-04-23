package com.example.app1.Login_Logout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.app1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordclass extends AppCompatActivity {

    EditText email;
    Button sendReset, back;

    FirebaseAuth lauth;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forgot_password);

        // hiding action bar
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        //------------------------------------------------------------------------------------------

        back = findViewById(R.id.resetpassword_back_btn);
        email = findViewById(R.id.editTextTextEmailAddress);
        sendReset = findViewById(R.id.login_btn);

        lauth = FirebaseAuth.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        sendReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim() != null){
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(getApplication(), "Email sent", Toast.LENGTH_LONG).show();
                            }else {
                                Toast.makeText(getApplication(), "Unable to send Email", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                }else {
                    Toast.makeText(getApplication(), "Please enter your email", Toast.LENGTH_LONG).show();
                }

            }
        });



    }
}