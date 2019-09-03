package com.sumuzu.tiketsaya;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class MyTicketDetailAct extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_refund;
    TextView xnama_wisata, xlokasi, xtime_wisata, xdate_wisata, xketentuan;
    ImageView barcode_tiket;
    TextView tv_tiket;

    DatabaseReference reference, reference2;

    String teksBarcode;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new ="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_ticket_detail);

        getUserNameLocal();

        btn_back = findViewById(R.id.btn_back);
        btn_refund = findViewById(R.id.btn_refund);
        barcode_tiket = findViewById(R.id.barcode_ticket);
        tv_tiket = findViewById(R.id.tv_tiket);

        xnama_wisata = findViewById(R.id.xnama_wisata);
        xlokasi = findViewById(R.id.xlokasi);
        xtime_wisata = findViewById(R.id.xtime_wisata);
        xdate_wisata = findViewById(R.id.xdate_wisata);
        xketentuan = findViewById(R.id.xketentuan);

        tv_tiket.setVisibility(View.GONE);

        //mengambil data intent dari act sebelumnya
        Bundle bundle = getIntent().getExtras();
        final String nama_wisata_baru = bundle.getString("nama_wisata");
        final String id_tiket_baru = bundle.getString("id_tiket");

        //mengambil data dari Firebase
        reference = FirebaseDatabase.getInstance().getReference().child("Wisata").child(nama_wisata_baru);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xnama_wisata.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                xlokasi.setText(dataSnapshot.child("lokasi").getValue().toString());
                xdate_wisata.setText(dataSnapshot.child("date_wisata").getValue().toString());
                xtime_wisata.setText(dataSnapshot.child("time_wisata").getValue().toString());
                xketentuan.setText(dataSnapshot.child("ketentuan").getValue().toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //mengambil data dr firebase, kode tiket menjadi barcode
        reference2 = FirebaseDatabase.getInstance().getReference().child("MyTickets").child(username_key_new).child("wisata").child(id_tiket_baru);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                teksBarcode=dataSnapshot.child("id_tiket").getValue().toString();
                tv_tiket.setText(dataSnapshot.child("id_tiket").getValue().toString());
//                Toast.makeText(getApplicationContext(),teksBarcode,Toast.LENGTH_SHORT).show();

                try {
                    BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
                    Bitmap bitmap = barcodeEncoder.encodeBitmap(teksBarcode, BarcodeFormat.EAN_13, 200, 50);
                    barcode_tiket.setImageBitmap(bitmap);


                } catch(Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        btn_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent gotoMyProfile = new Intent(MyTicketDetailAct.this, MyProfileAct.class);
//                startActivity(gotoMyProfile);
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
