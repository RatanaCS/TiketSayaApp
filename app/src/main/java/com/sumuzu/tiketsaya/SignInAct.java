package com.sumuzu.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SignInAct extends AppCompatActivity {

    TextView btn_new_account;
    Button btn_sign_in;
    EditText xusername, xpassword;

    DatabaseReference reference;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_new_account = findViewById(R.id.btn_new_account);
        xusername = findViewById(R.id.xusername);
        xpassword = findViewById(R.id.xpassword);

//        //ubah state button ke awal SIGN IN
//        btn_sign_in.setEnabled(true);
//        btn_sign_in.setText("SIGN IN");

        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoregister = new Intent(SignInAct.this, RegisterOneAct.class);
                startActivity(gotoregister);
//                finish();
            }
        });


        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //ubah state button menjadi loading
                btn_sign_in.setEnabled(false);
                btn_sign_in.setText("Loading ...");

                final String username = xusername.getText().toString();
                final String password = xpassword.getText().toString();

                if(username.isEmpty()){

                    Toast.makeText(getApplicationContext(), "Username harus diisi !!", Toast.LENGTH_SHORT).show();
//                    xusername.setFocusableInTouchMode(true);
//                    xusername.setFocusable(true);
                    xusername.requestFocus();
                    //ubah state button ke awal SIGN IN
                    btn_sign_in.setEnabled(true);
                    btn_sign_in.setText("SIGN IN");

                } else {

                    if(password.isEmpty()){
                        Toast.makeText(getApplicationContext(), "Password harus diisi !!", Toast.LENGTH_SHORT).show();

                        xpassword.requestFocus();
                        //ubah state button ke awal SIGN IN
                        btn_sign_in.setEnabled(true);
                        btn_sign_in.setText("SIGN IN");

                    }else{

                        reference = FirebaseDatabase.getInstance().getReference()
                                .child("Users").child(username);

                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){

                                    //ambil data password dari Firebase
                                    String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                    //validasi password dgn password di FB
                                    if(password.equals(passwordFromFirebase)){

                                        //simpan username(key) kpd local storage-hp
                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, xusername.getText().toString());
                                        editor.apply();

                                        Toast.makeText(getApplicationContext(),"Selamat datang "+username+" (^3^)",
                                                Toast.LENGTH_LONG).show();

                                        //pindah act ke HOME Profile
                                        Intent gotoHome = new Intent(SignInAct.this, HomeAct.class);
                                        startActivity(gotoHome);


                                    }else{

                                        //ubah state button ke awal SIGN IN
                                        btn_sign_in.setEnabled(true);
                                        btn_sign_in.setText("SIGN IN");

                                        Toast.makeText(getApplicationContext(),"Maaf, password tidak sesuai !!!",
                                                Toast.LENGTH_LONG).show();

                                    }

                                }else{

                                    //ubah state button ke awal SIGN IN
                                    btn_sign_in.setEnabled(true);
                                    btn_sign_in.setText("SIGN IN");

                                    Toast.makeText(getApplicationContext(),"User belum terdaftar!",
                                            Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                //ubah state button ke awal SIGN IN
                                btn_sign_in.setEnabled(true);
                                btn_sign_in.setText("SIGN IN");

                                Toast.makeText(getApplicationContext(),"Database ErroR!",
                                        Toast.LENGTH_SHORT).show();
                            }

                        });
                    }

                }


            }
        });


    }
}












