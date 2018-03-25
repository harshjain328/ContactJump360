package com.noobs.contactjump360;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DisplayContacts extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mContactsDatabaseReference;

    List<ContactInfo> contactInfos;
    ListView disContact;

    Button btnAdd,btnSignout;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_contacts);

        firebaseAuth= FirebaseAuth.getInstance();

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mContactsDatabaseReference=mFirebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("contacts");

        contactInfos=new ArrayList<>();

        disContact=findViewById(R.id.lstContact);

        btnAdd=findViewById(R.id.btnAdd);
        btnSignout=findViewById(R.id.btnSignOut);

        disContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactInfo contactInfo=contactInfos.get(i);
                Intent intent=new Intent(getApplicationContext(),AddContact.class);

                intent.putExtra("Name",contactInfo.name);
                intent.putExtra("Number",contactInfo.number+"");

                if(contactInfo.lat!=0) {
                    intent.putExtra("Lat", contactInfo.lat + "");
                    intent.putExtra("Lng", contactInfo.lng + "");
                }
                startActivity(intent);
            }
        });

        btnAdd.setOnClickListener(this);
        btnSignout.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        mContactsDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                contactInfos.clear();
                for(DataSnapshot ds: dataSnapshot.getChildren()){
                    ContactInfo contactInfo=ds.getValue(ContactInfo.class);
                    contactInfos.add(contactInfo);
                }
                ContactList contactListAdapter=new ContactList(DisplayContacts.this,contactInfos);
                disContact.setAdapter(contactListAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View view) {
        if(view==btnAdd){
            finish();
            startActivity(new Intent(getApplicationContext(),AddContact.class));
        }
        if(view==btnSignout){
            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(getApplicationContext(),Login.class));
        }
    }
}
