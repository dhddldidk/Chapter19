package kr.or.dgit.it.mapservice_application;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class LastKnownLocationActivity extends AppCompatActivity {

    ImageView onOffView;
    TextView allProviderView;
    TextView enableProviderView;
    TextView providerView;
    TextView latitudeView;
    TextView longitudeView;
    TextView accuracyView;
    TextView timeView;

    LocationManager manager;

    List<String> enabledProviders;
    float bestAccuracy;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_known_location);

        onOffView = (ImageView) findViewById(R.id.lab1_onOffView);
        allProviderView = (TextView) findViewById(R.id.lab1_allProviders);
        enableProviderView = (TextView) findViewById(R.id.lab1_enableProviders);
        providerView = (TextView) findViewById(R.id.lab1_provider);
        latitudeView = (TextView) findViewById(R.id.lab1_latitude);
        longitudeView = (TextView) findViewById(R.id.lab1_longitude);
        accuracyView = (TextView) findViewById(R.id.lab1_accuracy);
        timeView = (TextView) findViewById(R.id.lab1_time);

        //add~~~~~~~~~~~~~~~~~~~
        manager=(LocationManager)getSystemService(LOCATION_SERVICE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }else {
            getProviders();
            getLocation();
        }
    }

    private void showToast(String message){
        Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
        toast.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100 && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getProviders();
                getLocation();
            } else {
                showToast("no permission...");
            }
        }
    }

    private void getProviders(){
        //add~~~~~~~~~~~~~~
        String result="App Providers:";
        List<String> providers=manager.getAllProviders();
        for(String provider: providers){
            result += provider +",";
        }
        allProviderView.setText(result);

        result="Enabled Providers : ";
        enabledProviders=manager.getProviders(true);
        for(String provider : enabledProviders){
            result += provider+",";
        }
        enableProviderView.setText(result);
    }

    private void getLocation(){
        //add~~~~~~~~~~~~~
        for(String provider : enabledProviders){
            Location location=null;
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                location=manager.getLastKnownLocation(provider);
            }else {
                showToast("no permission");
            }
            if(location != null){
                float accuracy=location.getAccuracy();
                if(bestAccuracy==0){
                    bestAccuracy=accuracy;
                    setLocationInfo(provider, location);
                }else {
                    if(accuracy<bestAccuracy){
                        bestAccuracy=accuracy;
                        setLocationInfo(provider, location);
                    }
                }
            }
        }
    }
    private void setLocationInfo(String provider, Location location){
        //add~~~~~~~~~~~~~~~~
        if(location != null){
            providerView.setText(provider);
            latitudeView.setText(String.valueOf(location.getLatitude()));
            longitudeView.setText(String.valueOf(location.getLongitude()));
            accuracyView.setText(String.valueOf(location.getAccuracy()+"m"));
            Date date=new Date(location.getTime());
            SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            timeView.setText(sd.format(date));
            onOffView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_on, null));
        }else {
            showToast("location null");
        }
    }

}
