package com.example.pharma_aid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class main_seller extends AppCompatActivity {

    private TextView nameTv;
    private ImageButton logoutBtn;

    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main_seller );

        nameTv = findViewById( R.id.nameEt );
        logoutBtn = findViewById( R.id.logoutbtn );




        firebaseAuth =FirebaseAuth.getInstance();
        checKUser();


        logoutBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Make offline
                // Sign out
                // go to login Activity

                firebaseAuth.signOut();
                checKUser();
            }
        } );


    }

    private void checKUser() {

        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user==null)
        {
            startActivity(new Intent( main_seller.this, login.class) );
            finish();

        }
        else{
            loadMyInfo();

        }


    }

    private void loadMyInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild( "uid" ).equalTo( firebaseAuth.getUid() ).addValueEventListener( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot ds: dataSnapshot.getChildren()){

                    String name =""+ds.child( "name" ).getValue();
                    String accountType =""+ds.child( "accountType" ).getValue();

                    nameTv.setText( name+ " ("+accountType+")" );





                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        } );

    }


    private void makeMeOffline() {

        // after loggin in, make user online
        progressDialog.setMessage( "Logging Out...." );
        HashMap<String,Object> hashMap = new HashMap<>(  );
        hashMap.put("online","false"  );

        // update value to db

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference( "Users" );
        ref.child(firebaseAuth.getUid()).updateChildren( hashMap ).addOnSuccessListener( new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                // update successful

                firebaseAuth.signOut();
                checKUser();


            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                // Tailed updating
                progressDialog.dismiss();
                Toast.makeText( main_seller.this ,""+e.getMessage() , Toast.LENGTH_SHORT ).show();

            }
        } );




    }







}