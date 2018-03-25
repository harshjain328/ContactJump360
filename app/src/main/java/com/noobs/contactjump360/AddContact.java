package com.noobs.contactjump360;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddContact extends AppCompatActivity implements View.OnClickListener {

    private Button btnPlace;
    private EditText inpName,inpPhone,inpAdd;
    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mContactsDatabaseReference;
    int PLACE_PICKER_REQUEST = 1;
    private double lat,lng;
    FirebaseAuth firebaseAuth;
    String name,num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        getSupportActionBar().setTitle("Add contact");

        firebaseAuth=FirebaseAuth.getInstance();

        mFirebaseDatabase=FirebaseDatabase.getInstance();
        mContactsDatabaseReference=mFirebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("contacts");

        inpName=findViewById(R.id.edName);
        inpPhone=findViewById(R.id.edPhone);
        inpAdd=findViewById(R.id.edAddress);
        btnPlace=findViewById(R.id.btnPlacePick);

        Intent intent=getIntent();
        if(intent.hasExtra("Number")) {
            num = intent.getStringExtra("Number");
            name=intent.getStringExtra("Name");
            inpName.setText(name);
            inpPhone.setText(num);
            getSupportActionBar().setTitle("Edit Contact");
        }
        if(intent.hasExtra("Lat")) {
            //tmp=intent.getStringExtra("Lat");
            lat = Double.parseDouble(intent.getStringExtra("Lat"));
            lng = Double.parseDouble(intent.getStringExtra("Lng"));
            inpAdd.setText(lat+" "+lng);
        }

        btnPlace.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this,DisplayContacts.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(num!=null) {
            getMenuInflater().inflate(R.menu.menu_edit, menu);
        }
        else
            getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.btnMenuSave:
                storeContact();
                break;
            case R.id.btnMenuDelete:
                DatabaseReference mDelete=mFirebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("contacts").child(num);
                mDelete.removeValue();
                finish();
                startActivity(new Intent(getApplicationContext(),DisplayContacts.class));
                break;
            case R.id.btnMenuUpdate:
                DatabaseReference mUpdate=mFirebaseDatabase.getReference().child("users").child(firebaseAuth.getCurrentUser().getUid()).child("contacts").child(num);
                mUpdate.removeValue();
                storeContact();
                break;
        }
        return true;
    }

    private void storeContact(){
        String name=inpName.getText().toString().trim();
        Long phone=Long.parseLong(inpPhone.getText().toString());

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(phone+"")) {
            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        if(lat!=0){
            ContactInfo contactInfo=new ContactInfo(name,phone,lat,lng);
            mContactsDatabaseReference.child(phone.toString()).setValue(contactInfo);
        }
        else {
            ContactInfo contactInfo = new ContactInfo(name, phone);
            mContactsDatabaseReference.child(phone.toString()).setValue(contactInfo);
        }
        finish();
        startActivity(new Intent(getApplicationContext(),DisplayContacts.class));
    }

    @Override
    public void onClick(View view) {
        if(view==btnPlace){
            if(lat!=0){
                Intent intent=new Intent(this,Maps.class);
                intent.putExtra("Lat",lat);
                intent.putExtra("Lng",lng);
                intent.putExtra("Name",inpName.getText().toString().trim());
                startActivity(intent);
            }else{
                getPlace();
            }
        }
    }

    private void getPlace(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try{
            Intent intent=builder.build(this);
            startActivityForResult(intent,PLACE_PICKER_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==PLACE_PICKER_REQUEST && resultCode==RESULT_OK){
            Place place=PlacePicker.getPlace(data,this);
            inpAdd.setText(place.getAddress());
            lat=place.getLatLng().latitude;
            lng=place.getLatLng().longitude;
        }
    }
}
