package kr.or.dgit.it.mapservice_application;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class ReadLocationActivity extends AppCompatActivity {

    private LocationManager mLocMan;
    private TextView mStatus;
    private TextView mResult;
    private String mProvider;
    private int mCount;
    ImageView onOffView;
    TextView latitudeView;
    TextView longitudeView;
    TextView accuracyView;
    TextView timeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_location);
        setTitle(getIntent().getStringExtra("title"));

        mLocMan = (LocationManager) getSystemService(LOCATION_SERVICE);
        mStatus = findViewById(R.id.status);
        mResult = findViewById(R.id.result);

        onOffView = (ImageView) findViewById(R.id.lab2_onOffView);
        latitudeView = (TextView) findViewById(R.id.lab2_latitude);
        longitudeView = (TextView) findViewById(R.id.lab2_longitude);
        accuracyView = (TextView) findViewById(R.id.lab2_accuracy);
        timeView = (TextView) findViewById(R.id.lab2_time);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }

        

    }

    public void onResume() {
        super.onResume();
        mCount = 0;
        mProvider = mLocMan.getBestProvider(new Criteria(), true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mLocMan.requestLocationUpdates(mProvider, 300, 10, mListener);
        mStatus.setText("현재 상태 : 서비스 시작");
    }

    public void onPause() {
        super.onPause();
        mLocMan.removeUpdates(mListener);
        mStatus.setText("현재 상태 : 서비스 정지");
    }

    LocationListener mListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            mCount++;
            String sloc = String.format("수신회수:%d\n위도:%f\n경도:%f\n고도:%f", mCount, location.getLatitude(), location.getLongitude(),
                    location.getAltitude());
            mResult.setText(sloc);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            String sStatus = "";
            switch (status) {
                case LocationProvider.OUT_OF_SERVICE:
                    sStatus = "범위 벗어남";
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    sStatus = "일시적 불능";
                    break;
                case LocationProvider.AVAILABLE:
                    sStatus = "사용 가능";
                    break;
            }
            mStatus.setText(provider + "상태 변경 : " + sStatus);
        }

        @Override
        public void onProviderEnabled(String provider) {
            mStatus.setText("현재 상태 : 서비스 사용 가능");
        }

        @Override
        public void onProviderDisabled(String provider) {
            mStatus.setText("현재 상태 : 서비스 사용 불가");
        }
    };
}
