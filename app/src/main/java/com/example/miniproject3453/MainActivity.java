package com.example.miniproject3453;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tvLocation; // 0. oncreate밖에 전역으로 만들어줌
    LocationManager locationManager; // 전역변수로 선언.

    LocationListener lListener = new LocationListener() { //6. 이제 location
        @Override
        public void onLocationChanged(Location location) { //위치변경때마다바뀐거알려줌
//            Log.d("skku", "LAT: " + location.getLatitude() + ", LON: " + location.getLongitude());
            tvLocation.setText("LAT: " + location.getLatitude() + ", LON: " + location.getLongitude());
        }

        //아래 세 메소드는 굳이.
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvLocation = findViewById(R.id.tv_loccation); // 1. 인스턴스 획득. ui는 끝!

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            // 2. 현재 단말기의 버전이 상수 M 마시멜로버전보다 낮으면 넘어가고
            getLocationManager();
        } else {
            if(checkPermission()) { //권한이미있는거. 두 번 이상 실행한경우
                getLocationManager();
            } else { //권한없으니 요청해야하는거
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 12345 ); // 4. 사용자에게 시스템이 팝업이 뜬다.
            }
        }
    } //end onCreate

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) { // 5. 팝업을 누르면 구동
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 12345) {
            if (checkPermission()) {
                getLocationManager();
            } else { //권한팝업거부한경우..
                Toast.makeText(this, "위치권한 필요", Toast.LENGTH_LONG).show();
                finish(); //강제종료
            }
        }
    }

    private void getLocationManager() { // 세 가지 경우 작업실행시켜주는거...걍메소드로 만들어버림
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }


    @RequiresApi(api = Build.VERSION_CODES.M) // api가 m 이상일때만 쓰겟다는 문법. (checkSelfPermission때문에)
    private boolean checkPermission() { // 3. 메소드를 따로 만들어 한번만하게..?흠
        if(PackageManager.PERMISSION_GRANTED != checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)) { //permissino이 거부되엇을때
            return false;
        } else {
            return true;
        }

    }

    @SuppressLint("MissingPermission") //으로 에러처리
    @Override
    protected void onResume() { // 얘넨는무조건슈퍼함수불러야함. 엑티비티라이프사이클은ㅇㅇ. resume이랑 pause랑 페어로 맞춰줘야한다.
        super.onResume();
        if(locationManager != null)
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, lListener); // 1초, 10m를 벗어났을 때 (최솟값) 값을 주라고
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(locationManager != null)
            locationManager.removeUpdates(lListener);
    }

}
/*
1. 고영사진지도에올리기- 구글맵 마커 사용?
2. 달력 유통기한 냉장고
3. 다짐 카카오톡이나문자 관련api 어길 시 메세지보내기
4. 음성인식 아나운서 말 테스트 ㄴㅇㄻㅇㄴㄻ몰랑
 */