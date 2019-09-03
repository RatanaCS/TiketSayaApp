package com.sumuzu.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Random;

public class TicketCheckoutAct extends AppCompatActivity {

    Button btn_pay_now, btn_plus_tiket, btn_minus_tiket;
    LinearLayout btn_back, atur_qty;
    TextView text_qty_tiket, text_mybalance, text_totalharga;
    TextView lokasi_wisata, nama_wisata, ketentuan;
    ImageView notice_uang;

    Integer valueJumlahTiket = 1;
    Integer mybalance = 117;
    Integer totalharga = 0;
    Integer hargatiket = 50;
    Integer sisabalance =0;

    DatabaseReference reference, reference2, reference3, reference4, ref_username;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new ="";

    String date_wisata="";
    String time_wisata="";

    //generate nomor integer secara random, untuk membuat no transsksi secara unik
    Integer nomor_transaksi = new Random().nextInt();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUserNameLocal();

        //mengambil data intent dari act sebelumnya
        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("jenis_tiket");

        btn_pay_now = findViewById(R.id.btn_pay_now);
        btn_plus_tiket = findViewById(R.id.btn_plus);
        btn_minus_tiket = findViewById(R.id.btn_minus);
        btn_back= findViewById(R.id.btn_back);

        text_qty_tiket = findViewById(R.id.text_qty_ticket);
        text_mybalance = findViewById(R.id.text_balance);
        text_totalharga = findViewById(R.id.text_total_harga);
        notice_uang = findViewById(R.id.notice_uang);

        nama_wisata=findViewById(R.id.nama_wisata);
        lokasi_wisata=findViewById(R.id.lokasi_wisata);
        ketentuan=findViewById(R.id.ketentuan);

        //set awal qty tiket
        text_qty_tiket.setText(valueJumlahTiket.toString());
        btn_minus_tiket.animate().alpha(0).setDuration(300).start();
        btn_minus_tiket.setEnabled(false);
        btn_pay_now.animate().translationY(0).alpha(1).setDuration(300).start();
        btn_pay_now.setEnabled(true);

//        text_mybalance.setText("US$ "+ mybalance+"");
//        text_mybalance.setTextColor(Color.parseColor("#203DD1"));
//        totalharga = hargatiket*valueJumlahTiket;
//        text_totalharga.setText("US$ "+ totalharga+"");

        //setting visibility notice uang tdk cukup
        notice_uang.setVisibility(View.GONE);

        //mengambil data User dari Firebase
        reference2 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    mybalance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                    text_mybalance.setText("US$ "+ mybalance+"");
                    text_mybalance.setTextColor(Color.parseColor("#203DD1"));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //mengambil data dari firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(jenis_tiket_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    nama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                    lokasi_wisata.setText(dataSnapshot.child("lokasi").getValue().toString());
                    ketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());
                    date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                    time_wisata = dataSnapshot.child("time_wisata").getValue().toString();
                    hargatiket = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());

                    totalharga = hargatiket*valueJumlahTiket;
                    text_totalharga.setText("US$ "+ totalharga+"");


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        btn_minus_tiket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                valueJumlahTiket-=1;
                text_qty_tiket.setText(valueJumlahTiket.toString());

                if(valueJumlahTiket<2){
                    btn_minus_tiket.animate().alpha(0).setDuration(300).start();
                    btn_minus_tiket.setEnabled(false);
                }else{
//                    'do nothing'
                }
                totalharga = hargatiket*valueJumlahTiket;
                text_totalharga.setText("US$ "+ totalharga+"");

                if(totalharga<=mybalance){
                    btn_pay_now.animate().translationY(0).alpha(1).setDuration(300).start();
                    btn_pay_now.setEnabled(true);
                    text_mybalance.setTextColor(Color.parseColor("#203DD1"));
                    notice_uang.setVisibility(View.GONE);
                }
            }
        });

        btn_plus_tiket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                valueJumlahTiket+=1;
                text_qty_tiket.setText(valueJumlahTiket.toString());

                if(valueJumlahTiket>1){
                    btn_minus_tiket.animate().alpha(1).setDuration(300).start();
                    btn_minus_tiket.setEnabled(true);
                }else{
//                    'do nothing'
                }
                totalharga = hargatiket*valueJumlahTiket;
                text_totalharga.setText("US$ "+ totalharga+"");

                if(totalharga>mybalance){
                    btn_pay_now.animate().translationY(250).alpha(0).setDuration(300).start();
                    btn_pay_now.setEnabled(false);
                    text_mybalance.setTextColor(Color.parseColor("#D1206B"));
                    notice_uang.setVisibility(View.VISIBLE);
                }

            }
        });

        btn_pay_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                ref_username = FirebaseDatabase.getInstance().getReference()
                        .child("MyTickets").child(username_key_new).child("username");
                ref_username.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ref_username.getRef().setValue(username_key_new);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                //menyimpan data Tiket User dke Firebase dan membuat table baru "MyTickets"
                reference3 = FirebaseDatabase.getInstance().getReference()
                        .child("MyTickets").child(username_key_new).child("wisata")
                        .child(nama_wisata.getText().toString() + nomor_transaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        reference3.getRef().child("id_tiket").setValue(nama_wisata.getText().toString()+ nomor_transaksi);
                        reference3.getRef().child("nama_wisata").setValue(nama_wisata.getText().toString());
                        reference3.getRef().child("lokasi").setValue(lokasi_wisata.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(ketentuan.getText().toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);
                        reference3.getRef().child("jumlah_tiket").setValue(valueJumlahTiket.toString());

//                        Intent gotoSuccessBuyTicket = new Intent(TicketCheckoutAct.this, SuccessBuyTicketAct.class);
//                        startActivity(gotoSuccessBuyTicket);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



                //update data User ke Firebase
                reference4 = FirebaseDatabase.getInstance().getReference().child("Users").child(username_key_new);
                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        sisabalance=mybalance-totalharga;

                        reference4.getRef().child("user_balance").setValue(sisabalance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                Intent gotoSuccessBuyTicket = new Intent(TicketCheckoutAct.this, SuccessBuyTicketAct.class);
                startActivity(gotoSuccessBuyTicket);

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent gotoHomeProfile = new Intent(TicketCheckoutAct.this,TicketDetailAct.class);
//                startActivity(gotoHomeProfile);
//                finish();
                onBackPressed();
            }
        });

    }

    //mendapat username
    public void getUserNameLocal(){
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key, "");
    }

}
