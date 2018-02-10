package com.example.shreyas.hackathon_quark18;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.fence.AwarenessFence;
import com.google.android.gms.awareness.fence.FenceState;
import com.google.android.gms.awareness.snapshot.DetectedActivityResult;
import com.google.android.gms.awareness.snapshot.LocationResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.Places;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static com.example.shreyas.hackathon_quark18.R.id.spinner;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener {
    private static final int REQUEST_CODE_PERMISSION = 2;

    String mPermission = Manifest.permission.ACCESS_FINE_LOCATION;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("airports");
    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference();
    List<AirportItemFormat> items = new ArrayList<>();
    List<String> sourceCodes = new ArrayList<>();
    List<String> destCodes = new ArrayList<>();
    ArrayAdapter<String> spinnerAdapter;
    Button checkOffers;
    TextView latlong, activity;
    AppCompatSpinner sourceSpinner, destSpinner;
    AwarenessFence airports, airSrc, airDest;


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private static final String FENCE_RECEIVER_ACTION = "action";
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        /*databaseReference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                JSONObject jsonObject;
                String s=dataSnapshot.toString();
                try {
                    jsonObject = new JSONObject(s);
                    Log.e("MA",jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                    //Log.e("MA",jsonObject);
                }
                //Log.e("MA",jsonObject);

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/

        Log.e("MA","asda");

        sourceSpinner = (AppCompatSpinner) findViewById(spinner);
        destSpinner = (AppCompatSpinner) findViewById(R.id.spinner2);
        checkOffers = (Button) findViewById(R.id.button2);
        latlong = (TextView) findViewById(R.id.textView2);
        activity = (TextView) findViewById(R.id.textView3);
        spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, sourceCodes);
        // Specify the layout to use when the list of choices appears
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the spinnerAdapter to the spinner
        sourceSpinner.setAdapter(spinnerAdapter);

        // Apply the spinnerAdapter to the spinner
        destSpinner.setAdapter(spinnerAdapter);

        loading = ProgressDialog.show(this, "Loading", "fetching airport data");
        final GoogleApiClient apiClient = new GoogleApiClient.Builder(this)
                .addApi(Awareness.API)
                .addApi(Places.GEO_DATA_API)
                .build();
        apiClient.connect();
        try {
            if (ActivityCompat.checkSelfPermission(this, mPermission)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_PERMISSION);

                // If any permission above not allowed by user, this condition will execute every time, else your else part will work
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        Awareness.SnapshotApi.getLocation(apiClient).setResultCallback(new ResultCallback<LocationResult>() {
            @Override
            public void onResult(@NonNull LocationResult locationResult) {
                latlong.setText(locationResult.getLocation().getLatitude() + "," + locationResult.getLocation().getLongitude() + "\n\n" + locationResult.getLocation().toString());
            }
        });
        Awareness.SnapshotApi.getDetectedActivity(apiClient).setResultCallback(new ResultCallback<DetectedActivityResult>() {
            @Override
            public void onResult(@NonNull DetectedActivityResult detectedActivityResult) {
                try {
                    activity.setText(detectedActivityResult.getActivityRecognitionResult().getProbableActivities().get(0).toString());
                } catch (Exception e) {
                    activity.setText("No activity yet");
                }
            }
        });

        registerReceiver(myFenceReceiver, new IntentFilter(FENCE_RECEIVER_ACTION));

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();
                sourceCodes.clear();
                destCodes.clear();
                Log.e("MA",dataSnapshot.toString());
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    AirportItemFormat aif = dataSnapshot.getValue(AirportItemFormat.class);
                    items.add(new AirportItemFormat(child.getKey(), aif.getLat(), aif.getLon(), aif.getName()));
                    sourceCodes.add(child.getKey());
                    destCodes.add(child.getKey());
                }
                spinnerAdapter.notifyDataSetChanged();
                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                loading.dismiss();

            }
        });
        checkOffers.setOnClickListener(this);

        Log.e("MA",destCodes.toString()+" "+sourceCodes.toString()+" "+items.toString());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(myFenceReceiver);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    BroadcastReceiver myFenceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("TAG", "recd context");
            FenceState fenceState = FenceState.extract(intent);

            if (TextUtils.equals(fenceState.getFenceKey(), "chilling")) {
                switch (fenceState.getCurrentState()) {
                    case FenceState.TRUE:
                        Log.e("TAG", "chilling");
                        checkOffers.setText("Offers ready");
                        break;
                    case FenceState.FALSE:
                        Log.e("TAG", "no chill");
                        checkOffers.setText("Not in fence area");
                        break;
                    case FenceState.UNKNOWN:
                        Log.e("TAG", "Unknown fence issue");
                        checkOffers.setText("Unknown fence");
                        break;
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == checkOffers.getId()) {
            Intent intent = new Intent(this, Partner.class);
            intent.putExtra("keySrc", items.get(sourceSpinner.getSelectedItemPosition()));
            intent.putExtra("keyDest", items.get(destSpinner.getSelectedItemPosition()));
            startActivity(intent);
        }
    }
}