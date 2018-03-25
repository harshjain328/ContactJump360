package com.noobs.contactjump360;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity implements View.OnClickListener {

    private Button btnLogin;
    private EditText inpEmail,inpPass;
    private FirebaseAuth firebaseAuth;
    private TextView txtReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            //start contact activity
            finish();
            Intent intent=new Intent(this,DisplayContacts.class);
            startActivity(intent);
        }

        btnLogin = findViewById(R.id.btnLogin);

        txtReg = findViewById(R.id.txtSignUp);

        inpEmail = findViewById(R.id.edEmail);
        inpPass = findViewById(R.id.edPass);

        btnLogin.setOnClickListener(this);

        txtReg.setOnClickListener(this);
    }

    private void verify(){
        String email=inpEmail.getText().toString().trim();
        final String pass=inpPass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Login.this,"Login successful",Toast.LENGTH_LONG).show();

                            finish();
                            startActivity(new Intent(getApplicationContext(),DisplayContacts.class));
                        }
                        else{
                            Toast.makeText(Login.this,"Login failed",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        if(view==txtReg){
            finish();
            startActivity(new Intent(this,Registration.class));
        }
        if(view==btnLogin){
            verify();
        }
    }
}