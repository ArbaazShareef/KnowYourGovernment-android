package com.arbaaz.knowyourgovernment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener {
    private RecyclerView recyclerView; // Layout's recyclerview
    private List OfficialsList = new ArrayList<>();
    private OfficialAdapter Adapter;
    private Official official;
    private TextView LocationText;
    private static final String TAG = "MainActivity";
    private Locator locator;
    public String Address = "0";
    private AsyncTask<String, Void, String> asyncTaskOfficial;
    private Boolean NetworkFlag = true;
    private NoNetworkAdapter n;
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        String BackText = getIntent().getStringExtra("LOC");
        //Toast.makeText(this, BackText, Toast.LENGTH_SHORT).show();
        try {
          //  Toast.makeText(this, "Try Printed", Toast.LENGTH_SHORT).show();
            if(!(BackText.isEmpty())){
                LocationText.setText(BackText);
                GetDataForNewLocation(BackText);

            }
        }catch (Exception e){
            //Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            if(NetworkFlag == true){

                asyncTaskOfficial = new AsyncTaskOfficial(this);
                ((TextView) findViewById(R.id.LocationText)).setText(Address);
                asyncTaskOfficial.execute(Address);
            }else{
                Toast.makeText(this, "Network Not Available", Toast.LENGTH_SHORT).show();
            }
        }



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetworkFlag = isNetworkAvailable();

        locator = new Locator(this);
        LocationText = (TextView) findViewById(R.id.LocationText);
        if(NetworkFlag==true){
            recyclerView = (RecyclerView)findViewById(R.id.Rview);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            Adapter = new OfficialAdapter(OfficialsList,this);
            recyclerView.setAdapter(Adapter);

        }


      //  Toast.makeText(this, "Remember to set GPS or open Maps if using Emulator", Toast.LENGTH_LONG).show();




        }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.AboutMenuButton:
                 //Toast.makeText(this, "Pressed About Menu Button", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this,AboutActivity.class);
                this.startActivity(i);
                return true;

            case R.id.LocationMenuButton:
               // Toast.makeText(this, "Pressed Location Menu Button", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder builder = new AlertDialog.Builder(this);

                builder.setTitle("Location Selection");
                builder.setMessage("Enter a City,State or a Zip Code:");
                final EditText input = new EditText(this);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        String Symbol = input.getText().toString();
                        Address = Symbol;
                        if(Address.toString().equals("0")){
                        //    Toast.makeText(MainActivity.this, "Enter a correct zip code", Toast.LENGTH_SHORT).show();

                        }else{


                                recyclerView.setAdapter(Adapter);
                                LocationText.setText(Address);
                                GetDataForNewLocation(Symbol);
                                dialog.dismiss();


                        }
                    }
                });

                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // Do nothing
                        dialog.dismiss();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();

                return true;
            default:
                return true;
        }

    }

    private void GetDataForNewLocation(String symbol) {

        AsyncTaskOfficial async = new AsyncTaskOfficial(this);
        async.execute(symbol);
    }

    @Override
    public void onClick(View view) {
        int pos = recyclerView.getChildLayoutPosition(view);

        //Toast.makeText(this, "Clicked"+pos, Toast.LENGTH_SHORT).show();
        Official SelectedOfficial = new Official();
        SelectedOfficial = (Official) OfficialsList.get(pos);
       // Toast.makeText(this, SelectedOfficial.getTitle(), Toast.LENGTH_SHORT).show();
        Intent run = new Intent(this,OfficialActivity.class);
        run.putExtra("Official",SelectedOfficial);
        run.putExtra("Location",LocationText.getText());
        this.startActivity(run);



    }

    @Override
    public boolean onLongClick(View view) {
        return false;
    }

    public void setData(double lat, double lon) {

       // ((TextView) findViewById(R.id.lat)).setText(lat + "");
        //((TextView) findViewById(R.id.lon)).setText(lon + "");

        Log.d(TAG, "setData: Lat: " + lat + ", Lon: " + lon);
        String address = doAddress(lat, lon);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResult: CALL: " + permissions.length);
        Log.d(TAG, "onRequestPermissionsResult: PERM RESULT RECEIVED");

        if (requestCode == 5) {
            Log.d(TAG, "onRequestPermissionsResult: permissions.length: " + permissions.length);
            for (int i = 0; i < permissions.length; i++) {
                if (permissions[i].equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                        Log.d(TAG, "onRequestPermissionsResult: HAS PERM");
                        locator.setUpLocationManager();
                        locator.determineLocation();
                    } else {
                        Toast.makeText(this, "Location permission was denied - cannot determine address", Toast.LENGTH_LONG).show();
                        Log.d(TAG, "onRequestPermissionsResult: NO PERM");
                    }
                }
            }
        }
        Log.d(TAG, "onRequestPermissionsResult: Exiting onRequestPermissionsResult");
    }


    private String doAddress(double latitude, double longitude) {

        Log.d(TAG, "doAddress: Lat: " + latitude + ", Lon: " + longitude);



        List<Address> addresses = null;
        for (int times = 0; times < 3; times++) {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            try {
                Log.d(TAG, "doAddress: Getting address now");


                addresses = geocoder.getFromLocation(latitude, longitude, 1);
                StringBuilder sb = new StringBuilder();

                for (Address ad : addresses) {
                    Log.d(TAG, "doLocation: " + ad);

                    sb.append("\nAddress\n\n");
                    for (int i = 0; i < ad.getMaxAddressLineIndex(); i++){
                        sb.append("\t" + ad.getAddressLine(i) + "\n");
                        Address = ad.getAddressLine(i);
                    }

                    sb.append("\t" + ad.getCountryName() + " (" + ad.getCountryCode() + ")\n");

                }

                return sb.toString();
            } catch (IOException e) {
                Log.d(TAG, "doAddress: " + e.getMessage());

            }
            Toast.makeText(this, "GeoCoder service is slow - please wait", Toast.LENGTH_SHORT).show();
        }
       Toast.makeText(this, "GeoCoder service timed out - please try again", Toast.LENGTH_LONG).show();
        return null;
    }

    public void noLocationAvailable() {
        Toast.makeText(this, "No location providers were available", Toast.LENGTH_LONG).show();
    }

    public void setOfficialList(List officialsList) {
            OfficialsList.clear();
        try{
            Official locOFF = (Official) officialsList.get(1);
            if(locOFF.getNormAddress().toString().equals(" ")){
                LocationText.setText("No Data For Location");
                OfficialsList.clear();
                Adapter.notifyDataSetChanged();


            }
            else{
                //   Toast.makeText(this, "Location :" +locOFF.getNormAddress() , Toast.LENGTH_SHORT).show();
                // Toast.makeText(this, locOFF.getName(), Toast.LENGTH_SHORT).show();
                //Toast.makeText(this, "Reached Else Statement", Toast.LENGTH_SHORT).show();
                OfficialsList.addAll(officialsList);
                LocationText.setText(locOFF.getNormAddress());
                Adapter.notifyDataSetChanged();
            }
        }catch (Exception e){
            Toast.makeText(this, "Wrong Location Input Given", Toast.LENGTH_SHORT).show();
            LocationText.setText("No Data For Location");
        }

            }
}
