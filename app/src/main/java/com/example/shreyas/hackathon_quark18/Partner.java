package com.example.shreyas.hackathon_quark18;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class Partner extends AppCompatActivity {
    RecyclerView recyclerView;
    List<PartnerItemFormat> partnerItemFormats = new ArrayList<>();
    ProgressBar progressBar;
    PartnerRV partnerRV;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("airports");
    AirportItemFormat src,dest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (toolbar != null) {
            toolbar.setNavigationOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    });
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        src = getIntent().getParcelableExtra("keySrc");
        dest = getIntent().getParcelableExtra("keyDest");

        Log.e("TAG","Check" + src.getName());
        Log.e("TAG","Check" + dest.getName());

        recyclerView = (RecyclerView) findViewById(R.id.content_partner_rv);
        progressBar = (ProgressBar) findViewById(R.id.content_partner_progress);
        recyclerView.setHasFixedSize(true);
        partnerRV = new PartnerRV(this, partnerItemFormats);
        recyclerView.setAdapter(partnerRV);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        databaseReference.keepSynced(true);
    }

    @Override
    protected void onStart() {
        super.onStart();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                progressBar.setVisibility(VISIBLE);
                partnerItemFormats.clear();
                for (DataSnapshot shot : dataSnapshot.child(src.getKey()).child("partners").getChildren()) {

                    partnerItemFormats.add(shot.getValue(PartnerItemFormat.class));
                    // Log.v("tAG", partnerItemFormats.get(0).getName());
                }for (DataSnapshot shot : dataSnapshot.child(dest.getKey()).child("partners").getChildren()) {

                    partnerItemFormats.add(shot.getValue(PartnerItemFormat.class));
                    // Log.v("tAG", partnerItemFormats.get(0).getName());
                }
                //Need to notify adapter of data set change
                partnerRV.notifyDataSetChanged();
                progressBar.setVisibility(INVISIBLE);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                progressBar.setVisibility(INVISIBLE);
            }
        });

    }
}
