package com.sumuzu.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneAct extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_continue;
    EditText username, password, email_address;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        email_address = findViewById(R.id.email_address);

        //ubah state button menjadi loading
        btn_continue.setEnabled(true);
        btn_continue.setText("CONTINUE");

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtosignin = new Intent(RegisterOneAct.this,SignInAct.class);
                startActivity(backtosignin);
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ubah state button menjadi loading
                btn_continue.setEnabled(false);
                btn_continue.setText("Loading ...");

                String cusername = username.getText().toString();
                String cpassword = password.getText().toString();
                String cemail = email_address.getText().toString();

                if(cusername.isEmpty()){

                    Toast.makeText(getApplicationContext(),"User Name harus diisi!!",Toast.LENGTH_SHORT).show();

                    username.requestFocus();
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");

                } else if(cpassword.isEmpty()){

                    Toast.makeText(getApplicationContext(),"Password harus diisi!!",Toast.LENGTH_SHORT).show();

                    password.requestFocus();
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");

                } else if(cemail.isEmpty()){

                    Toast.makeText(getApplicationContext(),"Email harus diisi!!",Toast.LENGTH_SHORT).show();

                    email_address.requestFocus();
                    btn_continue.setEnabled(true);
                    btn_continue.setText("CONTINUE");

                } else{

                    //menyimpan data kepada local storage (handphone)
                    SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(username_key, username.getText().toString());
                    editor.apply();

//                //test apakah username sudah masuk storage local
//                Toast.makeText(getApplicationContext(), "username "+
//                        username.getText().toString(),Toast.LENGTH_SHORT).show();

                    //simpan kepada database
                    reference = FirebaseDatabase.getInstance().getReference()
                            .child("Users").child(username.getText().toString());
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            dataSnapshot.getRef().child("username").setValue(username.getText().toString());
                            dataSnapshot.getRef().child("password").setValue(password.getText().toString());
                            dataSnapshot.getRef().child("email_address").setValue(email_address.getText().toString());
                            dataSnapshot.getRef().child("user_balance").setValue(1000);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    //pindah activity
                    Intent gotonextregister = new Intent(RegisterOneAct.this,RegisterTwoAct.class);
                    startActivity(gotonextregister);

                }

            }
        });


    }



}
