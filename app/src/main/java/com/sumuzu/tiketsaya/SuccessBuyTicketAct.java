package com.sumuzu.tiketsaya;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class SuccessBuyTicketAct extends AppCompatActivity {

    Button btn_my_dashboard, btn_view_tiket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_buy_ticket);

        btn_my_dashboard = findViewById(R.id.btn_my_dashboard);
        btn_view_tiket = findViewById(R.id.btn_view_ticket);

        btn_view_tiket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gotoprofile = new Intent(SuccessBuyTicketAct.this, MyProfileAct.class);
                startActivity(gotoprofile);

            }
        });

        btn_my_dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gotomyhome = new Intent(SuccessBuyTicketAct.this, HomeAct.class);
                startActivity(gotomyhome);
            }
        });

    }
}
