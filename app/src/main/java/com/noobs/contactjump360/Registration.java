package com.noobs.contactjump360;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity implements View.OnClickListener {

    private Button btnReg;
    private EditText inpEmail,inpPass;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth=FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser()!=null){
            //start contact activity
            finish();
            startActivity(new Intent(this,DisplayContacts.class));
        }

        btnReg = findViewById(R.id.btnReg);

        inpEmail = findViewById(R.id.edEmail);
        inpPass = findViewById(R.id.edPass);

        btnReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnReg){
            registerUser();
        }
    }

    private void registerUser(){
        String email=inpEmail.getText().toString().trim();
        String pass=inpPass.getText().toString().trim();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Registration.this,"Registration successful",Toast.LENGTH_LONG).show();

                            finish();
                            startActivity(new Intent(getApplicationContext(),DisplayContacts.class));
                        }
                        else{
                            Toast.makeText(Registration.this,"Registration unsuccessful",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }
}
