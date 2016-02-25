package swu.sun.yut.ob.sentlocationdrivingbetter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    //Explicit
    private TextView latTextView, lngTextView;
    private LocationManager locationManager;
    private Criteria criteria;
    private boolean GPSABoolean, networkABoolean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Bind Widget
        bindWidget();

        //Setup Location
        setupLocation();

    }   // Main Method

    @Override
    protected void onResume() {
        super.onResume();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);
        String strLat = "Unknow";
        String strLng = "Unknow";

        Location networkLocation = requestLocation(LocationManager.NETWORK_PROVIDER,
                "network Error");
        if (networkLocation != null) {

            strLat = String.format("%.7f", networkLocation.getLatitude());
            strLng = String.format("%.7f", networkLocation.getLongitude());

        }   //if

        Location GPSLocation = requestLocation(LocationManager.GPS_PROVIDER,
                "GPS Error");
        if (GPSLocation != null) {

            strLat = String.format("%.7f", GPSLocation.getLatitude());
            strLng = String.format("%.7f", GPSLocation.getLongitude());

        }   // if

        latTextView.setText(strLat);
        lngTextView.setText(strLng);

    }   // onResume

    @Override
    protected void onStop() {
        super.onStop();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(locationListener);

    }   // onStop

    @Override
    protected void onStart() {
        super.onStart();

        GPSABoolean = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!GPSABoolean) {

            networkABoolean = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!networkABoolean) {
                Toast.makeText(MainActivity.this, "Cannot Find Location", Toast.LENGTH_SHORT).show();
            }   // if

        }   // if

    }   // onStart

    public Location requestLocation(String strProvider, String strError) {

        Location location = null;

        if (locationManager.isProviderEnabled(strProvider)) {

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return null;
            }
            locationManager.requestLocationUpdates(strProvider, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvider);

        } else {
            Log.d("gps", strError);
        } // if

        return location;
    }



    //Create Class
    public final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            latTextView.setText(String.format("%.7f", location.getLatitude()));
            lngTextView.setText(String.format("%.7f", location.getLongitude()));
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    private void setupLocation() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);

    }   //setupLocation

    private void bindWidget() {
        latTextView = (TextView) findViewById(R.id.textView2);
        lngTextView = (TextView) findViewById(R.id.textView4);
    }

}   // Main Class