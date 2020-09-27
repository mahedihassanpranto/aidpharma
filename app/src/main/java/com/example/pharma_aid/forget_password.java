package com.example.pharma_aid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class forget_password extends AppCompatActivity {

    private ImageButton backBtn;
    private EditText emailEt;
    private Button recoverBtn;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        backBtn= findViewById(R.id.backBtn);
        emailEt= findViewById(R.id.emailEt);
        recoverBtn=findViewById(R.id.recoverBtn);


        firebaseAuth =FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog( this );
        progressDialog.setTitle( "Please Wait" );
        progressDialog.setCanceledOnTouchOutside( false );




        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        recoverBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recoverPassword();

            }
        } );


    }


    private String email;
    private void recoverPassword() {

        email = emailEt.getText().toString().trim();
        if(!Patterns.EMAIL_ADDRESS.matcher( email ).matches())
        {
            Toast.makeText(this,"Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.setMessage( "Sending instruction to reset password  " );
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail( email ).addOnSuccessListener( new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // instruction sent

                progressDialog.dismiss();
                Toast.makeText( forget_password.this, " password  reset instruction sent to your email " , Toast.LENGTH_SHORT ).show();

            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // Failed sending instruction

                progressDialog.dismiss();
                Toast.makeText( forget_password.this, ""+e.getMessage(), Toast.LENGTH_SHORT ).show();

            }
        } );


    }
}