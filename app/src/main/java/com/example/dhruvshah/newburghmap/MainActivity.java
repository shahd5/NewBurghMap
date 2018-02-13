package com.example.dhruvshah.newburghmap;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dhruvshah.newburghmap.services.MyService;
import com.example.dhruvshah.newburghmap.utils.NetworkHelper;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback,Serializable {

    GoogleMap mMap;
    private boolean networkOK;
    private static final int ERROR_DIALOG_REQUEST = 9001;
    private static final String JSON_URL = "http://560057.youcanlearnit.net/services/json/itemsfeed.php";
    //private static final String JSON_URL = "https://www.googleapis.com/fusiontables/v2/query?sql=SELECT%20%27latitude%27%2C%20%27longitude%27%2C%20%27icon%27%2C%20%27group%27%2C%20%27name%27%2C%20%27address%27%2C%20%27phone%27%2C%20%27link%27%2C%20%27description%27%2C%20%27hours%27%20FROM%201Gi1ZodgPaB8z3jp22-m6NeABCwy379mAfdILb4Ks%20WHERE%20%27group%27%3D%27employment%27&key=AIzaSyAPtyWPhurnjmBL9B8XRZUCbeMJbDhfnXY&callback=jQuery112406408739767775731_1518223785774&_=1518223785785";


    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra(MyService.MY_SERVICE_PAYLOAD);
            System.out.println(message+"\n");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });


        if (servicesOK() && NetworkHelper.hasNetworkAccess(this)) {
            setContentView(R.layout.activity_map);
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
            Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(myToolbar);
            LocalBroadcastManager.getInstance(getApplicationContext())
                    .registerReceiver(mBroadcastReceiver,new IntentFilter(MyService.MY_SERVICE_MESSAGE));
        } else {
            setContentView(R.layout.activity_main);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(mBroadcastReceiver);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (mMap != null) {
            Toast.makeText(this, "Ready to map!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Map not connected!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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


        //Add menu handling code
        switch (id) {

            case R.id.mapTypeNormal:
                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case R.id.mapTypeSatellite:
                mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.mapTypeTerrain:
                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case R.id.mapTypeHybrid:
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
        }


        return super.onOptionsItemSelected(item);

    }

    public boolean servicesOK() {
        int isAvailable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        } else if (GooglePlayServicesUtil.isUserRecoverableError(isAvailable)) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvailable, this, ERROR_DIALOG_REQUEST);
            dialog.show();
        } else {
            Toast.makeText(this, "Can't connet to mapping service", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void gotoLocation(double lat, double lng, float zoom) {
        LatLng latLng = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, zoom);
        mMap.moveCamera(update);
    }

    private void hideSoftKeyboard(View v) {
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromInputMethod(v.getWindowToken(), 0);
    }

    public void geoLocate(View v) throws IOException {
        hideSoftKeyboard(v);
        TextView tv = (TextView) findViewById(R.id.editText1);

        if(tv.getText().toString()==""){
            Toast.makeText(this, "emtpty ", Toast.LENGTH_SHORT).show();
        }

        String searchString = tv.getText().toString();
        Geocoder gc = new Geocoder(this);
        List<Address> list = gc.getFromLocationName(searchString, 1);

        if (list.size() > 0) {
            Address add = list.get(0);
            String locality = add.getLocality();
            Toast.makeText(this, "Found: " + locality, Toast.LENGTH_SHORT).show();

            double lat = add.getLatitude();
            double lng = add.getLongitude();
            gotoLocation(lat, lng, 10);
        }else{
            Toast.makeText(this, "Not Found: "+searchString , Toast.LENGTH_SHORT).show();

        }
    }

    public void showCurrentLocation(MenuItem item) throws IOException {
        if(NetworkHelper.hasNetworkAccess(this)){
            System.out.println("yolo1");
            Intent intent = new Intent(this,MyService.class);
            intent.setData(Uri.parse(JSON_URL));
            startService(intent);
        }else{
            Toast.makeText(this,"Network not available!",Toast.LENGTH_SHORT).show();
        }


    }
}




