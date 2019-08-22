package com.sumuzu.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeAct extends AppCompatActivity {

    LinearLayout btn_ticket_pisa, btn_ticket_torri, btn_ticket_pagoda;
    LinearLayout btn_ticket_candi, btn_ticket_sphinx, btn_ticket_monas;
    CircleView btn_to_profile;
    TextView xuser_balance, xbio, xnama_lengkap;
    ImageView xpic_home_user;

    DatabaseReference reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUserNameLocal();

        btn_ticket_pisa = findViewById(R.id.btn_ticket_pisa);
        btn_ticket_torri = findViewById(R.id.btn_ticket_torri);
        btn_ticket_pagoda = findViewById(R.id.btn_ticket_pagoda);
        btn_ticket_candi = findViewById(R.id.btn_ticket_candi);
        btn_ticket_sphinx = findViewById(R.id.btn_ticket_sphinx);
        btn_ticket_monas = findViewById(R.id.btn_ticket_monas);

        btn_to_profile = findViewById(R.id.btn_to_profile);
        xnama_lengkap = findViewById(R.id.xnama_lengkap);
        xbio = findViewById(R.id.xbio);
        xuser_balance = findViewById(R.id.xuser_balance);
        xpic_home_user = findViewById(R.id.xpic_home_user);

        //ambil data dr Firebase
        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //ambil data dr database Users
                String Balance_nom = dataSnapshot.child("user_balance").getValue().toString();

                xnama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                xbio.setText(dataSnapshot.child("bio").getValue().toString());
                xuser_balance.setText("US$ "+Balance_nom);

                Picasso.with(HomeAct.this)
                        .load(dataSnapshot.child("url_photo_profile")
                                .getValue().toString()).centerCrop().fit().into(xpic_home_user);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_to_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoprofile = new Intent(HomeAct.this, MyProfileAct.class);
                startActivity(gotoprofile);
            }
        });


        btn_ticket_pisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoPisaTicket = new Intent(HomeAct.this,TicketDetailAct.class);
                //meletakan data kepada intent
                gotoPisaTicket.putExtra("jenis_tiket","Pisa");
                startActivity(gotoPisaTicket);
            }
        });

        btn_ticket_torri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoPisaTicket = new Intent(HomeAct.this,TicketDetailAct.class);
                gotoPisaTicket.putExtra("jenis_tiket","Torri");
                startActivity(gotoPisaTicket);
            }
        });

        btn_ticket_pagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoPisaTicket = new Intent(HomeAct.this,TicketDetailAct.class);
                gotoPisaTicket.putExtra("jenis_tiket","Pagoda");
                startActivity(gotoPisaTicket);
            }
        });

        btn_ticket_candi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoPisaTicket = new Intent(HomeAct.this,TicketDetailAct.class);
                gotoPisaTicket.putExtra("jenis_tiket","Candi");
                startActivity(gotoPisaTicket);
            }
        });

        btn_ticket_sphinx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoPisaTicket = new Intent(HomeAct.this,TicketDetailAct.class);
                gotoPisaTicket.putExtra("jenis_tiket","Sphinx");
                startActivity(gotoPisaTicket);
            }
        });

        btn_ticket_monas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoPisaTicket = new Intent(HomeAct.this,TicketDetailAct.class);
                gotoPisaTicket.putExtra("jenis_tiket","Monas");
                startActivity(gotoPisaTicket);
            }
        });


    }


    //mendapat username
    public void getUserNameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }


}
