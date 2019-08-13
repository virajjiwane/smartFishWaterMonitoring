package com.example.pro;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class device extends AppCompatActivity {
    Button on1,off1,on2,off2,on3,off3;
    TextView d1st,d2st,d3st;
    DatabaseReference reff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device);
        on1=(Button)findViewById(R.id.on1);
        off1=(Button)findViewById(R.id.off1);
        on2=(Button)findViewById(R.id.ON2);
        off2=(Button)findViewById(R.id.off2);
        on3=(Button)findViewById(R.id.ON3);
        off3=(Button)findViewById(R.id.off3);
        d1st=(TextView)findViewById(R.id.d1st);
        d2st=(TextView)findViewById(R.id.d2st);
        d3st=(TextView)findViewById(R.id.d3st);

        reff = FirebaseDatabase.getInstance().getReference();

        on1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reff.child("Device1").child("St").setValue("ON");
            }
        });
        off1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                reff.child("Device1").child("St").setValue("OFF");
            }
        });
        on2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reff.child("Device2").child("St").setValue("ON");
            }
        });
        off2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reff.child("Device2").child("St").setValue("OFF");
            }
        });
        on3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reff.child("Device3").child("St").setValue("ON");
            }
        });
        off3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reff.child("Device3").child("St").setValue("OFF");
            }
        });
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               /* for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if (dataSnapshot.hasChild("/Device1/Status")) {
                        String d1 = dataSnapshot.getValue(String.class);
                        Toast.makeText(device.this, d1, Toast.LENGTH_SHORT).show();

                    }*/
                String D1Status = dataSnapshot.child("Device1").child("Status").getValue(String.class);
                String D1time = dataSnapshot.child("Device1").child("time").getValue(String.class);

                String D2Status = dataSnapshot.child("Device2").child("Status").getValue(String.class);
                String D2time = dataSnapshot.child("Device2").child("time").getValue(String.class);
                String D3Status = dataSnapshot.child("Device3").child("Status").getValue(String.class);
                String D3time = dataSnapshot.child("Device3").child("time").getValue(String.class);


                d1st.setText("Areator is "+D1Status+" from "+D1time );
                d2st.setText("Device1 is "+D2Status+" from "+D2time);
                d3st.setText("Device2 is "+D3Status+" from "+D3time);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
